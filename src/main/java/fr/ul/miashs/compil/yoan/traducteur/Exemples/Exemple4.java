package fr.ul.miashs.compil.yoan.traducteur.Exemples;
import fr.ul.miashs.compil.tds.*;
import fr.ul.miashs.compil.arbre.*;
import fr.ul.miashs.compil.yoan.traducteur.Generateur;

public class Exemple4 {
    public static void main(String[] args) {
        Tds tds = new Tds();
        Generateur gen = new Generateur();
        //Initialisation de la tds
        Element main_elem = new Element("main", Element.Type.VOID, Element.Cat.FONCTION);
        Element res_elem = new Element("res", Element.Type.INT, Element.Cat.GLOBAL);
        tds.ajouter(main_elem.getNom(), main_elem);
        tds.ajouter(res_elem.getNom(), res_elem);

        //Initialiation des noeuds
        Prog prog = new Prog();
        Fonction fonction = new Fonction(main_elem);
        prog.ajouterUnFils(fonction);
        Affectation affectation = new Affectation();
        Ecrire ecrire = new Ecrire();
        fonction.ajouterUnFils(affectation);
        fonction.ajouterUnFils(ecrire);
        Idf res = new Idf(res_elem);
        affectation.setFilsGauche(res);
        ecrire.setLeFils(res);
        Plus plus = new Plus();
        affectation.setFilsDroit(plus);
        Multiplication mul = new Multiplication();
        Division div = new Division();
        plus.setFilsGauche(mul);
        plus.setFilsDroit(div);
        Lire lire = new Lire();
        Const deux = new Const(2);
        mul.setFilsGauche(lire);
        mul.setFilsDroit(deux);
        Moins moins = new Moins();
        Const trois = new Const(3);
        div.setFilsGauche(moins);
        div.setFilsDroit(trois);
        moins.setFilsGauche(lire);
        Const cinq = new Const(5);
        moins.setFilsDroit(cinq);

        TxtAfficheur.afficher(prog);
        System.out.println(tds);
        System.out.println(gen.generer_programme(prog, tds));
    }
}
