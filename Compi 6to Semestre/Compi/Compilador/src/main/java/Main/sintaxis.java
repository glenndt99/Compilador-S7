/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.util.Stack;

public class sintaxis {

    String errores[][] = {
        //0                     1
        /*0*/{"Se espera la palablra 'algoritmo'", "301"},
        /*1*/ {"Se esperaba un identificador", "302"},
        /*2*/ {"Se esperaba el simbolo (", "303"},
        /*3*/ {"Se esperaba el simbolo )", "304"},
        /*4*/ {"Se esperaba la palabra 'es'", "305"},
        /*5*/ {"Se esperaba el simbolo ':'", "306"},
        /*6*/ {"Se esperaba un tipo de variable: 'entero', 'decimal', 'cadena' o 'logico'", "307"},
        /*7*/ {"Se esperaba un asignador :=", "308"},
        /*8*/ {"Se espeaba la palabra 'fin'", "309"},
        /*9*/ {"Algoritmo cerrado, escribe codigo entre las etiquetas 'algoritmo' y 'fin'", "310"},
        /*10*/ {"Se esperaba un simbolo ;", "311"},
        /*11*/ {"Se esperaba un operador aritmetico (+, -, /, *) o un ''", "312"},
        /*12*/ {"se esperaba la palabra 'inicio'", "313"},
        /*13*/ {"Se espera una accion", "314"},
        /*14*/ {"Se espera 'fin_mientras' ", "315"},
        /*14*/ {"Se espera 'fin_si' ", "316"},
        /*11*/ {"Se esperaba un operador relacional (>, <, >=,<=,<>, =)", "317"},};

    //Errores despues de 330 son semantico
    String erroresSemantico[][] = {
        /*0*/{"Error semantico nombre de algoritmo duplicado", "330"},
        /*1*/ {"Error semantico variable ya declarada", "331"},
        /*2*/ {"Error semantico variable no existe", "332"},
        /*3*/ {"Error semantico incompatibilidad de tipos declarados", "333"},};

    nodo p;
    boolean errorEncontrado = false;
    String temporalSemantico, lexemaSemantico;
    int tipoSemantico;
    nodoSemantico cabezaSemantico = null, pSemantico, recorridoSemantico;
    
    Stack< Integer> E = new Stack<>(); //Pila entrada
    Stack< Integer> Ei = new Stack<>(); //Pila entrada invertida
    Stack< Integer> O = new Stack<>(); //Pila temporal para operadores
    Stack< Integer> S = new Stack<>(); //Pila salida
    Stack< Integer> Saux = new Stack<>(); //Salida invertida
    
    Stack< String> Es = new Stack<>(); //Pila entrada lexema
    Stack< String> Eis = new Stack<>(); //Pila entrada invertida lexema
    Stack< String> Os = new Stack<>(); //Pila entrada lexema
    
    
        
    matrices matriz = new matrices();

    sintaxis(nodo cabeza) {

        p = cabeza;
        try {
            while (p != null) {
                if (p.token == 214) { //algoritmo
                    p = p.sig;
                    if (p.token == 100) { //identificador

                        temporalSemantico = p.lexema;

                        p = p.sig;
                        if (p.token == 115) { // (
                            p = p.sig;
                            if (p.token == 116) { // )
                                p = p.sig;
                                if (p.token == 215) { //es
                                    p = p.sig;
                                    decVariable();
                                    if (p.token == 216) {//inicio
                                        accion();
                                        if (p.token == 217) { //fin                                            
                                            p = null;                                            
                                        } else {
                                            imprimirMensajeError(309);//esperaba fin
                                        }
                                    } else {
                                        imprimirMensajeError(313); //esperaba inicio
                                    }
                                } else {
                                    imprimirMensajeError(305);//se esperaba la palabra 'es'
                                }
                            } else {
                                imprimirMensajeError(304); //se esperaba el simbolo ')'
                            }
                        } else {
                            imprimirMensajeError(303); //se esperaba el simbolo '('
                        }
                    } else {
                        imprimirMensajeError(302); //se esperaba un identificador
                    }
                } else {
                    imprimirMensajeError(301); //se esperaba la palabra 'algoritmo'
                }
            }
        } catch (Exception e) {            
            System.out.println("Fin de archivo inesperado");
        }
    }

