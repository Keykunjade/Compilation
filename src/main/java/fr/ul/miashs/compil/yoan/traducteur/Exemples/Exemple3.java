package fr.ul.miashs.compil.yoan.traducteur.Exemples;
import fr.ul.miashs.compil.tds.*;
import fr.ul.miashs.compil.arbre.*;
import fr.ul.miashs.compil.yoan.traducteur.Generateur;

public class Exemple3 {
    public static void main(String[] args) {
        Tds tds = new Tds();
        Generateur gen = new Generateur();
        //Remplissage de la table des symboles
        Element main_elem = new Element("main", Element.Type.VOID, Element.Cat.FONCTION);
        Element x = new Element("x", Element.Type.INT, Element.Cat.GLOBAL, 0);
        Element a = new Element("a", Element.Type.INT, Element.Cat.GLOBAL, 100);
        Element b = new Element("b", Element.Type.INT, Element.Cat.GLOBAL, 170);

        //Création des noeuds
        Prog prog = new Prog();
        Fonction main = new Fonction(main_elem);
        Affectation affectation = new Affectation();
        Idf idf_x = new Idf(x);
        Idf idf_a = new Idf(a);
        Idf idf_b = new Idf(b);
        Plus plus = new Plus();
        Multiplication mul = new Multiplication();
        Const deux = new Const(2);
        Division div = new Division();
        Moins moins = new Moins();
        Const trois = new Const(3);
        Const cinq = new Const(5);

        //Liaison des noeuds
        prog.ajouterUnFils(main);
        main.ajouterUnFils(affectation);
        affectation.setFilsGauche(idf_x);
        affectation.setFilsDroit(plus);
        plus.setFilsGauche(mul);
        mul.setFilsGauche(idf_a);
        mul.setFilsDroit(deux);
        plus.setFilsDroit(div);
        div.setFilsGauche(moins);
        moins.setFilsGauche(idf_b);
        moins.setFilsDroit(cinq);
        div.setFilsDroit(trois);

        //Affichage
        TxtAfficheur.afficher(prog);
        System.out.println(tds);
        System.out.println(gen.generer_programme(prog, tds));
    }
}
