import os
import sys
import cgi
import html
import json
import requests
import argparse
from http.server import BaseHTTPRequestHandler, HTTPServer
from src.reader import Reader
from src.writer import Writer
import src.generator as gen


START_SERVER = True
BROKER_HOST, BROKER_PORT = '127.0.0.1', 9000
SERVER_HOST, SERVER_PORT = '127.0.0.1', None
META_MODELS, META_RULES = 'res/models.meta', 'res/rules.meta'
GEN_MODELS, GEN_CRULES, GEN_FRULES = 'gen/models.py', 'gen/rules_class.py', 'gen/rules_functional.py'


def _register():
    data = {'name': 'validator', 'host': SERVER_HOST, 'port': SERVER_PORT}
    requests.request('POST', f'http://{BROKER_HOST}:{BROKER_PORT}/services', data=data)


def _validate(model):
    models, rules = Reader(META_MODELS, META_RULES).read()
    Writer(GEN_MODELS, GEN_CRULES, GEN_FRULES).write(models, rules)
    from src.validator import Validator
    return Validator().validate(model)


class Handler(BaseHTTPRequestHandler):

    def do_POST(self):
        if self.path == '/validate':
            self.send_response(200)
            self.send_header('Content-type', 'application/json')
            self.end_headers()
            
            form = cgi.FieldStorage(
                fp=self.rfile,
                headers=self.headers,
                environ={
                    'REQUEST_METHOD': 'POST',
                    'CONTENT_TYPE': self.headers['Content-Type']})
            
            model = html.unescape(form.getvalue('model'))
            
            response = {'errors': _validate(model)}
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
    
    if START_SERVER:
        arg_parser.add_argument('server_name')
        arg_parser.add_argument('server_port')
        args = arg_parser.parse_args()

        SERVER_PORT = int(args.server_port)

        os.chdir(args.server_name)

        _register()
        
        with HTTPServer((SERVER_HOST, SERVER_PORT), Handler) as server:
            server.serve_forever() 
    else:
        arg_parser.add_argument('model')
        args = arg_parser.parse_args()
        print(_validate(args.model))


if __name__ == '__main__':
    _main()
