package fr.gsbcr.android;

import java.util.ArrayList;
import java.util.List;

/** Modèle de l'application
 * 
 * @author rafina
 *
 */
public class Modele  {
	private static Collaborateur visiteur ;
	private static CompteRendu cr;
	private static List<CompteRendu> lesCR = null;
	private static List<Medicament> medicaments = null;
	private static String addressAndPort = null;

	/**
	 * @return the addressAndPort
	 */
	public static String getAddressAndPort() {
		return addressAndPort;
	}

	/**
	 * @param addressAndPort the addressAndPort to set
	 */
	public static void setAddressAndPort(String addressAndPort) {
		Modele.addressAndPort = addressAndPort;
	}

	/**
	 * @return the lesCR
	 */
	public static List<CompteRendu> getLesCR() {
		return lesCR;
	}

	/**
	 * @param lesCR the lesCR to set
	 */
	public static void setLesCR(List<CompteRendu> lesCR) {
		Modele.lesCR = lesCR;
	}

	/**
	 * @return the cr
	 */
	public static CompteRendu getCr() {
		return cr;
	}

	/**
	 * @param cr the cr to set
	 */
	public static void setCr(CompteRendu cr) {
		Modele.cr = cr;
	}

	/**
	 * @param visiteur the visiteur to set
	 */
	public static void setVisiteur(Collaborateur visiteur) {
		Modele.visiteur = visiteur;
	}

	/**
	 * @return the visiteur
	 */
	public static Collaborateur getVisiteur() {
		return visiteur;
	}

	/**
	 * @return the medicaments
	 */
	public static List<Medicament> getMedicaments() {
		return medicaments;
	}

	/**
	 * @param medicaments the medicaments to set
	 */
	public static void setMedicaments(List<Medicament> medicaments) {
		Modele.medicaments = medicaments;
	}
	
	static public void clear(){
		Modele.setVisiteur(null);
		Modele.setLesCR(null);
		Modele.setMedicaments(null);
		Modele.setCr(null);
	}
	
}
