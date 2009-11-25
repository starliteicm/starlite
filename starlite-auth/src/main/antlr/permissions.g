grammar Permissions;

tokens {
	AND			:	"&&" ;
	OR			:	"||" ;
	NOT			:	'!' ;
	LPAREN		:	'(' ;
	RPAREN		:	')'	;
}

/*
 *	PARSER RULES	
 */
expr	:	orExpr ;
orExpr	:	andExpr (OR andExpr)* ;
andExpr	:	notExpr (AND notExpr)* ;
notExpr	:	(NOT)? PERMISSION ;

/*
 *	LEXER RULES	
 */
PERMISSION	:	('a'..z'|'A'..'Z'|'0'..'9)+ ;
