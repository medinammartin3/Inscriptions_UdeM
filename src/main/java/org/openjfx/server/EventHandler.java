package org.openjfx.server;

/**
 * Interface fonctionnelle qui permet de déclarer dynamiquement
 * des 'event listeners' qui sont des classes anonymes qui
 * implémentent l'interface dont la méthode exécute une instruction.
 *
 * @author Oussama Ben Sghaier
 */
@FunctionalInterface
public interface EventHandler {
    /**
     * La fonction handle gère un événement donné.
     * @param cmd La commande à traiter.
     * @param arg Les arguments de la commande.
     */
    void handle(String cmd, String arg);
}
