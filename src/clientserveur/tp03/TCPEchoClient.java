package clientserveur.tp03;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
/**
 * Classe principale du Client.
 *
 */
public class TCPEchoClient {
	/**
	 * Flux de sortie obtenu à partir de la socket.
	 */
	OutputStream out;
	/**
	 * Flux d'entrée obtenu à partir de la socket.
	 */
	BufferedReader in;
	/**
	 * Socket reliant le client au server
	 */
	Socket toServer;

	/**
	 * Constructeur
	 */
	public TCPEchoClient() {
	}

	/**
	 * Méthode initialisant le client.
	 * @param host : le nom ou l'adresse du serveur
	 * @param port : le port TCP sur lequel on désire effectuer la connexion
	 * @throws UnknownHostException : levée si le nom ou l'adresse sont invalides.
	 * @throws IOException : levée si on n'arrive pas à récupérer les flux d'entrée ou de sortie.
	 */
	public void start(String host, int port) throws UnknownHostException, IOException {
		if (toServer == null) {
			toServer = new Socket();
		}

		toServer.connect(new InetSocketAddress(InetAddress.getByName(host), port));
		out = toServer.getOutputStream();
		in = new BufferedReader(new InputStreamReader(toServer.getInputStream()));
		System.out.println("Connexion au serveur réussie");
		
	}
	
    /**
     * Méthode d'arrêt du client. Elle ferme simplement la socket.
     */
	public void stop() {

		try {
			if (toServer != null && !toServer.isClosed())
				toServer.close();
		} catch (IOException e) {
		}

		toServer = null;

	}
	
	/**
	 * Méthode auxiliaire permettant d'écrire une chaine sur le flux de sortie.
	 * @param msg : la chaîne de caractères à envoyer
	 * @throws IOException  : levée en cas de problème d'écriture.
	 */
	private void sendMsg(String msg) throws IOException
	{
		out.write(msg.getBytes());
		out.flush();
	}
	
	/**
	 * Méthode principale du client qui implémente le protocole.
	 */
	public void mainLoop()
	{
		try {
			//Après que la connexion TCP est établie dans start(), on envoie un
			//message CONNECT au serveur.
			sendMsg("CONNECT\n");
			
			//On lit la réponse du serveur. Attention readLine() supprime le retour à la ligne.
			String ans = in.readLine();
			System.out.println(" <- Réponse du serveur : '" + ans + "'");
			
			//Si la réponse n'est pas celle à laquelle on s'attend, on quitte.
			if (!ans.equals("CONNECT OK")) return;
			
			//Pour lire l'entrée standard ligne à ligne
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			
			while (true)
			{
				//On lit une commande sur l'entrée standard et on l'envoie au serveur
				String cmd = input.readLine();
				System.out.println(" -> Envoi de la commande : '" + cmd + "'");
				sendMsg(cmd + "\n");
				
				//On affiche sur la sortie standard la réponse envoyée par le serveur.
				ans = in.readLine();
				System.out.println(" <- Réponse du serveur : '" + ans + "'");
				
				//Si on a donné la commande de déconnexion ou que le serveur a fermé
				//sa socket, on quitte.
				if (cmd.equals("DISCONNECT") || ans == null)
				{
					break;
				}
			}
			
			
		} catch (IOException e) {
			//Levée lors d'une écriture sur out si le serveur est déconnecté.
		}
		
	}
	
	
	public static void main(String[] args) {

		try {
			TCPEchoClient c = new TCPEchoClient();
			c.start("localhost", 12345);
			c.mainLoop();
			c.stop();			
		} catch (Exception e) {
			System.err.println("Erreur :");
			e.printStackTrace(System.err);
		}
		
	}

}
