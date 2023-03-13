package com.invitation.module.rds.entity.privacy.user;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity // DB 기록용 데이터 형태
@Table(name="t_token")
@DynamicInsert
@DynamicUpdate
public class TokenEntity implements Serializable {

    @Id @GeneratedValue
    private long seq;

    @Column(unique = true, name="user_id")
    private String userId;

    @Column(unique = true, name = "access_token")
    private String accessToken;

    @Column(unique = true, name = "refresh_token")
    private String refreshToken;

    @Column(name = "token_type")
    private String tokenType;

    @Column(name = "state")
    private String state;

    @Column(name = "expire_time")
    private String expireTime;

    @Column(name = "update_time")
    private String updateTime;

    @Column(name = "create_time")
    private String createTime;
}
