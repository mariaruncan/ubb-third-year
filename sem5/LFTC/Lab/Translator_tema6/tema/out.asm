%include 'io.inc'
global main
section .text
main:
        call io_readint
        mov [a], eax

        mov eax, 0
        add eax, [a]
        mov edx, 0
        mov ebx, 2
        idiv ebx

        mov ebx, 3
        imul ebx

        mov edx, 0
        mov ebx, 6
        idiv ebx

        mov [b], eax
        mov eax, [b]
        call io_writeint


        ret


section .data
        a dd 0
        b dd 0