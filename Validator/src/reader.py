class Reader:

    def __init__(self, models, rules):
        self._models = models
        self._rules = rules


    def read(self):
        models = self._readModels()
        rules = self._readRules()

        return models, rules


    def _readModels(self):
        models = {}
        enums = set()
        model = None
        
        with open(self._models, 'r') as f:
            for line in [x.strip() for x in f.readlines() if len(x) > 1]:
                tokens = line.split()

                if line.startswith('node'):
                    model = tokens[1]
                    models[model] = {}
                else:
                    key = tokens[0]
                    models[model][key] = None

                    if key.isupper():
                        enums.add(model)
                    elif len(tokens) == 2 and tokens[1] == 'list':
                        models[model][key] = []
        
        models['enums'] = enums

        return models


    def _readRules(self):
        rules = []
        rule = None
        
        with open(self._rules, 'r') as f:
            for line in [x.strip() for x in f.readlines() if len(x) > 1]:
                if line.startswith('#'):
                    continue
                elif line.startswith('rule'):
                    tokens = line.split()
                    rule = {}
                    rule['type'] = tokens[1]
                    rule['error'] = ' '.join(tokens[2:])
                else:
                    rule['code'] = line
                    rules.append(rule)

        return rules
