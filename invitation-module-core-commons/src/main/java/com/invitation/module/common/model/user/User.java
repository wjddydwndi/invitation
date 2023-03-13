package com.invitation.module.common.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;

@Data
@NoArgsConstructor
public class User {

    @JsonProperty("seq")
    private int seq;

    @JsonProperty("id")
    private String id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("birth")
    private String birth;

    @JsonProperty("nationality")
    private String nationality;

    @JsonProperty("cell_no")
    private String cellNo;

    @JsonProperty("status")
    private String status;

    @JsonProperty("update_time")
    private Date updateAt;

    @JsonProperty("create_time")
    private Date createAt;

    //private Collection<? extends GrantedAuthority> authorities;
}
