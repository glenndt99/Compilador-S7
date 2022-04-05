/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.FileWriter;   // Import the FileWriter class
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
    int tipoSemantico,contS,contM;
    nodoSemantico cabezaSemantico = null, pSemantico, recorridoSemantico, cabezaASM = null, pASM, recorridoASM;
    nodoSemantico2 uno, tres = null;
    
    String codigo;
    String newLine = System.getProperty("line.separator");
    
    Stack< Integer> E = new Stack<>(); //Pila entrada
    Stack< Integer> Ei = new Stack<>(); //Pila entrada invertida
    Stack< Integer> O = new Stack<>(); //Pila temporal para operadores
    Stack< Integer> S = new Stack<>(); //Pila salida
    Stack< Integer> S2 = new Stack<>(); //Pila salida
    Stack< Integer> Saux = new Stack<>(); //Salida invertida
    Stack< Integer> S2i = new Stack<>(); //Pila 
    
    Stack< String> Es = new Stack<>(); //Pila entrada lexema
    Stack< String> Eis = new Stack<>(); //Pila entrada invertida lexema
    Stack< String> Os = new Stack<>(); //Pila entrada lexema
    Stack< String> Ss = new Stack<>(); //Pila salida
    Stack< String> Ssi = new Stack<>(); //Salida invertida

    Stack< Integer> contadorSi = new Stack<>();
    Stack< Integer> contadorMientras = new Stack<>();
    
    Stack< nodoSemantico2> stackASM = new Stack<>();
    Stack< nodoSemantico2> istackASM = new Stack<>();
    
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
                                            imprimirNodos();
                                            crearASM();
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
                            insertarASM(lexemaSemantico, 101);
                            break;
                        case 219:
                            tipoSemantico = 102;
                            insertarASM(lexemaSemantico, 102);
                            break;
                        case 220:
                            tipoSemantico = 103;
                            insertarASM(lexemaSemantico, 103);
                            break;
                        default:
                            tipoSemantico = p.token;
                            insertarASM(lexemaSemantico, p.token);
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
    private void insertarASM(String s, int i) {
        nodoSemantico asm = new nodoSemantico(s,i);

        if (cabezaASM == null) {
            cabezaASM = asm;
            pASM = cabezaASM;
            recorridoASM = cabezaASM;
        } else {
            pASM.sig = asm;
            pASM = asm;
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

        private void insertarNodoSemantico2(String lexema, int token, String etiqueta) {
        nodoSemantico2 dos = new nodoSemantico2(lexema, token, etiqueta);
            if(uno == null){
                uno = dos;
                tres = uno;
            }else{
                tres.sig = dos;
                tres= dos;
            }
    }
        private void imprimirNodos(){
            tres = uno;
            while (tres!=null){
                if(tres.etiqueta.equals("")){
                    System.out.println(" Lexema: " +tres.lexema + "    Token: " + tres.token);
                }else{
                    System.out.println(" Lexema: " +tres.lexema + "    Token: " + tres.token+ "   Apuntador: " + tres.etiqueta);
                }
                tres = tres.sig;
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
                    Es.add(p.lexema);
                    p = p.sig;
                    expresionNumerica();
                    crearPilaNormal();
                    
                    
                    //infijoposfijo();
                }
                break;

            case 205: //leer
                leer();
                crearPilaNormal();
                break;
            case 206: //escribir
                escribir();
                crearPilaNormal();
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
            Es.add(p.lexema);
        } else {
            if (p.token == 100) {
                checarExistencia();
            } else {
                E.add(p.token);
                Es.add(p.lexema);
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
        if (p.token == 118 || p.token == 217 || p.token == 116 || p.token == 209 || p.token == 212) { // ; || fin || entonces || hacer || fin_si || fin_mientras)
        } else {
            E.add(p.token);
            Es.add(p.lexema);
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
                Es.add(p.lexema);
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
        
        if (p.token == 120) {
            
        } else{
            E.add(p.token);
            Es.add(p.lexema);
            //crearPilaNormal();
        }
        //insertarNodoSemantico2(p.lexema,p.token,"");
        p = p.sig;
        if (p.token == 100) { //
            checarExistencia();
            //insertarNodoSemantico2(p.lexema,p.token,"");
            //E.add(p.token);
            //Es.add(p.lexema);
            //crearPilaNormal();
            p = p.sig;
            if (p.token == 120) { // ,                
                leer();
            }
        } else {
            imprimirMensajeError(302);
        }

    }

    private void escribir() {
        if (p.token == 120) {
            
        } else{
            E.add(p.token);
            Es.add(p.lexema);
            //crearPilaNormal();
        }
        //insertarNodoSemantico2(p.lexema,p.token,"");
        p = p.sig;
        if (p.token == 100) { //identificador
            //E.add(p.token);
            //Es.add(p.lexema);
            checarExistencia();
            //insertarNodoSemantico2(p.lexema,p.token,"");
            p = p.sig;
            if (p.token == 120) { // ,                
                escribir();
            }
        } else {
            imprimirMensajeError(302);
        }
    }

    private void si() {
        contS++;
        contadorSi.add(contS);
        insertarNodoSemantico2("Apuntador",0,"A"+contadorSi.lastElement());        
        expresionLogica();
        crearPilaNormal();        
        if (p.token == 208) { //entonces
            insertarNodoSemantico2("Brinco",50,"B"+contadorSi.lastElement());//50 condicional
            accion();
            insertarNodoSemantico2("Brinco",51,"C"+contadorSi.lastElement()); //51 incondicional
            insertarNodoSemantico2("Apuntador",0,"B"+contadorSi.lastElement());
            if (p.token == 210) { // sino                
                accion();
            }
            if (p.token == 209) { // fin-si
                insertarNodoSemantico2("Apuntador",0,"C"+contadorSi.pop());                                
                p = p.sig;
            } else {
                imprimirMensajeError(316); // espera fin-si
            }
        } else {
            imprimirMensajeError(316); // espera fin-si
        }
    }

    private void mientras() {
        contM++;
        contadorMientras.add(contM);
        //insertarNodoSemantico2(p.lexema,p.token,"");
        insertarNodoSemantico2("Apuntador",0,"D"+contadorMientras.lastElement());
        expresionLogica();
        crearPilaNormal();        

        if (p.token == 213) { //hacer
            insertarNodoSemantico2("Brinco",50,"E"+contadorMientras.lastElement()); //50 condicional
            accion();
            insertarNodoSemantico2("Brinco",51,"D"+contadorMientras.lastElement()); //51 incondiconal
            if (p.token == 212) { //fin-mientras   
                insertarNodoSemantico2("Apuntador",0,"E"+contadorMientras.pop());                
                p = p.sig;
            } else {
                imprimirMensajeError(315);
            }
        }
    }

    private void expresionLogica1() {
        if (p.token == 200 || p.token == 201) { // and || or  
            E.add(p.token);
            Es.add(p.lexema);
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
        Eis.push(")");
        while (!E.empty()) {
            Ei.push(E.pop());
            Eis.push(Es.pop());
        }
        Ei.push(115);
        Eis.push("(");

        //System.out.println("Invertida: " + Ei);
        while (!Ei.empty()) {
            switch (jerarquia(Ei.peek())) {
                case 0: // ---> :=
                    O.push(Ei.pop());
                    Os.push(Eis.pop());
                    break;
                case 1: // ---> (
                    O.push(Ei.pop());
                    Os.push(Eis.pop());
                    break;
                case 2: // ---> )
                    while (!O.peek().equals(115)) {
                        S.push(O.pop());
                        Ss.push(Os.pop());
                    }
                    O.pop();
                    Os.pop();
                    Ei.pop();
                    Eis.pop();
                    break;
                case 3: // and or
                case 4:   // not                 
                case 5: // relacional
                case 6: // + o -
                case 7: // * o /
                    while (jerarquia(O.peek()) >= jerarquia(Ei.peek())) {
                        S.push(O.pop());
                        Ss.push(Os.pop());
                    }
                    O.push(Ei.pop());
                    Os.push(Eis.pop());
                    break;
                case 8: // Token default.
                    S.push(Ei.pop());
                    Ss.push(Eis.pop());
                    break;
            }
        }
        S2 = (Stack<Integer>) S.clone();
        System.out.println("Salida: " + S);
        System.out.println("Salida: " + Ss);       
        System.out.println("");
        
          
        //inversion de pila
        while (!S2.empty()) {
            S2i.push(S2.pop()); //token
            Ssi.push(Ss.pop()); //lexema
        }
        //while simmple insertar nodo y vaciar pila
        while (!S2i.empty()) {
            insertarNodoSemantico2(Ssi.pop(),S2i.pop(),"");
        }

        evaluarposfijo();
              
        Os.removeAllElements();
        Es.removeAllElements();
        Eis.removeAllElements();
        Ss.removeAllElements();
        Ssi.removeAllElements();
        O.removeAllElements();
        E.removeAllElements();
        Ei.removeAllElements();
        S.removeAllElements();
        S2.removeAllElements();
        S2i.removeAllElements();
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
        if (op == 114 || op == 205 || op == 206) {// :=, leer, escribir
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
                Es.add(p.lexema);
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
            Es.add(p.lexema);
        } else {
            if (p.token == 118 || p.token == 217 || p.token == 208 || p.token == 213) { // ; || fin || entonces || hacer
            } else {
                E.add(p.token);
                Es.add(p.lexema);
            }
        }
        
        System.out.println("Original: " + E);
        System.out.println("Original: " + Es);
        infijoposfijo();
        E.removeAllElements();
        Es.removeAllElements();

    }

    private void evaluarposfijo() {
        int columna = 0;
        int renglon = 0;
        boolean flag = false;
        int operando1,operando2 = 0;
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


    
    private void crearASM() {                        
        codigo = ("""
                         INCLUDE MACROS.MAC
                         .MODEL SMALL
                         .586
                         .STACK  100h
                         .DATA""");
        
        //declaracion de variables
        recorridoASM = cabezaASM;
            while (recorridoASM!=null){
                if (recorridoASM.tipo != 103) { //concatenar todo excepto cadenas al declarar
                codigo = codigo.concat(newLine);
                codigo = codigo.concat("        "+ recorridoASM.lexema + " DB '0'");
                }else{ //declarar cadenas con un valor default
                    codigo = codigo.concat(newLine);
                    codigo = codigo.concat("        " + recorridoASM.lexema +" DB 'default', 13,10, '$'");
                }
                recorridoASM = recorridoASM.sig;
            } 
            codigo = codigo.concat(newLine);
            codigo = codigo.concat("""
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
                                   COMPI     PROC""");   
            
            codigoASM();
            codigo = codigo.concat(newLine);
            codigo = codigo.concat("""
                                   ret
                                   COMPI ENDP
                                   .EXIT
                                   END""");
        //Crear archivo                
        try {
         FileWriter myWriter = new FileWriter("test2.txt");
         myWriter.write(codigo);
         myWriter.close();
         System.out.println("Successfully wrote TXT.");
       } catch (IOException e) {
         System.out.println("An error occurred.");
         e.printStackTrace();
       }
        try {
         FileWriter myWriter = new FileWriter("test2.asm");
         myWriter.write(codigo);
         myWriter.close();
         System.out.println("Successfully wrote ASM.");
       } catch (IOException e) {
         System.out.println("An error occurred.");
         e.printStackTrace();
       }        
    }

    private void codigoASM() {
        nodoSemantico2 op1,op2;        
        tres = uno;        //tres = recorrido nodos, uno = cabeza de nodos
        while (tres!=null) {
            switch (tres.lexema) {
                case "+":
                op1 = stackASM.pop();
                op2 = stackASM.pop();
                codigo = codigo.concat(newLine);
                codigo = codigo.concat("        " + "SUMAR "+ op1.lexema+ ","+ op2.lexema+", resultadoNormal");                
                    break;
                case "-":
                op2 = stackASM.pop();
                op1 = stackASM.pop();
                codigo = codigo.concat(newLine);
                codigo = codigo.concat("        " + "RESTA "+ op1.lexema+ ","+ op2.lexema+", resultadoNormal");
                
                    break;        
                case "*":
                op2 = stackASM.pop();
                op1 = stackASM.pop();
                codigo = codigo.concat(newLine);
                codigo = codigo.concat("        " + "MULTI "+ op1.lexema+ ","+ op2.lexema+", resultadoNormal");
                
                    break;
                case "/":
                op2 = stackASM.pop();
                op1 = stackASM.pop();
                codigo = codigo.concat(newLine);
                codigo = codigo.concat("        " + "DIVIDE "+ op1.lexema+ ","+ op2.lexema+", resultadoNormal");
                
                    break;   
                case "Apuntador":
                    codigo = codigo.concat(newLine);
                    codigo = codigo.concat(tres.etiqueta+":");
                    stackASM.add(tres);
                    break;
                case "Brinco":
                    //condicional token = 50
                    //incondicional token = 51
                    codigo = codigo.concat(newLine);
                    if (tres.token == 50) {
                        codigo = codigo.concat("        " + "JNE "+ tres.etiqueta);
                    }else if(tres.token == 51){
                        codigo = codigo.concat("        " + "JMP "+ tres.etiqueta);
                    }
                    
                    stackASM.add(tres);
                    break;
                case ":=":                    
                    op1 = stackASM.pop();
                    op2 = stackASM.pop();
                    //TODO cadenas 
                    if (op1.token == 103) {
                        /*
                    codigo = codigo.concat(newLine);
                    codigo = codigo.concat("        " + "Macro Asignar reultadoCadenas = "+ op1.lexema);
                    codigo = codigo.concat(newLine);
                    codigo = codigo.concat("        " + "Macro Asignar "+ op2.lexema + " = reultadoCadenas");
                        */
                        codigo = codigo.replace(op2.lexema + " DB 'default'", op2.lexema + " DB " + op1.lexema);
                    } else{
                        //si es booleano
                        if (op1.lexema.equals("verdadero")) {
                            codigo = codigo.concat(newLine);
                            codigo = codigo.concat("        " + "I_ASIGNAR resultadoNormal,1");
                        }else if (op1.lexema.equals("falso")) {
                            codigo = codigo.concat(newLine);
                            codigo = codigo.concat("        " + "I_ASIGNAR resultadoNormal,0");
                        }
                        //si es una sola o si es operacion en una sola linea
                        if (op1.token == (101)) {
                            codigo = codigo.concat(newLine);                    
                            codigo = codigo.concat("        " + "I_ASIGNAR "+ op2.lexema + "," +op1.lexema);
                        }else{
                            codigo = codigo.concat(newLine);                    
                            codigo = codigo.concat("        " + "I_ASIGNAR "+ op2.lexema + "," +"resultadoNormal");
                        }
                    }
                    stackASM.add(tres);
                        break;
                case "escribir":
                    op1 = stackASM.pop();
                    codigo = codigo.concat(newLine);
                    if (op1.token == 103) {
                    codigo = codigo.concat("        " + "LEELN "+ op1.lexema);
                    }else{                        
                    codigo = codigo.concat("        " + "WRITE "+ op1.lexema);
                    }
                    stackASM.add(tres);
                    break;  
                case "leer":
                    op1 = stackASM.pop();
                    codigo = codigo.concat(newLine);
                    codigo = codigo.concat("        " + "LEER "+ op1.lexema);
                    stackASM.add(tres);
                    break;                     
                default:
                    
                    break;
            }
            //relacionales
            switch (tres.token) {
                case 108: // >
                    op2 = stackASM.pop();
                    op1 = stackASM.pop();
                    codigo = codigo.concat(newLine);                   
                    codigo = codigo.concat("        " + "I_MAYOR "+ op1.lexema+","+op2.lexema+",resultadoNormal");  
                    codigo = codigo.concat(newLine);
                    codigo = codigo.concat("        " + "CMP resultadoNormal,1");
                    stackASM.add(tres);                    
                    break;
                case 109: // <
                    op2 = stackASM.pop();
                    op1 = stackASM.pop();
                    codigo = codigo.concat(newLine);                    
                    codigo = codigo.concat("        " + "I_MENOR "+ op1.lexema+","+op2.lexema+",resultadoNormal");
                    codigo = codigo.concat(newLine);
                    codigo = codigo.concat("        " + "CMP resultadoNormal,1");
                    stackASM.add(tres);                    
                    break;
                case 110: // >=
                    op2 = stackASM.pop();
                    op1 = stackASM.pop();
                    codigo = codigo.concat(newLine);                    
                    codigo = codigo.concat("        " + "I_MAYORIGUAL "+ op1.lexema+","+op2.lexema+",resultadoNormal");
                    codigo = codigo.concat(newLine);
                    codigo = codigo.concat("        " + "CMP resultadoNormal,1");
                    stackASM.add(tres);                    
                    break;
                case 111: // <=
                    op2 = stackASM.pop();
                    op1 = stackASM.pop();
                    codigo = codigo.concat(newLine);                    
                    codigo = codigo.concat("        " + "I_MENORIGUAL "+ op1.lexema+","+op2.lexema+",resultadoNormal");
                    codigo = codigo.concat(newLine);
                    codigo = codigo.concat("        " + "CMP resultadoNormal,1");
                    stackASM.add(tres);                    
                    break;
                case 112: // <>                  
                    op2 = stackASM.pop();
                    op1 = stackASM.pop();
                    System.out.println(op2.lexema);
                    if (op2.lexema.equals("verdadero")) {
                        op2.lexema = "1";
                    } else if(op2.lexema.equals("falso")){
                        op2.lexema = "0";
                    }
                    codigo = codigo.concat(newLine);                   
                    codigo = codigo.concat("        " + "I_DIFERENTES "+ op1.lexema+","+op2.lexema+",resultadoNormal");
                    codigo = codigo.concat(newLine);
                    codigo = codigo.concat("        " + "CMP resultadoNormal,1");
                    stackASM.add(tres);                    
                    break;
                case 113: // =
                    op2 = stackASM.pop();
                    op1 = stackASM.pop();                    
                    if (op2.lexema.equals("verdadero")) {
                        op2.lexema = "1";
                    } else if(op2.lexema.equals("falso")){
                        op2.lexema = "0";
                    }                    
                    codigo = codigo.concat(newLine);                    
                    codigo = codigo.concat("        " + "I_IGUAL "+ op1.lexema+","+op2.lexema+",resultadoNormal");
                    codigo = codigo.concat(newLine);
                    codigo = codigo.concat("        " + "CMP resultadoNormal,1");
                    stackASM.add(tres);
                    break;
                default:
                    
                    break;
            }            
            stackASM.add(tres);
            tres = tres.sig;           
        }
    }       
}
