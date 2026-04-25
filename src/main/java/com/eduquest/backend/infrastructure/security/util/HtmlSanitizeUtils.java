package com.eduquest.backend.infrastructure.security.util;

import lombok.RequiredArgsConstructor;
import org.owasp.html.PolicyFactory;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.databind.node.StringNode;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class HtmlSanitizeUtils {

    private final PolicyFactory policyFactory;
    private final ObjectMapper objectMapper;

    public String sanitizeString(String input) {
        return policyFactory.sanitize(input);
    }

    public String sanitizeJsonString(String json) throws IOException {
        JsonNode root = objectMapper.readTree(json);
        sanitizeJsonNode(root);
        return objectMapper.writeValueAsString(root);
    }

    private void sanitizeJsonNode(JsonNode node) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            for (String fieldName : objectNode.propertyNames()) {
                JsonNode child = objectNode.get(fieldName);
                if (child.isString()) {
                    String sanitized = sanitizeString(child.stringValue());
                    objectNode.put(fieldName, sanitized);
                } else {
                    sanitizeJsonNode(child);
                }
            }
        } else if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode child = arrayNode.get(i);
                if (child.isString()) {
                    String sanitized = sanitizeString(child.stringValue());
                    arrayNode.set(i, new StringNode(sanitized));
                } else {
                    sanitizeJsonNode(child);
                }
            }
        }
    }

}
