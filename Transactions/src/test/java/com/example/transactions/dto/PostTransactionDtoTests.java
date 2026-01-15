package com.example.transactions.dto;

import com.example.transactions.dto.request.PostTransactionDto;
import com.example.transactions.enums.Currency;
import com.example.transactions.enums.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PostTransactionDtoTests {

    @Test
    void testPostTransactionDtoConstructor() {
        // Arrange & Act
        PostTransactionDto dto = new PostTransactionDto(
                "0987654321",
                "acc-001",
                "acc-002",
                "1234567890",
                TransactionType.TRANSFER,
                new BigDecimal("100.00"),
                Currency.RON,
                "Test transaction"
        );

        // Assert
        assertThat(dto.getToAccountNumber()).isEqualTo("0987654321");
        assertThat(dto.getFromAccountId()).isEqualTo("acc-001");
        assertThat(dto.getToAccountId()).isEqualTo("acc-002");
        assertThat(dto.getFromAccountNumber()).isEqualTo("1234567890");
        assertThat(dto.getTransactionType()).isEqualTo(TransactionType.TRANSFER);
        assertThat(dto.getAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(dto.getCurrency()).isEqualTo(Currency.RON);
        assertThat(dto.getDescription()).isEqualTo("Test transaction");
    }

    @Test
    void testPostTransactionDtoSetters() {
        // Arrange
        PostTransactionDto dto = new PostTransactionDto(
                "0987654321",
                "acc-001",
                "acc-002",
                "1234567890",
                TransactionType.TRANSFER,
                new BigDecimal("100.00"),
                Currency.RON,
                "Test"
        );

        // Act
        dto.setToAccountNumber("1111111111");
        dto.setFromAccountId("acc-003");
        dto.setToAccountId("acc-004");
        dto.setFromAccountNumber("2222222222");
        dto.setTransactionType(TransactionType.DEPOSIT);
        dto.setAmount(new BigDecimal("200.00"));
        dto.setCurrency(Currency.EUR);
        dto.setDescription("Updated description");

        // Assert
        assertThat(dto.getToAccountNumber()).isEqualTo("1111111111");
        assertThat(dto.getFromAccountId()).isEqualTo("acc-003");
        assertThat(dto.getToAccountId()).isEqualTo("acc-004");
        assertThat(dto.getFromAccountNumber()).isEqualTo("2222222222");
        assertThat(dto.getTransactionType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(dto.getAmount()).isEqualByComparingTo(new BigDecimal("200.00"));
        assertThat(dto.getCurrency()).isEqualTo(Currency.EUR);
        assertThat(dto.getDescription()).isEqualTo("Updated description");
    }

    @Test
    void testPostTransactionDtoWithDifferentCurrencies() {
        // Test with EUR
        PostTransactionDto euroDto = new PostTransactionDto(
                "0987654321",
                "acc-001",
                "acc-002",
                "1234567890",
                TransactionType.TRANSFER,
                new BigDecimal("100.00"),
                Currency.EUR,
                "EUR transaction"
        );
        assertThat(euroDto.getCurrency()).isEqualTo(Currency.EUR);

        // Test with USD
        PostTransactionDto usdDto = new PostTransactionDto(
                "0987654321",
                "acc-001",
                "acc-002",
                "1234567890",
                TransactionType.TRANSFER,
                new BigDecimal("100.00"),
                Currency.USD,
                "USD transaction"
        );
        assertThat(usdDto.getCurrency()).isEqualTo(Currency.USD);
    }

    @Test
    void testPostTransactionDtoWithDifferentTypes() {
        // Test WITHDRAWAL
        PostTransactionDto withdrawalDto = new PostTransactionDto(
                "0987654321",
                "acc-001",
                "acc-002",
                "1234567890",
                TransactionType.WITHDRAWAL,
                new BigDecimal("50.00"),
                Currency.RON,
                "Withdrawal"
        );
        assertThat(withdrawalDto.getTransactionType()).isEqualTo(TransactionType.WITHDRAWAL);

        // Test PAYMENT
        PostTransactionDto paymentDto = new PostTransactionDto(
                "0987654321",
                "acc-001",
                "acc-002",
                "1234567890",
                TransactionType.PAYMENT,
                new BigDecimal("75.00"),
                Currency.RON,
                "Payment"
        );
        assertThat(paymentDto.getTransactionType()).isEqualTo(TransactionType.PAYMENT);
    }

    @Test
    void testPostTransactionDtoWithLargeAmount() {
        // Arrange & Act
        PostTransactionDto dto = new PostTransactionDto(
                "0987654321",
                "acc-001",
                "acc-002",
                "1234567890",
                TransactionType.TRANSFER,
                new BigDecimal("999999999.99"),
                Currency.RON,
                "Large amount"
        );

        // Assert
        assertThat(dto.getAmount()).isEqualByComparingTo(new BigDecimal("999999999.99"));
    }
}

