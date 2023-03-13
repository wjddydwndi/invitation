package com.invitation.module.common.model.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Data
public class Configuration {

    @JsonProperty("seq")
    private int seq;

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

    @JsonProperty("is_reference")
    private boolean isReference;

    @JsonProperty("create_time")
    private Date createTime;

    @JsonProperty("update_time")
    private Date updateTime;

    @JsonProperty("database_name")
    private String databaseName;

    @JsonProperty("table_name")
    private String tableName;

    @JsonProperty("child")
    private List<ConfigurationReference> childList;

}
