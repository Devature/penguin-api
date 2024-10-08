package dev.devature.penguin_api.service;

import dev.devature.penguin_api.utils.EmailPasswordValidationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class EmailPasswordValidationUtilsTest {
    @Test
    public void emailRequirementTest(){
        String[] email = {"normal@example.com", "first.last@example.com", "user+tag@example.com",
                "\"quoted@local\"@example.com", "special!#$%&'*+-/=?^_`{|}~@example.com", "plainaddress@.com",
                "user@com", "user..name@example.com", "user@-example.com", "user@exa(mple).com"};
        boolean[] expectedResult = {true, true, true, true, true, false, false, false, false, false};
        boolean[] actualResult = new boolean[10];

        for(int i = 0; i < email.length; ++i){
            actualResult[i] = EmailPasswordValidationUtils.isValidEmail(email[i]);
        }

        Assertions.assertArrayEquals(expectedResult, actualResult,
                "Expected Boolean True, True, True, True, True, False, False, False, False, False - " +
                        "Actual Boolean was: " + Arrays.toString(actualResult));
    }


    @Test
    public void passwordRequirementTest() {
        // Given
        String[] password = {"1234", "Test_Password12", "TestPassword12", "Test_Password"};
        boolean[] expectedResult = {false, true, false, false};
        boolean[] actualResult = new boolean[4];

        // When
        for(int i = 0; i < password.length; ++i){
            actualResult[i] = EmailPasswordValidationUtils.isValidPassword(password[i]);
        }

        // Then
        Assertions.assertArrayEquals(expectedResult, actualResult,
                "Expected Boolean False, True, False, False - Actual Boolean was: " +
                        Arrays.toString(actualResult));
    }
}