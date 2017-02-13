package com.zafu.waterquality.RxjavaRetrofit.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mengxin on 17-2-12.
 */

public class HttpResult<T> {
    @SerializedName("success")
    private boolean isSuccess;
    @SerializedName("data")
    private T subjects;

    @Override
    public String toString() {
        return "HttpResult{" +
                "isSuccess=" + isSuccess +
                ", subjects=" + subjects +
                '}';
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public T getSubjects() {
        return subjects;
    }

    public void setSubjects(T subjects) {
        this.subjects = subjects;
    }
}
