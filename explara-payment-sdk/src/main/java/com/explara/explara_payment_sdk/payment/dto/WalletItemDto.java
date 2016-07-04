package com.explara.explara_payment_sdk.payment.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anudeep on 16/01/16.
 */
public class WalletItemDto {

    @SerializedName("walletId")
    public int walletId;
    @SerializedName("walletName")
    public String walletName;
}
