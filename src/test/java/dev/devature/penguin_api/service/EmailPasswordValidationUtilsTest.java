package dev.devature.penguin_api.service;

import dev.devature.penguin_api.utils.EmailPasswordValidationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class EmailPasswordValidationUtilsTest {
    @ParameterizedTest
    @ValueSource(strings = { "normal@example.com", "first.last@example.com", "user+tag@example.com",
            "\"quoted@local\"@example.com", "special!#$%&'*+-/=?^_`{|}~@example.com" })
    public void validEmailRequirementTest(String email) {
        Assertions.assertTrue(EmailPasswordValidationUtils.isValidEmail(email),
                String.format("isValidEmail function should return true for valid email %s.", email));
    }

    @ParameterizedTest
    @ValueSource(strings = { "plainaddress@.com", "user@com", "user..name@example.com", "user@-example.com",
            "user@exa(mple).com" })
    public void invalidEmailRequirementTest(String email) {
        Assertions.assertFalse(EmailPasswordValidationUtils.isValidEmail(email),
                String.format("isValidEmail function should return false for invalid email %s.", email));
    }

    @ParameterizedTest
    @ValueSource(strings = { "Test_Password12" })
    public void validPasswordRequirementTest(String password) {
        Assertions.assertTrue(EmailPasswordValidationUtils.isValidPassword(password),
                String.format("isValidPassword should return true for valid password %s.", password));
    }

    @ParameterizedTest
    @ValueSource(strings = { "1234", "TestPassword12", "Test_Password" })
    public void invalidPasswordRequirementTest(String password) {
        Assertions.assertFalse(EmailPasswordValidationUtils.isValidPassword(password),
                String.format("isValidPassword should return false for invalid password %s.", password));
    }
}