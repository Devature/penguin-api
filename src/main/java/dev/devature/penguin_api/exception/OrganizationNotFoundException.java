package dev.devature.penguin_api.exception;

public class OrganizationNotFoundException extends Exception {
    public OrganizationNotFoundException() {
        super();
    }

    public OrganizationNotFoundException(String message) {
        super(message);
    }
}
