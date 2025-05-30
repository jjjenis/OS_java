package org.example.Requests;

import lombok.Data;

import java.util.ArrayList;
@Data
public class UpdateFriendsRequest {
    private ArrayList<String> friendLogins;
}
