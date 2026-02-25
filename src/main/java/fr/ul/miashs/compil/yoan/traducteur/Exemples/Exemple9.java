package fr.ul.miashs.compil.yoan.traducteur.Exemples;
import fr.ul.miashs.compil.tds.*;
import fr.ul.miashs.compil.arbre.*;
import fr.ul.miashs.compil.yoan.traducteur.Generateur;

public class Exemple9 {
    public static void main(String[] args) {
        Tds tds = new Tds();
        Generateur gen = new Generateur();
        //Instanciation  et ajout des éléments dans la table des symboles
        Element main_elem = new Element("main", Element.Type.VOID, Element.Cat.FONCTION);
        Element f_elem = new Element("f", Element.Type.INT, Element.Cat.FONCTION);
        f_elem.setNb_param(1);
        Element a_elem = new Element("a", Element.Type.INT, Element.Cat.PARAM, 0, f_elem);
        tds.ajouter("main",main_elem);
        tds.ajouter("f",f_elem);
        tds.ajouter("a",a_elem);

        //Instanciation et liaison des noeuds de l'AST
        Prog prog = new Prog();
        Fonction main = new Fonction(main_elem);
        Fonction f = new Fonction(f_elem);
        prog.ajouterUnFils(f);
        prog.ajouterUnFils(main);

        Ecrire ecrire = new Ecrire();
        Appel appel_main = new Appel(f_elem);
        Const six = new Const(6);
        main.ajouterUnFils(ecrire);
        ecrire.ajouterUnFils(appel_main);
        appel_main.ajouterUnFils(six);

        Si si = new Si();
        Plus plus = new Plus();
        Retour retour = new Retour(f_elem);
        retour.setLeFils(plus);
        f.ajouterUnFils(si);
        f.ajouterUnFils(retour);
        Idf a = new Idf(a_elem);
        Appel appel_f  = new Appel(f_elem);
        plus.setFilsGauche(a);
        plus.setFilsDroit(appel_f);
        Moins moins = new Moins();
        appel_f.ajouterUnFils(moins);
        Idf a_r = new Idf(a_elem);
        Const un = new Const(1);
        moins.setFilsDroit(un);
        moins.setFilsGauche(a_r);

        InferieurEgal inferieurEgal = new InferieurEgal();
        Bloc bloc = new Bloc();
        si.setCondition(inferieurEgal);
        si.setBlocAlors(bloc);
        Idf a_ie = new Idf(a_elem);
        Const zero = new Const(0);
        inferieurEgal.setFilsDroit(zero);
        inferieurEgal.setFilsGauche(a_ie);
        Const zero_r = new Const(0);
        Retour retour_f = new Retour(f_elem);
        bloc.ajouterUnFils(retour_f);
        retour_f.setLeFils(zero_r);

        //Affichage
        TxtAfficheur.afficher(prog);
        System.out.println(tds);
        System.out.println(gen.generer_programme(prog,tds));
    }
}
