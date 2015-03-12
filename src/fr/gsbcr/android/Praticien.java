package fr.gsbcr.android;

/** Classe Praticien de l'application
 * @author rafina
 *
 */
public class Praticien  {
	private int praNum;
	private String praNom;
	private String praPrenom;
	private String praAdresse;
	private int praCP;
	private String praVille;
	private float praCoefN;
	private String praProfession;
	private String praLieuTravail;


	

	/**
	 * @param praNum
	 * @param praNom
	 * @param praPrenom
	 * @param praAdresse
	 * @param praCP
	 * @param praVille
	 * @param praCoefN
	 * @param praProfession
	 * @param praLieuTravail
	 */
	public Praticien(int praNum, String praNom, String praPrenom,
			String praAdresse, int praCP, String praVille, float praCoefN,
			String praProfession, String praLieuTravail) {
		this.praNum = praNum;
		this.praNom = praNom;
		this.praPrenom = praPrenom;
		this.praAdresse = praAdresse;
		this.praCP = praCP;
		this.praVille = praVille;
		this.praCoefN = praCoefN;
		this.praProfession = praProfession;
		this.praLieuTravail = praLieuTravail;
	}


	/** Obtenir le numéro du praticien
	 * @return le numéro du praticien
	 */
	public int getPraNum() {
		return praNum;
	}


	/** Modifier le numéro du praticien 
	 * @param praNum Le nouveau numéro 
	 */
	public void setPraNum(int praNum) {
		this.praNum = praNum;
	}


	/** Obtenir le nom du praticien
	 * @return le nom du praticien
	 */
	public String getPraNom() {
		return praNom;
	}


	/** Modifier le nom du praticien
	 * @param praNom Le nouveau nom 
	 */
	public void setPraNom(String praNom) {
		this.praNom = praNom;
	}


	/** Obtenir le prénom du praticien
	 * @return Le prénom du praticien
	 */
	public String getPraPrenom() {
		return praPrenom;
	}


	/** Modifier le prénom du praticien
	 * @param praPrenom Le nouveau prénom
	 */
	public void setPraPrenom(String praPrenom) {
		this.praPrenom = praPrenom;
	}
	

	

	/**
	 * @return the praAdresse
	 */
	public String getPraAdresse() {
		return praAdresse;
	}


	/**
	 * @param praAdresse the praAdresse to set
	 */
	public void setPraAdresse(String praAdresse) {
		this.praAdresse = praAdresse;
	}


	/**
	 * @return the praCP
	 */
	public int getPraCP() {
		return praCP;
	}


	/**
	 * @param praCP the praCP to set
	 */
	public void setPraCP(int praCP) {
		this.praCP = praCP;
	}


	/**
	 * @return the praVille
	 */
	public String getPraVille() {
		return praVille;
	}


	/**
	 * @param praVille the praVille to set
	 */
	public void setPraVille(String praVille) {
		this.praVille = praVille;
	}


	/**
	 * @return the praCoefN
	 */
	public float getPraCoefN() {
		return praCoefN;
	}


	/**
	 * @param praCoefN the praCoefN to set
	 */
	public void setPraCoefN(float praCoefN) {
		this.praCoefN = praCoefN;
	}


	/**
	 * @return the praProfession
	 */
	public String getPraProfession() {
		return praProfession;
	}


	/**
	 * @param praProfession the praProfession to set
	 */
	public void setPraProfession(String praProfession) {
		this.praProfession = praProfession;
	}


	/**
	 * @return the praLieuTravail
	 */
	public String getPraLieuTravail() {
		return praLieuTravail;
	}


	/**
	 * @param praLieuTravail the praLieuTravail to set
	 */
	public void setPraLieuTravail(String praLieuTravail) {
		this.praLieuTravail = praLieuTravail;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return praNom+" "+praPrenom ;
	}

}
