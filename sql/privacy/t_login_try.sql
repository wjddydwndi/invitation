-- auto-generated definition
create table t_login_try
(
    seq         bigint auto_increment
        primary key,
    user_id     varchar(36)                         not null,
    retry_count int       default 0                 not null,
    update_time timestamp default CURRENT_TIMESTAMP not null,
    create_time timestamp default CURRENT_TIMESTAMP not null
);

/* login시도 3회일 경우 lock 걸림*/
DELIMITER //
CREATE TRIGGER lock_login_user
    AFTER UPDATE ON privacy.t_login_try
    FOR EACH ROW # 각 행마다 적용

BEGIN
        IF NEW.retry_count > 2 THEN
UPDATE privacy.t_user SET status = 'L' WHERE id = NEW.user_id;
END IF;
END
// DELIMITER;