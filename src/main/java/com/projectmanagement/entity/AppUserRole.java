package com.projectmanagement.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Set;

@Document(collection = "User_Role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserRole{

    @MongoId
    private  String Id;
    private String Role;
    private  Set<AppUserPermission> permissions;
    public AppUserRole(String role, Set<AppUserPermission> permissions) {
        Role = role;
        this.permissions=permissions;
    }

}