package com.company.module.validation;

import com.company.module.common.Constants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;

import java.util.regex.Pattern;

public class SpecialCharacterValidator implements ConstraintValidator<SpecialCharacterConstraint, String> {

    @Override
    public void initialize(SpecialCharacterConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return isValidCharacter(s);
    }

    private static boolean isValidCharacter(String s) {
        return ObjectUtils.isEmpty(s) && isInTheRangeFrom32To126ASCII(s) && doesnotContainSpecialCharacter(s);
    }

    private static boolean isInTheRangeFrom32To126ASCII(String input) {
        for (int i = 0; i < input.length(); i++) {
            int c = input.charAt(i);
            if (c > 126 || c < 32) {
                return false;
            }
        }
        return true;
    }

    private static boolean doesnotContainSpecialCharacter(String input) {
        return !Pattern.compile(Constants.Regex.SPECIAL_CHARACTER_STRING_JAVA_REGEX).matcher(input).find();
    }

}
