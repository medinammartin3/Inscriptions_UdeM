package org.openjfx.server;

import javafx.util.Pair;
import org.openjfx.server.models.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Modélise un serveur qui tourne à l'infini et qui attend des événements
 * envoyés par les clients. Il peut envoyer les cours disponibles pour une session demandé au client.
 * Il peut aussi inscrire un étudiant à un cours à l'aide d'une requête du client.
 *
 * @author Oussama Ben Sghaier
 * @author Étienne Mitchell-Bouchard
 * @author Martin Medina
 */
public class Server {

    /**
     * La variable REGISTER_COMMAND est la commande pour inscrire un cours.
     */
    public final static String REGISTER_COMMAND = "INSCRIRE";

    /**
     * La variable LOAD_COMMAND est la commande pour charger un cours.
     */
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    /**
     * Le constructeur initialise le ServerSocket au port reçu en paramètre
     * et instancie le ArrayList handlers. Il ajoute aussi un EventHandler qui a pour fonction
     * handle() la fonction handleEvents() défini plus loin.
     *
     * @param port Le port auquel le serveur écoute.
     * @throws IOException La création du ServerSocket peut lancer une IOException
     */
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    /**
     * Ajoute un EventHandler au ArrayList handlers.
     *
     * @param h Le EventHandler à ajouter.
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    /**
     * Procédure principale du Server. Elle contient une boucle infini qui attend la connection
     * d'un client. Elle attend ensuite une commande du client, la procède et le déconnecte par la suite pour
     * attendre la connection d'un autre client.
     */
    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Écoute la commande du client. Une fois la commande reçue, elle est manipulée pour être plus facile
     * à gérer et est envoyée à la fonction alertHandlers().
     *
     * @throws IOException La lecture d'un objet avec ObjectInputStream peut lancer une IOException.
     * @throws ClassNotFoundException L'objet lu avec ObjectInputStream peut appartenir à une classe non trouvée.
     */
    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    /**
     * Transforme la commande du client en une paire de Strings qui
     * contient le type de commande et ses arguments pour faciliter
     * la gestion de la commande dans le serveur.
     *
     * @param line Commande du client en une ligne.
     * @return Paire de Strings contenant le type de la commande du client et ces arguments.
     */
    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    /**
     * Permet de déconnecter le client connecté du serveur pour
     * permettre à un autre client de se connecter.
     *
     * @throws IOException La fermeture des streams et du client peut causer une IOException.
     */
    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    /**
     * Décide quel fontion appeler selon le type de la commande du client
     * et envoie les paramètres reçus en arguments si nécessaire.
     *
     * @param cmd Le type de commande du client.
     * @param arg Les arguments de la commande du client.
     */
    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     Lire un fichier texte contenant des informations sur les cours et les transformer en liste d'objets 'Course'.
     La méthode filtre les cours par la session spécifiée en argument.
     Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/java/org/openjfx/server/data/cours.txt"));
            ArrayList<Course> courseList = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splitLine = line.split("\t");
                Course course = new Course(splitLine[1], splitLine[0], splitLine[2]);
                if (course.getSession().equals(arg)) {
                    courseList.add(course);
                }
            }
            objectOutputStream.writeObject(courseList);
            reader.close();

        } catch (IOException e) {
            System.out.println("Erreur IO: " + e);
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("handleLoadCourses(): IndexOutOfBoundsException: " + e);
        }

    }
    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {
        try {
            RegistrationForm registrationForm = (RegistrationForm) objectInputStream.readObject();
            Course course = registrationForm.getCourse();
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/org/openjfx/server/data/inscription.txt", true));
            String[] registration = new String[]{
                    course.getSession(), course.getCode(), registrationForm.getMatricule(),
                    registrationForm.getPrenom(), registrationForm.getNom(), registrationForm.getEmail()
            };
            writer.write(String.join("\t", registration) + "\n");
            writer.close();
            objectOutputStream.writeObject("\nFélicitations! Inscription réussie de " + registrationForm.getPrenom() +
                    " au cours " + registrationForm.getCourse().getCode() + ".");
        }
        catch (IOException e) {
            System.out.println("Erreur IO: " + e);
        }
        catch (ClassNotFoundException e) {
            System.out.println("Class pas trouvé:" + e);
        }
    }
}