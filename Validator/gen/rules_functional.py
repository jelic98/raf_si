from gen.models import *
class FunctionalRules:

	def actorCanOnlyRealizeActors(self, nodes):
		for n in nodes:
			for p in n.parent:
				if type(n) == Actor and type(p) != Actor:
					return "Actor can only realize actors"

	def actorCannotIncludeAnything(self, nodes):
		for n in nodes:
			if type(n) == Actor and len(n.includes) > 0:
				return "Actor cannot include anything"

	def actorCannotExtendAnything(self, nodes):
		for n in nodes:
			if type(n) == Actor and len(n.extends) > 0:
				return "Actor cannot extend anything"

	def actorCannotBeAssociatedWithActors(self, nodes):
		for n in nodes:
			for a in n.associations:
				if type(n) == Actor and type(a) == Actors:
					return "Actor cannot be associated with actors"

	def useCaseCanOnlyRealizeUseCases(self, nodes):
		for n in nodes:
			for p in n.parent:
				if type(n) == UseCase and type(p) != UseCase:
					return "Use case can only realize use cases"
