%{
#include <string.h>
#include <stdio.h>
#include <stdlib.h>

int posConst = 0;
int posId = 0;
int pos = 0;
int crtPosFip = 0;
int lineNumber = 1;

struct Node {
	int code; // 0 - id, 1 - const, 2 - other
    char* data;
	int position;
    struct Node* next;
};

struct Entry {
	int position;
	struct Node* node;
	struct Entry* next;
};

struct Entry* fip = NULL;
struct Node* tsId = NULL;
struct Node* tsConst = NULL;

void addConst(char* item) {
	struct Node* new = (struct Node*)malloc(sizeof(struct Node));
	new->code = 1;
	new->data = item;
	new->position = ++posConst;
	new->next = NULL;
	
	struct Node* current;
    if (tsConst == NULL || strcmp(tsConst->data, new->data) >= 0) {
        new->next = tsConst;
        tsConst = new;
    }
    else {
        current = tsConst;
        while (current->next != NULL && strcmp(current->next->data, new->data) < 0) {
            current = current->next;
        }
		new->next = current->next;
        current->next = new;
    }
}

void addId(char* item) {
	struct Node* new = (struct Node*)malloc(sizeof(struct Node));
	new->code = 0;
	new->data = item;
	new->position = ++posId;
	new->next = NULL;
	
	struct Node* current;
    if (tsId == NULL || strcmp(tsId->data, new->data) >= 0) {
        new->next = tsId;
        tsId = new;
    }
    else {
        current = tsId;
        while (current->next != NULL && strcmp(current->next->data, new->data) < 0) {
            current = current->next;
        }
		new->next = current->next;
        current->next = new;
    }
}

void addToFip(struct Entry* item) {
	if(fip == NULL) {
		fip = item;
	}
	else {
		struct Entry* current = fip;
		while(current->next != NULL) {
			current = current->next;
		}
		current->next = item;
	}
}

int findFipPos(char* item) {
	if(fip == NULL) return -1;
	struct Entry* current = fip;
	while(current != NULL) {
		if(strcmp(current->node->data, item) == 0) {
			return current->position;
		}
		current = current->next;
	}
	return -1;
}

int findConstPos(char* item) {
	if(tsConst == NULL) return -1;
	struct Node* current = tsConst;
	while(current != NULL) {
		if(strcmp(current->data, item) == 0) {
			return current->position;
		}
		current = current->next;
	}
	return -1;
}

int findIdPos(char* item) {
	if(tsId == NULL) return -1;
	struct Node* current = tsId;
	while(current != NULL) {
		if(strcmp(current->data, item) == 0) {
			return current->position;
		}
		current = current->next;
	}
	return -1;
}

struct Entry* createEntry(int code, char* data, int position) {
	struct Entry* entry = (struct Entry*)malloc(sizeof(struct Entry));
	struct Node* new = (struct Node*)malloc(sizeof(struct Node));
	new->code = code;
	new->data = data;
	new->position = 0;
	new->next = NULL;
	
	entry->position = position;
	entry->node = new;
	entry->next = NULL;
	
	return entry;
}

char* string_copy(char *string) {
    int size = (int)strlen(string);
    char* new_string = malloc((size+1) * sizeof (char));
    for (int i = 0; i <= size; ++i)
        new_string[i] = string[i];
    return new_string;
}

void printResults() {
	printf("\n\nTS ID\n");
	struct Node* crt = tsId;
	while(crt != NULL) {
		printf("%d\t%s\n", crt->position, crt->data);
		crt = crt->next;
	}
	printf("\n\n");
	
	printf("TS CONST\n");
	crt = tsConst;
	while(crt != NULL) {
		printf("%d\t%s\n", crt->position, crt->data);
		crt = crt->next;
	}
	printf("\n\n");
	
	printf("FIP\tpos\tts\n");
	struct Entry* el = fip;
	while(el != NULL) {
		struct Node* node = el->node;
		if(node->code == 0) {
			printf("%s\t%d\t%d\n", node->data, el->position, findIdPos(node->data));
		}
		else if(node->code == 1) {
			printf("%s\t%d\t%d\n", node->data, el->position, findConstPos(node->data));
		}
		else {
			printf("%s\t%d\n", node->data, el->position);
		}
		el = el->next;
	}
}

%}

