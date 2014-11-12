package cs;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


@SuppressWarnings("serial")
public class IHMJoueurClient  extends Box implements ActionListener, Observer {
	private JButton feuilleM = new JButton(new ImageIcon("feuille.png"));
	private JButton ciseauxM = new JButton(new ImageIcon("ciseaux.png"));
	private JButton caillouM= new JButton(new ImageIcon("caillou.png"));
	private JButton spockM = new JButton(new ImageIcon("spock.png"));
	private JButton lezardM= new JButton(new ImageIcon("lezard.png"));
	private JButton feuilleA = new JButton(new ImageIcon("feuille_adv.png"));
	private JButton ciseauxA = new JButton(new ImageIcon("ciseaux_adv.png"));
	private JButton caillouA= new JButton(new ImageIcon("caillou_adv.png"));
	private JButton spockA = new JButton(new ImageIcon("spock_adv.png"));
	private JButton lezardA= new JButton(new ImageIcon("lezard_adv.png"));
	private JLabel nombrePoints = new JLabel("0");
	private JLabel action = new JLabel("Attends s'il te plaît");
	private JoueurClient joueur;
	private JLabel nombrePointsAdversaire = new JLabel("0");
	private Color couleurBouton;
	private JButton boutonChoixAdversaire;
	private JButton boutonChoisi;

	public IHMJoueurClient(JoueurClient joueur) throws FontFormatException, IOException {
		super(BoxLayout.Y_AXIS);
		this.joueur = joueur;
		joueur.addObserver(this);
		
		//Couleurs
		Color violetClair = Color.decode("#575D94");
		Color yellowClair = Color.decode("#f05a28");
		Color back = Color.decode("#bababa");
		
		//Font
		Font font = new Font("Verdana", 20, 20);
		Font fontTitre = new Font("Impact", 24, 24);

		JPanel panneau = new JPanel();
		panneau.setBackground(back);
		JLabel labelTitre = new JLabel("Pierre - Feuille - Cisceaux - Lézard - Spock");
		labelTitre.setFont(fontTitre);
		labelTitre.setForeground(yellowClair);
		labelTitre.setBackground(violetClair);
		panneau.add(labelTitre);
		panneau.add(action);
		add(panneau);
		
		
		panneau = new JPanel();
		panneau.setBackground(back);
		panneau.add(new JLabel(joueur.pseudo + "  : "));
		panneau.add(action);
		add(panneau);

		
		feuilleM.setBorder(null);
		ciseauxM.setBorder(null);
		caillouM.setBorder(null);
		spockM.setBorder(null);
		lezardM.setBorder(null);
		
		panneau = new JPanel();
		panneau.setBackground(back);
		JLabel label = new JLabel("Nombre de duel(s) remporté(s) : ");
		label.setForeground(yellowClair);
		labelTitre.setFont(font);
		panneau.add(label);
		nombrePoints.setForeground(yellowClair);
		panneau.add(nombrePoints);
		
		add(panneau);
		
		panneau = new JPanel();
		panneau.setBackground(back);
		label = new JLabel("Choisis ton arme");
		label.setForeground(yellowClair);
		labelTitre.setFont(font);
		panneau.add(label);
		add(panneau);
		
		
		panneau = new JPanel();
		panneau.setBackground(back);
		panneau.add(feuilleM);
		panneau.add(ciseauxM);
		panneau.add(caillouM);
		panneau.add(spockM);
		panneau.add(lezardM);
		add(panneau);
		if (!joueur.jouer) {
			feuilleM.setEnabled(false);
			ciseauxM.setEnabled(false);
			caillouM.setEnabled(false);
			spockM.setEnabled(false);
			lezardM.setEnabled(false);
		}
		else action.setText("Tu peux jouer !");
		label.setForeground(yellowClair);
		labelTitre.setFont(font);
		feuilleM.setActionCommand(Choix.FEUILLE.toString());
		ciseauxM.setActionCommand(Choix.CISEAUX.toString());
		caillouM.setActionCommand(Choix.CAILLOU.toString());
		spockM.setActionCommand(Choix.SPOCK.toString());
		lezardM.setActionCommand(Choix.LEZARD.toString());
		feuilleM.addActionListener(this);
		ciseauxM.addActionListener(this);
		caillouM.addActionListener(this);
		spockM.addActionListener(this);
		lezardM.addActionListener(this);
		
		
		feuilleA.setBorder(null);
		ciseauxA.setBorder(null);
		caillouA.setBorder(null);
		spockA.setBorder(null);
		lezardA.setBorder(null);
		
		
		panneau = new JPanel();
		panneau.setBackground(back);
		label = new JLabel("Voici l'arme choisie par ton adversaire");
		label.setForeground(yellowClair);
		labelTitre.setFont(font);
		panneau.add(label);
		add(panneau);
		
		panneau = new JPanel();
		panneau.setBackground(back);
		panneau.add(feuilleA);
		panneau.add(ciseauxA);
		panneau.add(caillouA);
		panneau.add(spockA);
		panneau.add(lezardA);
		add(panneau);

		panneau = new JPanel();
		panneau.setBackground(back);
		JLabel lb = new JLabel("Nombre de victoire(s) de ton adversaire : ");
		lb.setForeground(yellowClair);
		panneau.add(lb);
		nombrePointsAdversaire.setForeground(yellowClair);
		labelTitre.setFont(font);
		panneau.add(nombrePointsAdversaire);
		add(panneau);

		couleurBouton = feuilleM.getBackground();
	}

