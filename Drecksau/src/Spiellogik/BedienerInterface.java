package Spiellogik;

public interface BedienerInterface {
	void spielerHinzufuegen(String name, boolean computerSpieler, int schwierigkeitsgrad);

	String getSpielerAmZug();
	
	int getAnzahlSpieler();

	String naechteRunde();

	boolean getSpielerAmZugIstComputer();

	int getKartenAnzahlAktuelleRunde();

	String punkteAuswerten();

	String stichAuswerten();

	String gewinnerAuswerten();

	String karteLegen(String karte);

	int getAktuelleRunde();

	int computerSticheAnsagenLassen();

	int menschSticheAnsagen(int anzahlStiche);
}