package com.github.boybeak.retrofit;

import retrofit2.Callback;

public interface CallbackImpl<T> extends Callback<T> {
    void onPreCall();
}
