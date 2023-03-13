package com.invitation.module.common.model.configuration;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ConfigurationReference {

    @JsonProperty("seq")
    private long seq;

    @JsonProperty("category")
    private String category;

    @JsonProperty("code")
    private String code;

    @JsonProperty("value")
    private String value;

    @JsonProperty("description")
    private String description;

    @JsonProperty("enabled")
    private boolean enabled;

    @JsonProperty("create_time")
    private Date createTime;

    @JsonProperty("update_time")
    private Date updateTime;

    @JsonProperty("database_name")
    private String databaseName;

    @JsonProperty("table_name")
    private String tableName;
}
