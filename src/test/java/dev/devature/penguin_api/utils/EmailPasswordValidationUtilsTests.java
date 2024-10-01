package dev.devature.penguin_api.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmailPasswordValidationUtilsTests {

    @Test
    public void emailRequirementTest(){
        String[] email = {"normal@example.com", "first.last@example.com", "user+tag@example.com",
                "\"quoted@local\"@example.com", "special!#$%&'*+-/=?^_{|}~@example.com", "plainaddress@.com",
                "user@com", "user..name@example.com", "user@-example.com", "user@exa(mple).com"};
        boolean[] expectedResult = {true, true, true, true, true, false, false, false, false, false};

        for (int i = 0; i < email.length; ++i) {
            assertEquals(expectedResult[i], EmailPasswordValidationUtils.isValidEmail(email[i]));
        }
    }

    @Test
    public void passwordRequirementTest() {
        String[] password = {"1234", "Test_Password12", "TestPassword12", "Test_Password"};
        boolean[] expectedResult = {false, true, false, false};

        for (int i = 0; i < password.length; ++i) {
            assertEquals(expectedResult[i], EmailPasswordValidationUtils.isValidPassword(password[i]));
        }
    }
}
