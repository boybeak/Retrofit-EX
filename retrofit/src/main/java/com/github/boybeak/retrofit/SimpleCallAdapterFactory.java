package com.github.boybeak.retrofit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class SimpleCallAdapterFactory extends CallAdapter.Factory {

    public static SimpleCallAdapterFactory create() {
        return new SimpleCallAdapterFactory();
    }

    private SimpleCallAdapterFactory () {

    }

    @Nullable
    @Override
    public CallAdapter<?, ?> get(@NonNull Type returnType, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
        if (getRawType(returnType) != SimpleCall.class) {
            return null;
        }
        final Type responseType = getCallResponseType(returnType);
        return new SimpleCallAdapter<>(responseType, retrofit.callbackExecutor());
    }

    private static Type getCallResponseType(Type returnType) {
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalArgumentException(
                    "Call return type must be parameterized as Call<Foo> or Call<? extends Foo>");
        }
        return getParameterUpperBound(0, (ParameterizedType) returnType);
    }

}
