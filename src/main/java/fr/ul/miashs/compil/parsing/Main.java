package fr.ul.miashs.compil.parsing;

import java.io.FileReader;
import fr.ul.miashs.compil.arbre.Prog;
import fr.ul.miashs.compil.tds.Tds;
import fr.ul.miashs.compil.yoan.traducteur.Generateur;
import fr.ul.miashs.compil.tds.ConstructeurTDS;
public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Erreur : Veuillez spécifier un fichier source à compiler.");
            System.err.println("Usage : java Main <fichier_source.txt>");
            return;
        }

        try {
            FileReader fichierSource = new FileReader(args[0]);
            Scanner lexer = new Scanner(fichierSource);
            parser analyseur = new parser(lexer);
            java_cup.runtime.Symbol resultat = analyseur.parse();
            // Récupération de l'AST
            Prog arbreSyntaxique = (Prog) resultat.value;
            ConstructeurTDS constructeur = new ConstructeurTDS();
            Tds tableDesSymboles = constructeur.construire(arbreSyntaxique);
            System.out.println( tableDesSymboles);
            Generateur generateur = new Generateur();
            String codeAssembleur = generateur.generer_programme(arbreSyntaxique, tableDesSymboles);
            System.out.println(codeAssembleur);


            String nomFichierUasm = "programme_genere.uasm";
            java.nio.file.Files.writeString(java.nio.file.Paths.get(nomFichierUasm), codeAssembleur);
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", "bsim.jar", nomFichierUasm);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(process.getInputStream())
            );

            String ligneSortie;
            while ((ligneSortie = reader.readLine()) != null) {
                System.out.println(ligneSortie);
            }
            process.waitFor();

        } catch (Exception e) {
            System.err.println("Erreur lors de la compilation");
            e.printStackTrace();
        }
    }
}