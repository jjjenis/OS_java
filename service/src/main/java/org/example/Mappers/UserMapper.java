package org.example.Mappers;

import org.example.DTOs.UserDTO;
import org.example.Entities.User;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setAge(user.getAge());
        userDTO.setName(user.getName());
        userDTO.setLogin(user.getLogin());
        userDTO.setGender(user.getGender());
        userDTO.setHairColor(user.getHairColor());
        userDTO.setFriends(user.getFriends());
        return userDTO;
    }
}