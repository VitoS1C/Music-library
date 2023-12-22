package URFU_Music.service;

import URFU_Music.dto.UserDto;
import URFU_Music.entity.User;

import java.util.List;

public interface UserService{
    void saveUser(UserDto userDto);
    User findUserByEmail(String email);
    List<UserDto> findAllUsers();
    User findCurrentUser();

}
