package fr.ul.miashs.compil.tds;

import java.util.HashMap;
import java.util.Map;
/**
 * Description : Classe représentant la table des symboles
 * C'est une table contenant des instances de la classe Element.
 * @author Ighir Yoan
 */
public class Tds {
    private Map<String, Element> tds;

    public Tds(){
        this.tds = new HashMap<String, Element>();
    }

    public void ajouter(String cle, Element elem){
        /**
         * Ajout d'un élément à la TDS
         * @param cle : clé de l'élément dans la TDS
         * @param elem : instance de la classe Element
         */
        if (tds.containsKey(cle)){
            throw new RuntimeException("L'element est deja declare" + cle);
        }
        tds.put(cle, elem);
    }

    public String toString(){
        StringBuffer res = new StringBuffer();
        for(Map.Entry<String, Element> s : tds.entrySet()){
            res.append(s.getValue() + "\n");
        }
        return res.toString();
    }

    //getters & setters
    public Map<String, Element> getTds() {
        return tds;
    }

    public void setTds(Map<String, Element> tds) {
        this.tds = tds;
    }
}
