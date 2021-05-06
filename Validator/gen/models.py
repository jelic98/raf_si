from enum import Enum, auto


class Class:
	def __init__(self):
		self.name = None
		self.attributes = []
		self.methods = []
		self.parent = []

class Interface:
	def __init__(self):
		self.name = None
		self.methods = []
		self.attributes = []
		self.parent = []

class Method:
	def __init__(self):
		self.name = None
		self.type = None
		self.visibility = None
		self.enclosing = None
		self.parameters = []

class Attribute:
	def __init__(self):
		self.name = None
		self.type = None
		self.visibility = None
		self.enclosing = None

class Parameter:
	def __init__(self):
		self.name = None
		self.type = None
		self.enclosing = None

class Visibility(Enum):
	PUBLIC = auto()
	PRIVATE = auto()
	PACKAGE = auto()
	PROTECTED = auto()
