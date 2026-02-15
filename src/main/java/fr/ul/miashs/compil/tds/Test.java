package fr.ul.miashs.compil.tds;

public class Test {
    public static void main(String[] args) {
        Tds tds = new Tds();
        Element i = new Element("i", Element.Type.INT, Element.Cat.GLOBAL,10);
        Element j = new Element("i", Element.Type.INT, Element.Cat.GLOBAL, 5);
        tds.ajouter("i", i);
        tds.ajouter("j", j);
        System.out.println(tds.rechercher(i));
        System.out.println(tds);

    }
}
