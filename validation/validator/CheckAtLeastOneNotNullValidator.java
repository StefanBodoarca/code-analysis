package com.lmig.ci.grs.salesforce.file_connector.validation.validator;

import com.lmig.ci.grs.salesforce.file_connector.validation.annotation.CheckAtLeastOneNotNull;
import org.apache.commons.beanutils.PropertyUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator which checks for at least one null element in an Object.
 * Validator for {@link CheckAtLeastOneNotNull}
 */
public class CheckAtLeastOneNotNullValidator implements ConstraintValidator<CheckAtLeastOneNotNull, Object> {

    private String[] fieldNames;

    @Override
    public void initialize(CheckAtLeastOneNotNull constraintAnnotation) {
        this.fieldNames = constraintAnnotation.fieldNames();
    }

    public boolean isValid(Object object, ConstraintValidatorContext constraintContext) {

        if (object == null) {
            return true;
        }
        try {
            for (String fieldName : fieldNames) {
                final Object property = PropertyUtils.getProperty(object, fieldName);

                if (property != null) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
