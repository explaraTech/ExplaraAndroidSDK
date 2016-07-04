package com.explara_core.common_dto;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Debasish on 17/07/15.
 */

@DatabaseTable
public class CurrencyDetailsDto {

    public static final String CURRENCY_LIST_FIELD_NAME = "currency_list_id";
    public static final String CURRENCY_NAME = "currency";
    public static final String SYMBOL_NAME = "symbol";
    public static final String COUNTRY_NAME = "country";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    public String currency;

    @DatabaseField
    public String symbol;

    @DatabaseField
    public String country;

    //@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = CURRENCY_LIST_FIELD_NAME)
    //private CurrencyList currencyList;

    public CurrencyDetailsDto() {

    }

    public CurrencyDetailsDto(String currency, String symbol, String country) {
        //this.currencyList = currencyList;
        this.currency = currency;
        this.symbol = symbol;
        this.country = country;
    }

   /* public CurrencyDetailsDto(CurrencyList currencyList,String currency, String symbol, String country) {
        //this.currencyList = currencyList;
        this.currency = currency;
        this.symbol = symbol;
        this.country = country;
    }*/
}
