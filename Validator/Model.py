from enum import Enum, auto


class Class(DataType):

    def __init__(self):
        self.final = false
        self.static = false
        self.abstract = false
        self.name = None
        self.methods = []
        self.attributes = []
        self.parentClass = []


class Interface(DataType):

    def __init__(self):
        self.name = None
        self.methods = []
        self.attributes = []
        self.parentInterfaces = []


class Method:

    def __init__(self):
        self.final = false
        self.static = false
        self.abstract = false
        self.name = None
        self.type = None
        self.visibility = None
        self.enclosingClass = None
        self.parameters = []


class Attribute(NameTypePair):

    def __init__(self):
        self.final = false
        self.static = false
        self.visibility = None
        self.enclosingClass = None


class Parameter(NameTypePair):

    def __init__(self):
        self.enclosingMethod = None


class Visibility(Enum):

    PUBLIC = auto()
    PRIVATE = auto()
    PACKAGE = auto()
    PROTECTED = auto()


class NameTypePair:

    def __init__(self):
        self.name = None
        self.type = None


class Namespace:

    def __init__(self):
        pass


class DataType:

    def __init__(self):
        pass


class Actor:

    def __init__(self):
        self.parentActor = None
        self.uses = []


class UseCase:

    def __init__(self):
        self.parentUseCase = None
        self.includes = []
        self.extends = []
