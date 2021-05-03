class Validator:
    
    def __init__(self):
        pass

    def nameCannotBeNull(model):
        for node in model['odes']:
            if node.name is None:
                return ERROR
        return OK 
