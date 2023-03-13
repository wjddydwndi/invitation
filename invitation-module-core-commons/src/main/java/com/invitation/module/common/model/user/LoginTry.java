package com.invitation.module.common.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginTry {

    @JsonProperty("seq")
    private long seq;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("retry_count")
    private int retryCount;

    @JsonProperty("update_time")
    private String updateTime;

    @JsonProperty("create_time")
    private String createTime;

}
