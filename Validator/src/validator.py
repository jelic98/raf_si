import json
import inspect
from gen.models import *
from gen.rules import Rules

class Validator:

    def validate(self, model):
        nodes = {}
        model = json.loads(model)

        for n in model['nodes']:
            clazz = Class()
            clazz.name = n['name']
            nodes[clazz.name] = clazz
        
        for name, clazz in nodes.items():
            for l in model['links']:
                if l['type'] == 'generalization' and l['from'] == name:
                    clazz.parentClass.append(nodes[l['to']])
        
        rules = Rules()
        attrs = [getattr(rules, name) for name in dir(rules)]
        methods = filter(inspect.ismethod, attrs)
        errors = []
        for m in methods:
            e = m(nodes.values())
            if e is not None:
                errors.append(e)
        return errors
