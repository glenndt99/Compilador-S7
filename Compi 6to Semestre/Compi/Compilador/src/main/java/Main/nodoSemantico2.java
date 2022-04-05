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
public class nodoSemantico2 {
    String lexema;
    int token;
    String etiqueta;
    nodoSemantico2 sig = null;
    
    nodoSemantico2(String lexema, int token, String etiqueta){
        this.lexema = lexema;
        this.token = token;
        this.etiqueta = etiqueta;
    }
}
