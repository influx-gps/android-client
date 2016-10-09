package com.gut.follower.activities;

public interface BasePresenter<V> {

    void attachView(V view);

    void detachView();
}
