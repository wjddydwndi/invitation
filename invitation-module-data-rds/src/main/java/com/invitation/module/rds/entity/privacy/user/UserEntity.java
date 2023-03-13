package com.invitation.module.rds.entity.privacy.user;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter @Setter
@NoArgsConstructor
@Entity // DB 기록용 데이터 형태
@Table(name="t_user")
public class UserEntity implements Serializable {

    @Id @GeneratedValue
    @Column(unique = true, name = "seq")
    private int seq;

    @Column(unique = true, name= "id")
    private String id;

    @Column(unique = true, name = "email")
    private String email;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "password")
    private String password;

    @Column(name = "birth")
    private String birth;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "cell_no")
    private String cellNo;

    @Column(name = "update_time")
    private Date updateAt;

    @Column(name = "create_time")
    private Date createAt;
}
