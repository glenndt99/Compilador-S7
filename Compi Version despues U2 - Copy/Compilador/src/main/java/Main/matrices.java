/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

/**
 *
 * @author glenn
 */
public class matrices {

    boolean opAsignacion[][] = {
        //entero    decimal   cadena    logico
        /*entero */{true, false, false, false},
        /*decimal*/ {true, true, false, false},
        /*cadena */ {false, false, true, false},
        /*logico */ {false, false, false, true}
    };

    int opAritmeticosSuma[][] = {
        //entero    decimal   cadena    logico
        /*entero */{101, 102, 0, 0},
        /*decimal*/ {102, 102, 0, 0},
        /*cadena */ {0, 0, 103, 0},
        /*logico */ {0, 0, 0, 0}

    };

    int opAritmeticosRestaMulti[][] = {
        //entero    decimal   cadena    logico
        /*entero */{101, 102, 0, 0},
        /*decimal*/ {102, 102, 0, 0},
        /*cadena */ {0, 0, 0, 0},
        /*logico */ {0, 0, 0, 0}
    };

    int opAritmeticosDivision[][] = {
        //entero    decimal   cadena    logico
        /*entero */{102, 102, 0, 0},
        /*decimal*/ {102, 102, 0, 0},
        /*cadena */ {0, 0, 0, 0},
        /*logico */ {0, 0, 0, 0}
    };

    int relMayorMenorEIguales[][] = {
        //        entero     decimal      cadena      logico
        /*entero*/{221, 221, 0, 0},
        /*decimal*/ {221, 221, 0, 0},
        /*cadena*/ {0, 0, 0, 0},
        /*logico*/ {0, 0, 0, 0}
    };

    int relDiferenteIgualA[][] = {
        //        entero     decimal      cadena      logico
        /*entero */{221, 221, 0, 0},
        /*decimal*/ {221, 221, 0, 0},
        /*cadena */ {0, 0, 221, 0},
        /*logico */ {0, 0, 0, 221}
    };

    boolean LogicoAndOr[][] = {
        //entero    decimal   cadena    logico
        /*entero */{false, false, false, false},
        /*decimal*/ {false, false, false, false},
        /*cadena */ {false, false, false, false},
        /*logico */ {false, false, false, true}
    };

}
