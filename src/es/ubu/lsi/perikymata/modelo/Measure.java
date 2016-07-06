package es.ubu.lsi.perikymata.modelo;

/**
 * Class that stores the measure of the image.
 * 
 * @author Sergio Chico Carrancio
 */
public class Measure {
	
	/**
	 * Coordinates of the start and the end of the measure.
	 */
	private double[] startMeasure = new double[2];
	private double[] endMeasure = new double[2];
	
	/**
	 * Value and unit of the measure.
	 */
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