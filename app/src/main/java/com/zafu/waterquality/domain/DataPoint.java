package com.zafu.waterquality.domain;

public class DataPoint {
	
	private String PonitName ;
	private double tempVale ;
	private double phValue ;
	private double oxgasValue ;
	private double nValue ;
	private double zhouValue ;
	
	public DataPoint(){
		
	}

	public DataPoint(String ponitName, double tempVale, double phValue,
					 double oxgasValue, double nValue, double zhouValue) {
		super();
		PonitName = ponitName;
		this.tempVale = tempVale;
		this.phValue = phValue;
		this.oxgasValue = oxgasValue;
		this.nValue = nValue;
		this.zhouValue = zhouValue;
	}

	public String getPonitName() {
		return PonitName;
	}

	public void setPonitName(String ponitName) {
		PonitName = ponitName;
	}

	public double getTempVale() {
		return tempVale;
	}

	public void setTempVale(double tempVale) {
		this.tempVale = tempVale;
	}

	public double getPhValue() {
		return phValue;
	}

	public void setPhValue(double phValue) {
		this.phValue = phValue;
	}

	public double getOxgasValue() {
		return oxgasValue;
	}

	public void setOxgasValue(double oxgasValue) {
		this.oxgasValue = oxgasValue;
	}

	public double getnValue() {
		return nValue;
	}

	public void setnValue(double nValue) {
		this.nValue = nValue;
	}

	public double getZhouValue() {
		return zhouValue;
	}

	public void setZhouValue(double zhouValue) {
		this.zhouValue = zhouValue;
	}
	

}
