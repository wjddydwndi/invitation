package com.invitation.module.common.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("seq")
    private long seq;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("state")
    private long state;

    @JsonProperty("access_token_expire_time")
    private Date accessTokenExpireTime;

    @JsonProperty("refresh_token_expire_time")
    private Date refreshTokenExpireTime;

    @JsonProperty("renew_cnt")
    private int renewCnt;

    @JsonProperty("update_time")
    private Date updateTime;

    @JsonProperty("create_time")
    private Date createTime;
}
