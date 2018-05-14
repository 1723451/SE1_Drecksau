package Spiellogik;

import java.util.ArrayList;

public class Spiel implements bedienerInterface {

	private final Karte[] kartenDeck = new Karte[32];
	private final int[] kartenAnzahl = { 1, 2, 3, 4, 5, 6, 6, 6, 6, 5, 4, 3, 2, 1 };
	private Spieler[] spieler;
	private Spieler spielerAmZug;
	private int aktuelleRunde;
	private Spieler erstAnsager;
	private int anzahlAngesagteStiche;
	private ArrayList<Karte> gelegteKarten = new ArrayList<>();

	public Spiel(int anzahlSpieler) {
		if (anzahlSpieler < 3 || anzahlSpieler > 5) {
			throw new RuntimeException("Spieleranzahl: " + anzahlSpieler + " ungueltig.");
		}
		this.spieler = new Spieler[anzahlSpieler];
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

	@Override
	public int getKartenAnzahlAktuelleRunde() {
		return this.kartenAnzahl[this.aktuelleRunde - 1];
	}

	@Override
	public int getAnzahlSpieler() {
		return spieler.length;
	}

	@Override
	public int getAktuelleRunde() {
		return this.aktuelleRunde;
	}

	@Override
	public void spielerHinzufuegen(String name, boolean computerSpieler, int schwierigkeitsgrad) {
		for (int a = 0; a < spieler.length; a++) {
			if (spieler[a] == null) {
				if (computerSpieler) {
					spieler[a] = new ComputerSpieler(schwierigkeitsgrad);
					return;
				}
				spieler[a] = new Spieler(name);
				return;
			}
		}
		throw new RuntimeException("Keine neuen Spieler moeglich.");

	}

	@Override
	public String getSpielerAmZug() {
		if (this.spielerAmZug != null) {
			return this.spielerAmZug.getName();
		}
		return null;
	}

	@Override
	public boolean getSpielerAmZugIstComputer() {
		if (this.spielerAmZug != null && this.spielerAmZug instanceof ComputerSpieler) {
			return true;
		}
		return false;
	}

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

	@Override
	public String naechteRunde() {
		this.aktuelleRunde++;
		if (this.aktuelleRunde == 1) {
			this.spielerAmZug = spieler[(int) (Math.random() * spieler.length)];
			this.erstAnsager = this.spielerAmZug;
		} else {
			this.spielerAmZug = erstAnsager;
			this.erstAnsager = getNaechsterSpielerAmZug();
			this.spielerAmZug = erstAnsager;
		}
		kartenVerteilen();
		String back = "Runde: " + this.aktuelleRunde;
		if (this.aktuelleRunde == 1 || this.aktuelleRunde == 14) {
			back += "\n" + this.spieler[0].getName() + " deine Gegner haben folgende Karten:";
			for (int a = 1; a < this.spieler.length; a++) {
				back += "\n" + spieler[a].getName() + ": " + spieler[a].getKarten()[0].getName();
			}
		} else {
			back += "\n" + spieler[0].getName() + " du hast folgende Karten: ";
			boolean first = true;
			for (Karte karte : spieler[0].getKarten()) {
				if (!first) {
					back += ", ";
				}
				back += karte.getName();
				first = false;
			}
		}
		if (this.aktuelleRunde != 7 && this.aktuelleRunde != 8) {
			back += "\n---------------------------------------------------------\t";
			back += "\nAngesagte Stiche:";
			while (getSpielerAmZugIstComputer()) { // Computer sagen Stiche an, bis Spieler am Zug ist
				ComputerSpieler spieler = (ComputerSpieler) this.spielerAmZug;
				int angesagteStiche = spieler.sticheAnsagen(getGegnerKarten(spieler),
						this.kartenAnzahl[this.aktuelleRunde - 1] - this.anzahlAngesagteStiche, this.aktuelleRunde,
						false);
				this.anzahlAngesagteStiche += angesagteStiche;
				back += "\n" + this.spielerAmZug.getName() + " hat " + angesagteStiche + " Stich(e) angesagt.";
				this.spielerAmZug = getNaechsterSpielerAmZug();
			}
		} else if (this.aktuelleRunde == 7) {
			back += "\nDie naechste Runde ist eine Sonderrunde. Versuchen sie so viele Stiche wie moeglich zu machen.";
		} else {
			back += "\nDie naechste Runde ist eine Sonderrunde. Versuchen sie so wenige Stiche wie moeglich zu machen.";
		}
		return back;
	}

	@Override
	public String sticheAnsagen(int anzahlStiche) {
		if (this.aktuelleRunde == 7 || this.aktuelleRunde == 8) {
			return null; // wenn Sonderrunde
		}
		if (!getSpielerAmZugIstComputer()) {
			if (getNaechsterSpielerAmZug().equals(erstAnsager)) {
				if (this.anzahlAngesagteStiche + anzahlStiche == kartenAnzahl[this.aktuelleRunde - 1]) {
					throw new RuntimeException("Die Anzahl der angesagten Stiche ist nicht moeglich.");
				}
			}
			this.spielerAmZug.setAngesagteStiche(anzahlStiche);
			this.anzahlAngesagteStiche += anzahlStiche;
			String back = this.spielerAmZug.getName() + " hat " + anzahlStiche + " Stich(e) angesagt.";
			this.spielerAmZug = getNaechsterSpielerAmZug();
			while (!this.spielerAmZug.equals(this.erstAnsager)) {
				ComputerSpieler spieler = (ComputerSpieler) this.spielerAmZug;
				int angesagteStiche = spieler.sticheAnsagen(getGegnerKarten(spieler),
						this.kartenAnzahl[this.aktuelleRunde - 1] - this.anzahlAngesagteStiche, this.aktuelleRunde,
						(getNaechsterSpielerAmZug().equals(this.erstAnsager)) ? true : false);
				this.anzahlAngesagteStiche += angesagteStiche;
				back += "\n" + this.spielerAmZug.getName() + " hat " + angesagteStiche + " Stich(e) angesagt.";
				this.spielerAmZug = getNaechsterSpielerAmZug();
			}
			return back;
		}
		throw new RuntimeException("Spieler Am Zug ist ein Computer.");
	}

	private Spieler getNaechsterSpielerAmZug() {
		for (int a = 0; a < this.spieler.length; a++) {
			if (spielerAmZug.equals(spieler[a])) {
				return spieler[(a + 1) % this.spieler.length];
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
			Karte[] karten = new Karte[this.spieler.length - 1];
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
			sp.sticheAuswerten(aktuelleRunde);
			back += sp.getName() + " hat " + sp.getPunkte() + " Punkte.";
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
		if (this.gelegteKarten.size() != this.spieler.length) {
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
		gewinner.add(this.spieler[0]);
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
