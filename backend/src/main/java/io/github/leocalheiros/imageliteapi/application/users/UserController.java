package io.github.leocalheiros.imageliteapi.application.users;

import io.github.leocalheiros.imageliteapi.domain.entity.User;
import io.github.leocalheiros.imageliteapi.domain.exception.DuplicatedTupleException;
import io.github.leocalheiros.imageliteapi.domain.exception.NotAuthorizedException;
import io.github.leocalheiros.imageliteapi.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity save(@RequestBody UserDTO dto){
        try {
            User user = userMapper.mapToUser(dto);
            userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (DuplicatedTupleException e){
            Map<String, String > jsonResult = Map.of("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(jsonResult);
        }
    }

    @PostMapping("/auth")
    public ResponseEntity authenticate(@RequestBody CredentialsDTO credentials){
        try {
            var token = userService.authenticate(credentials.getEmail(), credentials.getPassword());
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            return ResponseEntity.ok(token);
        } catch (NotAuthorizedException e) {
            Map<String, String > jsonResult = Map.of("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jsonResult);
            }
        }
    }
