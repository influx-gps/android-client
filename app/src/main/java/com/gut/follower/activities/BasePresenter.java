package com.gut.follower.activities;

public interface BasePresenter<V> {

    void start();

    void attachView(V view);

    void detachView();
}
