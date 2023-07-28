package com.projectmanagement.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection ="User_Permission")
@Data
public class AppUserPermission{
    @MongoId
    private  String id;
    private String permission ;

    public AppUserPermission(String permission) {
        this.permission = permission;
    }
}
