package fr.ul.miashs.compil.yoan.traducteur;

import fr.ul.miashs.compil.arbre.*;
import fr.ul.miashs.compil.tds.Element;
import fr.ul.miashs.compil.tds.Tds;
import java.util.Map;

/**
 * Description : Classe contenant toutes les méthodes nécessaires à la génération
 * d'un code assembleur (Bsim) à partir d'un arbre abstrait.
 * @author Ighir Yoan
 */

public class Generateur {

    public String generer_affectation(Affectation a) {
        /**
         * Générer une affectation
         * @param a : Noeud de type Affectation
         * @return code assembleur correspondant
         */
        StringBuffer code = new StringBuffer();
        code.append(generer_expression(a.getFilsDroit()));
        code.append("\tPOP(R0)\n");
        Idf i = (Idf) a.getFilsGauche();
        Element val = (Element) i.getValeur();
        //SI GLOBAL
        if (val.getCat() == Element.Cat.GLOBAL){
            code.append("\tST(R0, " + val.getNom() + ")\n");
        }
        //SI LOCAL
        else if (val.getCat() == Element.Cat.LOCAL){
            int offset = (1 + val.getRang())*4;
            code.append("\tPUTFRAME(R0, "+ offset+")\n");
        }
        //SI PARAM
        else if (val.getCat() == Element.Cat.PARAM){
            int offset = (-1 - val.getScope().getNb_param() + val.getRang()) * 4;
            code.append("\tPUTFRAME(R0, "+ offset +")\n");
        }
        return code.toString();
    }

    public String generer_programme(Prog p, Tds tds){
        /**
         * Générer le programme
         * @param p : Racine de l'arbre syntaxique de type Prog
         * @param tds : Table des symboles associée à l'arbre syntaxique
         * @return code assembleur correspondant
         */
        StringBuffer code = new StringBuffer();
        code.append(".include beta.uasm\n");
        code.append(".include intio.uasm\n");
        //permet d'afficher la console dans Bsim
        code.append(".options tty\n");
        code.append("\tCMOVE(pile, SP)\n");
        code.append("\tBR(debut)\n");
        code.append(generer_data(tds));
        code.append("debut:\n");
        code.append("\tCALL(main)\n");
        code.append("\tHALT()\n");
        //fonctions
        for (Noeud f : p.getFils()){
            Fonction fonction = (Fonction) f;
            code.append(generer_fonction(fonction));
        }
        code.append("pile:\n");
        //permet de sauter 1024 bits pour avoir un espace reservé pour la pile.
        code.append("\t.=. +1024");
        return code.toString();
    }

    public String generer_data(Tds tds){
        /**
         * Génère les données à partir de la table des symboles
         * @param tds : Table des symboles associée à l'arbre syntaxique
         * @return code assembleur correspondant
         */
        StringBuffer code = new StringBuffer();
        for (Map.Entry<String, Element> e : tds.getTds().entrySet()){
            if (e.getValue().getCat() == Element.Cat.GLOBAL){
                //Cas où la valeur n'est pas spécifiée
                if (e.getValue().getVal() != null){
                    code.append(e.getValue().getNom() +":"+ "\tLONG("+e.getValue().getVal()+")\n");
                }
                else{
                    code.append(e.getValue().getNom() +":"+ "\tLONG(0)\n");
                }
            }
        }
        return code.toString();
    }

    public String generer_instruction(Noeud n){
        /**
         * Générer une instruction
         * @param n : Noeud de l'AST.
         * @return code assembleur correspondant
         */
        StringBuffer code = new StringBuffer();
        if (n instanceof Affectation){
            Affectation a = (Affectation) n;
            code.append(generer_affectation(a));
        }
        else if(n instanceof Si){
            code.append(generer_si((Si) n));
        }
        else if(n instanceof TantQue){
            code.append(generer_tq((TantQue)n));
        }
        else if(n instanceof Appel){
            code.append(generer_appel(n));
        }
        else if (n instanceof Ecrire){
            code.append(generer_ecrire(n));
        }
        else if (n instanceof Retour){
            code.append(generer_retour(n));
        }
        return code.toString();
    }

    public String generer_ecrire(Noeud n){
        /**
         * Générer écriture
         * @param n : Noeud de l'AST
         * @return code assembleur correspondant
         */
        StringBuffer code = new StringBuffer();
        code.append(generer_expression(n.getFils().get(0)));
        code.append("\tPOP(R0)\n");
        //macri intio.uasm
        code.append("\tWRINT()\n");
        return code.toString();
    }

