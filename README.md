Le javadoc complet est présent dans le dossier 'javadoc' du projet GitHub. Dans le dossier javadoc/serveur présent dans ce dossier, vous trouverez seulement les fichiers HTML de Server.java, ServerLauncher.java, EventHandler.java.

Pour que server.jar puisse interagir avec les fichiers cours.txt et inscription.txt, il faut les placer dans un dossier 'data' qui se situe dans le même dossier que server.jar. (Exactement comme on a déjà fait dans le dossier 'fichiers_jar')

Quand on exécute client_fx.jar avec le terminal, nous obtenons "Warning : Unsupported JavaFX configuration: classes were loaded from 'unnamed module @15c89abd'". Cependant cela ne cause aucun problème de fonctionnement et/ou d'exécution. De plus, nous avons vérifié sur internet que cela n'était pas problématique.

Pour client_fx.jar, nous avons rencontré des problèmes lorsque nous essayons d'ouvrir le fichier créé avec macOS sur Windows et vice-versa (quantum errors). Alors, nous avons décidé de faire une version pour macOS et une pour Windows. Les deux fichiers sont vérifiés (testés sur plusieurs machines) et fonctionnent sur leur système d'exploitation respectif. De plus, nous avons eu l'approbation de Michalis et Mouna pour procéder de cette manière. Veuillez s'il vous plaît exécuter le fichier correspondant à votre système d'exploitation (soit "client_fx_mac.jar" ou "client_fx_windows.jar").

On n'a pas implémenté le bonus car il nous semblait peu utile pour notre projet grâce à la manière dont nous avons implémenté le côté client et le fonctionnement du serveur qui se déconnecte après chaque commande. Pour plus de détails, veuillez	lire le fichier bonus.txt.

Étant donné que nous avons eu des problèmes avec GitHub, nous avons push le projet complet d'un seul coup. Nous certifions (Martin Medina et Étienne Mitchell-Bouchard) que nous avons tous les deux travaillé en équipe et équitablement sur le projet, donc on demande d'avoir la même note. 
De plus, nous avons parlé avec Mouna en cours et elle a confirmé que c'était correct de faire cela.
