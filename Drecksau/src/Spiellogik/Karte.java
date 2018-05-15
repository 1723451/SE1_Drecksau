package Spiellogik;

class Karte {
	private final String name;
	private final int wert;

	public Karte(String name, int wert) {
		this.name = name;
		this.wert = wert;
	}

	/**
	 * gibt den Namen der Karte zurueck
	 * 
	 * @return Namen der Karte
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * gibt den Wert der Karte zurueck
	 * 
	 * @return Wert der Karte
	 */
	public int getWert() {
		return this.wert;
	}
}
