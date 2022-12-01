drop table heart   if exists;
drop table reply if exists;
drop table comment if exists;
drop table post_tag if exists;
drop table tag if exists;
drop table post if exists;
drop table member if exists;

DROP DATABASE sns if exists;
CREATE DATABASE sns DEFAULT CHARACTER SET = utf8 COLLATE = utf8;

CREATE TABLE MEMBER(
    member_id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    member_email VARCHAR(100) NOT NULL UNIQUE,
    member_pwd VARCHAR(20) NOT NULL,
    created_date TIMESTAMP,
    modified_date TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8;

CREATE TABLE POST(
    post_id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    post_title VARCHAR(100) NOT NULL,
    post_content TEXT NOT NULL,
    post_hearts INT NOT NULL DEFAULT 0,
    post_views INT NOT NULL DEFAULT 0,
    member_id BIGINT NOT NULL,
    created_date TIMESTAMP,
    modified_date TIMESTAMP,
    constraint POST_MEMBER_KF foreign key(member_id)
        references MEMBER(member_id)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8;


CREATE TABLE HEART(
    heart_id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    member_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    constraint HERAT_MEMBER_FK foreign key(member_id)
        references MEMBER(member_id),
    constraint HERAT_POST_FK foreign key(post_id)
        references POST(post_id)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8;

CREATE TABLE TAG(
    tag_id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    tag_name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8;

CREATE TABLE POST_TAG(
    post_tag_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_tag_name VARCHAR(50) NOT NULL,
    post_id BIGINT NOT NULL,
    tag_id  BIGINT NOT NULL,
    FOREIGN KEY (post_id) REFERENCES POST(post_id),
    FOREIGN KEY (tag_id) REFERENCES TAG(tag_id)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8;


CREATE TABLE COMMENT (
    comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(200) NOT NULL,
    member_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    created_date TIMESTAMP,
    modified_date TIMESTAMP,
    constraint COMMENT_MEMBER_FK foreign key (member_id)
        references MEMBER(member_id),
    constraint COMMENT_POST_FK foreign key (post_id)
        references POST(post_id)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8;

CREATE TABLE REPLY
(
    reply_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(200) NOT NULL,
    member_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    comment_id BIGINT NOT NULL,
    created_date TIMESTAMP,
    modified_date TIMESTAMP,
    constraint REPLY_MEMBER_FK foreign key(member_id)
        references MEMBER(member_id),
    constraint REPLY_POST_FK foreign key(post_id)
        references POST(post_id),
    constraint REPLY_COMMENT_FK foreign key(comment_id)
        references COMMENT(comment_id)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8;
