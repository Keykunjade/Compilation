package fr.ul.miashs.compil.yoan.traducteur;

import fr.ul.miashs.compil.arbre.*;
import fr.ul.miashs.compil.yoan.traducteur.Generateur;

public class Main {
    public static void main(String[] args) {
        // x = a*2+(b-5)/3
        Idf x = new Idf("x");
        Idf a = new Idf("a");
        Idf b = new Idf("b");
        Const c2 = new Const(2);
        Const c5 = new Const(5);
        Const c3 = new Const(3);

        Fonction principal = new Fonction("main");
        Prog p = new Prog();
        Multiplication mul = new Multiplication();
        Moins moins = new Moins();
        Division div = new Division();
        Plus plus = new Plus();
        Affectation affectation = new Affectation();

        p.ajouterUnFils(principal);
        principal.ajouterUnFils(affectation);
        affectation.setFilsGauche(x);
        affectation.setFilsDroit(plus);
        plus.setFilsDroit(mul);
        plus.setFilsGauche(div);
        mul.setFilsDroit(a);
        mul.setFilsGauche(c2);
        div.setFilsGauche(moins);
        div.setFilsDroit(c3);
        moins.setFilsGauche(b);
        moins.setFilsDroit(c5);

        Generateur generateur = new Generateur();
        String code = generateur.generer_affectation(affectation);

        System.out.println(code);
        TxtAfficheur.afficher(principal);
    }
}
