from src.reader import Reader
from src.writer import Writer
from src.parser import Parser
from src.validator import Validator


META_MODELS, META_RULES = 'meta/Models.meta', 'meta/Rules.meta'
GEN_MODELS, GEN_RULES = 'gen/Models.py', 'gen/Rules.py'
diagram = '{"nodes":[{"name":"1"}, {"name":"2"}], "links":[{"from":"1","to":"2","type":"generalization"}]}'


def main():
    models, rules = Reader(META_MODELS, META_RULES).read()
    Writer(GEN_MODELS, GEN_RULES).write(models, rules)
    Validator().validate(diagram)


if __name__ == '__main__':
    main()
