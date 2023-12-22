package com.lmig.ci.grs.salesforce.file_connector.util.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.lmig.ci.grs.salesforce.file_connector.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Helper class used to eliminate null values from objects.
 */
@Slf4j
public final class PatchHelper {

    private static final String ERROR_MESSAGE = "Could not patch object to JsonNode. ";
    private static final ObjectMapper OBJECT_MAPPER = Constants.OBJECT_MAPPER;

    private PatchHelper() { }

    private static <T> JsonNode applyPatch(T object, JsonPatch jsonPatch) {
        try {
            return jsonPatch.apply(OBJECT_MAPPER.convertValue(object, JsonNode.class));
        } catch (JsonPatchException e) {
            log.error(ERROR_MESSAGE);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE);
        }
    }

    private static <T> JsonPatch createJsonPatchForObject(T object) {
        final Field[] fields = object.getClass().getDeclaredFields();

        final ArrayNode patchOperations = OBJECT_MAPPER.createArrayNode();
        for (Field field : fields) {
            final PropertyDescriptor propertyDescriptor;
            try {
                propertyDescriptor = new PropertyDescriptor((String) field.getName(), object.getClass());
                final Method getter = propertyDescriptor.getReadMethod();
                final Object value = getter.invoke(object);
                if (value != null) {
                    patchOperations.add(
                            OBJECT_MAPPER.createObjectNode()
                                    .put("op", "replace")
                                    .put("path", "/" + field.getName())
                                    .put("value", (String) value)
                    );
                }
            } catch (Exception e) {
                log.error(ERROR_MESSAGE + e.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE + e.getMessage());
            }
        }

        try {
            return JsonPatch.fromJson(patchOperations);
        } catch (IOException e) {
            log.error(ERROR_MESSAGE + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE + e.getMessage());
        }
    }

    public static <T> JsonNode patch(T object) {
        final JsonPatch jsonPatch = createJsonPatchForObject(object);
        return applyPatch(object, jsonPatch);
    }
}