    private void decVariable() {
        if (p.token == 100) { //identificador
            if (p.lexema.equals(temporalSemantico)) {

                imprimirMensajeErrorSemantico(330); //error semantico nombre de algoritmo duplicado

            }
            //primero checar valores nodosSemanticos con un for o algo asi y arrojar error de duplicado
            checarVariablesSemantico();
            //Agregar nodoSemantico.lexma
            
            
            lexemaSemantico = p.lexema;

            p = p.sig;
            if (p.token == 119) { // simbolo :
                p = p.sig;
                if (p.token == 218 || p.token == 219 || p.token == 220 || p.token == 221) { // tipos de variables
                    //agregar nodoSemantico.tipo

                    switch (p.token) {
                        case 218:
                            tipoSemantico = 101;
                            break;
                        case 219:
                            tipoSemantico = 102;
                            break;
                        case 220:
                            tipoSemantico = 103;
                            break;
                        default:
                            tipoSemantico = p.token;
                    }

                    p = p.sig;

                    //avanzar nodoSemantico
                    insertarNodoSemantico();
                    if (p.token == 118) {  // ;
                        p = p.sig;
                        decVariable();
                    }
                } else {
                    imprimirMensajeError(307); //Se esperaba un tipo de variable
                }
            } else {
                imprimirMensajeError(306); //se esperaba el sibmolo ':'
            }
        } else {
            imprimirMensajeError(302); //se esperaba un identificador
        }

    }

    private void insertarNodoSemantico() {
        nodoSemantico nodoSemantico = new nodoSemantico(lexemaSemantico, tipoSemantico);

        if (cabezaSemantico == null) {
            cabezaSemantico = nodoSemantico;
            pSemantico = cabezaSemantico;
            recorridoSemantico = cabezaSemantico;
        } else {
            pSemantico.sig = nodoSemantico;
            pSemantico = nodoSemantico;
        }
    }
    

    private void imprimirMensajeError(int numerror) {
        for (String[] error : errores) {
            if (numerror == Integer.valueOf(error[1])) {
                System.out.println("El error encontrado es: " + error[0] + " error " + numerror + " en el renglon " + p.renglon);
            }
        }
        errorEncontrado = true;
        p = null;
        System.exit(0);
    }

    private void imprimirMensajeErrorSemantico(int numerror) {
        for (String[] error : erroresSemantico) {
            if (numerror == Integer.valueOf(error[1])) {
                System.out.println("El error encontrado es: " + error[0] + " error " + numerror + " en el renglon " + p.renglon);
            }
        }
        errorEncontrado = true;
        p = null;
        System.exit(0);
    }

    private void accion() {
        
        
        p = p.sig;
        switch (p.token) {
            case 100:  //identificador
                        
                checarExistencia();
                
                p = p.sig;
                if (p.token == 114) {// :=
                    E.add(114);
                    p = p.sig;
                    expresionNumerica();
                    crearPilaNormal();
                    
                    
                    //infijoposfijo();
                }
                break;

            case 205: //leer
                leer();
                break;
            case 206: //escribir
                escribir();
                break;
            case 207: //si                   
                si();
                break;
            case 211: // mientras
                mientras();
            default:

                break;
        }

        if (p.token == 118) { // ;
            accion();
        }
    }

    private void expresionNumerica() {

        if (p.token == 203 || p.token == 204) {
            E.add(221);
        } else {
            if (p.token == 100) {
                checarExistencia();
            } else {
                E.add(p.token);
            }
        }
        switch (p.token) {
            case 115: // (
                p = p.sig;
                expresionNumerica();

                if (p.token == 116) { // )
                    p = p.sig;
                    expresionNumerica1();
                } else {
                    imprimirMensajeError(304); // se espera )
                }
                break;
            case 105: // -
                p = p.sig;
                expresionNumerica();
                expresionNumerica1();
                break;
            case 100: // id     
                p = p.sig;
                expresionNumerica1();
                break;
            case 101: // num entero       
                p = p.sig;
                expresionNumerica1();
                break;
            case 102: // num decimal    
                p = p.sig;
                expresionNumerica1();

                break;
            case 203: // true
                p = p.sig;
                expresionNumerica1();
                break;
            case 204: //false    
                p = p.sig;
                expresionNumerica1();
                break;
            case 103: //cadena       
                p = p.sig;
                expresionNumerica1();
                break;
                
            default:
                break;
        }
    }

    private void expresionNumerica1() {
        if (p.token == 118 || p.token == 217 || p.token == 116) { // ; || fin || entonces || hacer || )
        } else {
            E.add(p.token);
        }
        switch (p.token) {
            case 104: // +    
                p = p.sig;
                expresionNumerica();
                expresionNumerica1();
                break;
            case 105: // -
                p = p.sig;
                expresionNumerica();
                expresionNumerica1();
                break;
            case 106: // *
                p = p.sig;
                expresionNumerica();
                expresionNumerica1();
                break;
            case 107: // /
                p = p.sig;
                expresionNumerica();
                expresionNumerica1();
                break;
            default:
                break;
        }
    }