    public String generer_fonction(Fonction f){
        /**
         * Générer une fonction
         * @param f : Noeud de l'AST de type Fonction
         * @return code assembleur correspondant
         */
        StringBuffer code = new StringBuffer();
        Element val = (Element) f.getValeur();
        code.append(val.getNom() + ":\n");
        code.append("\tPUSH(LP)\n");
        code.append("\tPUSH(BP)\n");
        code.append("\tMOVE(SP, BP)\n");
        code.append("\tALLOCATE("+ val.getNb_local() +")\n");
        for (Noeud n : f.getFils()){
            code.append(generer_instruction(n));
        }
        code.append("ret_"+val.getNom()+":\n");
        code.append("\tDEALLOCATE("+ val.getNb_local()+")\n");
        code.append("\tPOP(BP)\n");
        code.append("\tPOP(LP)\n");
        code.append("\tRTN()\n");
        return code.toString();
    }

    public String generer_retour(Noeud n){
        /**
         * Générer un retour
         * @param n : Noeud de l'AST
         * @return code assembleur correspondant
         */
        StringBuffer code = new StringBuffer();
        Retour r = (Retour) n;
        code.append(generer_expression(r.getLeFils()));
        code.append("\tPOP(R0)\n");
        Element val = (Element) r.getValeur();
        if (val.getScope() != null){
            code.append("\tBR(ret_"+val.getScope().getNom()+")\n");
        }
        else{
            code.append("\tBR(ret_"+val.getNom()+")\n");
        }
        return code.toString();
    }

    public String generer_appel(Noeud n){
        /**
         * Générer un appel
         * @param n : Noeud de l'AST
         * @return code assembleur correspondant
         */
        StringBuffer code = new StringBuffer();
        Appel a = (Appel) n;
        for (Noeud noeud : a.getFils()){
            code.append(generer_expression(noeud));
        }
        Element val = (Element) a.getValeur();
        code.append("\tCALL("+val.getNom() +")\n");
        int nbArgs = a.getFils().size();
        code.append("\tDEALLOCATE("+ nbArgs +")\n");
        return code.toString();
    }

    public String generer_expression(Noeud n) {
        /**
         * Générer une expression
         * @param n : Noeud de l'AST
         * @return code assembleur correspondant
         */
        StringBuffer code = new StringBuffer();
        // CAS 1: Constante
        if (n instanceof Const) {
            Const c = (Const) n;
            code.append("\tCMOVE(" + c.getValeur() + ", R0)\n");
            code.append("\tPUSH(R0)\n");
        }
        // CAS 2: Variable
        else if (n instanceof Idf) {
            Idf i = (Idf) n;
            Element val = (Element) i.getValeur();
            //SOUS CAS 1 : Global
            if (val.getCat() == Element.Cat.GLOBAL) {
                code.append("\tLD(").append(val.getNom()).append(", R0)\n");
            }
            //SOUS CAS 2 : Local
            else if (val.getCat() == Element.Cat.LOCAL) {
                int offset = (1 + val.getRang()) * 4;
                code.append("\tGETFRAME(").append(offset).append(", R0)\n");
            }
            //SOUS CAS 3 : Paramètres
            else if (val.getCat() == Element.Cat.PARAM) {
                int offset = (-2 - val.getScope().getNb_param() + val.getRang()) * 4;
                code.append("\tGETFRAME(").append(offset).append(", R0)\n");
            }
            code.append("\tPUSH(R0)\n");
        }

        //CAS LIRE
        else if (n instanceof Lire) {
            code.append("\tRDINT()\n");
            code.append("\tPUSH(R0)\n");
        }

        //CAS APPEL
        else if (n instanceof Appel) {
            code.append(generer_appel(n));
            code.append("\tPUSH(R0)\n");
        }

        // CAS 3: Operations
        else if (n instanceof Plus || n instanceof Moins || n instanceof Multiplication || n instanceof Division) {
            if (n instanceof Multiplication){
                Multiplication m = (Multiplication) n;
                code.append(generer_expression(m.getFilsGauche()));
                code.append(generer_expression(m.getFilsDroit()));
                code.append("\tPOP(R1)\n");
                code.append("\tPOP(R0)\n");
                code.append("\tMUL(R1, R0, R0)\n");
            } else if (n instanceof Division){
                Division d = (Division) n;
                code.append(generer_expression(d.getFilsGauche()));
                code.append(generer_expression(d.getFilsDroit()));
                code.append("\tPOP(R1)\n");
                code.append("\tPOP(R0)\n");
                code.append("\tDIV(R0, R1, R0)\n");
            } else if (n instanceof Plus){
                Plus p = (Plus) n;
                code.append(generer_expression(p.getFilsGauche()));
                code.append(generer_expression(p.getFilsDroit()));
                code.append("\tPOP(R1)\n");
                code.append("\tPOP(R0)\n");
                code.append("\tADD(R1, R0, R0)\n");
            } else {
                Moins m = (Moins) n;
                code.append(generer_expression(m.getFilsGauche()));
                code.append(generer_expression(m.getFilsDroit()));
                code.append("\tPOP(R1)\n");
                code.append("\tPOP(R0)\n");
                code.append("\tSUB(R0, R1, R0)\n");
            }
            code.append("\tPUSH(R0)\n");
            }
        return code.toString();
    }

