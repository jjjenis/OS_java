package org.example.DTOs;

import lombok.Data;
import org.example.Enums.HairColor;

import java.util.Set;

@Data
public class UserDTO {
    private int age;
    private String name;
    private String login;
    private String gender;
    private HairColor hairColor;
    private Set<String> friends;
    private Long id;
}