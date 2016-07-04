package com.explara.explara_payment_sdk.utils;

import com.citrus.sdk.Environment;

import static com.citrus.sdk.Environment.PRODUCTION;

/**
 * Created by anudeep on 17/01/16.
 */
public class CitrusHelper {

    //    String signupId, String signupSecret,
//    String signinId, String signinSecret, int
//    actionBarItemColor, Context context, Environment
//    environment,String vanity, String billGenerator, String
//            returnURL


//    CitrusFlowManager.initCitrusConfig("33g3ywh2un-signup",
//            "c260cdcc277c0275e7a966e784b534f9", "33g3ywh2un-signin",
//            "7f96ae671b246d8b716025bea1fd8361", getResources().getColor(R.color.white),
//    this, Environment.SANDBOX, "33g3ywh2un", "", Constants.CITRUS_RETURN_URL);


//    #define VanityUrl @"lawxtfkjz8"
//            #define SignInId @"lawxtfkjz8-signin"
//            #define SignInSecretKey @"cf64b059870cf73b486a3c11e6a8b763"
//            #define SubscriptionId @"lawxtfkjz8-signup"
//            #define SubscriptionSecretKey @"40a784184e29425773018a1b05c158c6"
//
////// URLs
//            #define LoadWalletReturnUrl @"https://in.explara.com/em/ticket/checkout/citrus-load-money-return-url"
//            #define BillUrl @"https://in.explara.com/em/ticket/checkout/citrus-bill"

    public interface CitrusValues {
        String SIGN_UP_ID_PRODUCTION = "lawxtfkjz8-signup";
        String SIGN_UP_SCRET_PRODUCTION = "40a784184e29425773018a1b05c158c6";

        String SIGN_IN_ID_PRODUCTION = "lawxtfkjz8-signin";
        String SIGN_IN_SECRET_PRODUCTION = "cf64b059870cf73b486a3c11e6a8b763";

        String VANITY_SAND_BOX = "33g3ywh2un";
        String VANITY_PRODUCTION = "lawxtfkjz8";

        String SIGN_UP_ID_SAND_BOX = "33g3ywh2un-signup";
        String SIGN_UP_SCRET_SAND_BOX = "c260cdcc277c0275e7a966e784b534f9";
        String SIGN_IN_ID_SAND_BOX = "33g3ywh2un-signin";
        String SIGN_IN_SECRET_SAND_BOX = "7f96ae671b246d8b716025bea1fd8361";
    }

    public static String getSignUpId() {
//        if (Constants.DEBUG) {
//            return CitrusValues.SIGN_UP_ID_SAND_BOX;
//        }
        return CitrusValues.SIGN_UP_ID_PRODUCTION;
    }

    public static String getSignUpSecret() {
//        if (Constants.DEBUG) {
//            return CitrusValues.SIGN_UP_SCRET_SAND_BOX;
//        }
        return CitrusValues.SIGN_UP_SCRET_PRODUCTION;
    }


    public static String getSignInId() {
//        if (Constants.DEBUG) {
//            return CitrusValues.SIGN_IN_ID_SAND_BOX;
//        }
        return CitrusValues.SIGN_IN_ID_PRODUCTION;
    }

    public static String getSignInSecret() {
//        if (Constants.DEBUG) {
//            return CitrusValues.SIGN_IN_SECRET_SAND_BOX;
//        }
        return CitrusValues.SIGN_IN_SECRET_PRODUCTION;
    }

    public static String getVanity() {
//        if (Constants.DEBUG) {
//            return CitrusValues.VANITY_SAND_BOX;
//        }
        return CitrusValues.VANITY_PRODUCTION;
    }

    public static Environment getEnvironMent() {
//        if (Constants.DEBUG)
//            return SANDBOX;
        return PRODUCTION;
    }
}
