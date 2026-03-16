package fr.ul.miashs.compil.tds;

import fr.ul.miashs.compil.arbre.*;

/**
 * Parcourt l'AST pour construire la table des symboles (TDS)
 * et lier les noeuds de l'arbre (Idf, Fonction, Appel) à leurs Elements.
 */
public class ConstructeurTDS {
    private Tds tds;
    private Element scopeCourant; // Pointe vers la fonction actuelle (null si global)
    private int cptParam;
    private int cptLocal;

    public ConstructeurTDS() {
        this.tds = new Tds();
    }

    public Tds construire(Prog p) {
        // --- PASSE 1 : Variables globales et déclaration des Fonctions ---
        for (Noeud n : p.getFils()) {
            if (n instanceof Idf) {
                // Déclaration globale sans affectation (ex: int k)
                String nom = (String) ((Idf) n).getValeur();
                Element e = new Element(nom, Element.Type.INT, Element.Cat.GLOBAL, null);
                tds.ajouter(nom, e);
                ((Idf) n).setValeur(e); // Le lien magique pour ton Générateur !

            } else if (n instanceof Affectation) {
                // Déclaration globale avec affectation (ex: int i = 10)
                Idf idf = (Idf) ((Affectation) n).getFilsGauche();
                String nom = (String) idf.getValeur();

                Integer val = null;
                if (((Affectation) n).getFilsDroit() instanceof Const) {
                    val = ((Const) ((Affectation) n).getFilsDroit()).getValeur();
                }

                Element e = new Element(nom, Element.Type.INT, Element.Cat.GLOBAL, val);
                tds.ajouter(nom, e);
                idf.setValeur(e);

            } else if (n instanceof Fonction) {
                // Déclaration de fonction
                String nomFunc = (String) ((Fonction) n).getValeur();
                Element e = new Element(nomFunc, Element.Type.VOID, Element.Cat.FONCTION); // Mis à VOID par défaut, à adapter
                tds.ajouter(nomFunc, e);
                ((Fonction) n).setValeur(e);
            }
        }

        // --- PASSE 2 : Résolution du corps des fonctions ---
        for (Noeud n : p.getFils()) {
            if (n instanceof Fonction) {
                traiterFonction((Fonction) n);
            }
        }

        return tds;
    }

    private void traiterFonction(Fonction f) {
        scopeCourant = (Element) f.getValeur();
        cptParam = 0;
        cptLocal = 0;

        // 1. Déclarer les paramètres (enfants directs de la fonction avant le bloc)
        for (Noeud n : f.getFils()) {
            if (n instanceof Idf) {
                String nomParam = (String) ((Idf) n).getValeur();
                Element eParam = new Element(nomParam, Element.Type.INT, Element.Cat.PARAM, cptParam, scopeCourant);
                // On utilise "nomFonction.nomParam" pour éviter les collisions dans la TDS
                tds.ajouter(scopeCourant.getNom() + "." + nomParam, eParam);
                ((Idf) n).setValeur(eParam);
                cptParam++;
            }
        }
        scopeCourant.setNb_param(cptParam);

        // 2. Parcourir le bloc de la fonction pour lier toutes les autres variables
        for (Noeud n : f.getFils()) {
            if (n instanceof Bloc) {
                parcourirNoeud(n);
            }
        }

        scopeCourant.setNb_local(cptLocal);
        scopeCourant = null; // Sortie de la fonction
    }

    private void parcourirNoeud(Noeud n) {
        if (n == null) return;

        if (n instanceof Idf) {
            lierIdf((Idf) n);
        }
        else if (n instanceof Appel) {
            // Lier l'appel à la fonction cible
            String nomFunc = (String) ((Appel) n).getValeur();
            Element eFunc = tds.getTds().get(nomFunc);
            if (eFunc == null) {
                throw new RuntimeException("Erreur: Appel a une fonction non declaree -> " + nomFunc);
            }
            ((Appel) n).setValeur(eFunc);

            // Ne pas oublier d'analyser les arguments de l'appel !
            for (Noeud arg : n.getFils()) {
                parcourirNoeud(arg);
            }
        }
        else {
            // Descente récursive pour tous les autres noeuds (Affectation, Si, Plus...)
            if (n.getFils() != null) {
                for (Noeud fils : n.getFils()) {
                    parcourirNoeud(fils);
                }
            }
        }
    }

    private void lierIdf(Idf idf) {
        // Si c'est déjà un Element (ex: paramètre déjà traité), on ignore
        if (idf.getValeur() instanceof Element) return;

        String nom = (String) idf.getValeur();
        Element e = null;

        // 1. Chercher en local/paramètre
        if (scopeCourant != null) {
            e = tds.getTds().get(scopeCourant.getNom() + "." + nom);
        }
        // 2. Chercher en global
        if (e == null) {
            e = tds.getTds().get(nom);
        }

        // 3. Déduction intelligente : si la variable n'existe nulle part,
        // c'est qu'il s'agit d'une nouvelle variable locale.
        // (Car ton AST actuel ne différencie pas "int x = 5" de "x = 5").
        if (e == null && scopeCourant != null) {
            e = new Element(nom, Element.Type.INT, Element.Cat.LOCAL);
            e.setRang(cptLocal);
            tds.ajouter(scopeCourant.getNom() + "." + nom, e);
            cptLocal++;
        } else if (e == null) {
            throw new RuntimeException("Erreur: Variable non declaree -> " + nom);
        }

        // 4. On remplace la String par l'Element !
        idf.setValeur(e);
    }
}
