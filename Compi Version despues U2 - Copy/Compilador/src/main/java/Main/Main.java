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
public class Main {

    nodo p;

    public static void main(String[] args) {
        lexico lexico = new lexico();
        if (!lexico.errorEncontrado) {
           System.out.println("Analisis Lexico Terminado");
           sintaxis sintaxis = new sintaxis(lexico.cabeza);
            if (!sintaxis.errorEncontrado) {
                System.out.println("Analisis Sintactico Terminado");
            }


        }
    }
}
