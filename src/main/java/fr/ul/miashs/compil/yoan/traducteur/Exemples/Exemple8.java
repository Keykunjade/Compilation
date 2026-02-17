package fr.ul.miashs.compil.yoan.traducteur.Exemples;
import fr.ul.miashs.compil.tds.*;
import fr.ul.miashs.compil.arbre.*;
import fr.ul.miashs.compil.yoan.traducteur.Generateur;

public class Exemple8 {
    public static void main(String[] args) {
        Tds tds = new Tds();
        Generateur gen = new Generateur();
        Element main_elem = new Element("main", Element.Type.VOID, Element.Cat.FONCTION);
        Element i_elem = new Element("i", Element.Type.INT, Element.Cat.GLOBAL);
        tds.ajouter("main",main_elem);
        tds.ajouter("i",i_elem);

        Prog prog = new Prog();
        Fonction main = new Fonction(main_elem);
        prog.ajouterUnFils(main);
        Affectation affectation = new Affectation();
        Idf i = new Idf(i_elem);
        Const zero = new Const(0);
        affectation.setFilsGauche(i);
        affectation.setFilsDroit(zero);
        TantQue tq = new TantQue();
        main.ajouterUnFils(tq);
        Inferieur inferieur = new Inferieur();
        Bloc bloc = new Bloc();
        tq.setCondition(inferieur);
        tq.setBloc(bloc);
        Idf i_tq = new Idf(i_elem);
        Const six = new Const(6);
        inferieur.setFilsGauche(i_tq);
        inferieur.setFilsDroit(six);
        Ecrire ecrire = new Ecrire();
        Affectation affectation1 = new Affectation();
        Idf i_ecrire = new Idf(i_elem);
        ecrire.setLeFils(i_ecrire);
        bloc.ajouterUnFils(ecrire);
        bloc.ajouterUnFils(affectation1);
        Idf i_affectation1 = new Idf(i_elem);
        Plus plus = new Plus();
        affectation1.setFilsGauche(i_affectation1);
        affectation1.setFilsDroit(plus);
        Idf i_plus = new Idf(i_elem);
        plus.setFilsGauche(i_plus);
        Const un = new Const(1);
        plus.setFilsDroit(un);

        TxtAfficheur.afficher(prog);
        System.out.println(tds);
        System.out.println(gen.generer_programme(prog, tds));
    }
}
