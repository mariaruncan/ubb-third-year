%{
/* 
* Lexical analyzer for the toy language 
*/
#include <string.h> /* for strcpy */

#include "bAttrib.h"
#include "bComp.tab.h"

extern int lineno;  

%}

alpha   [A-Za-z]
digit   [0-9]
alphanum  [A-Za-z0-9]

%%
[ \t\r]        /*   ignore white spaces */
[\n]                    lineno++;
                       
"var"                   return VAR;
"integer"               return INTEGER;
"begin"                 return BEGINPAR;
"end"                   return ENDPAR;
"read"                  return READ;
"writeln"               return WRITE;

{alpha}{alphanum}*      {
                         strcpy(yylval.varname,yytext);
                         return ID;
                         }

{digit}+                {strcpy(yylval.varname,yytext);    
                        return CONST;
                        }

"("                     return LPAREN;
")"                     return RPAREN;
":="                    return ASSIGN;
";"                     return SEMICOLON;
":"                     return COLON;
"+"                     return PLUS;
"."                     return DOT;

.                       { 
                        printf( "My: Illegal character \n %s \n on line #%d\n",
                                yytext,lineno);
                        
                        }

%%

int
yywrap() {
  return 1; /* tell scanner no further files need to be processed */
}

