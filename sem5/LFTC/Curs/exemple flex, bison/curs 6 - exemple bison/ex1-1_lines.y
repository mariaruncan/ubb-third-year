%{
#include <stdio.h>
%}

%token LETTER

%%
input :  /* secv. vida */	    { printf("OK\n");}	
        | line  '\n' input
        ;

line :
      | LETTER line
     ;

%%
yylex() {
        int c;
        c = getchar();
	    if (isalpha(c))
				return LETTER;
        return c;
}

yyerror()
{
    printf("syntax error\n");
}

main()
{
    yyparse();
}
