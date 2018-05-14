package Spiellogik;

public interface bedienerInterface {
	void spielerHinzufuegen(String name, boolean computerSpieler, int schwierigkeitsgrad);

	String getSpielerAmZug();
	
	int getAnzahlSpieler();

	String naechteRunde();

	String sticheAnsagen(int anzahlStiche);

	boolean getSpielerAmZugIstComputer();

	int getKartenAnzahlAktuelleRunde();

	String punkteAuswerten();

	String stichAuswerten();

	String gewinnerAuswerten();

	String karteLegen(String karte);

	int getAktuelleRunde();
}
