package org.openjfx.client;

import org.openjfx.server.models.*;

import java.util.Scanner;
import java.util.*;

public class ClientLauncher {
    public final static int PORT = 1500;
    static Client client;
    static ArrayList<Course> courseList;
    public static void main(String[] args){
        client = new Client();
        System.out.println("*** Bienvenue au portail d'inscriptions de cours de l'UDEM ***");
        choixSession();
    }

    static void choixSession(){
        System.out.println("\nVeuillez choisir la session pour laquelle vous voulez consulter la liste de cours :");
        System.out.print("1. Automne\n2. Hiver\n3. Ete\n> Choix: ");
        String[] cours = new String[]{"Automne", "Hiver", "Ete"};
        Scanner scannerSession = new Scanner(System.in);
        int session;
        String entree = scannerSession.next();
        if (entree.equals("1"))
            session = 1;
        else if (entree.equals("2"))
            session = 2;
        else if (entree.equals("3"))
            session = 3;
        else {
            System.out.println("Entrée invalide! Choix par défaut: Automne");
            session = 1;
        }
        System.out.println("Les cours offerts pendant la session d'" + cours[session - 1].toLowerCase() + " sont :");
        client.connect("127.0.0.1", PORT);
        courseList = client.getCourses(cours[session - 1]);
        if (courseList != null) {
            for(int i = 0; i < courseList.size(); i++){
                System.out.println(i + 1 + ". " + courseList.get(i).getCode() + "\t" + courseList.get(i).getName());
            }
            decisionSuivante();
        }
        else {
            System.out.println("Erreur du serveur. Inscription impossible.");
            choixSession();
        }
    }

    static void decisionSuivante(){
        System.out.println("\nVeuillez choisir ce que vous voulez faire ensuite:");
        System.out.println("1. Consulter les cours offerts pour une autre session");
        System.out.print("2. Inscription à un cours\n> Choix: ");
        Scanner scannerDecision = new Scanner(System.in);
        String decision = scannerDecision.next();
        if (decision.equals("1")){
            choixSession();
        } else if (decision.equals("2")) {
            inscriptionCours();
        }
        else {
            System.out.println("Choix invalide! Veuillez réessayer.");
            decisionSuivante();
        }
    }

    static void inscriptionCours() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nVeuillez saisir votre Prénom: ");
        String prenom = scanner.next();
        System.out.print("Veuillez saisir votre Nom: ");
        String nom = scanner.next();
        System.out.print("Veuillez saisir votre Email: ");
        String email = scanner.next();
        System.out.print("Veuillez saisir votre Matricule: ");
        String matricule = scanner.next();

        boolean validCode = true;
        Course course = null;
        while (validCode)    {
            System.out.print("Veuillez saisir le Code du Cours: ");
            String codeCours = scanner.next();
            for(Course c : courseList) {
                if (c.getCode().equals(codeCours)) {
                    course = c;
                    validCode = false;
                    break;
                }
            }
            if (course == null) {
                System.out.println("Le code de cours que vous avez entré est invalide!");
            }
        }
        client.connect("127.0.0.1", PORT);
        String answer = client.registerToCourse(new RegistrationForm(prenom, nom, email, matricule, course));
        if (answer != null) {
            System.out.println(answer);
        }
        else {
            System.out.println("Erreur avec le serveur au moment de l'inscription. Veuillez réessayer plus tard.");
            inscriptionCours();
        }
        client.stop();
    }
}