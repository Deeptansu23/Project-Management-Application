package com.projectmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "User_Details")
public class User {

    @Id
    private String userId;
    private String fullName;
    private String username;
    private String password;
    Set<AppUserPermission> permissions;
    boolean isAccountNonExpired;

    boolean isAccountNonLocked;

    boolean isCredentialsNonExpired;

    boolean isEnabled;
    @DBRef(lazy = true)
    private AppUserRole  role;
    private String contactNo;

    private String department;
    private boolean projectAssigned;
    private String projectId;
    private int otp;

    public User(String fullName,String username, String password, AppUserRole role, Set<AppUserPermission> authorities, boolean isAccountNonExpired, boolean isAccountNonLocked, boolean isCredentialsNonExpired, boolean isEnabled, String contactNo, String department, boolean projectAssigned, String projectId,int otp) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.permissions=authorities;
        this.role = role;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
        this.contactNo = contactNo;
        this.department = department;
        this.projectAssigned = projectAssigned;
        this.projectId = projectId;
        this.otp = otp;
    }
}
