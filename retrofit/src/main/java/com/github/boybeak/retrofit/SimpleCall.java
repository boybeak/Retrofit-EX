package com.github.boybeak.retrofit;

import android.support.annotation.NonNull;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.concurrent.Executor;

public class SimpleCall<R> implements Call<R> {

    private final Call<R> workerCall;
    private final Executor callbackExecutor;

    public SimpleCall(Call<R> workerCall, Executor callbackExecutor) {
        this.workerCall = workerCall;
        this.callbackExecutor = callbackExecutor;
    }

    @Override
    public Response<R> execute() throws IOException {
        return workerCall.execute();
    }

    @Override
    public void enqueue(@NonNull final Callback<R> callback) {
        if (callback instanceof CallbackImpl) {
            ((CallbackImpl)callback).onPreCall();
        }
        workerCall.enqueue(new Callback<R>() {
            @Override
            public void onResponse(@NonNull final Call<R> call, @NonNull final Response<R> response) {
                callbackExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResponse(call, response);
                    }
                });
            }

            @Override
            public void onFailure(@NonNull final Call<R> call, @NonNull final Throwable t) {
                callbackExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(call, t);
                    }
                });
            }
        });
    }

    @Override
    public boolean isExecuted() {
        return workerCall.isExecuted();
    }

    @Override
    public void cancel() {
        workerCall.cancel();
    }

    @Override
    public boolean isCanceled() {
        return workerCall.isCanceled();
    }

    @Override
    public SimpleCall<R> clone() {
        return new SimpleCall<>(workerCall.clone(), callbackExecutor);
    }

    @Override
    public Request request() {
        return workerCall.request();
    }
}
