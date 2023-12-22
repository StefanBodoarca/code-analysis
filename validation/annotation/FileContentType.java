package com.lmig.ci.grs.salesforce.file_connector.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.springframework.web.multipart.MultipartFile;

import com.lmig.ci.grs.salesforce.file_connector.validation.validator.FileContentTypeValidator;


/**
 * Validator annotation for file content type for a {@link MultipartFile}.
 */
@Documented
@Constraint(validatedBy = FileContentTypeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileContentType {

    String message() default "Invalid file content type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] contentType();
}