    public String generer_si(Si s){
        /**
         * Générer une condition
         * @param s : Noeud de l'AST de type SI
         * @return code assembleur correspondant
         */
        StringBuffer code = new StringBuffer();
        code.append("si" + s.getValeur() + ":\n");
        code.append(generer_condition(s.getCondition()));
        code.append("\tPOP(R0)\n");
        code.append("\tBF(R0, sinon_"+s.getValeur()+")\n");
        code.append(generer_bloc(s.getBlocAlors()));
        code.append("\tBR(fsi_"+s.getValeur()+")\n");
        code.append("sinon_"+s.getValeur()+":\n");
        code.append(generer_bloc(s.getBlocSinon()));
        code.append("fsi_"+s.getValeur()+":\n");
        return code.toString();
    }

    public String generer_bloc(Bloc b){
        /**
         * Générer un bloc
         * @param b : Noeud de l'AST de type Bloc
         * @return code assembleur correspondant
         */
        StringBuffer code = new StringBuffer();
        for (Noeud f : b.getFils()){
            code.append(generer_instruction(f));
        }
        return code.toString();
    }

    public String generer_condition(Noeud c){
        /**
         * Générer une condition
         * @param c : Noeud de l'AST
         * @return code assembleur correspondant
         */
        StringBuffer code = new StringBuffer();
        if (c instanceof Superieur) {
            Superieur s = (Superieur) c;
            code.append(generer_expression(s.getFilsGauche()));
            code.append(generer_expression(s.getFilsDroit()));
            code.append("\tPOP(R1)\n");
            code.append("\tPOP(R2)\n");
            code.append("\tCMPLT(R1, R2, R3)\n");
        }
        else if (c instanceof Inferieur) {
            Inferieur i = (Inferieur) c;
            code.append(generer_expression(i.getFilsGauche()));
            code.append(generer_expression(i.getFilsDroit()));
            code.append("\tPOP(R1)\n");
            code.append("\tPOP(R2)\n");
            code.append("\tCMPLT(R2, R1, R3)\n");
        }
        else if (c instanceof Egal) {
            Egal e = (Egal) c;
            code.append(generer_expression(e.getFilsGauche()));
            code.append(generer_expression(e.getFilsDroit()));
            code.append("\tPOP(R1)\n");
            code.append("\tPOP(R2)\n");
            code.append("\tCMPEQ(R2, R1, R3)\n");
        }
        else if (c instanceof SuperieurEgal) {
            SuperieurEgal e = (SuperieurEgal) c;
            code.append(generer_expression(e.getFilsGauche()));
            code.append(generer_expression(e.getFilsDroit()));
            code.append("\tPOP(R1)\n");
            code.append("\tPOP(R2)\n");
            code.append("\tCMPLE(R1, R2, R3)\n");
        }
        else if (c instanceof InferieurEgal) {
            InferieurEgal e = (InferieurEgal) c;
            code.append(generer_expression(e.getFilsGauche()));
            code.append(generer_expression(e.getFilsDroit()));
            code.append("\tPOP(R1)\n");
            code.append("\tPOP(R2)\n");
            code.append("\tCMPLE(R2, R1, R3)\n");
        }
        else if (c instanceof Different) {
            Different d = (Different) c;
            code.append(generer_expression(d.getFilsGauche()));
            code.append(generer_expression(d.getFilsDroit()));
            code.append("\tPOP(R1)\n");
            code.append("\tPOP(R2)\n");
            code.append("\tCMPEQ(R2, R1, R3)\n");
            code.append("\tCMPEQC(R3, 0, R3)\n");
        }
        code.append("\tPUSH(R3)\n");
        return code.toString();
    }


    public String generer_tq(TantQue tq){
        /**
         * Générer une itération
         * @param tq : Noeud de l'AST de type TantQue
         * @return code assembleur correspondant
         */
        StringBuffer code = new StringBuffer();
        code.append("tq_"+tq.getValeur()+":\n");
        code.append(generer_condition((tq.getCondition())));
        code.append("\tPOP(R0)\n");
        code.append("\tBF(R0, ftq_"+tq.getValeur()+")\n");
        code.append(generer_bloc(tq.getBloc()));
        code.append("\tBR(tq_"+tq.getValeur()+")\n");
        code.append("ftq_"+tq.getValeur()+":\n");
        return code.toString();

    }
}
