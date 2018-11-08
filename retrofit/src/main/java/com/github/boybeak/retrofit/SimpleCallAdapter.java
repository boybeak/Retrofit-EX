package com.github.boybeak.retrofit;

import android.support.annotation.NonNull;
import retrofit2.Call;
import retrofit2.CallAdapter;

import java.lang.reflect.Type;
import java.util.concurrent.Executor;

public class SimpleCallAdapter<T> implements CallAdapter<T, SimpleCall<T>> {

    private final Type responseType;
    private final Executor callbackExecutor;

    public SimpleCallAdapter(Type responseType, Executor callbackExecutor) {
        this.responseType = responseType;
        this.callbackExecutor = callbackExecutor;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public SimpleCall<T> adapt(@NonNull Call<T> call) {
        return new SimpleCall<>(call, callbackExecutor);
    }
}
