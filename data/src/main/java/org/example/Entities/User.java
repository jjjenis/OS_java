package org.example.Entities;

import lombok.Getter;
import org.example.Enums.HairColor;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Getter
    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Getter
    private String name;
    @Getter
    private int age;
    private String gender;

    @Getter
    @Enumerated(EnumType.STRING)
    private HairColor hairColor;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_login", referencedColumnName = "login"),
            inverseJoinColumns = @JoinColumn(name = "friend_login", referencedColumnName = "login")
    )
    private final Set<User> friends = new HashSet<>();

    public User() {}

    public User(String login, String name, int age, String gender, HairColor hairColor) {
        if (login == null || login.isEmpty()) {
            throw new IllegalArgumentException("Login cannot be null or empty");
        }
        this.login = login;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.hairColor = hairColor;
    }

    public String getGender() {
        return String.valueOf(gender);
    }

    public void addFriend(User friend) {
        friends.add(friend);
        friend.friends.add(this);
    }

    public Set<String> getFriends() {
        return friends.stream()
                .map(User::getLogin)
                .collect(Collectors.toSet());
    }

    public String getId() {
        return login;
    }
}