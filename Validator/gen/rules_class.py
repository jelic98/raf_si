from gen.models import *
class ClassRules:

	def nameCannotBeNull(self, nodes):
		for n in nodes:
			if n.name == None:
				return "Name cannot be null"

	def nameIsAlreadyDefined(self, nodes):
		for n1 in nodes:
			for n2 in nodes:
				if n1 != n2 and n1.name == n2.name:
					return "Name is already defined"

	def classCanOnlyExtendClasses(self, nodes):
		for n in nodes:
			for p in n.parent:
				if type(n) == Class and type(p) != Class:
					return "Class can only extend classes"

	def interfaceCanOnlyExtendInterfaces(self, nodes):
		for n in nodes:
			for p in n.parent:
				if type(n) == Interface and type(p) != Interface:
					return "Interface can only extend interfaces"

	def finalClassCannotBeExtended(self, nodes):
		for n1 in nodes:
			for n2 in nodes:
				if n2.parent == n1 and n1.final:
					return "Final class cannot be extended"

	def childClassCannotHaveMultipleParentClasses(self, nodes):
		for n in nodes:
			if len(n.parent) > 1:
				return "Child class cannot have multiple parent classes"

	def allAttributesOfInterfaceMustBePublic(self, nodes):
		for n in nodes:
			for a in n.attributes:
				if type(n) == Interface and a.visibility != Visibility.PUBLIC:
					return "All attributes of interface must be public"

	def allMethodsOfInterfaceMustBePublic(self, nodes):
		for n in nodes:
			for m in n.methods:
				if type(n) == Interface and m.visibility != Visibility.PUBLIC:
					return "All methods of interface must be public"

	def conflictingAttributes(self, nodes):
		for n in nodes:
			for a1 in n.attributes:
				for a2 in n.attributes:
					if a1 != a2 and a1.name == a2.name:
						return "Conflicting attributes"
