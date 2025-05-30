package org.example.Repositories;

import org.example.Entities.User;
import org.example.Enums.HairColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringUserRepository extends JpaRepository<User, Long> {
    List<User> findAllByHairColorAndGender(HairColor hairColor, String gender);

    List<User> findAllByHairColor(HairColor hairColor);

    List<User> findAllByGender(String gender);

    User findByLogin(String login);
    @Query("select u from User u join u.friends f where u.login = :login")
    List<User> findFriendsByUserLogin(@Param("login") String login);

    List<User> findFriendsByUserId(Long userId);

    List<User> findByHairColor(HairColor hairColor);

    List<User> findByGender(String gender);

    List<User> findByHairColorAndGender(HairColor hairColor, String gender);
}