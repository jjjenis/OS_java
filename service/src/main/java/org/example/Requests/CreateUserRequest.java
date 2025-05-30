package org.example.Requests;

import lombok.Data;
import org.example.Enums.HairColor;

@Data
public class CreateUserRequest {
    private String login;
    private String name;
    private int age;
    private String gender;
    private HairColor hairColor;
}