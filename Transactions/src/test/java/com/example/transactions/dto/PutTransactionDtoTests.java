package com.example.transactions.dto;

import com.example.transactions.dto.request.PutTransactionDto;
import com.example.transactions.enums.Currency;
import com.example.transactions.enums.TransactionStatus;
import com.example.transactions.enums.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PutTransactionDtoTests {

    @Test
    void testPutTransactionDtoConstructor() {
        // Arrange & Act
        PutTransactionDto dto = new PutTransactionDto(
                TransactionType.PAYMENT,
                "acc-001",
                "acc-002",
                "1234567890",
                "0987654321",
                new BigDecimal("150.00"),
                Currency.EUR,
                "Updated transaction",
                TransactionStatus.PENDING
        );

        // Assert
        assertThat(dto.getTransactionType()).isEqualTo(TransactionType.PAYMENT);
        assertThat(dto.getFromAccountId()).isEqualTo("acc-001");
        assertThat(dto.getToAccountId()).isEqualTo("acc-002");
        assertThat(dto.getFromAccountNumber()).isEqualTo("1234567890");
        assertThat(dto.getToAccountNumber()).isEqualTo("0987654321");
        assertThat(dto.getAmount()).isEqualByComparingTo(new BigDecimal("150.00"));
        assertThat(dto.getCurrency()).isEqualTo(Currency.EUR);
        assertThat(dto.getDescription()).isEqualTo("Updated transaction");
        assertThat(dto.getStatus()).isEqualTo(TransactionStatus.PENDING);
    }

    @Test
    void testPutTransactionDtoSetters() {
        // Arrange
        PutTransactionDto dto = new PutTransactionDto(
                TransactionType.TRANSFER,
                "acc-001",
                "acc-002",
                "1234567890",
                "0987654321",
                new BigDecimal("100.00"),
                Currency.RON,
                "Test",
                TransactionStatus.PENDING
        );

        // Act
        dto.setTransactionType(TransactionType.WITHDRAWAL);
        dto.setFromAccountId("acc-003");
        dto.setToAccountId("acc-004");
        dto.setFromAccountNumber("1111111111");
        dto.setToAccountNumber("2222222222");
        dto.setAmount(new BigDecimal("250.00"));
        dto.setCurrency(Currency.USD);
        dto.setDescription("Modified description");
        dto.setStatus(TransactionStatus.COMPLETED);

        // Assert
        assertThat(dto.getTransactionType()).isEqualTo(TransactionType.WITHDRAWAL);
        assertThat(dto.getFromAccountId()).isEqualTo("acc-003");
        assertThat(dto.getToAccountId()).isEqualTo("acc-004");
        assertThat(dto.getFromAccountNumber()).isEqualTo("1111111111");
        assertThat(dto.getToAccountNumber()).isEqualTo("2222222222");
        assertThat(dto.getAmount()).isEqualByComparingTo(new BigDecimal("250.00"));
        assertThat(dto.getCurrency()).isEqualTo(Currency.USD);
        assertThat(dto.getDescription()).isEqualTo("Modified description");
        assertThat(dto.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
    }

    @Test
    void testPutTransactionDtoWithCompletedStatus() {
        // Arrange & Act
        PutTransactionDto dto = new PutTransactionDto(
                TransactionType.DEPOSIT,
                "acc-001",
                "acc-002",
                "1234567890",
                "0987654321",
                new BigDecimal("100.00"),
                Currency.RON,
                "Completed transaction",
                TransactionStatus.COMPLETED
        );

        // Assert
        assertThat(dto.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
    }

    @Test
    void testPutTransactionDtoWithFailedStatus() {
        // Arrange & Act
        PutTransactionDto dto = new PutTransactionDto(
                TransactionType.TRANSFER,
                "acc-001",
                "acc-002",
                "1234567890",
                "0987654321",
                new BigDecimal("100.00"),
                Currency.RON,
                "Failed transaction",
                TransactionStatus.FAILED
        );

        // Assert
        assertThat(dto.getStatus()).isEqualTo(TransactionStatus.FAILED);
    }

    @Test
    void testPutTransactionDtoWithCancelledStatus() {
        // Arrange & Act
        PutTransactionDto dto = new PutTransactionDto(
                TransactionType.TRANSFER,
                "acc-001",
                "acc-002",
                "1234567890",
                "0987654321",
                new BigDecimal("100.00"),
                Currency.RON,
                "Cancelled transaction",
                TransactionStatus.CANCELLED
        );

        // Assert
        assertThat(dto.getStatus()).isEqualTo(TransactionStatus.CANCELLED);
    }

    @Test
    void testPutTransactionDtoWithDifferentCurrencies() {
        // Test EUR
        PutTransactionDto eurDto = new PutTransactionDto(
                TransactionType.TRANSFER,
                "acc-001",
                "acc-002",
                "1234567890",
                "0987654321",
                new BigDecimal("100.00"),
                Currency.EUR,
                "EUR transaction",
                TransactionStatus.PENDING
        );
        assertThat(eurDto.getCurrency()).isEqualTo(Currency.EUR);

        // Test USD
        PutTransactionDto usdDto = new PutTransactionDto(
                TransactionType.TRANSFER,
                "acc-001",
                "acc-002",
                "1234567890",
                "0987654321",
                new BigDecimal("100.00"),
                Currency.USD,
                "USD transaction",
                TransactionStatus.PENDING
        );
        assertThat(usdDto.getCurrency()).isEqualTo(Currency.USD);
    }
}

