package org.example.service;

import lombok.Getter;
import org.example.dto.User;
import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Service
public class UserManager {
    @Autowired
    private UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll().stream()
                .map(UserEntity::createDTO)
                .collect(Collectors.toList());
    }

    public void addUser(long userId) {
        userRepository.save(new UserEntity(userId));
    }

    public void removeUser(long userId) {
        Optional<UserEntity> userEntityOpt = userRepository.findById(userId);
        if (userEntityOpt.isEmpty()) {
            return;
        }
        userRepository.delete(userEntityOpt.get());
    }

    public int getUserCount() {
        return userRepository.findAll().size();
    }
}

