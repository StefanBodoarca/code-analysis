package com.lmig.ci.grs.salesforce.file_connector.validation.annotation;

import com.lmig.ci.grs.salesforce.file_connector.validation.validator.CheckAtLeastOneNotNullValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Validator annotation for Objects that should contain at least one @NotNull element.
 * The element could be any that's why we need an annotation.
 */
@Documented
@Constraint(validatedBy = CheckAtLeastOneNotNullValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAtLeastOneNotNull {
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] fieldNames();
}
