import re
import src.generator as gen


class Writer:


    def __init__(self, models, crules, frules):
        self._models = models
        self._crules = crules
        self._frules = frules


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
        with open(self._crules, 'w') as fc, open(self._frules, 'w') as ff:
            fc.write('from gen.models import *\n')
            fc.write('class ClassRules:\n')
            fc_write = False

            ff.write('from gen.models import *\n')
            ff.write('class FunctionalRules:\n')
            ff_write = False

            for rule in rules:
                error = rule['error']
                code = rule['code']

                method = ''.join(x.capitalize() or ' ' for x in error.split(' '))
                method = method[0].lower() + method[1:]

                if rule['type'] == 'class':
                    fc.write(gen.parse(method, code, error))
                    fc_write = True
                elif rule['type'] == 'functional':
                    ff.write(gen.parse(method, code, error))
                    ff_write = True

            if not fc_write:
                fc.write('\tpass\n');

            if not ff_write:
                ff.write('\tpass\n');
