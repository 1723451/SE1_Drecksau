package Spiellogik;

class ComputerSpieler extends Spieler {
	private static int computerZaehler = 0;
	private int schwierigkeitsgrad;

	public ComputerSpieler(int schwierigkeitsgrad) {
		super("Computer " + (++computerZaehler));
		if (schwierigkeitsgrad < 1 || schwierigkeitsgrad > 3) {
			throw new RuntimeException("Schwierigkeitsgrad: " + schwierigkeitsgrad + " ungueltig.");
		}
		this.schwierigkeitsgrad = schwierigkeitsgrad;
	}

	/**
	 * legt die angesagten Stiche abhängig vom Schwierigkeistgrad fest
	 * 
	 * @param gegnerKarten
	 *            Karten der Gegner
	 * @param diffMoeglicheSticheGemachteStiche
	 *            Differenz zischen moeglichen Stichen und gemachten Stichen
	 * @param runde
	 *            aktuelle Runde
	 * @param letzterSpieler
	 *            gibt an, ob der letzte Spieler am Zug ist
	 * @return angesagte Zuege
	 */
	public int sticheAnsagen(Karte[] gegnerKarten, int diffMoeglicheSticheGemachteStiche, int runde,
			boolean letzterSpieler) {
		if (runde == 7 || runde == 8) {
			throw new RuntimeException("In der 7. und 8. Runde wird nicht angesagt.");
		}
		if (gegnerKarten != null && letzterSpieler && diffMoeglicheSticheGemachteStiche == 1) {
			setAngesagteStiche(0);
			return 0;
		}
		int ansage = 0;
		switch (schwierigkeitsgrad) {
		case 1: {
			ansage = sticheAnsagenSchwierigkeitsgrad1(gegnerKarten, diffMoeglicheSticheGemachteStiche, runde,
					letzterSpieler);
			break;
		}
		case 2: {
			ansage = sticheAnsagenSchwierigkeitsgrad2(gegnerKarten, diffMoeglicheSticheGemachteStiche, runde,
					letzterSpieler);
			break;
		}
		case 3: {
			ansage = sticheAnsagenSchwierigkeitsgrad3(gegnerKarten, diffMoeglicheSticheGemachteStiche, runde,
					letzterSpieler);
			break;
		}
		}
		setAngesagteStiche(ansage);
		return ansage;
	}

	/**
	 * legt die angesagten Stiche fuer den Schwierigkeitsgrad 1 fest (Haelfte der
	 * Moeglichen)
	 * 
	 * @param gegnerKarten
	 *            Karten der Gegner
	 * @param diffMoeglicheSticheGemachteStiche
	 *            Differenz zischen moeglichen Stichen und gemachten Stichen
	 * @param runde
	 *            aktuelle Runde
	 * @param letzterSpieler
	 *            gibt an, ob der letzte Spieler am Zug ist
	 * @return angesagte Zuege
	 */
	private int sticheAnsagenSchwierigkeitsgrad1(Karte[] gegnerKarten, int diffMoeglicheSticheGemachteStiche, int runde,
			boolean letzterSpieler) {
		if (gegnerKarten != null) {
			return 1;
		}
		int anzahl = getKarten().length / 2;
		if (runde != 7 && runde != 8 && letzterSpieler && diffMoeglicheSticheGemachteStiche == anzahl) {
			anzahl++;
		}
		return anzahl;
	}

	/**
	 * legt die angesagten Stiche fuer den Schwierigkeitsgrad 2 fest (Anzahl Karten
	 * groesser 16)
	 * 
	 * @param gegnerKarten
	 *            Karten der Gegner
	 * @param diffMoeglicheSticheGemachteStiche
	 *            Differenz zischen moeglichen Stichen und gemachten Stichen
	 * @param runde
	 *            aktuelle Runde
	 * @param letzterSpieler
	 *            gibt an, ob der letzte Spieler am Zug ist
	 * @return angesagte Zuege
	 */
	private int sticheAnsagenSchwierigkeitsgrad2(Karte[] gegnerKarten, int diffMoeglicheSticheGemachteStiche, int runde,
			boolean letzterSpieler) {
		if (gegnerKarten != null) {
			int summeGegnerWerte = 0;
			for (Karte karte : gegnerKarten) {
				summeGegnerWerte += karte.getWert();
			}
			if (summeGegnerWerte / gegnerKarten.length <= 16) {
				return 1;
			}
			return 0;
		}
		int anzahlEigeneKartenGroesserAls16 = 0;
		for (Karte karte : getKarten()) {
			if (karte.getWert() > 16) {
				anzahlEigeneKartenGroesserAls16++;
			}
		}
		if (runde != 7 && runde != 8 && letzterSpieler
				&& diffMoeglicheSticheGemachteStiche == anzahlEigeneKartenGroesserAls16) {
			anzahlEigeneKartenGroesserAls16++;
		}
		return anzahlEigeneKartenGroesserAls16;
	}

