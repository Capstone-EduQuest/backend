package com.eduquest.backend.infrastructure.security.config;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XssPolicyConfig {

    @Bean
    public PolicyFactory policyFactory() {
        return Sanitizers.FORMATTING.and(Sanitizers.LINKS);
    }

}
