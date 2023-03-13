package com.invitation.module.common.util;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
public class SyncConfiguration {

    private String databaseName;
    private Date updateDate;
    private boolean isSync;

    public SyncConfiguration(String databaseName, Date updateDate) {
        this.databaseName = databaseName;
        this.updateDate = updateDate;
        isSync = false;
    }
}
