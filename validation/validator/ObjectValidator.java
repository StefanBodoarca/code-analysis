package com.lmig.ci.grs.salesforce.file_connector.validation.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * General use Validator; applied to type Object.
 */
@Slf4j
@Component
public class ObjectValidator {
    private final Validator validator;

    @Autowired
    public ObjectValidator(final Validator validator) {
        this.validator = validator;
    }

    public void validateObject(Object o, HttpStatus status) {
        final Set<ConstraintViolation<Object>> violations = validator.validate(o);

        if (!violations.isEmpty()) {
            final StringBuilder errorBuilder = new StringBuilder("Validation failed:");
            for (ConstraintViolation<Object> violation : violations) {
                final String propertyPath = violation.getPropertyPath().toString();
                final String message = violation.getMessage();
                errorBuilder.append(String.format("%n- Property: %s, Error: %s", propertyPath, message));
            }

            final String error = errorBuilder.toString();
            log.error(error);
            throw new ResponseStatusException(status, error);
        }
    }

    public boolean validateMapValueNotNullAgainstKeysList(
            final List<String> predefinedKeys, final Map<String, String> values, final String errorMessage) {
        final Predicate<String> isKeyAndValuePresent = k -> values.containsKey(k) && !values.get(k).isBlank();

        if (predefinedKeys.stream().allMatch(isKeyAndValuePresent)) {
            return true;
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
    }
}
