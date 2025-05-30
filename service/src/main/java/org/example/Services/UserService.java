// Java
package org.example.Services;

import jakarta.transaction.Transactional;
import org.example.Entities.User;
import org.example.Enums.HairColor;
import org.example.Repositories.SpringUserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserService {
    private final SpringUserRepository userRepository;

    public UserService(SpringUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String login, String name, int age, String gender, String hairColor) {
        if (login == null || login.isEmpty()) throw new IllegalArgumentException("Login cannot be null or empty");
        User user = new User(login, name, age, gender, HairColor.valueOf(hairColor.toUpperCase()));
        System.out.println("Success");
        return userRepository.save(user);
    }

    public User getUserInfo(String login) {
        return userRepository.findByLogin(login);
    }

    public void updateFriends(String login, ArrayList<String> friendLogins) {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + login);
        }
        user.getFriends().clear();
        for (String friendLogin : friendLogins) {
            User friend = userRepository.findByLogin(friendLogin);
            if (friend != null) {
                user.addFriend(friend);
            } else {
                System.out.println("Friend with login " + friendLogin + " not found.");
            }
        }
        userRepository.save(user);
    }

    public List<User> getAllUsers(HairColor hairColor, String gender) {
        if (hairColor != null && gender != null) {
            return userRepository.findAllByHairColorAndGender(hairColor, gender);
        } else if (hairColor != null) {
            return userRepository.findAllByHairColor(hairColor);
        } else if (gender != null) {
            return userRepository.findAllByGender(gender);
        } else {
            return userRepository.findAll();
        }
    }

    public void addFriend(String login, String friendLogin) {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + login);
        }
        User friend = userRepository.findByLogin(friendLogin);
        if (friend == null) {
            throw new IllegalArgumentException("Friend not found: " + friendLogin);
        }
        user.addFriend(friend);
        userRepository.save(user);
    }

    public void removeFriend(String login, String friendLogin) {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + login);
        }
        Set<String> friends = user.getFriends();
        if (!friends.contains(friendLogin)) {
            throw new IllegalArgumentException(friendLogin + " is not a friend of " + login);
        }
        friends.remove(friendLogin);
        userRepository.save(user);
    }

    public List<User> getFriendsByUserLogin(String login) {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + login);
        }
        return userRepository.findFriendsByUserLogin(user.getLogin());
    }

    public List<User> getAllUsersFiltered(HairColor hairColor, String gender) {
        if (hairColor != null && gender != null) {
            return userRepository.findByHairColorAndGender(hairColor, gender);
        } else if (hairColor != null) {
            return userRepository.findByHairColor(hairColor);
        } else if (gender != null) {
            return userRepository.findByGender(gender);
        } else {
            return userRepository.findAll();
        }
    }
}