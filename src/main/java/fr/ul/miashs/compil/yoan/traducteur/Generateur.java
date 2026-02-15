package fr.ul.miashs.compil.yoan.traducteur;

import fr.ul.miashs.compil.arbre.*;
import fr.ul.miashs.compil.tds.Element;
import fr.ul.miashs.compil.tds.Tds;
import java.util.Map;

public class Generateur {

    public String generer_affectation(Affectation a) {
        StringBuffer code = new StringBuffer();
        code.append(generer_expression(a.getFilsDroit()));
        code.append("\tPop R0\n");
        Idf i = (Idf) a.getFilsGauche();
        Element val = (Element) i.getValeur();
        //SI GLOBAL
        if (val.getCat() == Element.Cat.GLOBAL){
            code.append("\tST(R0, " + val.getVal() + ")\n");
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
        StringBuffer code = new StringBuffer();
        code.append(".include beta.uasm\n");
        code.append(".include intio.uasm\n");
        code.append("\tCMOVE(pile, SP)\n");
        code.append("\tBR(debut)\n");
        //variables globales
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
        return code.toString();
    }

    public String generer_data(Tds tds){
        StringBuffer code = new StringBuffer();
        for (Map.Entry<String, Element> e : tds.getTds().entrySet()){
            if (e.getValue().getCat() == Element.Cat.GLOBAL){
                code.append(e.getValue().getNom() +":"+ "\tLONG("+e.getValue().getVal()+")\n");
            }
        }
        return code.toString();
    }

    public String generer_instruction(Noeud n){
        String res;
        if (n instanceof Affectation){
            Affectation a = (Affectation) n;
            res = generer_affectation(a);
        }
        else if(n instanceof Si){
            res = generer_si((Si) n);
        }
        else if(n instanceof TantQue){
            res = generer_tq((TantQue)n);
        }
        else if(n instanceof Appel){
            res = generer_appel(n);
        }
        else{
            res = generer_ecrire(n);
        }
        return res;
    }

    public String generer_ecrire(Noeud n){
        StringBuffer code = new StringBuffer();
        code.append(generer_expression(n));
        code.append("\tPOP(R0)\n");
        code.append("\tWRINT()\n");
        return code.toString();
    }

    public String generer_fonction(Fonction f){
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
        StringBuffer code = new StringBuffer();
        return code.toString();
    }

    public String generer_appel(Noeud a){
        StringBuffer code = new StringBuffer();
        if (a.getLabel().equals(Element.Type.VOID)){
            code.append("ALLOCATE(1)");
        }
        return "f";
    }

    public String generer_expression(Noeud n) {
        StringBuffer code = new StringBuffer();
        // CAS 1: Constante
        if (n instanceof Const) {
            Const c = (Const) n;
            code.append("\tCMOVE(" + c.getValeur() + ", R0)\n");
            code.append("\tPush R0\n");
        }
        // CAS 2: Variable
        else if (n instanceof Idf) {
            Idf i = (Idf) n;
            Element val = (Element) i.getValeur();
            //SOUS CAS 1 : Global
            if (val.getCat() == Element.Cat.GLOBAL) {
                code.append("\tLD(").append(val.getVal()).append(", R0)\n");
            }
            //SOUS CAS 2 : Local
            else if (val.getCat() == Element.Cat.LOCAL) {
                int offset = (1 + val.getRang()) * 4;
                code.append("\tGETFRAME(").append(offset).append(", R0)\n");
            }
            //SOUS CAS 3 : Paramètres
            else if (val.getCat() == Element.Cat.PARAM) {
                int offset = (-1 - val.getScope().getNb_param() + val.getRang()) * 4;
                code.append("\tGETFRAME(").append(offset).append(", R0)\n");
            }
            code.append("\tPUSH(R0)\n");
        }

        //CAS LIRE..

        //CAS APPEL

        // CAS 3: Operations
        else if (n instanceof Plus || n instanceof Moins || n instanceof Multiplication || n instanceof Division) {
            if (n instanceof Multiplication){
                Multiplication m = (Multiplication) n;
                code.append(generer_expression(m.getFilsGauche()));
                code.append(generer_expression(m.getFilsDroit()));
                code.append("\tPop(R1)\n");
                code.append("\tPOP(R0)\n");
                code.append("\tMUL(R1, R0)\n");
            } else if (n instanceof Division){
                Division d = (Division) n;
                code.append(generer_expression(d.getFilsGauche()));
                code.append(generer_expression(d.getFilsDroit()));
                code.append("\tPop(R1)\n");
                code.append("\tPop(R0)\n");
                code.append("\tDIV(R1, R0)\n");
            } else if (n instanceof Plus){
                Plus p = (Plus) n;
                code.append(generer_expression(p.getFilsGauche()));
                code.append(generer_expression(p.getFilsDroit()));
                code.append("\tPop(R1)\n");
                code.append("\tPop(R0)\n");
                code.append("\tADD(R1, R0)\n");
            } else {
                Moins m = (Moins) n;
                code.append(generer_expression(m.getFilsGauche()));
                code.append(generer_expression(m.getFilsDroit()));
                code.append("\tPop(R1)\n");
                code.append("\tPop(R0)\n");
                code.append("\tSUB(R1, R0)\n");
            }
            code.append("\tPush(R0)\n");
            }
        return code.toString();
    }

    public String generer_si(Si s){
        StringBuffer code = new StringBuffer();
        code.append("si " + s.getValeur() + ":\n");
        //code.append(generer_condition(s.getFils(0)));
        code.append("\tPOP(R0)\n");
        code.append("\tBF(R0, sinon_"+s.getValeur()+"\n");
        //code.append(generer_bloc(s.getFils(1)));
        code.append("\tPOP(R0, fsi_"+s.getValeur()+"\n");
        code.append("sinon_"+s.getValeur()+":\n");
        //code.append(generer_bloc(s.getFils(2)));
        code.append("fsi_"+s.getValeur()+":\n");
        return code.toString();
    }

    public String generer_bloc(Bloc b){
        StringBuffer code = new StringBuffer();
        for (Noeud f : b.getFils()){
            code.append(generer_instruction(f));
        }
        return code.toString();
    }

    public String generer_condition(Noeud c){
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
        StringBuffer code = new StringBuffer();
        code.append("tq_"+tq.getValeur()+"\n");
        code.append(generer_condition((tq.getCondition())));
        code.append("\tPOP(R0)\n");
        code.append("\tBF(ftq_)"+tq.getValeur()+"\n");
        code.append("\tBR(tq_)"+tq.getValeur()+"\n");
        code.append("ftq_"+tq.getValeur()+"\n");
        return code.toString();

    }
}
