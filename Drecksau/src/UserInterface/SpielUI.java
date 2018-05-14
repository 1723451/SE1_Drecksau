package UserInterface;

import java.util.Scanner;

import Spiellogik.Spiel;
import Spiellogik.bedienerInterface;

public class SpielUI {
	private Scanner sc = new Scanner(System.in);
	private bedienerInterface spiel;

	public static void main(String[] args) {
		SpielUI ui = new SpielUI();
		ui.neuesSpiel();
		ui.hauptspiel();
		ui.sc.close();
	}

	public void neuesSpiel() {
		System.out
				.println("Willkommen bei 'Drecksau'. Geben sie ein mit wie vielen Spielern sie spielen wollen (3-5).");
		int anzahlSpieler;
		while (true) {
			try {
				anzahlSpieler = Integer.valueOf(sc.nextLine());
				this.spiel = new Spiel(anzahlSpieler);
				break;
			} catch (NumberFormatException e) {
				System.out.println("Fehler bei der Eingabe, geben sie die Anzahl der Spieler erneut ein (3-5).");
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
			}
		}
		System.out.println("Geben sie den Namen des menschlichen Spielers ein.");
		String name = sc.nextLine();
		this.spiel.spielerHinzufuegen(name, false, 0);
		for (int a = 1; a < anzahlSpieler; a++) {
			System.out.println("Geben sie den Schwierigkeitsgrad des Computers " + a + " ein.");
			while (true) {
				try {
					int schwierigkeitsgrad = Integer.valueOf(sc.nextLine());
					this.spiel.spielerHinzufuegen(null, true, schwierigkeitsgrad);
					break;
				} catch (RuntimeException e) {
					System.out.println("Fehler bei der Eingabe, geben sie den Schwierigkeitsgrad des Computers " + a
							+ " erneut ein.");
				}
			}
		}
	}

	private void hauptspiel() {
		for (int a = 0; a < 14; a++) {
			System.out.println("-----------------------------------------------------------------------------");
			System.out.println(this.spiel.naechteRunde());
			if (this.spiel.getAktuelleRunde() != 7 && this.spiel.getAktuelleRunde() != 8) {
				System.out.println(
						spiel.getSpielerAmZug() + " geben sie die Anzahl der Stiche an, die sie machen wollen.");
				while (true) {
					try {
						System.out.println(spiel.sticheAnsagen(Integer.valueOf(sc.nextLine())));
						break;
					} catch (NumberFormatException e) {
						System.out.println("Fehler bei der Eingabe, geben sie die Zahl erneut ein.");
					} catch (RuntimeException e) {
						System.out.println(e.getMessage() + " Geben sie die Zahl erneut ein.");
					}
				}
			}
			for (int c = 0; c < this.spiel.getKartenAnzahlAktuelleRunde(); c++) {
				System.out.println("---------------------------------------------------------\t");
				for (int b = 0; b < this.spiel.getAnzahlSpieler(); b++) {
					if (this.spiel.getSpielerAmZugIstComputer() || a == 0 || a == 13) {
						System.out.println(spiel.getSpielerAmZug() + " hat " + spiel.karteLegen(null) + " gelegt.");
					} else {
						while (true) {
							try {
								System.out.println(
										spiel.getSpielerAmZug() + " geben sie die Karte ein, die sie spielen wollen.");
								System.out.println(spiel.getSpielerAmZug() + " hat " + spiel.karteLegen(sc.nextLine())
										+ " gelegt.");
								break;
							} catch (RuntimeException e) {
								System.out.println(e.getMessage());
							}
						}
					}
				}
				System.out.println("-> " + this.spiel.stichAuswerten() + " hat den Stich gewonnen.");
			}
			System.out.println("---------------------------------------------------------\t");
			System.out.println(this.spiel.punkteAuswerten());
		}
		System.out.println("-----------------------------------------------------------------------------");
		System.out.println(this.spiel.gewinnerAuswerten());
	}
}
