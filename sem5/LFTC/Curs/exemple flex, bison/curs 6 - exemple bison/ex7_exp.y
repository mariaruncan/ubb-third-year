%{
#include <stdio.h>
%}

%token DIGIT
%token END

%%
line : 	expr END    {  printf("%d\n",$1); }
	 ;
expr  : term '+' expr 	{ $$ = $1 + $3;}
	  | term			{ $$ = $1;}
	;
term  : fact '*' term 	{ $$ = $1 * $3;}
	  | fact			{ $$ = $1;}
	;
fact  : DIGIT				{ $$ = $1;}
     ;

%%

yyerror()
{
    printf("syntax error\n");
}

main()
{
    yyparse();
}
