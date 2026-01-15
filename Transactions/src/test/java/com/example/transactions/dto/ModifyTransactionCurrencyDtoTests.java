package com.example.transactions.dto;

import com.example.transactions.dto.response.ModifyTransactionCurrencyDto;
import com.example.transactions.enums.Currency;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ModifyTransactionCurrencyDtoTests {

    @Test
    void testModifyTransactionCurrencyDtoConstructor() {
        // Arrange & Act
        ModifyTransactionCurrencyDto dto = new ModifyTransactionCurrencyDto(
                "TRX123",
                Currency.EUR,
                new BigDecimal("20.00")
        );

        // Assert
        assertThat(dto.getTransactionId()).isEqualTo("TRX123");
        assertThat(dto.getCurrency()).isEqualTo(Currency.EUR);
        assertThat(dto.getAmount()).isEqualByComparingTo(new BigDecimal("20.00"));
    }

    @Test
    void testModifyTransactionCurrencyDtoSetters() {
        // Arrange
        ModifyTransactionCurrencyDto dto = new ModifyTransactionCurrencyDto(
                "TRX123",
                Currency.RON,
                new BigDecimal("100.00")
        );

        // Act
        dto.setTransactionId("TRX456");
        dto.setCurrency(Currency.USD);
        dto.setAmount(new BigDecimal("25.00"));

        // Assert
        assertThat(dto.getTransactionId()).isEqualTo("TRX456");
        assertThat(dto.getCurrency()).isEqualTo(Currency.USD);
        assertThat(dto.getAmount()).isEqualByComparingTo(new BigDecimal("25.00"));
    }

    @Test
    void testModifyTransactionCurrencyDtoWithRON() {
        // Arrange & Act
        ModifyTransactionCurrencyDto dto = new ModifyTransactionCurrencyDto(
                "TRX123",
                Currency.RON,
                new BigDecimal("500.00")
        );

        // Assert
        assertThat(dto.getCurrency()).isEqualTo(Currency.RON);
        assertThat(dto.getAmount()).isEqualByComparingTo(new BigDecimal("500.00"));
    }

    @Test
    void testModifyTransactionCurrencyDtoWithEUR() {
        // Arrange & Act
        ModifyTransactionCurrencyDto dto = new ModifyTransactionCurrencyDto(
                "TRX123",
                Currency.EUR,
                new BigDecimal("100.00")
        );

        // Assert
        assertThat(dto.getCurrency()).isEqualTo(Currency.EUR);
        assertThat(dto.getAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    void testModifyTransactionCurrencyDtoWithUSD() {
        // Arrange & Act
        ModifyTransactionCurrencyDto dto = new ModifyTransactionCurrencyDto(
                "TRX123",
                Currency.USD,
                new BigDecimal("80.00")
        );

        // Assert
        assertThat(dto.getCurrency()).isEqualTo(Currency.USD);
        assertThat(dto.getAmount()).isEqualByComparingTo(new BigDecimal("80.00"));
    }

    @Test
    void testModifyTransactionCurrencyDtoWithDecimalAmount() {
        // Arrange & Act
        ModifyTransactionCurrencyDto dto = new ModifyTransactionCurrencyDto(
                "TRX123",
                Currency.EUR,
                new BigDecimal("123.456789")
        );

        // Assert
        assertThat(dto.getAmount()).isEqualByComparingTo(new BigDecimal("123.456789"));
    }

    @Test
    void testModifyTransactionCurrencyDtoWithSmallAmount() {
        // Arrange & Act
        ModifyTransactionCurrencyDto dto = new ModifyTransactionCurrencyDto(
                "TRX123",
                Currency.EUR,
                new BigDecimal("0.01")
        );

        // Assert
        assertThat(dto.getAmount()).isEqualByComparingTo(new BigDecimal("0.01"));
    }

    @Test
    void testModifyTransactionCurrencyDtoWithLargeAmount() {
        // Arrange & Act
        ModifyTransactionCurrencyDto dto = new ModifyTransactionCurrencyDto(
                "TRX123",
                Currency.EUR,
                new BigDecimal("99999999.99")
        );

        // Assert
        assertThat(dto.getAmount()).isEqualByComparingTo(new BigDecimal("99999999.99"));
    }
}

