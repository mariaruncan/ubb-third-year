%{
#include <stdio.h> /* for (f)printf() */
#include <stdlib.h> /* for exit() */
#include <string.h>
#include "bAttrib.h"
#include "bCodASM.h"

int  lineno = 1; /* number of current source line */
extern int yylex(); /* lexical analyzer generated from lex.l */
extern char *yytext; /* last token, defined in lex.l  */

void
yyerror(char *s) {
  printf("\n \n \nMy error: \n");
  printf( "Syntax error on line #%d: %s\n", lineno, s);
  printf( "Last token was \"%s\"\n", yytext);
  exit(1);
}

int tempnr = 1;
void newTempName(char* s){
  sprintf(s,"temp%d",tempnr);
  tempnr++;
}

char tempbuffer[250];
char datasegmentbuffer[500];

void addTemps2String(char* str){
 int i;
 for(i=1;i<tempnr;i++){
    sprintf(tempbuffer,"temp%d dw ?\n",i);
    strcat(str,tempbuffer);
 }
}

%}

%union {
 char varname[10];
 attributes pairAttrib;
}

%token VAR
%token BEGINPAR
%token ENDPAR
%token INTEGER

%token LPAREN
%token RPAREN
%token ASSIGN
%token SEMICOLON
%token PLUS
%token WRITE
%token READ

%token COLON
%token DOT

%token <varname> ID 
%token <varname> CONST 
%type <pairAttrib> term
%type <pairAttrib> expression

%%
program :		{   strcpy(datasegmentbuffer,"");
                      printf(INCEPUT);
                }
            	 
		    BEGINPAR {
					printf(TEXTSEGINCEPUT);
					printf(TEXTSEGMIJLOC);
		          }
		    statement_list 
            ENDPAR   {
                       printf(TEXTSEGSFARSIT);        
                       printf(SFARSIT);
                    }
            DOT
            ;


statement_list  : statement_list SEMICOLON statement
                | statement
                ;
                                                                                   
statement       : assignment
                | read_statement
                | write_statement
                ;
                                        

assignment      : ID ASSIGN expression
                  {
                    printf("%s\n",$3.cod);
                    printf("mov ax, %s\n",$3.varn);
                    printf("mov %s,ax\n",$1);
                   }
                ;

read_statement  : READ LPAREN ID RPAREN { 
                       /*printf("call read_int %s\n - not implemented",$3);*/
                       }
                ;

write_statement : WRITE LPAREN expression RPAREN { 
                      /* printf("call print_int %s\n - not implemented",$3.varn); */
                       }
                ;

expression      : term {
                      strcpy($$.cod,$1.cod);
                      strcpy($$.varn,$1.varn);
                       }
                | expression PLUS term { 
                     newTempName($$.varn);
                     sprintf($$.cod,"%s\n%s\n",$1.cod,$3.cod);
                     sprintf(tempbuffer,ADD_ASM_FORMAT,$1.varn,$3.varn,$$.varn);
                     strcat($$.cod,tempbuffer);
                     }
                ;

term            : CONST {
                      strcpy($$.cod,"");
                      strcpy($$.varn,$1); 
                      }
                | ID { 
                        strcpy($$.cod,"");
                        strcpy($$.varn,$1); 
                      }
                     
                ;

%%

int
main(int argc,char *argv[]) {
  return yyparse();
} 
