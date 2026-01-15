package com.example.transactions.dto;

import com.example.transactions.dto.response.TransactionDto;
import com.example.transactions.enums.Currency;
import com.example.transactions.enums.TransactionStatus;
import com.example.transactions.enums.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionDtoTests {

    @Test
    void testTransactionDtoConstructor() {
        // Arrange & Act
        TransactionDto dto = new TransactionDto(
                "acc-002",
                "TRX123",
                "acc-001",
                "1234567890",
                "0987654321",
                TransactionType.TRANSFER,
                new BigDecimal("100.00"),
                Currency.RON,
                "Test transaction",
                TransactionStatus.PENDING,
                null
        );

        // Assert
        assertThat(dto.getToAccountId()).isEqualTo("acc-002");
        assertThat(dto.getTransactionId()).isEqualTo("TRX123");
        assertThat(dto.getFromAccountId()).isEqualTo("acc-001");
        assertThat(dto.getFromAccountNumber()).isEqualTo("1234567890");
        assertThat(dto.getToAccountNumber()).isEqualTo("0987654321");
        assertThat(dto.getTransactionType()).isEqualTo(TransactionType.TRANSFER);
        assertThat(dto.getAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(dto.getCurrency()).isEqualTo(Currency.RON);
        assertThat(dto.getDescription()).isEqualTo("Test transaction");
        assertThat(dto.getStatus()).isEqualTo(TransactionStatus.PENDING);
        assertThat(dto.getFailureReason()).isNull();
    }

    @Test
    void testTransactionDtoSetters() {
        // Arrange
        TransactionDto dto = new TransactionDto(
                "acc-002",
                "TRX123",
                "acc-001",
                "1234567890",
                "0987654321",
                TransactionType.TRANSFER,
                new BigDecimal("100.00"),
                Currency.RON,
                "Test",
                TransactionStatus.PENDING,
                null
        );

        // Act
        dto.setToAccountId("acc-003");
        dto.setTransactionId("TRX456");
        dto.setFromAccountId("acc-004");
        dto.setToAccountNumber("2222222222");
        dto.setTransactionType(TransactionType.PAYMENT);
        dto.setAmount(new BigDecimal("200.00"));
        dto.setCurrency(Currency.EUR);
        dto.setDescription("Updated description");
        dto.setStatus(TransactionStatus.COMPLETED);
        dto.setFailureReason("Test failure");

        // Assert
        assertThat(dto.getToAccountId()).isEqualTo("acc-003");
        assertThat(dto.getTransactionId()).isEqualTo("TRX456");
        assertThat(dto.getFromAccountId()).isEqualTo("acc-004");
        assertThat(dto.getToAccountNumber()).isEqualTo("2222222222");
        assertThat(dto.getTransactionType()).isEqualTo(TransactionType.PAYMENT);
        assertThat(dto.getAmount()).isEqualByComparingTo(new BigDecimal("200.00"));
        assertThat(dto.getCurrency()).isEqualTo(Currency.EUR);
        assertThat(dto.getDescription()).isEqualTo("Updated description");
        assertThat(dto.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
        assertThat(dto.getFailureReason()).isEqualTo("Test failure");
    }

    @Test
    void testTransactionDtoWithFailureReason() {
        // Arrange & Act
        TransactionDto dto = new TransactionDto(
                "acc-002",
                "TRX123",
                "acc-001",
                "1234567890",
                "0987654321",
                TransactionType.TRANSFER,
                new BigDecimal("100.00"),
                Currency.RON,
                "Failed transaction",
                TransactionStatus.FAILED,
                "Insufficient funds"
        );

        // Assert
        assertThat(dto.getStatus()).isEqualTo(TransactionStatus.FAILED);
        assertThat(dto.getFailureReason()).isEqualTo("Insufficient funds");
    }

    @Test
    void testTransactionDtoWithCompletedStatus() {
        // Arrange & Act
        TransactionDto dto = new TransactionDto(
                "acc-002",
                "TRX123",
                "acc-001",
                "1234567890",
                "0987654321",
                TransactionType.DEPOSIT,
                new BigDecimal("500.00"),
                Currency.USD,
                "Completed deposit",
                TransactionStatus.COMPLETED,
                null
        );

        // Assert
        assertThat(dto.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
        assertThat(dto.getTransactionType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(dto.getCurrency()).isEqualTo(Currency.USD);
    }

    @Test
    void testTransactionDtoWithCancelledStatus() {
        // Arrange & Act
        TransactionDto dto = new TransactionDto(
                "acc-002",
                "TRX123",
                "acc-001",
                "1234567890",
                "0987654321",
                TransactionType.WITHDRAWAL,
                new BigDecimal("300.00"),
                Currency.EUR,
                "Cancelled withdrawal",
                TransactionStatus.CANCELLED,
                "User cancelled"
        );

        // Assert
        assertThat(dto.getStatus()).isEqualTo(TransactionStatus.CANCELLED);
        assertThat(dto.getTransactionType()).isEqualTo(TransactionType.WITHDRAWAL);
        assertThat(dto.getFailureReason()).isEqualTo("User cancelled");
    }

    @Test
    void testTransactionDtoWithDifferentAmounts() {
        // Small amount
        TransactionDto smallDto = new TransactionDto(
                "acc-002",
                "TRX123",
                "acc-001",
                "1234567890",
                "0987654321",
                TransactionType.TRANSFER,
                new BigDecimal("0.01"),
                Currency.RON,
                "Small amount",
                TransactionStatus.PENDING,
                null
        );
        assertThat(smallDto.getAmount()).isEqualByComparingTo(new BigDecimal("0.01"));

        // Large amount
        TransactionDto largeDto = new TransactionDto(
                "acc-002",
                "TRX456",
                "acc-001",
                "1234567890",
                "0987654321",
                TransactionType.TRANSFER,
                new BigDecimal("99999999.99"),
                Currency.RON,
                "Large amount",
                TransactionStatus.PENDING,
                null
        );
        assertThat(largeDto.getAmount()).isEqualByComparingTo(new BigDecimal("99999999.99"));
    }
}

