/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.module.validation;

import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateValidator implements ConstraintValidator<DateConstraint, String> {

    private Boolean isOptional;
    private String pattern;

    @Override
    public void initialize(DateConstraint validDate) {
        this.isOptional = validDate.optional();
        this.pattern = validDate.pattern();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        boolean validDate = isValidFormat(this.pattern, value);
        return isOptional || validDate;
    }

    private static boolean isValidFormat(String format, String value) {
        try {
            if (StringUtils.isBlank(value) || StringUtils.isBlank(format)) {
                return true;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(value);
            return value.equals(sdf.format(date));
        } catch (ParseException ex) {
            return false;
        }
    }
}
