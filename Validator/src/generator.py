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
	'lte': 'LTE',
    'and': 'AND'}

tokens = ['ID', 'INT'] + list(reserved.values())

t_INT = r'\d+'

def t_ID(t):
	r'[a-zA-Z][a-zA-Z0-9]*'
	t.type = reserved.get(t.value, 'ID')
	return t

t_ignore = ' \t\n'

def t_error(t):
    print("Illegal character '%s'" % t.value[0])
    t.lexer.skip(1)

def p_rule(p):
    'rule : FOR iter IF logic'
    p[0] = f'\ndef {_m}(self, nodes):\n{p[2]}if {p[4]}:\nprint("<ERROR>")'

def p_iter(p):
    '''iter : var AS ID
            | iter AND iter'''
    if p[2] == 'and':
        p[0] = f'{p[1]}{p[3]}'
    else:
        p[0] = f'for {p[3]} in {p[1]}:\n'

def p_var(p):
    '''var : ID
            | ID OF var'''
    if len(p) > 2:
        if p[1] == 'length':
            p[0] = f'len({p[3]})'
        elif p[1] == 'type':
            p[0] = f'type({p[3]})'
        else:
            p[0] = f'{p[3]}.{p[1]}'
    else:
        p[0] = p[1]

def p_logic(p):
    '''logic : expr
            | expr AND expr'''
    if len(p) > 2:
        p[0] = f'{p[1]} {p[2]} {p[3]}'
    else:
        p[0] = p[1]

def p_expr(p):
    '''expr : term
            | term oper term'''
    if len(p) > 2:
        p[0] = f'{p[1]} {p[2]} {p[3]}'
    else:
        p[0] = p[1]

def p_term(p):
    '''term : var
            | INT'''
    if p[1].isdigit():
        p[0] = int(p[1])
    else:
        if p[1] == 'null':
            p[0] = 'None'
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

def parse(method, code, error):
    global _m
    _m = method
    lexer = lex.lex()
    parser = yacc.yacc()
    gen, out = '', parser.parse(code).replace('<ERROR>', error)
    for i, line in enumerate(out.split('\n')):
        gen += '\t'*i + line + '\n'
    return gen