	public void actionPerformed(ActionEvent evt) {
		Font font = new Font("Verdana", 20, 20);
		Color yellowClair = Color.decode("#f05a28");
		
		boutonChoisi = (JButton) evt.getSource();
		Border bordure = new LineBorder(Color.RED, 6);
		boutonChoisi.setBorder(bordure);
		joueur.choix = Choix.valueOf(boutonChoisi.getActionCommand());
		joueur.out.println(Constantes.CHOIX + " " + boutonChoisi.getActionCommand());
		action.setText("Attends s'il te plaît");
		action.setForeground(yellowClair);
		action.setFont(font);
		feuilleM.setEnabled(false);
		ciseauxM.setEnabled(false);
		caillouM.setEnabled(false);
		spockM.setEnabled(false);
		lezardM.setEnabled(false);
		joueur.jouer = false;
		indiquerChoixAdversaire();
	}

	public void indiquerChoixAdversaire() {
		Choix choixAdversaire = joueur.adversaire.choix;

		if ((joueur.choix != null) &&( choixAdversaire != null)) {
			if (choixAdversaire == Choix.FEUILLE) boutonChoixAdversaire = feuilleA;
			else if (choixAdversaire == Choix.CISEAUX) boutonChoixAdversaire = ciseauxA;
			else if (choixAdversaire == Choix.CAILLOU) boutonChoixAdversaire = caillouA;
			else if (choixAdversaire == Choix.SPOCK) boutonChoixAdversaire = spockA;
			else if (choixAdversaire == Choix.LEZARD) boutonChoixAdversaire = lezardA;
			Border bordure = new LineBorder(Color.RED, 6);
			boutonChoixAdversaire.setBorder(bordure);
		}
	}
	
	public void update(Observable o, Object obj) {
		Font font = new Font("Verdana", 20, 20);
		Color yellowClair = Color.decode("#f05a28");
		
		nombrePoints.setText(Integer.toString(joueur.nbPoints));
		nombrePointsAdversaire.setText(Integer.toString(joueur.adversaire.nbPoints));
		indiquerChoixAdversaire();		
		if (joueur.partieGagnee) {
			action.setText("BRAVO ! Tu as remarquablement gagn√©");
			action.setForeground(yellowClair);
			action.setFont(font);
			feuilleM.setEnabled(false);
			ciseauxM.setEnabled(false);
			caillouM.setEnabled(false);
			spockM.setEnabled(false);
			lezardM.setEnabled(false);
		}
		else if (joueur.partiePerdue) {
			action.setText("DOMMAGE ! Tu as lamentablement perdu");
			action.setForeground(yellowClair);
			action.setFont(font);
			feuilleM.setEnabled(false);
			ciseauxM.setEnabled(false);
			caillouM.setEnabled(false);
			spockM.setEnabled(false);
			lezardM.setEnabled(false);
		}  
		else if (joueur.jouer) {
			if (boutonChoisi != null) boutonChoisi.setBorder(null);
	
			feuilleM.setEnabled(true);
			ciseauxM.setEnabled(true);
			caillouM.setEnabled(true);
			spockM.setEnabled(true);
			lezardM.setEnabled(true);
			action.setText("Tu peux jouer !");
			action.setForeground(yellowClair);
			action.setFont(font);
			if (boutonChoixAdversaire != null) boutonChoixAdversaire.setBorder(null); 
			
		}
	}
}
