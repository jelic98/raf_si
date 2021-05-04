def nameCannotBeNull(nodes):
	for n in nodes:
		if n.name == None:
			print("Name cannot be null")
				
def nameIsAlreadyDefined(nodes):
	for n1 in nodes:
		for n2 in nodes:
			if n1.name == n2.name:
				print("Name is already defined")
					
def classCanOnlyExtendClasses(nodes):
	for n in nodes:
		if type(n) == Class and type(n.parentClass) != Class:
			print("Class can only extend classes")
				
def classCanOnlyImplementInterfaces(nodes):
	for n in nodes:
		if type(n) == Class and type(n.parentClass) != Interface:
			print("Class can only implement interfaces")
				
def interfaceCanOnlyExtendInterfaces(nodes):
	for n in nodes:
		if type(n) == Interface and type(n.parentClass) != Class:
			print("Interface can only extend interfaces")
				
def finalClassCannotBeExtended(nodes):
	for n1 in nodes:
		for n2 in nodes:
			if n2.parentClass == n1 and n1.final:
				print("Final class cannot be extended")
					
def childClassCannotHaveMultipleParentClasses(nodes):
	for n in nodes:
		if len(n.parentClass) > 1:
			print("Child class cannot have multiple parent classes")
				
def allAttributesOfInterfaceMustBePublic(nodes):
	for n in nodes:
		for a in n.attributes:
			if type(n) == Interace and a.visibility != Visibility.PUBLIC:
				print("All attributes of interface must be public")
					
def allMethodsOfInterfaceMustBePublic(nodes):
	for n in nodes:
		for m in n.methods:
			if type(n) == Interace and m.visibility != Visibility.PUBLIC:
				print("All methods of interface must be public")
					
def conflictingAttributes(nodes):
	for n in nodes:
		for a1 in n.attributes:
			for a2 in n.attributes:
				if a1.name == a2.name:
					print("Conflicting attributes")
						
