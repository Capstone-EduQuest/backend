package com.eduquest.backend.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    // spring 7에서 Jackson2ObjectMapperBuilder deprecated
    @Bean
    public JsonMapper jsonMapper() {
        return JsonMapper.builder()
                .findAndAddModules() // Automatically discover modules via JDK ServiceLoader
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .build();
    }

}

