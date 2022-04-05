INCLUDE MACROS.MAC
.MODEL SMALL
.586
.STACK  100h
.DATA
        a DB '0'
        b DB '0'
        d DB 'Ingrese un numero mayor a 3', 13,10, '$'
        e DB '0'
        f DB 'Fin Programa buen dia', 13,10, '$'
        resultadoCadena DB 'cadenas', 13,10,'$'
        resultadoNormal DB '0'
        imprimir DB ?
.CODE
    .STARTUP
        MOV     AX, @DATA
        MOV     DS, AX
        CALL     COMPI
        MOV     AX, 4C00H
        INT     21H
COMPI     PROC
        SUMAR 2,1, resultadoNormal
        I_ASIGNAR a,resultadoNormal
        I_ASIGNAR resultadoNormal,1
        I_ASIGNAR b,resultadoNormal
        LEELN d
        LEER e
D1:
        I_IGUAL b,1,resultadoNormal
        CMP resultadoNormal,1
        JNE E1
D2:
        I_MENORIGUAL a,e,resultadoNormal
        CMP resultadoNormal,1
        JNE E2
        SUMAR 1,a, resultadoNormal
        I_ASIGNAR a,resultadoNormal
        WRITE a
        JMP D2
E2:
A1:
        I_MENOR e,a,resultadoNormal
        CMP resultadoNormal,1
        JNE B1
        I_ASIGNAR resultadoNormal,0
        I_ASIGNAR b,resultadoNormal
        JMP C1
B1:
C1:
        JMP D1
E1:
        LEELN f
ret
COMPI ENDP
.EXIT
END