package com.fastcampus.boardserver.dto.request;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserDeleteId {
    @NonNull
    private String id;
    @NonNull
    private String password;
}