import re

class Writer:

    def __init__(self, parser, models, rules):
        self._parser = parser
        self._models = models
        self._rules = rules


    def write(self, models, rules):
        self._writeModels(models)
        self._writeRules(rules)


    def _writeModels(self, models):
        with open(self._models, 'w') as f:
            f.write(f'from enum import Enum, auto\n\n')

            for name, model in models.items(): 
                if name == 'enums':
                    continue

                if name in models['enums']:
                    f.write(f'\nclass {name}(Enum):\n')

                    for key, _ in model.items():
                        f.write(f'\t{key} = auto()\n')
                else:
                    f.write(f'\nclass {name}:\n')
                    f.write(f'\tdef __init__(self):\n')

                    for key, value in model.items():
                        f.write(f'\t\tself.{key} = {value}\n')


    def _writeRules(self, rules):
        with open(self._rules, 'w') as f:
            parser = Parser()

            for rule in rules:
                error = rule['error']
                code = rule['code']

                method = ''.join(x.capitalize() or ' ' for x in error.split(' '))
                method = method[0].lower() + method[1:]

                f.write(self._parser.parse(code))
