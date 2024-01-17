package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.entity.UserEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long userId;

    public User(UserEntity userEntity) {
        userId = userEntity.getUserId();
    }
}
