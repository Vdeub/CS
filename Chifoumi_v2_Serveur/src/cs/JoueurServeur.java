package cs;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Modèle de Joueur héritant de la classe Joueur, adapté à la partie applicative du serveur
 * @author Frederic
 *
 */
public class JoueurServeur extends Joueur implements Runnable  {
	private ServeurChifoumi serveur;

	public JoueurServeur(Socket socket, int numero, ServeurChifoumi serveur) {
		super(socket, numero);
		this.serveur = serveur;
	}

	/**
	 * Envoyer le numéro du Joueur au JoueurClient
	 */
	public void envoyerNumero() {
		out.println(Constantes.NUM + " " + numero);
	}
	
	/**
	 * Traiter le message provenant du client (arme choisie pour le duel)
	 * @param message
	 */
	void traiterMessage(String message) {
		int type;
		Scanner scan = new Scanner(message);

		type =scan.nextInt();

		switch (type) {
		case Constantes.CHOIX : choix = Choix.valueOf(scan.next());
		serveur.prevenirAdversaire(numero);
		serveur.examinerChoix();
		break;
		}
	}

	/**
	 * Transmettre le message de victoire au client gagnant
	 */
	public void aGagne() {
		super.aGagne();
		out.println(Constantes.GAGNE);
		if (partieGagnee()) out.println(Constantes.PARTIE_GAGNEE); 
	}

	/**
	 * Transmettre le message de défaite au client perdant
	 */
	public void aPerdu() {
		super.aPerdu();
		out.println(Constantes.PERDU);
		if (adversaire.partieGagnee()) out.println(Constantes.PARTIE_PERDUE);
	}

	/**
	 * Transmettre le message d'égalité aux deux joueurs
	 */
	public void egalite() {
		System.out.println("egalite");
		super.egalite();
		out.println(Constantes.EGALITE);
	}

	/**
	 * Run du thread, traite les message venant du client
	 */
	public void run() {
		String message;

		try {
			message = in.readLine();
			while (!jeuFini) {
				traiterMessage(message);
				message = in.readLine();
			}
		}
		catch(IOException exc) {

		}
	}
}
