package Spiellogik;

import java.util.ArrayList;

public class Spiel implements BedienerInterface {

	private final Karte[] kartenDeck = new Karte[32];
	private final int[] kartenAnzahl = { 1, 2, 3, 4, 5, 6, 6, 6, 6, 5, 4, 3, 2, 1 };
	private ArrayList<Spieler> spieler = new ArrayList<>();
	private int spielerAnzahl;
	private Spieler spielerAmZug;
	private int aktuelleRunde;
	private Spieler erstAnsager;
	private int anzahlAngesagteStiche;
	private ArrayList<Karte> gelegteKarten = new ArrayList<>();

	/**
	 * Konstruktor, legt die Spielkarten an
	 * 
	 * @param anzahlSpieler
	 *            Anzahl der Spieler
	 */
	public Spiel(int anzahlSpieler) {
		if (anzahlSpieler < 3 || anzahlSpieler > 5) {
			throw new RuntimeException("Spieleranzahl: " + anzahlSpieler + " ungueltig.");
		}
		this.spielerAnzahl = anzahlSpieler;
		String[] symbole = { "Karo", "Herz", "Pik", "Kreuz" };
		String[] ziffern = { "7", "8", "9", "Bube", "Dame", "Koenig", "10", "Ass" };
		int counter = 0;
		for (String symbol : symbole) {
			for (String ziffer : ziffern) {
				this.kartenDeck[counter] = new Karte(symbol + " " + ziffer, counter);
				counter++;
			}
		}
	}

	/**
	 * gibt die Anzahl der Kanten fuer die aktuelle Runde
	 * 
	 * @return Anzahl der Kanten
	 */
	@Override
	public int getKartenAnzahlAktuelleRunde() {
		return this.kartenAnzahl[this.aktuelleRunde - 1];
	}

	/**
	 * gibt die Spieleranzahl zurueck
	 * 
	 * @return Anzahl der Spieler
	 */
	@Override
	public int getAnzahlSpieler() {
		return spieler.size();
	}

	/**
	 * gibt die Nummer der aktuellen Runde zurueck
	 * 
	 * @return Nummer der aktuellen Runde
	 */
	@Override
	public int getAktuelleRunde() {
		return this.aktuelleRunde;
	}

	/**
	 * fuegt einen Spieler hinzu
	 * 
	 * @param name
	 *            Name des Spielers
	 * @param computerSpieler
	 *            ist der Spieler ein Computer
	 * @param schwierigkeitsgrad
	 *            Schwierigkeitsgrad des Computers
	 */
	@Override
	public void spielerHinzufuegen(String name, boolean computerSpieler, int schwierigkeitsgrad) {
		if (spieler.size() < this.spielerAnzahl) {
			if (computerSpieler) {
				spieler.add(new ComputerSpieler(schwierigkeitsgrad));
			} else {
				spieler.add(new MenschlicherSpieler(name));
			}
			return;
		}
		throw new RuntimeException("Keine neuen Spieler moeglich.");
	}

	/**
	 * gibt den Namen des Spielers am Zug zurueck
	 * 
	 * @return Name des Spielers
	 */
	@Override
	public String getSpielerAmZug() {
		if (this.spielerAmZug != null) {
			return this.spielerAmZug.getName();
		}
		return null;
	}

	/**
	 * gibt zurueck ob der Spieler am Zug ein Computer ist
	 * 
	 * @return ob der Spieler ein Computer ist
	 */
	@Override
	public boolean getSpielerAmZugIstComputer() {
		if (this.spielerAmZug != null && this.spielerAmZug instanceof ComputerSpieler) {
			return true;
		}
		return false;
	}

	/**
	 * verteilt die Karten fuer alle Spieler
	 */
	private void kartenVerteilen() {
		ArrayList<Karte> restlicheKarten = new ArrayList<>();
		for (Karte karte : this.kartenDeck) {
			restlicheKarten.add(karte);
		}
		for (Spieler spieler : this.spieler) {
			Karte[] karten = new Karte[this.kartenAnzahl[this.aktuelleRunde - 1]];
			for (int a = 0; a < karten.length; a++) {
				int kartenIndex = (int) (Math.random() * restlicheKarten.size());
				karten[a] = restlicheKarten.get(kartenIndex);
				restlicheKarten.remove(kartenIndex);
			}
			spieler.setKarten(karten);
		}
	}

