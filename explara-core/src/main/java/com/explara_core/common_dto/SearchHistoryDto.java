package com.explara_core.common_dto;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by akshaya on 03/05/16.
 */

@DatabaseTable
public class SearchHistoryDto {

    public static final String SEARCH_HISTORY_ID = "id";
    @DatabaseField(generatedId = true, columnName = SEARCH_HISTORY_ID)
    public int id;
    @DatabaseField
    public String keyword;
    @DatabaseField
    public Date createdOn;


    public SearchHistoryDto() {
    }

    public SearchHistoryDto(String keyword, Date createdOn) {
        this.keyword = keyword;
        this.createdOn = createdOn;
    }
}
