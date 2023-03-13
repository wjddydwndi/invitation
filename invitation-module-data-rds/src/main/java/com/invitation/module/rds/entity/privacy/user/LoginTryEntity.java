package com.invitation.module.rds.entity.privacy.user;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity // DB 기록용 데이터 형태
@Table(name="t_login_try")
@DynamicInsert
@DynamicUpdate
public class LoginTryEntity {

    @Id
    @GeneratedValue
    private long seq;

    @Column(unique = true, name="user_id")
    private String userId;

    @Column(name="retry_count")
    private int retryCount;

    @Column(name="update_time")
    private String updateTime;

    @Column(name="create_time")
    private String createTime;
}
