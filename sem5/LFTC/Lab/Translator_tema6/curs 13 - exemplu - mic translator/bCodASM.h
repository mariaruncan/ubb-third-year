#ifndef CODEASM_H
#define CODEASM_H

#define INCEPUT "\
ASSUME cs:code,ds:data \n\n\
"

#define DATASEGINCEPUT "\
; data - segmentul de date in care vom defini variabilele \n\
data SEGMENT \n\
"

#define DATASEGSFARSIT "\
\n\
data ENDS\n\n\
"

#define TEXTSEGINCEPUT "\
;code - numele segmentului de cod \n\
code SEGMENT\n\n\
"


#define TEXTSEGMIJLOC "\
start:\n\
mov ax, data\n\
mov ds, ax\n\n\
;urmeaza instructiunile programului nostru \n\
"


#define TEXTSEGSFARSIT "\
\n\
; e apelata intreruperea 21, cu ah incarcat cu 4C \n\
; adica sfarsitul executiei cu succes \n\
mov ax,4C00h\n\
int 21h\n\
code ENDS\n\n\n\
"

#define SFARSIT "\
END start\n\n\n\
"

#define ADD_ASM_FORMAT "\
mov ax, %s \n\
add ax, %s \n\
mov %s, ax \n\
" 

#define INT_ASM_FORMAT "\
%s dw ? \n\
"

#endif