	/**
	 * laesst den Spieler am Zug Stiche ansagen, wenn dieser ein Computer ist und
	 * erhöht die Anzahl der angesagten Stiche
	 * 
	 * @return Anzahl der angesagten Stiche
	 * @throws RuntimeException
	 *             wenn kein Computerspieler am Zug ist
	 */
	@Override
	public int computerSticheAnsagenLassen() {
		if (this.spielerAmZug != null && this.spielerAmZug instanceof ComputerSpieler) {
			ComputerSpieler spieler = (ComputerSpieler) this.spielerAmZug;
			int angesagteStiche = spieler.sticheAnsagen(getGegnerKarten(spieler),
					this.kartenAnzahl[this.aktuelleRunde - 1] - this.anzahlAngesagteStiche, this.aktuelleRunde, false);
			this.anzahlAngesagteStiche += angesagteStiche;
			this.spielerAmZug = getNaechsterSpielerAmZug();
			return angesagteStiche;
		}
		throw new RuntimeException("Kein Computer Spieler am Zug.");
	}

	/**
	 * laesst den Spieler am Zug Stichen ansagen, wenn dieser ein menchlischer
	 * Spieler ist und erhöht automatisch die Anzahl der angesagten Stiche
	 * 
	 * @param anzahlStiche
	 *            Anzahl der angesagten Stiche
	 * @return die Anzahl der angesaten Stiche
	 * @throws RuntimeException
	 *             wenn kein menschlicher Spieler am Zug ist
	 */
	@Override
	public int menschSticheAnsagen(int anzahlStiche) {
		if (this.spielerAmZug != null && this.spielerAmZug instanceof MenschlicherSpieler) {
			if (getNaechsterSpielerAmZug().equals(erstAnsager)) {
				if (this.anzahlAngesagteStiche + anzahlStiche == kartenAnzahl[this.aktuelleRunde - 1]) {
					throw new RuntimeException("Die Anzahl der angesagten Stiche ist nicht moeglich.");
				}
			}
			this.spielerAmZug.setAngesagteStiche(anzahlStiche);
			this.anzahlAngesagteStiche += anzahlStiche;
			this.spielerAmZug = getNaechsterSpielerAmZug();
			return anzahlStiche;
		}
		throw new RuntimeException("Kein menschlicher Spieler am Zug.");
	}

	/**
	 * startet die naechste Runde und gibt entweder die Gegnerkarten (Runde 1&14)
	 * oder die eigenen Karten zurueck
	 * @return String mit den Karten
	 */
	@Override
	public String naechteRunde() {
		this.aktuelleRunde++;
		if (this.aktuelleRunde == 1) {
			this.spielerAmZug = spieler.get((int) (Math.random() * spieler.size()));
			this.erstAnsager = this.spielerAmZug;
		} else {
			this.spielerAmZug = erstAnsager;
			this.erstAnsager = getNaechsterSpielerAmZug();
			this.spielerAmZug = erstAnsager;
		}
		kartenVerteilen();
		String back = "Runde: " + this.aktuelleRunde;
		if (this.aktuelleRunde == 1 || this.aktuelleRunde == 14) {
			back += "\n" + this.spieler.get(0).getName() + " deine Gegner haben folgende Karten:";
			for (int a = 1; a < this.spieler.size(); a++) {
				back += "\n" + spieler.get(a).getName() + ": " + spieler.get(a).getKarten()[0].getName();
			}
		} else {
			back += "\n" + spieler.get(0).getName() + " du hast folgende Karten: ";
			boolean first = true;
			for (Karte karte : spieler.get(0).getKarten()) {
				if (!first) {
					back += ", ";
				}
				back += karte.getName();
				first = false;
			}
		}
		return back;
	}

	/**
	 * gibt den naechsten Spieler am Zug zurueck
	 * 
	 * @return nachster Spieler am Zug
	 */
	private Spieler getNaechsterSpielerAmZug() {
		for (int a = 0; a < this.spieler.size(); a++) {
			if (spielerAmZug.equals(spieler.get(a))) {
				return spieler.get((a + 1) % this.spieler.size());
			}
		}
		throw new RuntimeException("Naechsten Spieler nicht gefunden.");
	}

