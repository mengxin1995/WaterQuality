package com.zafu.waterquality.RxjavaRetrofit.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mengxin on 17-2-12.
 */

public class WaterData {
    private String date;
    private String time;
    private float ph;
    @SerializedName("diandaolv")
    private float dianDaoLv;
    @SerializedName("shuiwen")
    private float shuiWen;
    @SerializedName("andan")
    private float anDan;
    @SerializedName("rongjieyang")
    private float rongJieYang;

    @Override
    public String toString() {
        return "WaterData{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", ph=" + ph +
                ", dianDaoLv=" + dianDaoLv +
                ", shuiWen=" + shuiWen +
                ", anDan=" + anDan +
                ", rongJieYang=" + rongJieYang +
                '}';
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
