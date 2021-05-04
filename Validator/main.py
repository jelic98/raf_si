from src.reader import Reader
from src.writer import Writer
from src.validator import Validator
import src.rule_parser as rp


META_MODELS, META_RULES = 'meta/models.meta', 'meta/rules.meta'
GEN_MODELS, GEN_RULES = 'gen/models.py', 'gen/rules.py'
diagram = '{"nodes":[{"name":"1"}, {"name":"2"}], "links":[{"from":"1","to":"2","type":"generalization"}]}'


def main():
    print(rp.parse('for nodes as n if n eq 3'))
    #models, rules = Reader(META_MODELS, META_RULES).read()
    #Writer(GEN_MODELS, GEN_RULES).write(models, rules)
    #Validator().validate(diagram)


if __name__ == '__main__':
    main()
