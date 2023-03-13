package com.invitation.module.rds.entity.privacy.configuration;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter @Setter
@NoArgsConstructor
@Entity // DB 기록용 데이터 형태
@Table(name="t_configuration")
public class PrivateConfigurationEntity implements Serializable {

    @Id @GeneratedValue
    @Column(name = "seq")
    private int seq;

    @Column(name = "category")
    private String category;

    @Column(name="code")
    private String code;

    @Column(name = "value")
    private String value;

    @Column(name = "description")
    private String description;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "is_reference")
    private boolean isReference;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}

