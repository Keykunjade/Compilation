package fr.ul.miashs.compil.tds;
/**
 * Description : Classe représentant les élements d'une table des symboles
 * @author Ighir Yoan
 */
public class Element {
    private String nom;
    private Type type;
    private Cat cat;
    private Integer val;
    private Integer nb_param;
    private Integer rang;
    private Integer nb_local = 0;
    private Element scope;

    //ENUM
    //Enumération des types et des catégories
    public enum Type{
        INT, VOID
    }
    public enum Cat{
        GLOBAL, LOCAL, FONCTION, PARAM
    }

    //CONSTRUCTEURS
    //Constructeur variables
    public Element(String nom, Type type, Cat cat, Integer val) {
        this.nom = nom;
        this.type = type;
        this.cat = cat;
        this.val = val;
        this.nb_local = 0;
    }

    //Constructeur fonction sans paramètre
    public Element(String nom, Type type, Cat cat) {
        this.nom = nom;
        this.type = type;
        this.cat = cat;
    }

    //Constructeur fonction avec paramètre
    public Element(String nom, Type type, Cat cat, Integer nb_param, Integer nb_local){
        this.nom = nom;
        this.type = type;
        this.cat = cat;
        this.nb_param = nb_param;
        this.nb_local = nb_local;
    }

    //Constructeur paramètre
    public Element(String nom, Type type, Cat cat, Integer rang, Element scope){
        this.nom = nom;
        this.type = type;
        this.cat = cat;
        this.rang = rang;
        this.scope = scope;
    }

    //METHODES
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("{");

        sb.append(nom).append(", ");
        sb.append(type).append(", ");
        sb.append(cat);

        if (val != null) sb.append(", val:").append(val);
        if (nb_param != null) sb.append(", nbParam:").append(nb_param);
        if (rang != null) sb.append(", rang:").append(rang);
        if (nb_local != null && nb_local > 0) sb.append(", nbLocal:").append(nb_local);
        if (scope != null) sb.append(", scope:").append(scope.getNom());

        return sb.append("}").toString();
    }

    //getters & setters
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
