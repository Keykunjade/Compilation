package fr.ul.miashs.compil.tds;

import java.util.HashMap;
import java.util.Map;

public class Tds {
    private Map<String, Element> tds;

    public Tds(){
        this.tds = new HashMap<String, Element>();
    }

    public void ajouter(String cle, Element elem){
        if (tds.containsKey(cle)){
            throw new RuntimeException("L'element est deja declare" + cle);
        }
        tds.put(cle, elem);
    }

    public Element rechercher(Element e){
        for (Map.Entry<String, Element> s : tds.entrySet()){
            if (s.getKey().equals(e.getNom())){
                return s.getValue();
            }
        }
        return null;
    }

    public String toString(){
        StringBuffer res = new StringBuffer();
        for(Map.Entry<String, Element> s : tds.entrySet()){
            res.append(s.getValue() + "\n");
        }
        return res.toString();
    }

    public Map<String, Element> getTds() {
        return tds;
    }

    public void setTds(Map<String, Element> tds) {
        this.tds = tds;
    }
}
