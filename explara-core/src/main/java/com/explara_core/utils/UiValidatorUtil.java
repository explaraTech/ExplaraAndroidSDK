package com.explara_core.utils;

import android.util.Patterns;

/**
 * Created by anudeep on 28/08/15.
 */
public class UiValidatorUtil {

    public static boolean isPhoneNumberValid(String phoneNum) {
        return Patterns.PHONE.matcher(phoneNum).matches();
    }
}


