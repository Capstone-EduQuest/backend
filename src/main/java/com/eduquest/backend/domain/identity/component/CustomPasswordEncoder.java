package com.eduquest.backend.domain.identity.component;

public interface CustomPasswordEncoder {

    String encode(String plainText);

    boolean matches(String plainText, String encodedPassword);

}
