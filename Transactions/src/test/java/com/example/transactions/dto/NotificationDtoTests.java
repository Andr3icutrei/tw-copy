package com.example.transactions.dto;

import com.example.transactions.dto.response.NotificationDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationDtoTests {

    @Test
    void testNotificationDtoDefaultConstructor() {
        // Act
        NotificationDto dto = new NotificationDto();

        // Assert
        assertThat(dto).isNotNull();
    }

    @Test
    void testNotificationDtoParameterizedConstructor() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();

        // Act
        NotificationDto dto = new NotificationDto(
                1L,
                "NOTIF-123",
                100L,
                "user@example.com",
                "+40123456789",
                "EMAIL",
                "TRANSACTION_CREATED",
                "Transaction Notification",
                "Your transaction has been created",
                "SENT",
                "HIGH",
                now
        );

        // Assert
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNotificationId()).isEqualTo("NOTIF-123");
        assertThat(dto.getRecipientId()).isEqualTo(100L);
        assertThat(dto.getRecipientEmail()).isEqualTo("user@example.com");
        assertThat(dto.getRecipientPhone()).isEqualTo("+40123456789");
        assertThat(dto.getNotificationType()).isEqualTo("EMAIL");
        assertThat(dto.getTriggerEvent()).isEqualTo("TRANSACTION_CREATED");
        assertThat(dto.getSubject()).isEqualTo("Transaction Notification");
        assertThat(dto.getMessage()).isEqualTo("Your transaction has been created");
        assertThat(dto.getStatus()).isEqualTo("SENT");
        assertThat(dto.getPriority()).isEqualTo("HIGH");
        assertThat(dto.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void testNotificationDtoSetters() {
        // Arrange
        NotificationDto dto = new NotificationDto();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime scheduled = now.plusHours(1);
        LocalDateTime sent = now.plusHours(2);
        LocalDateTime delivered = now.plusHours(3);
        LocalDateTime failed = now.plusHours(4);

        // Act
        dto.setId(2L);
        dto.setNotificationId("NOTIF-456");
        dto.setRecipientId(200L);
        dto.setRecipientEmail("test@example.com");
        dto.setRecipientPhone("+40987654321");
        dto.setNotificationType("SMS");
        dto.setTriggerEvent("PAYMENT_COMPLETED");
        dto.setSubject("Payment Notification");
        dto.setMessage("Your payment has been processed");
        dto.setTemplateId("template-123");
        dto.setStatus("DELIVERED");
        dto.setPriority("MEDIUM");
        dto.setCreatedAt(now);
        dto.setScheduledAt(scheduled);
        dto.setSentAt(sent);
        dto.setDeliveredAt(delivered);
        dto.setFailedAt(failed);
        dto.setFailureReason("Network error");
        dto.setRetryCount(3);
        dto.setMaxRetries(5);
        dto.setRelatedAccountId(1000L);
        dto.setRelatedTransactionId(2000L);

        // Assert
        assertThat(dto.getId()).isEqualTo(2L);
        assertThat(dto.getNotificationId()).isEqualTo("NOTIF-456");
        assertThat(dto.getRecipientId()).isEqualTo(200L);
        assertThat(dto.getRecipientEmail()).isEqualTo("test@example.com");
        assertThat(dto.getRecipientPhone()).isEqualTo("+40987654321");
        assertThat(dto.getNotificationType()).isEqualTo("SMS");
        assertThat(dto.getTriggerEvent()).isEqualTo("PAYMENT_COMPLETED");
        assertThat(dto.getSubject()).isEqualTo("Payment Notification");
        assertThat(dto.getMessage()).isEqualTo("Your payment has been processed");
        assertThat(dto.getTemplateId()).isEqualTo("template-123");
        assertThat(dto.getStatus()).isEqualTo("DELIVERED");
        assertThat(dto.getPriority()).isEqualTo("MEDIUM");
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getScheduledAt()).isEqualTo(scheduled);
        assertThat(dto.getSentAt()).isEqualTo(sent);
        assertThat(dto.getDeliveredAt()).isEqualTo(delivered);
        assertThat(dto.getFailedAt()).isEqualTo(failed);
        assertThat(dto.getFailureReason()).isEqualTo("Network error");
        assertThat(dto.getRetryCount()).isEqualTo(3);
        assertThat(dto.getMaxRetries()).isEqualTo(5);
        assertThat(dto.getRelatedAccountId()).isEqualTo(1000L);
        assertThat(dto.getRelatedTransactionId()).isEqualTo(2000L);
    }

    @Test
    void testNotificationDtoWithEmailType() {
        // Arrange & Act
        NotificationDto dto = new NotificationDto();
        dto.setNotificationType("EMAIL");
        dto.setRecipientEmail("user@example.com");

        // Assert
        assertThat(dto.getNotificationType()).isEqualTo("EMAIL");
        assertThat(dto.getRecipientEmail()).isNotNull();
    }

    @Test
    void testNotificationDtoWithSMSType() {
        // Arrange & Act
        NotificationDto dto = new NotificationDto();
        dto.setNotificationType("SMS");
        dto.setRecipientPhone("+40123456789");

        // Assert
        assertThat(dto.getNotificationType()).isEqualTo("SMS");
        assertThat(dto.getRecipientPhone()).isNotNull();
    }

    @Test
    void testNotificationDtoWithPushType() {
        // Arrange & Act
        NotificationDto dto = new NotificationDto();
        dto.setNotificationType("PUSH");

        // Assert
        assertThat(dto.getNotificationType()).isEqualTo("PUSH");
    }

    @Test
    void testNotificationDtoWithPendingStatus() {
        // Arrange & Act
        NotificationDto dto = new NotificationDto();
        dto.setStatus("PENDING");

        // Assert
        assertThat(dto.getStatus()).isEqualTo("PENDING");
    }

    @Test
    void testNotificationDtoWithSentStatus() {
        // Arrange & Act
        NotificationDto dto = new NotificationDto();
        dto.setStatus("SENT");

        // Assert
        assertThat(dto.getStatus()).isEqualTo("SENT");
    }

    @Test
    void testNotificationDtoWithDeliveredStatus() {
        // Arrange & Act
        NotificationDto dto = new NotificationDto();
        dto.setStatus("DELIVERED");

        // Assert
        assertThat(dto.getStatus()).isEqualTo("DELIVERED");
    }

    @Test
    void testNotificationDtoWithFailedStatus() {
        // Arrange & Act
        NotificationDto dto = new NotificationDto();
        dto.setStatus("FAILED");
        dto.setFailureReason("Connection timeout");

        // Assert
        assertThat(dto.getStatus()).isEqualTo("FAILED");
        assertThat(dto.getFailureReason()).isEqualTo("Connection timeout");
    }

    @Test
    void testNotificationDtoWithHighPriority() {
        // Arrange & Act
        NotificationDto dto = new NotificationDto();
        dto.setPriority("HIGH");

        // Assert
        assertThat(dto.getPriority()).isEqualTo("HIGH");
    }

    @Test
    void testNotificationDtoWithMediumPriority() {
        // Arrange & Act
        NotificationDto dto = new NotificationDto();
        dto.setPriority("MEDIUM");

        // Assert
        assertThat(dto.getPriority()).isEqualTo("MEDIUM");
    }

    @Test
    void testNotificationDtoWithLowPriority() {
        // Arrange & Act
        NotificationDto dto = new NotificationDto();
        dto.setPriority("LOW");

        // Assert
        assertThat(dto.getPriority()).isEqualTo("LOW");
    }

    @Test
    void testNotificationDtoWithRetryCount() {
        // Arrange & Act
        NotificationDto dto = new NotificationDto();
        dto.setRetryCount(2);
        dto.setMaxRetries(5);

        // Assert
        assertThat(dto.getRetryCount()).isEqualTo(2);
        assertThat(dto.getMaxRetries()).isEqualTo(5);
    }

    @Test
    void testNotificationDtoWithTemplate() {
        // Arrange & Act
        NotificationDto dto = new NotificationDto();
        dto.setTemplateId("welcome-email-v1");

        // Assert
        assertThat(dto.getTemplateId()).isEqualTo("welcome-email-v1");
    }

    @Test
    void testNotificationDtoWithRelatedIds() {
        // Arrange & Act
        NotificationDto dto = new NotificationDto();
        dto.setRelatedAccountId(12345L);
        dto.setRelatedTransactionId(67890L);

        // Assert
        assertThat(dto.getRelatedAccountId()).isEqualTo(12345L);
        assertThat(dto.getRelatedTransactionId()).isEqualTo(67890L);
    }

    @Test
    void testNotificationDtoWithScheduledTime() {
        // Arrange
        LocalDateTime scheduled = LocalDateTime.of(2026, 2, 1, 9, 0);
        NotificationDto dto = new NotificationDto();

        // Act
        dto.setScheduledAt(scheduled);

        // Assert
        assertThat(dto.getScheduledAt()).isEqualTo(scheduled);
    }

    @Test
    void testNotificationDtoTimestamps() {
        // Arrange
        LocalDateTime created = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime scheduled = LocalDateTime.of(2026, 1, 1, 11, 0);
        LocalDateTime sent = LocalDateTime.of(2026, 1, 1, 11, 5);
        LocalDateTime delivered = LocalDateTime.of(2026, 1, 1, 11, 6);

        NotificationDto dto = new NotificationDto();

        // Act
        dto.setCreatedAt(created);
        dto.setScheduledAt(scheduled);
        dto.setSentAt(sent);
        dto.setDeliveredAt(delivered);

        // Assert
        assertThat(dto.getCreatedAt()).isEqualTo(created);
        assertThat(dto.getScheduledAt()).isEqualTo(scheduled);
        assertThat(dto.getSentAt()).isEqualTo(sent);
        assertThat(dto.getDeliveredAt()).isEqualTo(delivered);
        assertThat(dto.getScheduledAt()).isAfter(dto.getCreatedAt());
        assertThat(dto.getSentAt()).isAfter(dto.getScheduledAt());
        assertThat(dto.getDeliveredAt()).isAfter(dto.getSentAt());
    }

    @Test
    void testNotificationDtoWithMaxRetries() {
        // Arrange & Act
        NotificationDto dto = new NotificationDto();
        dto.setRetryCount(5);
        dto.setMaxRetries(5);
        dto.setStatus("FAILED");

        // Assert
        assertThat(dto.getRetryCount()).isEqualTo(dto.getMaxRetries());
        assertThat(dto.getStatus()).isEqualTo("FAILED");
    }

    @Test
    void testNotificationDtoTriggerEvents() {
        // Test various trigger events
        NotificationDto dto1 = new NotificationDto();
        dto1.setTriggerEvent("TRANSACTION_CREATED");
        assertThat(dto1.getTriggerEvent()).isEqualTo("TRANSACTION_CREATED");

        NotificationDto dto2 = new NotificationDto();
        dto2.setTriggerEvent("PAYMENT_COMPLETED");
        assertThat(dto2.getTriggerEvent()).isEqualTo("PAYMENT_COMPLETED");

        NotificationDto dto3 = new NotificationDto();
        dto3.setTriggerEvent("FRAUD_ALERT");
        assertThat(dto3.getTriggerEvent()).isEqualTo("FRAUD_ALERT");

        NotificationDto dto4 = new NotificationDto();
        dto4.setTriggerEvent("ACCOUNT_BALANCE_LOW");
        assertThat(dto4.getTriggerEvent()).isEqualTo("ACCOUNT_BALANCE_LOW");
    }
}

