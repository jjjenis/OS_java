package org.example.gatewayapi.requests_g;

import org.example.gatewayapi.entities.enums.HairCOLOR;

public class CreateUserRequest {
    private String login;
    private String password;
    private String role;

    private HairCOLOR hairColor;

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public HairCOLOR getHairColor() {
        return hairColor;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