	/**
	 * gibt die Karten der Gegner zurueck, nur fuer erste und letzte Runde, sonst
	 * null
	 * 
	 * @param spieler
	 *            der Spieler, von dessen Gegnern man die Karten moechte
	 * @return die Karten der Gegner oder null
	 */
	private Karte[] getGegnerKarten(Spieler spieler) {
		if (this.aktuelleRunde == 1 || this.aktuelleRunde == 14) {
			Karte[] karten = new Karte[this.spieler.size() - 1];
			int index = 0;
			for (Spieler sp : this.spieler) {
				if (!sp.equals(spieler)) {
					karten[index] = sp.getKarten()[0];
					index++;
				}
			}
			return karten;
		}
		return null;
	}

	/**
	 * legt eine Karte fuer den Spieler am Zug
	 * 
	 * @param karte
	 *            die Karte, die gelegt werden soll
	 * @return die Karte, die gelegt wurde
	 */
	@Override
	public String karteLegen(String karte) {
		Karte gelegteKarte;
		if (getSpielerAmZugIstComputer()) {
			ComputerSpieler spieler = (ComputerSpieler) this.spielerAmZug;
			Karte[] gelegte = new Karte[this.gelegteKarten.size()];
			for (int a = 0; a < this.gelegteKarten.size(); a++) {
				gelegte[a] = this.gelegteKarten.get(a);
			}
			gelegteKarte = this.spielerAmZug.karteLegen(spieler.karteAuswaehlen(gelegte).getName());
			this.gelegteKarten.add(gelegteKarte);
			this.spielerAmZug = getNaechsterSpielerAmZug();
			return gelegteKarte.getName();
		}
		if (this.aktuelleRunde == 1 || this.aktuelleRunde == 14) {
			karte = this.spielerAmZug.getKarten()[0].getName();
		}
		gelegteKarte = this.spielerAmZug.karteLegen(karte);
		this.gelegteKarten.add(gelegteKarte);
		this.spielerAmZug = getNaechsterSpielerAmZug();
		return gelegteKarte.getName();
	}

	/**
	 * Wertet die Punkte aus, wenn die Runde beendet ist
	 * 
	 * @return gibt die Punkte der Spieler zurueck
	 */
	@Override
	public String punkteAuswerten() {
		String back = "";
		boolean first = true;
		for (Spieler sp : this.spieler) {
			if (!first) {
				back += "\n";
			}
			int diffPunkte = sp.sticheAuswerten(aktuelleRunde);
			back += sp.getName() + " hat " + sp.getPunkte() + " Punkte. (" + diffPunkte + ")";
			first = false;
		}
		for (Spieler sp : this.spieler) {
			sp.setAngesagteStiche(0);
			sp.gemachteSticheZuruecksetzen();
		}
		return back;
	}

	/**
	 * wertet einen Stich aus
	 * 
	 * @return gibt den Namen des Spielers zurueck, der den Stich gemacht hat
	 */
	@Override
	public String stichAuswerten() {
		if (this.gelegteKarten.size() != this.spieler.size()) {
			throw new RuntimeException("Es haben noch nicht alle Spieler gelegt.");
		}
		int index = 0;
		for (int a = 0; a < this.gelegteKarten.size(); a++) {
			if (this.gelegteKarten.get(a).getWert() > this.gelegteKarten.get(index).getWert()) {
				index = a;
			}
		}
		for (int a = 0; a < index; a++) {
			this.spielerAmZug = getNaechsterSpielerAmZug();
		}
		this.spielerAmZug.stichHinzufuegen();
		this.gelegteKarten.clear();
		return this.spielerAmZug.getName();
	}

	/**
	 * wertet die Gewinner des Spiels aus
	 * 
	 * @return die Gewinner
	 */
	@Override
	public String gewinnerAuswerten() {
		ArrayList<Spieler> gewinner = new ArrayList<>();
		gewinner.add(this.spieler.get(0));
		for (Spieler sp : this.spieler) {
			if (sp.getPunkte() > gewinner.get(0).getPunkte()) {
				gewinner.clear();
				gewinner.add(sp);
			}
			if (sp.getPunkte() == gewinner.get(0).getPunkte() && !sp.equals(gewinner.get(0))) {
				gewinner.add(sp);
			}
		}
		String back = "";
		for (int a = 0; a < gewinner.size(); a++) {
			back += gewinner.get(a).getName() + " hat mit " + gewinner.get(a).getPunkte() + " Punkten gewonnen.";
		}
		return back;
	}
}
