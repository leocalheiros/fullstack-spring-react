package io.github.leocalheiros.imageliteapi.middlewares.jwt;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(String message) {
        super(message);
    }
}
