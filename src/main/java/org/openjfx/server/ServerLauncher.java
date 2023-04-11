package org.openjfx.server;

/**
 * Contient la fonction main de l'application server.jar. Elle permet de lancer un objet
 * Server au port spécifié à l'aide de la fonction Server.run().
 *
 * @author Oussama Ben Sghaier
 */
public class ServerLauncher {

    /**
     * Le port auquel le serveur va écouter les requêtes de connections.
     */
    public final static int PORT = 1500;

    /**
     * Construit un objet Server et le lance.
     *
     * @param args Les arguments du programme passé à l'exécution.
     */
    public static void main(String[] args) {
        Server server;
        try {
            server = new Server(PORT);
            System.out.println("Server is running...");
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}