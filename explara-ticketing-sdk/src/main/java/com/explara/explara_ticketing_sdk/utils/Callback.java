package com.explara.explara_ticketing_sdk.utils;

/**
 * Created by anudeep on 25/02/16.
 */
public interface Callback<T> {

    void success(T t);

    void error(ExplaraError error);
}