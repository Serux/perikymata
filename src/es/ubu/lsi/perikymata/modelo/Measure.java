package es.ubu.lsi.perikymata.modelo;

public class Measure {
	
	private double[] startMeasure;
	private double[] endMeasure;
	private int measureValue;
	private String measureUnit;
	/**
	 * @return the startMeasure
	 */
	public double[] getStartMeasure() {
		return startMeasure;
	}
	/**
	 * @param startMeasure the startMeasure to set
	 */
	public void setStartMeasure(double[] startMeasure) {
		this.startMeasure = startMeasure;
	}
	/**
	 * @return the endMeasure
	 */
	public double[] getEndMeasure() {
		return endMeasure;
	}
	/**
	 * @param endMeasure the endMeasure to set
	 */
	public void setEndMeasure(double[] endMeasure) {
		this.endMeasure = endMeasure;
	}
	/**
	 * @return the measureValue
	 */
	public int getMeasureValue() {
		return measureValue;
	}
	/**
	 * @param measureValue the measureValue to set
	 */
	public void setMeasureValue(int measureValue) {
		this.measureValue = measureValue;
	}
	/**
	 * @return the measureUnit
	 */
	public String getMeasureUnit() {
		return measureUnit;
	}
	/**
	 * @param measureUnit the measureUnit to set
	 */
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}
}
