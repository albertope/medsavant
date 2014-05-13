/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ut.biolab.medsavant.component.field.validator;

import org.ut.biolab.medsavant.component.field.editable.EditableFieldValidator;

/**
 *
 * @author mfiume
 */
public class EmailValidator extends EditableFieldValidator<String> {

    @Override
    public boolean validate(String value) {
        return org.apache.commons.validator.EmailValidator.getInstance().isValid(value);
    }

    @Override
    public String getDescriptionOfValidValue() {
        return "Invalid Email Address";
    }

}
