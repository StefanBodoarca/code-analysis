package com.lmig.ci.grs.salesforce.file_connector.util.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lmig.ci.grs.salesforce.file_connector.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Helper class used for JsonNode operations.
 */
@Slf4j
public final class JsonHelper {
    private JsonHelper() { }

    public static JsonNode capitalizeKeys(JsonNode node) {

        if (node.isObject()) {
            final ObjectNode objectNode = Constants.OBJECT_MAPPER.createObjectNode();
            node.fields().forEachRemaining(entry -> {
                final String capitalizedKey = StringUtils.capitalize(entry.getKey());
                final JsonNode value = entry.getValue();
                objectNode.set(capitalizedKey, capitalizeKeys(value));
            });
            return objectNode;
        }

        return node;
    }


    public static JsonNode mapKeys(JsonNode node, Map<String, String> mapper) {

        if (node.isObject()) {
            final ObjectNode objectNode = Constants.OBJECT_MAPPER.createObjectNode();
            node.fields().forEachRemaining(entry -> {
                final String mappedKey = mapper.get(entry.getKey());
                final JsonNode value = entry.getValue();
                objectNode.set(mappedKey, mapKeys(value, mapper));
            });
            return objectNode;
        }

        return node;
    }



}
