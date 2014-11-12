package cs;

import java.awt.Color;
import java.awt.Dimension;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Classe principale du client, elle hérite de l'objet Client, adapté à la partie applicative du cLient
 * @author Frederic
 *
 */
public class JoueurClient  extends Joueur implements Runnable  {
	public JoueurClient() {}

	public JoueurClient(Socket socket)  throws Exception{	
		super(socket);
		adversaire = new JoueurClient();
	}	

	/**
	 * Traiter les messages provenant du serveur
	 * @param message
	 */
	public void traiterMessage(String message) {
		Scanner scan = new Scanner(message);
		int type = scan.nextInt();
		switch(type) {
		case Constantes.NUM : 
			numero = scan.nextInt();
			jouer = true;
			break;
		case Constantes.GAGNE : 
			aGagne();
			break;
		case Constantes.PERDU : 
			aPerdu();
			break;
		case Constantes.EGALITE : 
			egalite();
			break;
		case Constantes.PARTIE_GAGNEE : 
			partieGagnee = true;
			jeuFini = true;
			break;
		case Constantes.PARTIE_PERDUE : 
			partiePerdue = true;
			jeuFini = true;
			break;
		case Constantes.CHOIX_ADVERSAIRE : 
			adversaire.choix = Choix.valueOf(scan.next());
			break;
		}
		setChanged();
		notifyObservers();
	}

	/**
	 * Tant que la partie n'est pas terminée, on traite les messages vennat du serveur
	 */
	public void run() {
		while(!jeuFini)
			try {
				String message = in.readLine();
				traiterMessage(message);
			}
		catch(Exception exc) {
			exc.printStackTrace();
		}
	}

	/**
	 * Au lancement de l'application, on demande au joueur son pseudo afin de personnaliser son espace
	 * @param arg
	 * @throws Exception
	 */
	public static void main(String[] arg) throws Exception {
		int portChifoumi = 4567;
		String nomMachine = "127.0.0.1";

		String pseudo = JOptionPane.showInputDialog
		(null, "Quel est votre pseudo  ?");
		
		
		Socket socket = new Socket(nomMachine, portChifoumi);
		JoueurClient joueur = new JoueurClient (socket);
		joueur.pseudo = pseudo;
		JFrame fenetre = new JFrame();
		fenetre.setPreferredSize(new Dimension(600, 600));
		Color couleurFond = Color.decode("#262262");
		fenetre.setBackground(couleurFond);
		fenetre.setContentPane(new IHMJoueurClient(joueur));
		fenetre.pack();
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetre.setVisible(true);
	}
}
