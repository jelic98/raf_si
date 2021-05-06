import json
import inspect
from gen.models import *
from gen.rules_class import ClassRules
from gen.rules_functional import FunctionalRules

class Validator:

    def validate(self, model):
        nodes = {}
        model = json.loads(model)

        if model['type'] == 'class':
            for n in model['details']['nodes']:
                node = Interface() if n['type'] == 'interface' else Class()
                node.name = n['name']
                nodes[node.name] = node
                for m in n['methods']:
                    method = Method()
                    method.name = m['name']
                    method.visibility = m['type']
                    method.visibility = Visibility[m['visibility'].upper()]
                    method.enclosing = node
                    node.methods.append(method)
                for a in n['properties']:
                    attribute = Attribute()
                    attribute.name = a['name']
                    attribute.type = a['type']
                    attribute.visibility = Visibility[a['visibility'].upper()]
                    node.attributes.append(attribute)

            link = 0

            for name, n in nodes.items():
                link += 1
                for l in model['details']['links']:
                    if l['from']['name'] == name:
                        if l['type'] == 'generalization':
                            n.parent.append(nodes[l['to']['name']])
                        elif l['type'] == 'aggregation' or l['type'] == 'composition':
                            attribute = Attribute()
                            attribute.name = f'Link_{link}'
                            attribute.type = l['to']['name']
                            node.attributes.append(attribute)

            rules = ClassRules()
        elif model['type'] == 'functional':
            rules = FunctionalRules()
        
        attrs = [getattr(rules, name) for name in dir(rules)]
        methods = filter(inspect.ismethod, attrs)
        errors = []
        for m in methods:
            e = m(nodes.values())
            if e is not None:
                errors.append(e)
        return errors
