package dev.devature.penguin_api.service;

import dev.devature.penguin_api.utils.EmailPasswordValidationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

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

    @Test
    public void passwordRequirementTest() {
        // Given
        String[] password = { "1234", "Test_Password12", "TestPassword12", "Test_Password" };
        boolean[] expectedResult = { false, true, false, false };
        boolean[] actualResult = new boolean[4];

        // When
        for (int i = 0; i < password.length; ++i) {
            actualResult[i] = EmailPasswordValidationUtils.isValidPassword(password[i]);
        }

        // Then
        Assertions.assertArrayEquals(expectedResult, actualResult,
                "Expected Boolean False, True, False, False - Actual Boolean was: " +
                        Arrays.toString(actualResult));
    }
}