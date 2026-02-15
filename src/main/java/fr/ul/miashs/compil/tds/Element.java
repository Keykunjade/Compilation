package fr.ul.miashs.compil.tds;

public class Element {
    private String nom;
    private Type type;
    private Cat cat;
    private Integer val;
    private Integer nb_param;
    private Integer rang;
    private Integer nb_local;
    private Element scope;

    //Variables
    public Element(String nom, Type type, Cat cat, Integer val) {
        this.nom = nom;
        this.type = type;
        this.cat = cat;
        this.val = val;
        this.nb_local = 0;
    }

    //Fonction
    public Element(String nom, Type type, Cat cat) {
        this.nom = nom;
        this.type = type;
        this.cat = cat;
        this.nb_local = 0;
    }

    public enum Type{
        INT, VOID
    }

    public enum Cat{
        GLOBAL, LOCAL, FONCTION, PARAM
    }
    public String toString() {
        return "{"+this.getNom() + "," + this.getType() + "," + this.getCat() + "," + this.getVal() + "}";
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getVal() {
        return val;
    }

    public void setVal(Integer val) {
        this.val = val;
    }

    public Integer getNb_param() {
        return nb_param;
    }

    public void setNb_param(Integer nb_param) {
        this.nb_param = nb_param;
    }

    public Integer getRang() {
        return rang;
    }

    public void setRang(Integer rang) {
        this.rang = rang;
    }

    public Integer getNb_local() {
        return nb_local;
    }

    public void setNb_local(Integer nb_local) {
        this.nb_local = nb_local;
    }

    public Element getScope() {
        return scope;
    }

    public void setScope(Element scope) {
        this.scope = scope;
    }
}
