package com.kemal.spring.customAnnotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Keno&Kemo on 08.12.2017..
 */
public class RoleNameValidator implements ConstraintValidator<ValidRoleName, String> {
    private static final String VALID_ROLE_NAME_PREFIX = "ROLE_";

    @Override
    public void initialize(ValidRoleName validRoleName) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.length() >= 5 && s.substring(0, 5).equals(VALID_ROLE_NAME_PREFIX);
    }

}
