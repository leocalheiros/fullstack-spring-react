package io.github.leocalheiros.imageliteapi.application.users;

import io.github.leocalheiros.imageliteapi.domain.AccessToken;
import io.github.leocalheiros.imageliteapi.domain.entity.User;
import io.github.leocalheiros.imageliteapi.domain.exception.DuplicatedTupleException;
import io.github.leocalheiros.imageliteapi.domain.exception.NotAuthorizedException;
import io.github.leocalheiros.imageliteapi.domain.service.UserService;
import io.github.leocalheiros.imageliteapi.infra.repository.UserRepository;
import io.github.leocalheiros.imageliteapi.middlewares.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public User save(User user) {
        var possibleUser = getByEmail(user.getEmail());
        if(possibleUser != null){
            throw new DuplicatedTupleException("User already exists!");
        }
        encodePassword(user);
        return userRepository.save(user);
    }

    @Override
    public AccessToken authenticate(String email, String password) {
        var user = getByEmail(email);
        if(user == null) {
            return null;
        }

        boolean matches = passwordEncoder.matches(password, user.getPassword());

        if(matches){
            return jwtService.generateToken(user);
        }
        throw new NotAuthorizedException("User not authorized");
    }

    private void encodePassword(User user){
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
    }
}
