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
public class nodoSemantico {

    String lexema;
    int tipo;
    nodoSemantico sig = null;

    nodoSemantico(String lexema, int tipo) {
        this.lexema = lexema;
        this.tipo = tipo;
    }
}