%option noyywrap
%option caseless

digit				[0-9]
octal_digit			[0-7]
ndigit				[1-9]
hex_digit			[0-9a-fA-F]
hex_prefix			0[xX]	

digit_sequence		{digit}+
decimal_exponent	[eE][+-]?{digit_sequence}
suffix				[fF]

int_const		({ndigit}{digit}*)|(0{octal_digit}*)|({hex_prefix}{hex_digit}+)|(0[bB][01]+)
float_const  	({digit}+{decimal_exponent}{suffix}?)|({digit_sequence}\.{decimal_exponent}?{suffix}?)|({digit_sequence}?\.{digit_sequence}{decimal_exponent}?{suffix}?)
id   			[a-zA-Z][a-zA-Z0-9]{0,7}
bad_id			[a-zA-Z0-9]{9,} 	

%%

"#include"|"<iostream>"|"using"|"namespace"|"std"|"main"|"int"|"float"|"cin"|"cout"|"if"|"else"|"while" {
	char* data = string_copy(yytext);
	printf("keyword: %s\n", data);
	
	int position = findFipPos(data);
	if(position == -1) {
		struct Entry* entry = createEntry(2, data, ++pos);
		addToFip(entry);
	}
	else {
		struct Entry* entry = createEntry(2, data, position);
		addToFip(entry);
	}
}

";"|"("|")"|"{"|"}"|"," {
	char* data = string_copy(yytext);
	printf("separator: %s\n", data);
	
	int position = findFipPos(data);
	if(position == -1) {
		struct Entry* entry = createEntry(2, data, ++pos);
		addToFip(entry);
	}
	else {
		struct Entry* entry = createEntry(2, data, position);
		addToFip(entry);
	}
}

"!="|">>"|"<<"|"&&"|"=="|">"|"<"|"="|"-"|"+"|"*" {
	char* data = string_copy(yytext);
	printf("operator: %s\n", data);
	
	int position = findFipPos(data);
	if(position == -1) {
		struct Entry* entry = createEntry(2, data, ++pos);
		addToFip(entry);
	}
	else {
		struct Entry* entry = createEntry(2, data, position);
		addToFip(entry);
	}
}

{bad_id} { 
	printf("Illegal token %s at line %d !\n", yytext, lineNumber); 
	printResults();
	exit(1); 
}

{id} { 
	char* data = string_copy(yytext);
	printf("id: %s\n", data);
	
	if(findIdPos(data) == -1) {
		addId(data);
	}
	struct Entry* entry = createEntry(0, data, 0);
	addToFip(entry);
}

{int_const}	{ 
	char* data = string_copy(yytext);
	printf("int const: %s\n", data);
	
	if(findConstPos(data) == -1) {
		addConst(data);
	}
	struct Entry* entry = createEntry(1, data, 1);
	addToFip(entry);
}

{float_const} {
	char* data = string_copy(yytext);
	printf("float const: %s\n", data);
	
	if(findConstPos(data) == -1) {
		addConst(data);
	}
	struct Entry* entry = createEntry(1, data, 1);
	addToFip(entry);
}
\n|\r|\r\n					{ lineNumber++; }
[ \t]+ 	            		{ ; }
.                			{ 
	printf("Illegal token %s at line %d !\n", yytext, lineNumber); 
	printResults();
	exit(1); 
}
%%

int main(int argc, char** argv) {
	FILE *fp;
	fp = fopen(argv[1], "r");
	
	yyin = fp;
	yylex();
	
	printResults();
}