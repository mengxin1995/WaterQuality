package com.zafu.waterquality.RxjavaRetrofit.subscribers;

import rx.Subscriber;

/**
 * Created by mengxin on 17-2-13.
 */

public class SimpleHttpSubscriber<T> extends Subscriber<T>{

    private SubscriberOnNextListener mSubscriberOnNextListener;

    public SimpleHttpSubscriber(SubscriberOnNextListener mSubscriberOnNextListener) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(T t) {
        if(mSubscriberOnNextListener != null){
            mSubscriberOnNextListener.onNext(t);
        }
    }
}
