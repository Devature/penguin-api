package dev.devature.penguin_api.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class EmailService {

    private final String apiToken;
    private final String apiURL;

    public EmailService(@Value("${mailing.token}") String apiToken, @Value("${mailing.host}") String apiURL){
        this.apiToken = apiToken;
        this.apiURL = apiURL;
    }

    public void sendEmail(String toEmail, String subject, String text) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");

        String jsonBody = String.format("""
            {
                "from": {
                    "email": "hello@devature.dev",
                    "name": "Devature Team"
                },
                "to": [{
                    "email": "%s"
                }],
                "subject": "%s",
                "text": "%s",
                "category": "Production"
            }
            """, toEmail, subject, text);

        RequestBody body = RequestBody.create(jsonBody, mediaType);

        Request request = new Request.Builder()
                .url(apiURL)
                .method("POST", body)
                .addHeader("Authorization", apiToken)
                .addHeader("Content-Type", "application/json")
                .build();

        try(Response response = client.newCall(request).execute()){
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error body";
                throw new RuntimeException("Failed to send email. Status: " +
                        response.code() + ", Error: " + errorBody);
            }
        }
    }
}