	/**
	 * legt die angesagten Stiche fuer den Schwierigkeitsgrad 1 fest (Anzahl Karten
	 * groesser 20)
	 * 
	 * @param gegnerKarten
	 *            Karten der Gegner
	 * @param diffMoeglicheSticheGemachteStiche
	 *            Differenz zischen moeglichen Stichen und gemachten Stichen
	 * @param runde
	 *            aktuelle Runde
	 * @param letzterSpieler
	 *            gibt an, ob der letzte Spieler am Zug ist
	 * @return angesagte Zuege
	 */
	private int sticheAnsagenSchwierigkeitsgrad3(Karte[] gegnerKarten, int diffMoeglicheSticheGemachteStiche, int runde,
			boolean letzterSpieler) {
		if (gegnerKarten != null) {
			int anzahlKartenGroeßerAls20 = 0;
			for (Karte karte : gegnerKarten) {
				if (karte.getWert() > 20) {
					anzahlKartenGroeßerAls20++;
				}
			}
			if ((double) (anzahlKartenGroeßerAls20) / gegnerKarten.length <= 0.5) {
				return 1;
			} else {
				return 0;
			}
		}
		int anzahlEigeneKartenGroesserAls20 = 0;
		for (Karte karte : getKarten()) {
			if (karte.getWert() > 20) {
				anzahlEigeneKartenGroesserAls20++;
			}
		}
		if (runde != 7 && runde != 8 && letzterSpieler
				&& diffMoeglicheSticheGemachteStiche == anzahlEigeneKartenGroesserAls20) {
			anzahlEigeneKartenGroesserAls20--;
		}
		return anzahlEigeneKartenGroesserAls20;
	}

	/**
	 * bestimmt die Karte je nach Schwierigkeitsgrad
	 * 
	 * @param gelegteKarten
	 *            Karten der Gegner
	 * @return gewaehlte Karte
	 */
	public Karte karteAuswaehlen(Karte[] gelegteKarten) {
		switch (this.schwierigkeitsgrad) {
		case 1:
			return karteAuswaehlenSchwierigkeitsgrad1();
		case 2:
			return karteAuswaehlenSchwierigkeitsgrad2();
		case 3:
			return karteAuswaehlenSchwierigkeitsgrad3(gelegteKarten);
		}
		throw new RuntimeException("Fehler beim Schwierigkeitsgrad");
	}

	/**
	 * legt zufaellig Karten
	 * 
	 * @return eine zufaellige Karte
	 */
	private Karte karteAuswaehlenSchwierigkeitsgrad1() {
		return this.getKarten()[(int) (Math.random() * this.getKarten().length)];
	}

	/**
	 * legt immer die groeßte Karte
	 * 
	 * @return die groesste Karte
	 */
	private Karte karteAuswaehlenSchwierigkeitsgrad2() {
		Karte groesste = getKarten()[0];
		for (Karte karte : getKarten()) {
			if (karte.getWert() > groesste.getWert()) {
				groesste = karte;
			}
		}
		return groesste;
	}

	/**
	 * legt immer die kleinst moegliche Karte, die groesser als die gelegten ist
	 * 
	 * @param gelegteKarten
	 *            Karten der Gegner
	 * @return
	 */
	private Karte karteAuswaehlenSchwierigkeitsgrad3(Karte[] gelegteKarten) {
		Karte groessteGelegte = null;
		if (gelegteKarten != null && gelegteKarten.length > 0) {
			groessteGelegte = gelegteKarten[0];
			for (Karte karte : gelegteKarten) {
				if (karte.getWert() > groessteGelegte.getWert()) {
					groessteGelegte = karte;
				}
			}
		}
		Karte kleinste = getKarten()[0];
		for (Karte karte : getKarten()) {
			if (groessteGelegte != null) {
				if (karte.getWert() > groessteGelegte.getWert() && karte.getWert() < kleinste.getWert()) {
					kleinste = karte;
				}
			} else {
				if (karte.getWert() < kleinste.getWert()) {
					kleinste = karte;
				}
			}
		}
		return kleinste;
	}
}
