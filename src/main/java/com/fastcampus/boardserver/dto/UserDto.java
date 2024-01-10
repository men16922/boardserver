package com.fastcampus.boardserver.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class UserDto {

    public enum Status {
        DEFAULT, ADMIN, DELETED;
    }

    private int id;
    private String userId;
    private String password;
    private String nickName;
    private boolean admin;
    private Date createTime;
    private boolean isWithdraw;
    private Status status;
    private Date updateTime;
}
