package com.example.transactions.utils;

import com.example.transactions.entity.Transaction;
import com.example.transactions.enums.Currency;
import com.example.transactions.enums.TransactionStatus;
import com.example.transactions.enums.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionHelperTests {

    @Test
    void testCheckAmountAntiFraud_HighRisk() {
        // Arrange - Amount > 10000
        Transaction transaction = Transaction.builder()
                .id(1L)
                .transactionId("TRX123")
                .amount(new BigDecimal("15000.00"))
                .currency(Currency.RON)
                .transactionType(TransactionType.TRANSFER)
                .status(TransactionStatus.PENDING)
                .initiatedAt(LocalDateTime.now())
                .build();

        // Act
        BigDecimal riskScore = TransactionHelper.checkAmountAntiFraud(transaction);

        // Assert
        assertThat(riskScore).isEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void testCheckAmountAntiFraud_MediumHighRisk() {
        // Arrange - Amount > 5000 and <= 10000
        Transaction transaction = Transaction.builder()
                .id(1L)
                .transactionId("TRX123")
                .amount(new BigDecimal("7500.00"))
                .currency(Currency.RON)
                .transactionType(TransactionType.TRANSFER)
                .status(TransactionStatus.PENDING)
                .initiatedAt(LocalDateTime.now())
                .build();

        // Act
        BigDecimal riskScore = TransactionHelper.checkAmountAntiFraud(transaction);

        // Assert
        assertThat(riskScore).isEqualByComparingTo(new BigDecimal("0.7"));
    }

    @Test
    void testCheckAmountAntiFraud_MediumRisk() {
        // Arrange - Amount > 2000 and <= 5000
        Transaction transaction = Transaction.builder()
                .id(1L)
                .transactionId("TRX123")
                .amount(new BigDecimal("3500.00"))
                .currency(Currency.RON)
                .transactionType(TransactionType.TRANSFER)
                .status(TransactionStatus.PENDING)
                .initiatedAt(LocalDateTime.now())
                .build();

        // Act
        BigDecimal riskScore = TransactionHelper.checkAmountAntiFraud(transaction);

        // Assert
        assertThat(riskScore).isEqualByComparingTo(new BigDecimal("0.4"));
    }

    @Test
    void testCheckAmountAntiFraud_LowRisk() {
        // Arrange - Amount <= 2000
        Transaction transaction = Transaction.builder()
                .id(1L)
                .transactionId("TRX123")
                .amount(new BigDecimal("1500.00"))
                .currency(Currency.RON)
                .transactionType(TransactionType.TRANSFER)
                .status(TransactionStatus.PENDING)
                .initiatedAt(LocalDateTime.now())
                .build();

        // Act
        BigDecimal riskScore = TransactionHelper.checkAmountAntiFraud(transaction);

        // Assert
        assertThat(riskScore).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void testCheckAmountAntiFraud_EdgeCase_Exactly10000() {
        // Arrange - Amount exactly 10000
        Transaction transaction = Transaction.builder()
                .id(1L)
                .transactionId("TRX123")
                .amount(new BigDecimal("10000.00"))
                .currency(Currency.RON)
                .transactionType(TransactionType.TRANSFER)
                .status(TransactionStatus.PENDING)
                .initiatedAt(LocalDateTime.now())
                .build();

        // Act
        BigDecimal riskScore = TransactionHelper.checkAmountAntiFraud(transaction);

        // Assert
        assertThat(riskScore).isEqualByComparingTo(new BigDecimal("0.7"));
    }

    @Test
    void testCheckAmountAntiFraud_EdgeCase_Exactly5000() {
        // Arrange - Amount exactly 5000
        Transaction transaction = Transaction.builder()
                .id(1L)
                .transactionId("TRX123")
                .amount(new BigDecimal("5000.00"))
                .currency(Currency.RON)
                .transactionType(TransactionType.TRANSFER)
                .status(TransactionStatus.PENDING)
                .initiatedAt(LocalDateTime.now())
                .build();

        // Act
        BigDecimal riskScore = TransactionHelper.checkAmountAntiFraud(transaction);

        // Assert
        assertThat(riskScore).isEqualByComparingTo(new BigDecimal("0.4"));
    }

    @Test
    void testCheckAmountAntiFraud_EdgeCase_Exactly2000() {
        // Arrange - Amount exactly 2000
        Transaction transaction = Transaction.builder()
                .id(1L)
                .transactionId("TRX123")
                .amount(new BigDecimal("2000.00"))
                .currency(Currency.RON)
                .transactionType(TransactionType.TRANSFER)
                .status(TransactionStatus.PENDING)
                .initiatedAt(LocalDateTime.now())
                .build();

        // Act
        BigDecimal riskScore = TransactionHelper.checkAmountAntiFraud(transaction);

        // Assert
        assertThat(riskScore).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void testConvertCurrency_SameCurrency() {
        // Arrange
        BigDecimal amount = new BigDecimal("100.00");

        // Act
        BigDecimal result = TransactionHelper.ConvertCurrency(Currency.RON, Currency.RON, amount);

        // Assert
        assertThat(result).isEqualByComparingTo(amount);
    }

    @Test
    void testConvertCurrency_RONToEUR() {
        // Arrange
        // RON value = 1, EUR value = 5
        // Formula: amount * prevVal / newVal = 100 * 1 / 5 = 20
        BigDecimal amount = new BigDecimal("100.00");

        // Act
        BigDecimal result = TransactionHelper.ConvertCurrency(Currency.RON, Currency.EUR, amount);

        // Assert
        assertThat(result).isEqualByComparingTo(new BigDecimal("20.00"));
    }

    @Test
    void testConvertCurrency_RONToUSD() {
        // Arrange
        // RON value = 1, USD value = 4
        // Formula: amount * prevVal / newVal = 100 * 1 / 4 = 25
        BigDecimal amount = new BigDecimal("100.00");

        // Act
        BigDecimal result = TransactionHelper.ConvertCurrency(Currency.RON, Currency.USD, amount);

        // Assert
        assertThat(result).isEqualByComparingTo(new BigDecimal("25.00"));
    }

    @Test
    void testConvertCurrency_EURToRON() {
        // Arrange
        // EUR value = 5, RON value = 1
        // Formula: amount * prevVal / newVal = 100 * 5 / 1 = 500
        BigDecimal amount = new BigDecimal("100.00");

        // Act
        BigDecimal result = TransactionHelper.ConvertCurrency(Currency.EUR, Currency.RON, amount);

        // Assert
        assertThat(result).isEqualByComparingTo(new BigDecimal("500.00"));
    }

    @Test
    void testConvertCurrency_EURToUSD() {
        // Arrange
        // EUR value = 5, USD value = 4
        // Formula: amount * prevVal / newVal = 100 * 5 / 4 = 125
        BigDecimal amount = new BigDecimal("100.00");

        // Act
        BigDecimal result = TransactionHelper.ConvertCurrency(Currency.EUR, Currency.USD, amount);

        // Assert
        assertThat(result).isEqualByComparingTo(new BigDecimal("125.00"));
    }

    @Test
    void testConvertCurrency_USDToRON() {
        // Arrange
        // USD value = 4, RON value = 1
        // Formula: amount * prevVal / newVal = 100 * 4 / 1 = 400
        BigDecimal amount = new BigDecimal("100.00");

        // Act
        BigDecimal result = TransactionHelper.ConvertCurrency(Currency.USD, Currency.RON, amount);

        // Assert
        assertThat(result).isEqualByComparingTo(new BigDecimal("400.00"));
    }

    @Test
    void testConvertCurrency_USDToEUR() {
        // Arrange
        // USD value = 4, EUR value = 5
        // Formula: amount * prevVal / newVal = 100 * 4 / 5 = 80
        BigDecimal amount = new BigDecimal("100.00");

        // Act
        BigDecimal result = TransactionHelper.ConvertCurrency(Currency.USD, Currency.EUR, amount);

        // Assert
        assertThat(result).isEqualByComparingTo(new BigDecimal("80.00"));
    }

    @Test
    void testConvertCurrency_WithDecimalAmount() {
        // Arrange
        BigDecimal amount = new BigDecimal("123.45");

        // Act
        BigDecimal result = TransactionHelper.ConvertCurrency(Currency.RON, Currency.EUR, amount);

        // Assert - 123.45 * 1 / 5 = 24.69
        assertThat(result).isEqualByComparingTo(new BigDecimal("24.69"));
    }

    @Test
    void testConvertCurrency_WithSmallAmount() {
        // Arrange
        BigDecimal amount = new BigDecimal("1.00");

        // Act
        BigDecimal result = TransactionHelper.ConvertCurrency(Currency.RON, Currency.EUR, amount);

        // Assert - 1 * 1 / 5 = 0.2
        assertThat(result).isEqualByComparingTo(new BigDecimal("0.2"));
    }

    @Test
    void testConvertCurrency_WithLargeAmount() {
        // Arrange
        BigDecimal amount = new BigDecimal("1000000.00");

        // Act
        BigDecimal result = TransactionHelper.ConvertCurrency(Currency.EUR, Currency.RON, amount);

        // Assert - 1000000 * 5 / 1 = 5000000
        assertThat(result).isEqualByComparingTo(new BigDecimal("5000000.00"));
    }

    @Test
    void testConvertCurrency_PrecisionHandling() {
        // Arrange - Test that division maintains proper precision
        BigDecimal amount = new BigDecimal("100.00");

        // Act
        BigDecimal result = TransactionHelper.ConvertCurrency(Currency.USD, Currency.EUR, amount);

        // Assert - Should have 8 decimal places precision
        assertThat(result.scale()).isGreaterThanOrEqualTo(2);
    }

    @Test
    void testCheckAmountAntiFraud_VeryHighAmount() {
        // Arrange
        Transaction transaction = Transaction.builder()
                .id(1L)
                .transactionId("TRX123")
                .amount(new BigDecimal("1000000.00"))
                .currency(Currency.RON)
                .transactionType(TransactionType.TRANSFER)
                .status(TransactionStatus.PENDING)
                .initiatedAt(LocalDateTime.now())
                .build();

        // Act
        BigDecimal riskScore = TransactionHelper.checkAmountAntiFraud(transaction);

        // Assert - Should return 1 (highest risk)
        assertThat(riskScore).isEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void testCheckAmountAntiFraud_VerySmallAmount() {
        // Arrange
        Transaction transaction = Transaction.builder()
                .id(1L)
                .transactionId("TRX123")
                .amount(new BigDecimal("0.01"))
                .currency(Currency.RON)
                .transactionType(TransactionType.TRANSFER)
                .status(TransactionStatus.PENDING)
                .initiatedAt(LocalDateTime.now())
                .build();

        // Act
        BigDecimal riskScore = TransactionHelper.checkAmountAntiFraud(transaction);

        // Assert - Should return 0 (lowest risk)
        assertThat(riskScore).isEqualByComparingTo(BigDecimal.ZERO);
    }
}

