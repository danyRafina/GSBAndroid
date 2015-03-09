package fr.gsbcr.android;

/** Classe Collaborateur de l'application 
 * @author rafina
 */

public class Collaborateur {
	
	private String sColMatricule ;
	private String sColNom;
	private String sColPrenom;
	private String sColMdp;
	
	
	/**
	 * @return the sColMdp
	 */
	public String getsColMdp() {
		return sColMdp;
	}

	/**
	 * @param sColMdp the sColMdp to set
	 */
	public void setsColMdp(String sColMdp) {
		this.sColMdp = sColMdp;
	}

	/** Création d'un collaborateur
	 * @param sColMatricule Matricule du collaborateur
	 * @param sColNom Nom du collaborateur
	 * @param sColPrenom Prénom du collaborateur
	 * @param sColMdp Mot de passe du collaborateur
	 */
	public Collaborateur(String sColMatricule, String sColNom,
			String sColPrenom,String sColMdp) {
		super();
		this.sColMatricule = sColMatricule;
		this.sColNom = sColNom;
		this.sColPrenom = sColPrenom;
		this.sColMdp = sColMdp;
	}
	
	/** Obtenir le matricule du collaborateur
	 * @return sColMatricule Matricule du collaborateur
	 */
	public String getsColMatricule() {
		return sColMatricule;
	}
	
	/** Modifier le matricule du collaborateur
	 * @param sColMatricule Matricule du collaborateur
	 */
	public void setsColMatricule(String sColMatricule) {
		this.sColMatricule = sColMatricule;
	}
	/** Obtenir le nom du collaborateur
	 * 
	 * @return sColNom Nom du collaborateur
	 */
	public String getsColNom() {
		return sColNom;
	}
	/** Modifier le nom du collaborateur
	 * 
	 * @param sColNom Nom du collaborateur
	 */
	public void setsColNom(String sColNom) {
		this.sColNom = sColNom;
	}
	/** Obtenir le prénom du collaborateur
	 * 
	 * @return sColPrenom du collaborateur
	 */
	public String getsColPrenom() {
		return sColPrenom;
	}
	
	/** Modifier le prénom du collaborateur
	 * 
	 * @param sColPrenom Prénom du collaborateur
	 */
	public void setsColPrenom(String sColPrenom) {
		this.sColPrenom = sColPrenom;
	}
	
	
	
	@Override
	/** Réprésentation d'un collaborateur sous forme textuelle
	 * @retun forme textuelle d'un collaborateur
	 */
	public String toString() {
		return "Collaborateur [sColMatricule=" + sColMatricule + ", sColNom="
				+ sColNom + ", sColPrenom=" + sColPrenom +  "]";
	}

	
}