    private void expresionLogica() {
        p = p.sig;
        //E.add(p.token);
        switch (p.token) {
            case 115: // (
                expresionLogica();
                //p = p.sig;
                if (p.token == 116) { // )  
                    p = p.sig;
                    expresionLogica1();
                } else {
                    imprimirMensajeError(304); // se espera )
                }

                break;
            case 202: // not
                E.add(p.token);
                expresionLogica();
                expresionLogica1();
                break;
            case 100: // id 

                if (p.sig.token == 108 || p.sig.token == 110 || p.sig.token == 109 || p.sig.token == 111 || p.sig.token == 113 || p.sig.token == 112 || p.sig.token == 203 || p.sig.token == 204) { // operador relacional >, < >=, <=, <>, =                                        
                    expresionRelacional();
                    expresionLogica1();

                } else {
                    checarExistencia();
                    p = p.sig;
                    expresionLogica1();
                }
                break;
            case 203: // true
                expresionLogica1();
                break;
            case 204: // false
                expresionLogica1();
                break;
            default:
                expresionRelacional();
                expresionLogica1();
        }
    }

    private void expresionRelacional() {
        //E.add(p.token);

        expresionNumerica();
        if (p.token == 108 || p.token == 109 || p.token == 110 || p.token == 111 || p.token == 112 || p.token == 113) { // operador relacional >, < >=, <=, <>, =            
            p = p.sig;
            expresionNumerica();
        } else {
            imprimirMensajeError(317);
        }
    }

    private void leer() {
        p = p.sig;
        if (p.token == 100) { //identificador
            p = p.sig;
            if (p.token == 120) { // ,
                leer();
            }
        } else {
            imprimirMensajeError(302);
        }

    }

    private void escribir() {
        p = p.sig;
        if (p.token == 100) { //identificador
            p = p.sig;
            if (p.token == 120) { // ,
                escribir();
            }
        } else {
            imprimirMensajeError(302);
        }
    }

    private void si() {

        expresionLogica();
        crearPilaNormal();        

        if (p.token == 208) { //entonces
            accion();
            if (p.token == 210) { // sino
                accion();
            }
            if (p.token == 209) { //fin_si
                p = p.sig;
            } else {
                imprimirMensajeError(316); // espera fin-si
            }
        } else {
            imprimirMensajeError(316); // espera fin-si
        }
    }

    private void mientras() {
        expresionLogica();
        crearPilaNormal();        

        if (p.token == 213) { //hacer
            accion();
            if (p.token == 212) { //fin-mientras   
                p = p.sig;
            } else {
                imprimirMensajeError(315);
            }
        }
    }

    private void expresionLogica1() {
        if (p.token == 200 || p.token == 201) { // and || or  
            E.add(p.token);
            expresionLogica();
            expresionLogica1();
        }
    }

    private void checarVariablesSemantico() {
        while (recorridoSemantico != null) {
            if (p.lexema.equals(recorridoSemantico.lexema)) {
                imprimirMensajeErrorSemantico(331); // Error semantico variable ya decalrada
            }
            recorridoSemantico = recorridoSemantico.sig;
        }
        recorridoSemantico = cabezaSemantico;
    }

    private void infijoposfijo() {
        //Inversion de la pila E hacia Ei

        Ei.push(116);
        while (!E.empty()) {
            Ei.push(E.pop());
        }
        Ei.push(115);

        //System.out.println("Invertida: " + Ei);
        while (!Ei.empty()) {
            switch (jerarquia(Ei.peek())) {
                case 0: // ---> :=
                    O.push(Ei.pop());
                    break;
                case 1: // ---> (
                    O.push(Ei.pop());
                    break;
                case 2: // ---> )
                    while (!O.peek().equals(115)) {
                        S.push(O.pop());
                    }
                    O.pop();
                    Ei.pop();
                    break;
                case 3: // and or
                case 4:   // not                 
                case 5: // relacional
                case 6: // + o -
                case 7: // * o /
                    while (jerarquia(O.peek()) >= jerarquia(Ei.peek())) {
                        S.push(O.pop());
                    }
                    O.push(Ei.pop());
                    break;
                case 8: // Token default.
                    S.push(Ei.pop());
                    break;
            }
        }

        System.out.println("Salida: " + S);
        System.out.println("");

        evaluarposfijo();
        O.removeAllElements();
        E.removeAllElements();
        Ei.removeAllElements();
        S.removeAllElements();
        Saux.removeAllElements();

    }

    private static int jerarquia(int op) {
        int prf = 8; // ---> Token default.

        if ((op == 106) || (op == 107)) { // * o /
            prf = 7;
        }
        if ((op == 104) || (op == 105)) { //  + o -
            prf = 6;
        }
        if ((op == 108) || (op == 109) || (op == 110) || (op == 111) || (op == 112) || (op == 113)) { // relacionales
            prf = 5;
        }
        if (op == 202) { // not
            prf = 4;
        }
        if ((op == 200) || (op == 201)) { // and or
            prf = 3;
        }
        if (op == 116) { // )
            prf = 2;
        }
        if (op == 115) { // (
            prf = 1;
        }
        if (op == 114) {// :=
            prf = 0;
        }

        return prf;
    }

