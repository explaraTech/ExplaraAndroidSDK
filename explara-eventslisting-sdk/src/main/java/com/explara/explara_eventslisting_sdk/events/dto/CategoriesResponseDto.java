package com.explara.explara_eventslisting_sdk.events.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dev on 01/08/15.
 */
public class CategoriesResponseDto {
    @SerializedName("category")
    private String category;

    @SerializedName("subCategoryEvents")
    private ArrayList<CollectionsEvent> subCategoryEvents;

    @SerializedName("subCategory")
    private String[] subCategory;

    @SerializedName("subCategoryId")
    public List<String> subCategoryId;

    @SerializedName("status")
    private String status;

    @SerializedName("filters")
    public Filter filters;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<CollectionsEvent> getSubCategoryEvents() {
        return subCategoryEvents;
    }

    public void setSubCategoryEvents(ArrayList<CollectionsEvent> subCategoryEvents) {
        this.subCategoryEvents = subCategoryEvents;
    }

    public String[] getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String[] subCategory) {
        this.subCategory = subCategory;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Filter getFilters() {
        return filters;
    }

    public void setFilters(Filter filters) {
        this.filters = filters;
    }

    @Override
    public String toString() {
        return "ClassPojo [category = " + category + ", subCategoryEvents = " + subCategoryEvents + ", subCategory = " + subCategory + ", status = " + status + ", filters = " + filters + "]";
    }
}
