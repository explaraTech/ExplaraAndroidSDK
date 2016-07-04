package com.explara_core.common_dto;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Debasish on 17/07/15.
 */

@DatabaseTable
public class NotificationDetailsDto {

    public static final String NOTIFICATION_ID = "id";

    @DatabaseField(generatedId = true, columnName = NOTIFICATION_ID)
    public int id;
    @DatabaseField
    public String notificationType;
    @DatabaseField
    public String notificationTitle;
    @DatabaseField
    public String notificationMsg;
    @DatabaseField
    public String notificationImage;
    @DatabaseField
    public String notificationTypeId;// For eventId,topicId,collectionId
    @DatabaseField
    public String currency;
    @DatabaseField
    public String notificationData;// categoryName,searchText,webviewUrl,collectionName
    @DatabaseField
    public String receivedDate;


    public NotificationDetailsDto() {

    }

    public NotificationDetailsDto(String notificationType, String notificationTitle, String notificationMsg, String notificationImage, String notificationTypeId, String currency, String notificationData, String receivedDate) {
        this.notificationType = notificationType;
        this.notificationTitle = notificationTitle;
        this.notificationMsg = notificationMsg;
        this.notificationImage = notificationImage;
        this.notificationTypeId = notificationTypeId;
        this.currency = currency;
        this.notificationData = notificationData;
        this.receivedDate = receivedDate;
    }
}
