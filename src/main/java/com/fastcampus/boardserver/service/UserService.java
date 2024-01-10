package com.fastcampus.boardserver.service;

import com.fastcampus.boardserver.dto.UserDto;

public interface UserService {

    void register(UserDto userProfile);

    UserDto login(String id, String password);

    boolean isDuplicatedId(String id);

    UserDto getUserInfo(String userId);

    void updatePassword(String id, String beforePassword, String afterPassword);

    void deleteId(String id, String password);
}
