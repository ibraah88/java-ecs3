TCP, programmation Multi-thread en Java, TCP en Java

Pseudo code du serveur :

— le serveur initialise une socket TCP sur le port 12345 et se place en attente de connexion sur cet socket.

— sur réception d’une connexion, le serveur crée un nouveau thread de gestion de connexion. Ce thread récupère les flux 
d’entrée et de sortie associés au socket en le reliant au client se trouvant à l’autre et se met à suivre le protocole de l’application.

— une fois le thread crée et s’exécutant en parallèle, le serveur se remet à l’écoute d’une connexion sur le socket

— si aucune connexion n’a lieu dans un délais de 1000ms, le serveur vérifie si l’utilisateur a écrit sur l’entrée standard. 
  Si c’est le cas, le serveur se termine, sinon il retourne en attente de connexion.
  
  
Pseudo code du client :

— Le client initialise un socket TCP et la connecte au port 12345 de l’adresse du serveur (localhost par défaut).
  Le client récupère les flux d’entrée et de sortie associés au socket et le relie au serveur.
  
— Une fois la connexion établie, le client attend que l’utilisateur écrive une commande sur l’entrée standard. 
  Celle-ci est envoyée au serveur et la réponse de ce dernier est affiché dans la console.
  
— Le client termine quand l’utilisateur envoie la commande de déconnexion au serveur.


Protocole de l’application : Tous les messages échangés entre le client et le serveur se terminent par un retour à la ligne ("\n").


— Une fois la connexion établie, le client envoie la chaîne de caractères "CONNECT\n" au serveur.

— Le serveur renvoie la réponse "CONNECT OK\n".

— le client peut envoyer au serveur la commande "ECHO ...\n" où « ... » représente un texte libre mais sans retour à la ligne.

— Pour chaque ligne "ECHO ...\n" reçue, le serveur affiche la ligne sur sa sortie standard et renvoie la réponse 
  "RECEIVED l\n", où « l » est la longueur du message reçu (sans compter la partie "ECHO " ni le retour à la ligne).
  
— le client peut envoyer au serveur la commande "DISCONNECT\n".

— le serveur renvoie en réponse le message "BYE\n" puis coupe sa connexion avec le client.

— sur réception du message "BYE\n", le client se termine.

— tout d’un message m par le client autre que ceux décrit ci-dessus provoque la réponse "INVALID m\n" de la part du serveur.

Organisation du code : le code du serveur est réparti en deux classes. 

 - Une classe principale, TCPEchoServer qui crée le socket serveur et attends les connexions sur le port TCP 12345 
 
 - une classe TCPEchoConnection qui étend la classe Thread (ce qui permet à son code de s’exécuter en parallèle du reste de l’application). 

Le code du client ne possède qu’une seule classe, TCPEchoClient.
