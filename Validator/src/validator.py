import json
import inspect
from gen.models import *
from gen.rules import Rules

class Validator:

    def validate(self, diagram):
        nodes = {}
        diagram = json.loads(diagram)

        for n in diagram['nodes']:
            clazz = Class()
            clazz.name = n['name']
            nodes[clazz.name] = clazz
        
        for name, clazz in nodes.items():
            for l in diagram['links']:
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
