package fr.ul.miashs.compil.yoan.traducteur.Exemples;

import fr.ul.miashs.compil.arbre.*;
import fr.ul.miashs.compil.tds.Element;
import fr.ul.miashs.compil.tds.Tds;
import fr.ul.miashs.compil.yoan.traducteur.Generateur;

public class Exemple1 {
    public static void main(String[] args) {
        //Programme minimal
        Generateur gen = new Generateur();
        Tds tds = new Tds();
        Element fonction = new Element("main", Element.Type.VOID, Element.Cat.FONCTION);
        tds.ajouter("fonction", fonction);
        Prog prog = new Prog();
        Noeud main = new Fonction(fonction);
        prog.ajouterUnFils(main);
        TxtAfficheur.afficher(prog);
        System.out.println(tds);
        System.out.println(gen.generer_programme(prog, tds));
    }
}
