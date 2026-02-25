package fr.ul.miashs.compil.yoan.traducteur.Exemples;
import fr.ul.miashs.compil.arbre.Prog;
import fr.ul.miashs.compil.tds.Element;
import fr.ul.miashs.compil.tds.Tds;
import fr.ul.miashs.compil.arbre.*;
import fr.ul.miashs.compil.yoan.traducteur.Generateur;

public class Exemple2 {
    public static void main(String[] args) {
        //Initialisation
        Tds tds = new Tds();
        Generateur gen = new Generateur();

        //création et ajout des éléments à la TDS
        Element main_elem = new Element("main", Element.Type.VOID, Element.Cat.FONCTION);
        Element i = new Element("i", Element.Type.INT, Element.Cat.GLOBAL, 10);
        Element j = new Element("j", Element.Type.INT, Element.Cat.GLOBAL, 20);
        Element k = new Element("k", Element.Type.INT, Element.Cat.GLOBAL);
        Element l = new Element("l", Element.Type.INT, Element.Cat.GLOBAL);
        tds.ajouter(i.getNom(), i);
        tds.ajouter(j.getNom(), j);
        tds.ajouter(k.getNom(), k);
        tds.ajouter(l.getNom(), l);
        tds.ajouter(main_elem.getNom(), main_elem);

        //création de l'AST
        Prog prog = new Prog();
        Fonction main = new Fonction(main_elem);
        prog.ajouterUnFils(main);

        //Affichage
        TxtAfficheur.afficher(prog);
        System.out.println(tds);
        System.out.println(gen.generer_programme(prog, tds));

    }

}
