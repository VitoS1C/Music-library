package lab_8.service;

import lab_8.dto.UserDto;
import lab_8.entity.User;

import java.util.List;

public interface UserService{
    void saveUser(UserDto userDto);
    User findUserByEmail(String email);
    List<UserDto> findAllUsers();
}
