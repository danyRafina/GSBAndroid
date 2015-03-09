package fr.gsbcr.android;

public class Motif {
	/**
	 * @return the numMotif
	 */
	public int getNumMotif() {
		return numMotif;
	}
	/**
	 * @param numMotif the numMotif to set
	 */
	public void setNumMotif(int numMotif) {
		this.numMotif = numMotif;
	}
	/**
	 * @return the labelMotif
	 */
	public String getLabelMotif() {
		return labelMotif;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return labelMotif ;
	}
	/**
	 * @param labelMotif the labelMotif to set
	 */
	public void setLabelMotif(String labelMotif) {
		this.labelMotif = labelMotif;
	}
	private int numMotif;
	private String labelMotif;
	/**
	 * @param numMotif
	 * @param labelMotif
	 */
	public Motif(int numMotif, String labelMotif) {
		this.numMotif = numMotif;
		this.labelMotif = labelMotif;
	}


}
