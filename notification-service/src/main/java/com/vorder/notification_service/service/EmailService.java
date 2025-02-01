package com.vorder.notification_service.service;

import com.vorder.notification_service.dto.request.EmailRequest;
import com.vorder.notification_service.dto.request.SendEmailRequest;
import com.vorder.notification_service.dto.request.Sender;
import com.vorder.notification_service.dto.response.EmailResponse;
import com.vorder.notification_service.exception.AppException;
import com.vorder.notification_service.exception.ErrorCode;
import com.vorder.notification_service.repository.httpclient.EmailClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    EmailClient emailClient;

    public EmailService(EmailClient emailClient) {
        this.emailClient = emailClient;
        this.apiKey = System.getenv("API_KEY"); // Get API_KEY from environment
    }

//    @Value("${notification.email.brevo-apikey}")
    @NonFinal
    String apiKey;

    public EmailResponse sendEmail(SendEmailRequest request) {
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()
                        .name("Vorder")
                        .email("destielisdestiny1111@gmail.com")
                        .build())
                .to(List.of(request.getTo()))
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .build();
        try {
            return emailClient.sendEmail(apiKey, emailRequest);
        } catch (FeignException e){
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}
