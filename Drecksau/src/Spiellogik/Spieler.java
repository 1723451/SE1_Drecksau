package Spiellogik;

import java.util.ArrayList;

class Spieler {
	private String name;
	private int punkte = 0;
	private ArrayList<Karte> karten;
	private int angesagteStiche;
	private int gemachteStiche = 0;

	public Spieler(String name) {
		this.name = name;
		karten = new ArrayList<>();
	}

	/**
	 * angesagte Stiche festlegen
	 * 
	 * @param angesagteStiche
	 *            Anzahl der angesagten Stiche
	 */
	public void setAngesagteStiche(int angesagteStiche) {
		if (angesagteStiche < 0) {
			throw new RuntimeException("Negative Anzahl an Stichen.");
		}
		this.angesagteStiche = angesagteStiche;
	}

	/**
	 * erhoeht die Anzahl der gemachten Stiche um 1
	 */
	public void stichHinzufuegen() {
		this.gemachteStiche++;
	}

	/**
	 * setzt die gemachten Stiche auf 0 zurueck
	 */
	public void gemachteSticheZuruecksetzen() {
		this.gemachteStiche = 0;
	}

	/**
	 * wertet die Stiche aus und fuegt Punkte hinzu
	 * 
	 * @param runde
	 *            Rundennummer
	 */
	public void sticheAuswerten(int runde) {
		if (runde == 7) {
			punkte += gemachteStiche;
		} else if (runde == 8) {
			punkte -= gemachteStiche;
		} else if (this.angesagteStiche == this.gemachteStiche) {
			punkte += 5 + this.gemachteStiche;
		} else {
			punkte -= Math.abs(this.angesagteStiche - this.gemachteStiche);
		}
	}

	/**
	 * gibt den Spielernamen zurueck
	 * 
	 * @return Spielername
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * gibt den Punktestand zurueck
	 * 
	 * @return Anzahl der Punkte
	 */
	public int getPunkte() {
		return this.punkte;
	}

	/**
	 * legt die Karten fest
	 * 
	 * @param karten
	 *            Karten des Spielers
	 */
	public void setKarten(Karte[] karten) {
		this.karten.clear();
		for (Karte karte : karten) {
			this.karten.add(karte);
		}
	}

	/**
	 * gibt die Karten als Array zurueck
	 * 
	 * @return Spielerkarten
	 */
	public Karte[] getKarten() {
		if (this.karten != null) {
			Karte[] karten = new Karte[this.karten.size()];
			for (int a = 0; a < this.karten.size(); a++) {
				karten[a] = this.karten.get(a);
			}
			return karten;
		}
		throw new RuntimeException("Spieler hat noch keine Karten.");
	}

	/**
	 * legt eine Karte
	 * 
	 * @param karte
	 *            die Karte, die gelegt werden soll
	 * @return gibt die Karte, die gelegt wurde zurueck
	 */
	public Karte karteLegen(String karte) {
		for (int a = 0; a < this.karten.size(); a++) {
			if (this.karten.get(a).getName().equals(karte)) {
				Karte k = this.karten.get(a);
				this.karten.remove(a);
				return k;
			}
		}
		throw new RuntimeException("Karte nicht gefunden.");
	}
}
