package fr.ul.miashs.compil.yoan.traducteur.Exemples;
import fr.ul.miashs.compil.tds.*;
import fr.ul.miashs.compil.arbre.*;
import fr.ul.miashs.compil.yoan.traducteur.Generateur;

import java.util.LinkedList;
import java.util.List;

public class Exemple6 {
    public static void main(String[] args) {
        Tds tds = new Tds();
        Generateur gen = new Generateur();
        Element main_elem = new Element("main", Element.Type.VOID, Element.Cat.FONCTION);
        Element f_elem = new Element("f", Element.Type.INT, Element.Cat.FONCTION, 2, 1);
        Element a_elem = new Element("a", Element.Type.INT, Element.Cat.GLOBAL, 100);
        Element c_elem = new Element("c", Element.Type.INT, Element.Cat.GLOBAL, 170);
        Element a_param = new Element("a_param", Element.Type.INT, Element.Cat.PARAM, 0, f_elem);
        Element b_param = new Element("b", Element.Type.INT, Element.Cat.PARAM, 1, f_elem);
        Element res_elem = new Element("res", Element.Type.INT, Element.Cat.LOCAL, 0, f_elem);
        tds.ajouter(main_elem.getNom(), main_elem);
        tds.ajouter(f_elem.getNom(), f_elem);
        tds.ajouter(a_elem.getNom(), a_elem);
        tds.ajouter(c_elem.getNom(), c_elem);
        tds.ajouter(a_param.getNom(), a_param);
        tds.ajouter(b_param.getNom(), b_param);
        tds.ajouter(res_elem.getNom(), res_elem);

        Prog prog = new Prog();
        Fonction main = new Fonction(main_elem);
        Fonction f = new Fonction(f_elem);
        prog.ajouterUnFils(main);
        prog.ajouterUnFils(f);
        Ecrire ecrire = new Ecrire();
        main.ajouterUnFils(ecrire);
        Appel appel = new Appel(f_elem);
        ecrire.setLeFils(appel);
        Noeud a = new Idf(a_elem);
        Noeud c = new Idf(c_elem);
        List<Noeud> list = new LinkedList<>();
        list.add(a);
        list.add(c);
        appel.ajouterDesFils(list);
        Affectation affectation = new Affectation();
        Retour retour = new Retour(res_elem);
        Idf res_retour = new Idf(res_elem);
        retour.setLeFils(res_retour);
        f.ajouterUnFils(affectation);
        f.ajouterUnFils(retour);
        Plus plus = new Plus();
        Idf res_affectation = new Idf(res_elem);
        affectation.setFilsGauche(res_affectation);
        affectation.setFilsDroit(plus);
        Multiplication multiplication = new Multiplication();
        Division division = new Division();
        plus.setFilsGauche(multiplication);
        plus.setFilsDroit(division);
        Idf a_p = new Idf(a_param);
        multiplication.setFilsGauche(a_p);
        Const deux = new Const(2);
        multiplication.setFilsDroit(deux);
        Const trois = new Const(3);
        Moins moins = new Moins();
        division.setFilsGauche(moins);
        division.setFilsDroit(trois);
        Idf b_p = new Idf(b_param);
        Const cinq = new Const(5);
        moins.setFilsDroit(cinq);
        moins.setFilsGauche(b_p);

        TxtAfficheur.afficher(prog);
        System.out.println(tds);
        System.out.println(gen.generer_programme(prog, tds));


    }
}
