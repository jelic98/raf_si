rule "Name cannot be null"
for nodes as n if n.name is null

rule "Name is already defined"
for nodes as n1 and nodes as n2 if name of n1 is name of n2

rule "Class can only extend classes"
for nodes as n if n is type Class and parentClass of n not type Class

rule "Class can only implement interfaces"
for nodes as n if n is type Class and parentClass of n not type Interface

rule "Interface can only extend interfaces"
for nodes as n if n is type Interface and parentClass of n not type Class

rule "Final class cannot be extended"
for nodes as n1 and nodes as n2 if parentClass of n2 is n1 and final of n1

rule "Child class cannot have multiple parent classes"
for nodes as n if length of parentClass of n gt 1

rule "All attributes of interface must be public"
for nodes as n and attributes of n as a if n is type Interace and visibility of a not PUBLIC

rule "All methods of interface must be public"
for nodes as n and methods of n as m if n is type Interace and visibility of m not PUBLIC

rule "Conflicting attributes"
for nodes as n and attributes of n as a1 and attributes of n as a2 if name of a1 is name of a2
