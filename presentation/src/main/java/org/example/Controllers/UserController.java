// Java
package org.example.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.DTOs.UserDTO;
import org.example.Mappers.UserMapper;
import org.example.Entities.User;
import org.example.Enums.HairColor;
import org.example.Requests.CreateUserRequest;
import org.example.Requests.UpdateFriendsRequest;
import org.example.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Endpoints for managing users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
        System.out.println("UserController initialized with UserService");
    }

    @Operation(summary = "Create a new user", description = "Creates a new user with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserRequest request) {
        User user = userService.createUser(
                request.getLogin(),
                request.getName(),
                request.getAge(),
                String.valueOf(request.getGender()),
                String.valueOf(request.getHairColor())
        );
        return ResponseEntity.status(201).body(UserMapper.toDTO(user));
    }

    @Operation(summary = "Get user info by login", description = "Returns detailed information about a user by their login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User info retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{login}")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable String login) {
        User user = userService.getUserInfo(login);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(UserMapper.toDTO(user));
    }

    @Operation(summary = "Get friends of a user", description = "Returns a list of friends for the specified user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friends retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{login}/friends")
    public ResponseEntity<List<UserDTO>> getFriendsByUserLogin(@PathVariable String login) {
        List<User> friends = userService.getFriendsByUserLogin(login);
        return ResponseEntity.ok(friends.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Add a friend to a user", description = "Adds a friend to the specified user's friend list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend added successfully"),
            @ApiResponse(responseCode = "404", description = "User or friend not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/{login}/friends/{friendLogin}")
    public ResponseEntity<Void> addFriend(
            @PathVariable String login,
            @PathVariable String friendLogin
    ) {
        userService.addFriend(login, friendLogin);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove a friend from a user", description = "Removes a friend from the specified user's friend list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend removed successfully"),
            @ApiResponse(responseCode = "404", description = "User or friend not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @DeleteMapping("/{login}/friends/{friendLogin}")
    public ResponseEntity<Void> removeFriend(
            @PathVariable String login,
            @PathVariable String friendLogin
    ) {
        userService.removeFriend(login, friendLogin);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update friends of a user", description = "Updates the friend list for the specified user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friends updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{login}/friends")
    public ResponseEntity<Void> updateFriends(
            @PathVariable String login,
            @RequestBody UpdateFriendsRequest request
    ) {
        userService.updateFriends(login, request.getFriendLogins());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all users", description = "Returns a list of users with optional filtering by hair color and gender")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid filter parameters")
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(
            @RequestParam(required = false) String hairColor,
            @RequestParam(required = false) String gender
    ) {
        HairColor hairColorEnum = null;
        if (hairColor != null && !hairColor.isEmpty()) {
            hairColorEnum = HairColor.valueOf(hairColor.toUpperCase());
        }
        String genderValue = null;
        if (gender != null && !gender.isEmpty()) {
            genderValue = gender.toUpperCase();
        }
        List<User> users = userService.getAllUsers(hairColorEnum, genderValue);
        return ResponseEntity.ok(users.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList()));
    }
}