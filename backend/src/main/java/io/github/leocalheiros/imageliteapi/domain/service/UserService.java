package io.github.leocalheiros.imageliteapi.domain.service;

import io.github.leocalheiros.imageliteapi.domain.AccessToken;
import io.github.leocalheiros.imageliteapi.domain.entity.User;

public interface UserService {
    User getByEmail(String email);
    User save(User user);
    AccessToken authenticate(String email, String password);
}
