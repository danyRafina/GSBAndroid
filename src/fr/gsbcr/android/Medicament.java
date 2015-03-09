package fr.gsbcr.android;

public class Medicament {
	
	protected int identifier;
	private String depotLegal;
	/**
	 * @return the depotLegal
	 */
	public String getDepotLegal() {
		return depotLegal;
	}

	/**
	 * @param depotLegal the depotLegal to set
	 */
	public void setDepotLegal(String depotLegal) {
		this.depotLegal = depotLegal;
	}

	/**
	 * @return the nomCommercial
	 */
	public String getNomCommercial() {
		return nomCommercial;
	}

	/**
	 * @param nomCommercial the nomCommercial to set
	 */
	public void setNomCommercial(String nomCommercial) {
		this.nomCommercial = nomCommercial;
	}

	private String nomCommercial;
	
	/**
	 * @param depotLegal
	 * @param nomCommercial
	 */
	public Medicament(String depotLegal, String nomCommercial,int identifer) {
		this.depotLegal = depotLegal;
		this.nomCommercial = nomCommercial;
		this.identifier = identifer;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return nomCommercial ;
	}
	

}
