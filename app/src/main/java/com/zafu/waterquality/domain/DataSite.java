package com.zafu.waterquality.domain;

public class DataSite {
	
	private String SiteName ;
	private String date;
	private String time;
	private float ph;
	private float dianDaoLv;
	private float shuiWen;
	private float anDan;
	private float rongJieYang;


	public String getSiteName() {
		return SiteName;
	}

	public void setSiteName(String siteName) {
		SiteName = siteName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public float getPh() {
		return ph;
	}

	public void setPh(float ph) {
		this.ph = ph;
	}

	public float getDianDaoLv() {
		return dianDaoLv;
	}

	public void setDianDaoLv(float dianDaoLv) {
		this.dianDaoLv = dianDaoLv;
	}

	public float getShuiWen() {
		return shuiWen;
	}

	public void setShuiWen(float shuiWen) {
		this.shuiWen = shuiWen;
	}

	public float getAnDan() {
		return anDan;
	}

	public void setAnDan(float anDan) {
		this.anDan = anDan;
	}

	public float getRongJieYang() {
		return rongJieYang;
	}

	public void setRongJieYang(float rongJieYang) {
		this.rongJieYang = rongJieYang;
	}
}
