import os
import json
import requests
import argparse
from http.server import BaseHTTPRequestHandler, HTTPServer
from src.reader import Reader
from src.writer import Writer
from src.validator import Validator
import src.generator as gen


BROKER_HOST, BROKER_PORT = '127.0.0.1', 9000
SERVER_HOST, SERVER_PORT = '127.0.0.1', None
META_MODELS, META_RULES = 'meta/models.meta', 'meta/rules.meta'
GEN_MODELS, GEN_RULES = 'gen/models.py', 'gen/rules.py'


def _register():
    data = {'name': 'validator', 'host': SERVER_HOST, 'port': SERVER_PORT}
    requests.request('POST', f'http://{BROKER_HOST}:{BROKER_PORT}/services', data=data)


def _validate(diagram):
    models, rules = Reader(META_MODELS, META_RULES).read()
    Writer(GEN_MODELS, GEN_RULES).write(models, rules)
    return Validator().validate(diagram)


class Handler(BaseHTTPRequestHandler):

    def do_POST(self):
        if self.path == '/validate':
            self.send_response(200)
            self.send_header('Content-type', 'application/json')
            self.end_headers()
            data = self.rfile.read(int(self.headers['Content-Length'])).decode('UTF-8')
            response = {'errors': _validate(data)}
            self.wfile.write(bytes(json.dumps(response), 'utf8'))
        else:
            self.send_response(404)
            self.send_header('Content-type', 'application/json')
            self.end_headers()
            response = {"code": 405, "message": "Not Found"}
            self.wfile.write(bytes(json.dumps(response), 'utf8'))


def _main():
    global SERVER_PORT

    arg_parser = argparse.ArgumentParser()
    arg_parser.add_argument('server_name')
    arg_parser.add_argument('server_port')
    args = arg_parser.parse_args()

    server_name = args.server_name
    SERVER_PORT = int(args.server_port)

    os.chdir(server_name)

    _register()
    
    with HTTPServer((SERVER_HOST, SERVER_PORT), Handler) as server:
        server.serve_forever() 


if __name__ == '__main__':
    _main()
