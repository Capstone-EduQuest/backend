package com.eduquest.backend.common.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public JsonMapper jsonMapper() {
        return JsonMapper.builder()
                .findAndAddModules() // Automatically discover modules via JDK ServiceLoader
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .build();
    }

}

