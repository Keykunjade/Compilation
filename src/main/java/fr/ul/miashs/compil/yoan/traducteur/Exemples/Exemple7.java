package fr.ul.miashs.compil.yoan.traducteur.Exemples;
import fr.ul.miashs.compil.tds.*;
import fr.ul.miashs.compil.arbre.*;
import fr.ul.miashs.compil.yoan.traducteur.Generateur;

public class Exemple7 {
    public static void main(String[] args) {
        Tds tds = new Tds();
        Generateur gen = new Generateur();

        //Instanciation  et ajout des éléments à la table des symboles
        Element main_elem = new Element("main", Element.Type.VOID, Element.Cat.FONCTION);
        Element a_elem = new Element("a", Element.Type.INT, Element.Cat.GLOBAL, 1);
        Element b_elem = new Element("b", Element.Type.INT, Element.Cat.GLOBAL, 2);
        Element x_elem = new Element("x", Element.Type.INT, Element.Cat.GLOBAL);

        tds.ajouter("main", main_elem);
        tds.ajouter("a", a_elem);
        tds.ajouter("b", b_elem);
        tds.ajouter("x", x_elem);

        //Instanciation  et liaison des noeuds de l'AST
        Prog prog = new Prog();
        Fonction main = new Fonction(main_elem);
        prog.ajouterUnFils(main);
        Si si = new Si();
        main.ajouterUnFils(si);
        Superieur superieur = new Superieur();
        Bloc bloc_alors = new Bloc();
        Bloc bloc_sinon = new Bloc();
        si.setCondition(superieur);
        si.setBlocAlors(bloc_alors);
        si.setBlocSinon(bloc_sinon);
        Idf a = new Idf(a_elem);
        Idf b = new Idf(b_elem);
        superieur.setFilsGauche(a);
        superieur.setFilsDroit(b);
        Affectation affectation_alors = new Affectation();
        bloc_alors.ajouterUnFils(affectation_alors);
        Idf x_alors = new Idf(x_elem);
        Const mille = new Const(1000);
        affectation_alors.setFilsGauche(x_alors);
        affectation_alors.setFilsDroit(mille);
        Affectation affectation_sinon = new Affectation();
        bloc_sinon.ajouterUnFils(affectation_sinon);
        Idf x_sinon = new Idf(x_elem);
        Const mille2 = new Const(2000);
        affectation_sinon.setFilsGauche(x_sinon);
        affectation_sinon.setFilsDroit(mille2);

        //Affichage
        TxtAfficheur.afficher(prog);
        System.out.println(tds);
        System.out.println(gen.generer_programme(prog, tds));

    }
}
