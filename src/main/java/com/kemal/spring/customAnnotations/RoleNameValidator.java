package com.kemal.spring.customAnnotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Keno&Kemo on 08.12.2017..
 */
public class RoleNameValidator implements ConstraintValidator<ValidRoleName, String> {
    String validRoleNamePrefix = "ROLE_";

    @Override
    public void initialize(ValidRoleName validRoleName) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.length() >= 5 && s.substring(0, 5).equals(validRoleNamePrefix);
    }

}
