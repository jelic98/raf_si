import src.ply.lex as lex
import src.ply.yacc as yacc

reserved = {
	'for': 'FOR',
	'if': 'IF',
	'as': 'AS',
	'of': 'OF',
	'eq': 'EQ',
	'neq': 'NEQ',
	'gt': 'GT',
	'lt': 'LT',
	'gte': 'GTE',
	'lte': 'LTE'}

tokens = ['ID', 'INT'] + list(reserved.values())

t_INT = r'\d+'

def t_ID(t):
	r'[a-zA-Z_]+'
	t.type = reserved.get(t.value, 'ID')
	return t

t_ignore = ' \t'

def t_error(t):
    print("Illegal character '%s'" % t.value[0])
    t.lexer.skip(1)

def p_rule(p):
	'rule : FOR var AS ID IF expr'
	p[0] = f'for {p[4]} in {p[2]}:\n\tif {p[6]}:\n\t\tprint("ERROR")'

def p_var(p):
	'var : ID'
	p[0] = p[1]

def p_expr(p):
	'expr : term oper term'
	p[0] = f'{p[1]}{p[2]}{p[3]}'

def p_term(p):
	'''term : ID
			| INT'''
	if p[1].isdigit():
		p[0] = int(p[1])
	else:
		p[0] = p[1]

def p_oper(p):
	'''oper : EQ
			| NEQ
			| GT
			| LT
			| GTE
			| LTE'''
	if p[1] == 'eq':
		p[0] = '=='
	elif p[1] == 'neq':
		p[0] = '!='
	elif p[1] == 'gt':
		p[0] = '>'
	elif p[1] == 'lt':
		p[0] = '<'
	elif p[1] == 'gte':
		p[0] = '<='
	elif p[1] == 'lte':
		p[0] = '>='

def p_error(p):
    print("Syntax error at '%s'" % p.value)

def parse(code):
	lexer = lex.lex()
	parser = yacc.yacc()
	lexer.input(code)
	for tok in lexer:
		print(tok)
	return parser.parse(code)
