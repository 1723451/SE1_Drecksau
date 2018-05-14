package Spiellogik;

class Karte {
	private final String name;
	private final int wert;

	public Karte(String name, int wert) {
		this.name = name;
		this.wert = wert;
	}

	public String getName() {
		return this.name;
	}

	public int getWert() {
		return this.wert;
	}
}
