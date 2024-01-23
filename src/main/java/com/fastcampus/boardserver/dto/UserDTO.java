package com.fastcampus.boardserver.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class UserDTO {

    public enum Status {
        DEFAULT, ADMIN, DELETED;
    }

    private int id;
    private String userId;
    private String password;
    private String nickName;
    private boolean admin;
    private Date createTime;
    private boolean isWithDraw;
    private Status status;
    private Date updateTime;

    public static boolean hasNullDataBeforeSignup(UserDTO userDTO) {
        return userDTO.getUserId() == null || userDTO.getPassword() == null
                || userDTO.getNickName() == null;
    }
}
