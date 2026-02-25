package fr.ul.miashs.compil.yoan.traducteur.Exemples;
import fr.ul.miashs.compil.yoan.traducteur.Generateur;
import fr.ul.miashs.compil.tds.*;
import fr.ul.miashs.compil.arbre.*;

public class Exemple5 {
    public static void main(String[] args) {
        //Initialisation
        Tds tds = new Tds();
        Generateur gen = new Generateur();

        //Instanciation  et ajout des éléments dans la TDS
        Element main_elem = new Element("main", Element.Type.VOID, Element.Cat.FONCTION);
        Element a_elem = new Element("a", Element.Type.INT, Element.Cat.GLOBAL, 100);
        Element b_elem = new Element("b", Element.Type.INT, Element.Cat.GLOBAL, 170);

        tds.ajouter(main_elem.getNom(), main_elem);
        tds.ajouter(a_elem.getNom(), a_elem);
        tds.ajouter(b_elem.getNom(), b_elem);

        //Instanciation  et liaison des noeuds de l'AST
        Prog prog = new Prog();
        Fonction main = new Fonction(main_elem);
        prog.ajouterUnFils(main);
        Ecrire ecrire = new Ecrire();
        main.ajouterUnFils(ecrire);
        Plus plus = new Plus();
        ecrire.ajouterUnFils(plus);
        Multiplication mul = new Multiplication();
        Division div = new Division();
        plus.setFilsGauche(mul);
        plus.setFilsDroit(div);
        Idf a = new Idf(a_elem);
        Const deux = new Const(2);
        mul.setFilsGauche(a);
        mul.setFilsDroit(deux);
        Const trois = new Const(3);
        Moins moins = new Moins();
        div.setFilsGauche(moins);
        div.setFilsDroit(trois);
        Idf b = new Idf(b_elem);
        Const cinq = new Const(5);
        moins.setFilsGauche(b);
        moins.setFilsDroit(cinq);

        //Affichage
        TxtAfficheur.afficher(prog);
        System.out.println(tds);
        System.out.println(gen.generer_programme(prog, tds));

    }
}
