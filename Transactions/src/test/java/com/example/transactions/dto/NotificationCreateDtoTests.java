package com.example.transactions.dto;

import com.example.transactions.dto.request.NotificationCreateDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationCreateDtoTests {

    @Test
    void testNotificationCreateDtoDefaultConstructor() {
        // Act
        NotificationCreateDto dto = new NotificationCreateDto();

        // Assert
        assertThat(dto).isNotNull();
    }

    @Test
    void testNotificationCreateDtoParameterizedConstructor() {
        // Arrange & Act
        NotificationCreateDto dto = new NotificationCreateDto(
                1L,
                "user@example.com",
                "+40123456789",
                "EMAIL",
                "TRANSACTION_CREATED",
                "Transaction Notification",
                "Your transaction has been created",
                "HIGH"
        );

        // Assert
        assertThat(dto.getRecipientId()).isEqualTo(1L);
        assertThat(dto.getRecipientEmail()).isEqualTo("user@example.com");
        assertThat(dto.getRecipientPhone()).isEqualTo("+40123456789");
        assertThat(dto.getNotificationType()).isEqualTo("EMAIL");
        assertThat(dto.getTriggerEvent()).isEqualTo("TRANSACTION_CREATED");
        assertThat(dto.getSubject()).isEqualTo("Transaction Notification");
        assertThat(dto.getMessage()).isEqualTo("Your transaction has been created");
        assertThat(dto.getPriority()).isEqualTo("HIGH");
    }

    @Test
    void testNotificationCreateDtoSetters() {
        // Arrange
        NotificationCreateDto dto = new NotificationCreateDto();

        // Act
        dto.setRecipientId(2L);
        dto.setRecipientEmail("test@example.com");
        dto.setRecipientPhone("+40987654321");
        dto.setNotificationType("SMS");
        dto.setTriggerEvent("PAYMENT_COMPLETED");
        dto.setSubject("Payment Notification");
        dto.setMessage("Your payment has been processed");
        dto.setTemplateId("template-123");
        dto.setPriority("MEDIUM");
        dto.setScheduledAt(LocalDateTime.of(2026, 1, 15, 10, 0));
        dto.setRelatedAccountId(100L);
        dto.setRelatedTransactionId(200L);

        // Assert
        assertThat(dto.getRecipientId()).isEqualTo(2L);
        assertThat(dto.getRecipientEmail()).isEqualTo("test@example.com");
        assertThat(dto.getRecipientPhone()).isEqualTo("+40987654321");
        assertThat(dto.getNotificationType()).isEqualTo("SMS");
        assertThat(dto.getTriggerEvent()).isEqualTo("PAYMENT_COMPLETED");
        assertThat(dto.getSubject()).isEqualTo("Payment Notification");
        assertThat(dto.getMessage()).isEqualTo("Your payment has been processed");
        assertThat(dto.getTemplateId()).isEqualTo("template-123");
        assertThat(dto.getPriority()).isEqualTo("MEDIUM");
        assertThat(dto.getScheduledAt()).isEqualTo(LocalDateTime.of(2026, 1, 15, 10, 0));
        assertThat(dto.getRelatedAccountId()).isEqualTo(100L);
        assertThat(dto.getRelatedTransactionId()).isEqualTo(200L);
    }

    @Test
    void testNotificationCreateDtoWithEmailType() {
        // Arrange & Act
        NotificationCreateDto dto = new NotificationCreateDto(
                1L,
                "user@example.com",
                null,
                "EMAIL",
                "TRANSACTION_CREATED",
                "Email Subject",
                "Email Message",
                "HIGH"
        );

        // Assert
        assertThat(dto.getNotificationType()).isEqualTo("EMAIL");
        assertThat(dto.getRecipientEmail()).isNotNull();
        assertThat(dto.getRecipientPhone()).isNull();
    }

    @Test
    void testNotificationCreateDtoWithSMSType() {
        // Arrange & Act
        NotificationCreateDto dto = new NotificationCreateDto(
                1L,
                null,
                "+40123456789",
                "SMS",
                "TRANSACTION_CREATED",
                "SMS Subject",
                "SMS Message",
                "HIGH"
        );

        // Assert
        assertThat(dto.getNotificationType()).isEqualTo("SMS");
        assertThat(dto.getRecipientEmail()).isNull();
        assertThat(dto.getRecipientPhone()).isNotNull();
    }

    @Test
    void testNotificationCreateDtoWithPushType() {
        // Arrange & Act
        NotificationCreateDto dto = new NotificationCreateDto(
                1L,
                "user@example.com",
                "+40123456789",
                "PUSH",
                "TRANSACTION_CREATED",
                "Push Notification",
                "Push Message",
                "LOW"
        );

        // Assert
        assertThat(dto.getNotificationType()).isEqualTo("PUSH");
    }

    @Test
    void testNotificationCreateDtoWithHighPriority() {
        // Arrange & Act
        NotificationCreateDto dto = new NotificationCreateDto(
                1L,
                "user@example.com",
                "+40123456789",
                "EMAIL",
                "FRAUD_ALERT",
                "Fraud Alert",
                "Suspicious activity detected",
                "HIGH"
        );

        // Assert
        assertThat(dto.getPriority()).isEqualTo("HIGH");
        assertThat(dto.getTriggerEvent()).isEqualTo("FRAUD_ALERT");
    }

    @Test
    void testNotificationCreateDtoWithMediumPriority() {
        // Arrange & Act
        NotificationCreateDto dto = new NotificationCreateDto(
                1L,
                "user@example.com",
                "+40123456789",
                "EMAIL",
                "TRANSACTION_COMPLETED",
                "Transaction Completed",
                "Your transaction was successful",
                "MEDIUM"
        );

        // Assert
        assertThat(dto.getPriority()).isEqualTo("MEDIUM");
    }

    @Test
    void testNotificationCreateDtoWithLowPriority() {
        // Arrange & Act
        NotificationCreateDto dto = new NotificationCreateDto(
                1L,
                "user@example.com",
                "+40123456789",
                "EMAIL",
                "MONTHLY_STATEMENT",
                "Monthly Statement",
                "Your monthly statement is ready",
                "LOW"
        );

        // Assert
        assertThat(dto.getPriority()).isEqualTo("LOW");
    }

    @Test
    void testNotificationCreateDtoWithScheduledTime() {
        // Arrange
        NotificationCreateDto dto = new NotificationCreateDto();
        LocalDateTime scheduledTime = LocalDateTime.of(2026, 2, 1, 9, 0);

        // Act
        dto.setScheduledAt(scheduledTime);

        // Assert
        assertThat(dto.getScheduledAt()).isEqualTo(scheduledTime);
    }

    @Test
    void testNotificationCreateDtoWithRelatedIds() {
        // Arrange
        NotificationCreateDto dto = new NotificationCreateDto();

        // Act
        dto.setRelatedAccountId(12345L);
        dto.setRelatedTransactionId(67890L);

        // Assert
        assertThat(dto.getRelatedAccountId()).isEqualTo(12345L);
        assertThat(dto.getRelatedTransactionId()).isEqualTo(67890L);
    }

    @Test
    void testNotificationCreateDtoWithTemplate() {
        // Arrange
        NotificationCreateDto dto = new NotificationCreateDto();

        // Act
        dto.setTemplateId("transaction-template-v1");

        // Assert
        assertThat(dto.getTemplateId()).isEqualTo("transaction-template-v1");
    }
}

