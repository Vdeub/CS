package cs;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

/**
 * ServeurChiFouMi, MŽthode principale de la partie applicative sur le serveur
 *
 * @author Frederic
 *
 */
public class ServeurChifoumi {
	private JoueurServeur[] lesJoueurs;
	private int numSuivant = 0;
	public ServeurChifoumi(JoueurServeur[] lesJoueurs) {
		this.lesJoueurs = lesJoueurs;
	}
	
	public static void main(String[] arg) throws Exception {
		int portChifoumi = 4567;
		ServerSocket  receptionniste =  new ServerSocket(portChifoumi);		
		JoueurServeur[] lesJoueurs = new JoueurServeur[2];

		ServeurChifoumi serveur = new ServeurChifoumi(lesJoueurs);
		for (int i = 0; i < 2; i++)	serveur.ajouterJoueur(receptionniste.accept());

		new ServeurChifoumi(lesJoueurs);
	}
	
	
	
/**
 * Ajouter un joueur pour commencer une partie
 * @param socket
 */
	public void ajouterJoueur(Socket socket) {
		lesJoueurs[numSuivant] = new JoueurServeur(socket, numSuivant, this);
		numSuivant++;
		// Si deux joueurs sont connectŽs, on peut jouer
		if (numSuivant == 2) donnerFeuVert();
	}

	/**
	 * Traiter le message recu par un client (joueur)
	 * @param message
	 * @param joueur
	 */
	public void traiterMessage(String message, Joueur joueur) {
		System.out.println(message);
		Scanner scan = new Scanner(message);
		int type = scan.nextInt();
	}

	/**
	 * Recherche du joueur gagnant pour chaque duel en fonction des armes utilisŽes par chacun des joueurs
	 */
	public void chercherJoueurGagnant() {
		attendre();
		Choix choix0 = lesJoueurs[0].choix;
		Choix choix1 = lesJoueurs[1].choix;
		if (choix0.equals(choix1)) egalite();
		
		if (choix0 == Choix.CISEAUX) {
			if (choix1 ==  Choix.FEUILLE) validerGain(0);
			else if  (choix1 ==  Choix.CAILLOU) validerGain(1);
			else if  (choix1 ==  Choix.LEZARD) validerGain(0);
			else if  (choix1 ==  Choix.SPOCK) validerGain(1);
		}
		else if (choix0 == Choix.FEUILLE)  {
			if (choix1 ==  Choix.CAILLOU) validerGain(0); 
			else if  (choix1 ==  Choix.CISEAUX) validerGain(1);
			else if  (choix1 ==  Choix.LEZARD) validerGain(1);
			else if  (choix1 ==  Choix.SPOCK) validerGain(0);
		}
		else if (choix0 == Choix.CAILLOU)  {
			if (choix1 ==  Choix.CISEAUX) validerGain(0);  
			else if  (choix1 ==  Choix.FEUILLE) validerGain(1);
			else if  (choix1 ==  Choix.LEZARD) validerGain(0);
			else if  (choix1 ==  Choix.SPOCK) validerGain(1);
		}
		else if (choix0 == Choix.LEZARD)  {
			if (choix1 ==  Choix.CISEAUX) validerGain(1);  
			else if  (choix1 ==  Choix.FEUILLE) validerGain(0);
			else if  (choix1 ==  Choix.CAILLOU) validerGain(1);
			else if  (choix1 ==  Choix.SPOCK) validerGain(0);
		}
		else if (choix0 == Choix.SPOCK)  {
			if (choix1 ==  Choix.CISEAUX) validerGain(0);  
			else if  (choix1 ==  Choix.FEUILLE) validerGain(1);
			else if  (choix1 ==  Choix.LEZARD) validerGain(1);
			else if  (choix1 ==  Choix.CAILLOU) validerGain(0);
		}
	}
	
	/**
	 * Prise en compte de l'ŽgalitŽ lorsque les deux joeurs on jouŽ la mme arme
	 */
	public void egalite() {	
		lesJoueurs[0].egalite();	
		lesJoueurs[1].egalite();
	}
	
	/**
	 * Annuler les choix d'arme retenu pour chaque joueur afin de jouer le prochain duel
	 */
	public void annulerChoix() {
		lesJoueurs[0].choix = null;
		lesJoueurs[1].choix = null;
	}

	/**
	 * Valider les gains des deux joueurs en incrŽmentant (ou pas) leur score 
	 * @param numJoueur
	 */
	void validerGain(int numJoueur) {
		lesJoueurs[numJoueur].aGagne();
		lesJoueurs[1 - numJoueur].aPerdu(); //Si 1 a gagnŽ alors 1-1 = 0 a perdu et si 0 a gagnŽ, alors 1- 0 a perdu		
	}
	
	/**
	 * On lance le feu vert pour commencer une partie quand les deux joueurs sont disponibles
	 */
	public void donnerFeuVert() {
		// On envoie le numŽro ˆ tous les joueurs
		for (JoueurServeur j : lesJoueurs) j.envoyerNumero();
	}


	/**
	 * On prŽvient le joueur de l'arme qui a ŽtŽ choisi par l'adversaire
	 * @param numero
	 */
	
	public void prevenirAdversaire(int numero) {
		Choix choix = lesJoueurs[numero].choix;
		lesJoueurs[1 - numero].out.println(Constantes.CHOIX_ADVERSAIRE + " " + choix);
	}

	/**
	 * Temporisation avant la rŽvŽlation des rŽsultats
	 */
	public void attendre() {
		try {
			Thread.sleep(3000);
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	/**
	 * MŽthode d'examen du duel
	 */
	public void examinerChoix() {
		// Le duel est fini si les deux joueurs ont faity un choix 
		boolean fini = ((lesJoueurs[0].choix != null) && (lesJoueurs[1].choix != null));
		System.out.println("Les deux joueurs ont jouŽs : " + fini);
		// Quand les deux jouers ont jouŽs, on cherche le gagnant et on remet ˆ 0 le choix fait pas chacun pour le prochain duel
		if (fini) {
			chercherJoueurGagnant();
			annulerChoix();
		}
	}
}
