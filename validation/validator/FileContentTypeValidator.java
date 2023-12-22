package com.lmig.ci.grs.salesforce.file_connector.validation.validator;

import java.util.Arrays;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

import com.lmig.ci.grs.salesforce.file_connector.validation.annotation.FileContentType;

import static java.util.stream.Collectors.toSet;

/**
 * Validator class for {@link FileContentType}.
 */
public class FileContentTypeValidator implements ConstraintValidator<FileContentType, MultipartFile> {

    private Set<String> validContentTypes;

    @Override
    public void initialize(FileContentType constraint) {
        validContentTypes = Arrays.stream(constraint.contentType())
                .collect(toSet());
    }

    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        return file == null || validContentTypes.contains(file.getContentType());
    }
}
