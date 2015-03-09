package fr.gsbcr.android;

public class Offrir {
	private Collaborateur colMatricule;
	private Praticien praticien;
	private CompteRendu cr;
	private int echantillon;
	private Medicament med;
	private int identifier;
	/**
	 * @param colMatricule
	 * @param praticien
	 * @param cr
	 * @param echantillon
	 */
	public Offrir(Collaborateur colMatricule, Praticien praticien,
			CompteRendu cr, int echantillon ,Medicament med , int identifier) {
		this.identifier = identifier;
		this.colMatricule = colMatricule;
		this.praticien = praticien;
		this.cr = cr;
		this.echantillon = echantillon;
		this.med = med;
	}
	
	
	
	/**
	 * @return the identifier
	 */
	public int getIdentifier() {
		return identifier;
	}



	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}



	/**
	 * @return the med
	 */
	public Medicament getMed() {
		return med;
	}


	/**
	 * @param med the med to set
	 */
	public void setMed(Medicament med) {
		this.med = med;
	}


	/**
	 * @return the colMatricule
	 */
	public Collaborateur getColMatricule() {
		return colMatricule;
	}
	/**
	 * @param colMatricule the colMatricule to set
	 */
	public void setColMatricule(Collaborateur colMatricule) {
		this.colMatricule = colMatricule;
	}
	/**
	 * @return the praticien
	 */
	public Praticien getPraticien() {
		return praticien;
	}
	/**
	 * @param praticien the praticien to set
	 */
	public void setPraticien(Praticien praticien) {
		this.praticien = praticien;
	}
	/**
	 * @return the cr
	 */
	public CompteRendu getCr() {
		return cr;
	}
	/**
	 * @param cr the cr to set
	 */
	public void setCr(CompteRendu cr) {
		this.cr = cr;
	}
	/**
	 * @return the echantillon
	 */
	public int getEchantillon() {
		return echantillon;
	}
	/**
	 * @param echantillon the echantillon to set
	 */
	public void setEchantillon(int echantillon) {
		this.echantillon = echantillon;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Offrir [colMatricule=" + colMatricule + ", praticien="
				+ praticien + ", cr=" + cr + ", echantillon=" + echantillon
				+ ", med=" + med + ", identifier=" + identifier + "]";
	}
	
	

}
