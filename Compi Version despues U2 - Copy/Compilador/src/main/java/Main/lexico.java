/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.RandomAccessFile;

/**
 *
 * @author glenn
 */
class lexico {

    nodo cabeza = null, p;
    int estado = 0, columna, valorMT, numRenglon = 1;
    int caracter = 0;
    String lexema = "";
    boolean errorEncontrado = false;

    String archivo = "C:\\Users\\glenn\\Documents\\Escuela\\Semestre 7\\Lenguajes y Automatas II\\Compi Version despues U2 - Copy\\Compilador\\semantico2.txt";

    int matriz[][] = { //                                                                                         ponerle 13 v
        //     L    D    _    .    '    +    -    *    /    >    <    =    (    )    ,    ;    :    {    }    Eb   Tab  Nl   Eof  Oc
        //     0    1    2    3    4    5    6    7    8    9    10   11   12   13   14   15   16   17   18   19   20   21   22   23
        /*0*/ {1  , 2  , 500, 117, 5  , 104, 105, 106, 107, 6  , 7  , 113, 115, 116, 120, 118, 8  , 9  , 500, 0  , 0  , 0  , 0  , 500},
        /*1*/ {1  , 1  , 1  , 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100},
        /*2*/ {101, 2  , 101, 3  , 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101},
        /*3*/ {501, 4  , 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501},        
        /*4*/ {102, 4  , 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102},
        /*5*/ {5  , 5  , 5  , 5  , 103, 5  , 5  , 5  , 5  , 5  , 5  , 5  , 5  , 5  , 5  , 5  , 5  , 5  , 5  , 5  , 5  , 503, 503, 5},
        /*6*/ {108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 110, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108},
        /*7*/ {109, 109, 109, 109, 109, 109, 109, 109, 109, 112, 109, 111, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109},
        /*8*/ {119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 114, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119},
        /*9*/ {9  , 9  , 9  , 9  , 9  , 9  , 9  , 9  , 9  , 9  , 9  , 9  , 9  , 9  , 9  , 9  , 9  , 9  , 0  , 9  , 9  , 9  , 502 , 9}
    };

    String palReservadas[][] = {
        {"and", "200"},
        {"or", "201"},
        {"not", "202"},
        {"verdadero", "203"},
        {"falso", "204"},
        {"leer", "205"},
        {"escribir", "206"},
        {"si", "207"},
        {"entonces", "208"},
        {"fin_si", "209"},
        {"sino", "210"},
        {"mientras", "211"},
        {"fin_mientras", "212"},
        {"hacer", "213"},
        {"algoritmo", "214"},
        {"es", "215"},
        {"inicio", "216"},
        {"fin", "217"},
        {"entero", "218"},
        {"decimal", "219"},
        {"cadena", "220"},
        {"logico", "221"}
    };

    String Errores[][] = {
        {"carácter no valido", "500"},
        {"número no valido o mal formado", "501"},
        {"comentario no cerrado", "502"},
        {"cadena no cerrada", "503"},};

    RandomAccessFile file = null;

    public lexico() {
        try {
            file = new RandomAccessFile(archivo, "r");
            while (caracter != -1) {
                caracter = file.read();

                if (Character.isLetter(((char) caracter))) {
                    columna = 0;
                } else if (Character.isDigit(((char) caracter))) {
                    columna = 1;
                } else {
                    switch ((char) caracter) {
                        case '_':
                            columna = 2;
                            break;
                        case '.':
                            columna = 3;
                            break;
                        case 39:
                            columna = 4;
                            break;
                        case '+':
                            columna = 5;
                            break;
                        case '-':
                            columna = 6;
                            break;
                        case '*':
                            columna = 7;
                            break;
                        case '/':
                            columna = 8;
                            break;
                        case '>':
                            columna = 9;
                            break;
                        case '<':
                            columna = 10;
                            break;
                        case '=':
                            columna = 11;
                            break;
                        case '(':
                            columna = 12;
                            break;
                        case ')':
                            columna = 13;
                            break;
                        case ',':
                            columna = 14;
                            break;
                        case ';':
                            columna = 15;
                            break;
                        case ':':
                            columna = 16;
                            break;
                        case '{':
                            columna = 17;
                            break;
                        case '}':
                            columna = 18;
                            break;
                        case 32:
                            columna = 19;
                            break;
                        case 9:
                            columna = 20;
                            break;
                        case 10: {
                            columna = 21;
                            numRenglon = numRenglon + 1;
                        }
                        case 13:
                            columna = 22;
                            break;
                        default:
                            columna = 23;
                    }
                }

                valorMT = matriz[estado][columna];

                if (valorMT < 100) {
                    estado = valorMT;

                    if (estado == 0) {
                        lexema = "";
                    } else {
                        lexema = lexema + (char) caracter;
                    }

                } else if (valorMT >= 100 && valorMT < 500) {
                    if (valorMT == 100) {
                        validarPalabraReservada();
                    }
                    if (valorMT == 100 || valorMT == 101 || valorMT == 102  || valorMT == 108 || valorMT == 109 || valorMT == 119 || valorMT >= 200) {
                        file.seek(file.getFilePointer() - 1);
                    } else {
                        lexema = lexema + (char) caracter;
                    }

                    insertarNodo();
                    estado = 0;
                    lexema = "";
                } else {
                    imprimirError();
                    break;
                }
            }
            imprimirNodos();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (file != null) {
                    file.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void imprimirError() {
        if (caracter != -1 && valorMT >= 500) {
            for (String[] errores : Errores) {
                if (valorMT == Integer.valueOf(errores[1])) {
                    System.out.println("El error encontrado fue " + errores[0] + " error " + valorMT + " caracter " + caracter + " en el renglon " + numRenglon);
                }
            }
            errorEncontrado = true;
            
        }
    }

    private void validarPalabraReservada() {
        for (String[] palReservada : palReservadas) {
            if (lexema.equals(palReservada[0])) {
                valorMT = Integer.valueOf(palReservada[1]);
            }
        }
    }

    private void insertarNodo() {
        nodo nodo = new nodo(lexema, valorMT, numRenglon);

        if (cabeza == null) {
            cabeza = nodo;
            p = cabeza;
        } else {
            p.sig = nodo;
            p = nodo;
        }
    }

    private void imprimirNodos() {
        p = cabeza;
        while (p != null) {
            System.out.println(p.lexema + " " + p.token + " " + p.renglon);
            p = p.sig;
        }
    }
}
