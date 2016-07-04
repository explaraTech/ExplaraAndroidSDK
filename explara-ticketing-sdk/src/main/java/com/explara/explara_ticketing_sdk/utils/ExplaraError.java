package com.explara.explara_ticketing_sdk.utils;

import com.android.volley.VolleyError;

/**
 * Created by anudeep on 25/02/16.
 */
public class ExplaraError extends VolleyError {
    private VolleyError error;

    public ExplaraError(VolleyError error) {

        this.error = error;
    }
}
