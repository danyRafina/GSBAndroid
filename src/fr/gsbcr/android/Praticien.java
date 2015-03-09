package fr.gsbcr.android;

/** Classe Praticien de l'application
 * @author rafina
 *
 */
public class Praticien  {
	private int praNum;
	private String praNom;
	private String praPrenom;


	
	/** Création du praticien
	 * @param praNum Numéro du praticien
	 * @param praNom Nom du praticien
	 * @param praPrenom Prénom du praticien
	 
	 */
	public Praticien(int praNum, String praNom, String praPrenom) {
		super();
		this.praNum = praNum;
		this.praNom = praNom;
		this.praPrenom = praPrenom;
	
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


	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return praNom+" "+praPrenom ;
	}

}
