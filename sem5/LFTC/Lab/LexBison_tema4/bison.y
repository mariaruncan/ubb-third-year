%{
#include <stdio.h>
#include <stdlib.h>

%}
%start program
%token INCLUDE
%token IOSTREAM
%token USING
%token NAMESPACE
%token STD
%token INT
%token FLOAT
%token MAIN
%token ID
%token CIN
%token COUT
%token CONST
%token IF
%token ELSE
%token WHILE
%token EQ
%token NEQ
%token LE
%token GE

%%

program :	bloc_importuri bloc_instructiuni
		;

bloc_importuri	:	INCLUDE IOSTREAM USING NAMESPACE STD ';'
				;
			
bloc_instructiuni	:	INT MAIN '(' ')' '{' lista_instructiuni '}'
					;
					
lista_instructiuni	:	instructiune lista_instructiuni
					|	instructiune
					;
					
instructiune	:	instr_declarativa
				|	instr_citire
				|	instr_scriere
				|	instr_atribuire
				|	instr_if
				|	instr_while
				;
				
instr_declarativa	:	tip lista_id ';'
					;

tip	:	INT
	|	FLOAT
	;
	
lista_id	:	ID ',' lista_id
			|	ID
			;
			
instr_citire	:	CIN ID ';'
				;
				
instr_scriere	:	COUT ID ';'
				;
				
instr_atribuire	:	ID '=' expresie ';'
				;
				
expresie	:	expresie '+' expresie
			|	expresie '-' expresie
			|	expresie '*' expresie
			|	expresie '/' expresie
			|	ID
			|	CONST
			;
				
instr_if	: IF '(' conditie ')' '{' lista_instructiuni '}' ELSE '{' lista_instructiuni '}'
			| IF '(' conditie ')' '{' lista_instructiuni '}'
			;
			
instr_while	: WHILE '(' conditie ')' '{' lista_instructiuni '}'
			;
			
conditie	:	expresie '>' expresie
conditie	|	expresie '<' expresie
			|	expresie EQ expresie
			|	expresie NEQ expresie
			|	expresie LE expresie
			|	expresie GE expresie
			;
%%

yyerror()
{
    printf("syntax error (from bison)\n");
}