    private void checarExistencia() {
        if (p.lexema.equals(temporalSemantico)) {

            imprimirMensajeErrorSemantico(330); //error semantico nombre de algoritmo duplicado
            System.exit(0);
        }
        boolean variableE = false;
        while (recorridoSemantico != null) {
            if (p.lexema.equals(recorridoSemantico.lexema)) {

                
                E.add(recorridoSemantico.tipo);
                variableE = true;
            }
            recorridoSemantico = recorridoSemantico.sig;
        }
        recorridoSemantico = cabezaSemantico;
        if (variableE == false) {
            imprimirMensajeErrorSemantico(332);
        }
    }

    private void crearPilaNormal() {
        if (p.token == 203 || p.token == 204) {
            E.add(221);
        } else {
            if (p.token == 118 || p.token == 217 || p.token == 208 || p.token == 213) { // ; || fin || entonces || hacer
            } else {
                E.add(p.token);
            }
        }
        
        System.out.println("Original: " + E);
        infijoposfijo();
        E.removeAllElements();

    }

    private void evaluarposfijo() {
        int columna = 0;
        int renglon = 0;
        boolean flag = false;
        int operando1 = 0, operando2 = 0;
        for (int i = 0; i < S.size(); i++) {
            int op = S.get(i);
            /* suma         resta       multiplicacion    division       asignacion etc*/
            if ((op == 104) || (op == 105) || (op == 106) || (op == 107) || (op == 114) || (op == 108) || (op == 109) || (op == 110) || (op == 111) || (op == 113) || (op == 112) || (op == 200) || (op == 201) || (op == 202)) {
                if (op == 202) {
                    operando1 = Saux.peek();
                } else {
                    operando1 = Saux.pop();
                    operando2 = Saux.pop();
                }
                switch (operando1) {
                    case 101:
                        columna = 0;
                        break;
                    case 102:
                        columna = 1;
                        break;
                    case 103:
                        columna = 2;
                        break;
                    case 221:
                        //case 203:
                        //case 204:
                        columna = 3;
                        break;
                }
                switch (operando2) {
                    case 101:
                        renglon = 0;
                        break;
                    case 102:
                        renglon = 1;
                        break;
                    case 103:
                        renglon = 2;
                        break;
                    case 221:
                        //case 203:
                        //case 204:
                        renglon = 3;
                        break;
                }
                switch (op) {
                    case 104: // suma
                        if (matriz.opAritmeticosSuma[renglon][columna] == 0) {
                            flag = true;
                        } else {
                            Saux.push(matriz.opAritmeticosSuma[renglon][columna]);
                        }
                        break;
                    case 105: //resta
                        if (matriz.opAritmeticosRestaMulti[renglon][columna] == 0) {
                            flag = true;
                        } else {
                            Saux.push(matriz.opAritmeticosRestaMulti[renglon][columna]);
                        }
                        break;
                    case 106: // multiplicacion
                        if (matriz.opAritmeticosRestaMulti[renglon][columna] == 0) {
                            flag = true;
                        } else {
                            Saux.push(matriz.opAritmeticosRestaMulti[renglon][columna]);
                        }
                        break;
                    case 107: // division
                        if (matriz.opAritmeticosDivision[renglon][columna] == 0) {
                            flag = true;
                        } else {
                            Saux.push(matriz.opAritmeticosDivision[renglon][columna]);
                        }
                        break;
                    case 108: //>
                    case 109: // <
                    case 110: // >=   
                    case 111: // <=    
                        if (matriz.relMayorMenorEIguales[renglon][columna] == 0) {
                            flag = true;
                        } else {
                            Saux.push(matriz.relMayorMenorEIguales[renglon][columna]);
                        }
                        break;
                    case 113: // =
                    case 112: // <>
                        if (matriz.relDiferenteIgualA[renglon][columna] == 0) {
                            flag = true;
                        } else {
                            Saux.push(matriz.relDiferenteIgualA[renglon][columna]);
                        }
                        break;
                    case 200: //and
                    case 201:// or
                        if (!matriz.LogicoAndOr[renglon][columna]) {
                            flag = true;
                            break;
                        } else{
                            Saux.push(221);
                        }
                        break;
                    case 114: //asignacion
                        if (!matriz.opAsignacion[renglon][columna]) {
                            flag = true;
                            break;
                        }
                        break;
                    case 202:
                        if (operando1 != 221) {
                            flag = true;
                            break;
                        }
                        break;
                    default:
                        break;
                }
            } else {
                Saux.push(S.get(i));
            }
            if (flag) {
                imprimirMensajeErrorSemantico(333);
                
                break;
            }
        }
    }
    
    

}
