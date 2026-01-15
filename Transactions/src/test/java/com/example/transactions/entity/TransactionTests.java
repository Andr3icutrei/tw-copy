package com.example.transactions.entity;

import com.example.transactions.enums.Currency;
import com.example.transactions.enums.TransactionStatus;
import com.example.transactions.enums.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionTests {

    @Test
    void testTransactionBuilder() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();

        // Act
        Transaction transaction = Transaction.builder()
                .id(1L)
                .transactionId("TRX-123")
                .fromAccountId("acc-001")
                .toAccountId("acc-002")
                .fromAccountNumber("1234567890")
                .toAccountNumber("0987654321")
                .transactionType(TransactionType.TRANSFER)
                .amount(new BigDecimal("100.00"))
                .currency(Currency.RON)
                .description("Test transaction")
                .status(TransactionStatus.PENDING)
                .initiatedAt(now)
                .build();

        // Assert
        assertThat(transaction).isNotNull();
        assertThat(transaction.getId()).isEqualTo(1L);
        assertThat(transaction.getTransactionId()).isEqualTo("TRX-123");
        assertThat(transaction.getFromAccountId()).isEqualTo("acc-001");
        assertThat(transaction.getToAccountId()).isEqualTo("acc-002");
        assertThat(transaction.getFromAccountNumber()).isEqualTo("1234567890");
        assertThat(transaction.getToAccountNumber()).isEqualTo("0987654321");
        assertThat(transaction.getTransactionType()).isEqualTo(TransactionType.TRANSFER);
        assertThat(transaction.getAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(transaction.getCurrency()).isEqualTo(Currency.RON);
        assertThat(transaction.getDescription()).isEqualTo("Test transaction");
        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.PENDING);
        assertThat(transaction.getInitiatedAt()).isEqualTo(now);
    }

    @Test
    void testTransactionNoArgsConstructor() {
        // Act
        Transaction transaction = new Transaction();

        // Assert
        assertThat(transaction).isNotNull();
    }

    @Test
    void testTransactionAllArgsConstructor() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();

        // Act
        Transaction transaction = new Transaction(
                1L,
                "TRX-123",
                "acc-001",
                "acc-002",
                "1234567890",
                "0987654321",
                TransactionType.TRANSFER,
                new BigDecimal("100.00"),
                Currency.RON,
                "Test transaction",
                TransactionStatus.PENDING,
                now,
                null,
                null,
                null
        );

        // Assert
        assertThat(transaction).isNotNull();
        assertThat(transaction.getId()).isEqualTo(1L);
        assertThat(transaction.getTransactionId()).isEqualTo("TRX-123");
    }

    @Test
    void testTransactionSettersAndGetters() {
        // Arrange
        Transaction transaction = new Transaction();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime completed = now.plusMinutes(5);
        LocalDateTime failed = now.plusMinutes(10);

        // Act
        transaction.setId(2L);
        transaction.setTransactionId("TRX-456");
        transaction.setFromAccountId("acc-003");
        transaction.setToAccountId("acc-004");
        transaction.setFromAccountNumber("1111111111");
        transaction.setToAccountNumber("2222222222");
        transaction.setTransactionType(TransactionType.PAYMENT);
        transaction.setAmount(new BigDecimal("200.00"));
        transaction.setCurrency(Currency.EUR);
        transaction.setDescription("Payment transaction");
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setInitiatedAt(now);
        transaction.setCompletedAt(completed);
        transaction.setFailedAt(failed);
        transaction.setFailureReason("Test failure");

        // Assert
        assertThat(transaction.getId()).isEqualTo(2L);
        assertThat(transaction.getTransactionId()).isEqualTo("TRX-456");
        assertThat(transaction.getFromAccountId()).isEqualTo("acc-003");
        assertThat(transaction.getToAccountId()).isEqualTo("acc-004");
        assertThat(transaction.getFromAccountNumber()).isEqualTo("1111111111");
        assertThat(transaction.getToAccountNumber()).isEqualTo("2222222222");
        assertThat(transaction.getTransactionType()).isEqualTo(TransactionType.PAYMENT);
        assertThat(transaction.getAmount()).isEqualByComparingTo(new BigDecimal("200.00"));
        assertThat(transaction.getCurrency()).isEqualTo(Currency.EUR);
        assertThat(transaction.getDescription()).isEqualTo("Payment transaction");
        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
        assertThat(transaction.getInitiatedAt()).isEqualTo(now);
        assertThat(transaction.getCompletedAt()).isEqualTo(completed);
        assertThat(transaction.getFailedAt()).isEqualTo(failed);
        assertThat(transaction.getFailureReason()).isEqualTo("Test failure");
    }

    @Test
    void testTransactionWithTransferType() {
        // Arrange & Act
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.TRANSFER)
                .amount(new BigDecimal("100.00"))
                .currency(Currency.RON)
                .status(TransactionStatus.PENDING)
                .initiatedAt(LocalDateTime.now())
                .build();

        // Assert
        assertThat(transaction.getTransactionType()).isEqualTo(TransactionType.TRANSFER);
    }

    @Test
    void testTransactionWithDepositType() {
        // Arrange & Act
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.DEPOSIT)
                .amount(new BigDecimal("500.00"))
                .currency(Currency.USD)
                .status(TransactionStatus.PENDING)
                .initiatedAt(LocalDateTime.now())
                .build();

        // Assert
        assertThat(transaction.getTransactionType()).isEqualTo(TransactionType.DEPOSIT);
    }

    @Test
    void testTransactionWithWithdrawalType() {
        // Arrange & Act
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.WITHDRAWAL)
                .amount(new BigDecimal("200.00"))
                .currency(Currency.EUR)
                .status(TransactionStatus.PENDING)
                .initiatedAt(LocalDateTime.now())
                .build();

        // Assert
        assertThat(transaction.getTransactionType()).isEqualTo(TransactionType.WITHDRAWAL);
    }

    @Test
    void testTransactionWithPaymentType() {
        // Arrange & Act
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.PAYMENT)
                .amount(new BigDecimal("75.00"))
                .currency(Currency.RON)
                .status(TransactionStatus.PENDING)
                .initiatedAt(LocalDateTime.now())
                .build();

        // Assert
        assertThat(transaction.getTransactionType()).isEqualTo(TransactionType.PAYMENT);
    }

    @Test
    void testTransactionWithPendingStatus() {
        // Arrange & Act
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.TRANSFER)
                .amount(new BigDecimal("100.00"))
                .currency(Currency.RON)
                .status(TransactionStatus.PENDING)
                .initiatedAt(LocalDateTime.now())
                .build();

        // Assert
        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.PENDING);
    }

    @Test
    void testTransactionWithCompletedStatus() {
        // Arrange & Act
        LocalDateTime now = LocalDateTime.now();
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.TRANSFER)
                .amount(new BigDecimal("100.00"))
                .currency(Currency.RON)
                .status(TransactionStatus.COMPLETED)
                .initiatedAt(now)
                .completedAt(now.plusMinutes(5))
                .build();

        // Assert
        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
        assertThat(transaction.getCompletedAt()).isNotNull();
    }

    @Test
    void testTransactionWithFailedStatus() {
        // Arrange & Act
        LocalDateTime now = LocalDateTime.now();
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.TRANSFER)
                .amount(new BigDecimal("100.00"))
                .currency(Currency.RON)
                .status(TransactionStatus.FAILED)
                .initiatedAt(now)
                .failedAt(now.plusMinutes(5))
                .failureReason("Insufficient funds")
                .build();

        // Assert
        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.FAILED);
        assertThat(transaction.getFailedAt()).isNotNull();
        assertThat(transaction.getFailureReason()).isEqualTo("Insufficient funds");
    }

    @Test
    void testTransactionWithCancelledStatus() {
        // Arrange & Act
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.TRANSFER)
                .amount(new BigDecimal("100.00"))
                .currency(Currency.RON)
                .status(TransactionStatus.CANCELLED)
                .initiatedAt(LocalDateTime.now())
                .failureReason("User cancelled")
                .build();

        // Assert
        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.CANCELLED);
        assertThat(transaction.getFailureReason()).isEqualTo("User cancelled");
    }

    @Test
    void testTransactionWithRONCurrency() {
        // Arrange & Act
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.TRANSFER)
                .amount(new BigDecimal("100.00"))
                .currency(Currency.RON)
                .status(TransactionStatus.PENDING)
                .initiatedAt(LocalDateTime.now())
                .build();

        // Assert
        assertThat(transaction.getCurrency()).isEqualTo(Currency.RON);
    }

    @Test
    void testTransactionWithEURCurrency() {
        // Arrange & Act
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.TRANSFER)
                .amount(new BigDecimal("100.00"))
                .currency(Currency.EUR)
                .status(TransactionStatus.PENDING)
                .initiatedAt(LocalDateTime.now())
                .build();

        // Assert
        assertThat(transaction.getCurrency()).isEqualTo(Currency.EUR);
    }

    @Test
    void testTransactionWithUSDCurrency() {
        // Arrange & Act
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.TRANSFER)
                .amount(new BigDecimal("100.00"))
                .currency(Currency.USD)
                .status(TransactionStatus.PENDING)
                .initiatedAt(LocalDateTime.now())
                .build();

        // Assert
        assertThat(transaction.getCurrency()).isEqualTo(Currency.USD);
    }

    @Test
    void testTransactionWithSmallAmount() {
        // Arrange & Act
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.TRANSFER)
                .amount(new BigDecimal("0.01"))
                .currency(Currency.RON)
                .status(TransactionStatus.PENDING)
                .initiatedAt(LocalDateTime.now())
                .build();

        // Assert
        assertThat(transaction.getAmount()).isEqualByComparingTo(new BigDecimal("0.01"));
    }

    @Test
    void testTransactionWithLargeAmount() {
        // Arrange & Act
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.TRANSFER)
                .amount(new BigDecimal("9999999999.99"))
                .currency(Currency.RON)
                .status(TransactionStatus.PENDING)
                .initiatedAt(LocalDateTime.now())
                .build();

        // Assert
        assertThat(transaction.getAmount()).isEqualByComparingTo(new BigDecimal("9999999999.99"));
    }

    @Test
    void testTransactionTimestamps() {
        // Arrange
        LocalDateTime initiated = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime completed = LocalDateTime.of(2026, 1, 1, 10, 5);

        // Act
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.TRANSFER)
                .amount(new BigDecimal("100.00"))
                .currency(Currency.RON)
                .status(TransactionStatus.COMPLETED)
                .initiatedAt(initiated)
                .completedAt(completed)
                .build();

        // Assert
        assertThat(transaction.getInitiatedAt()).isEqualTo(initiated);
        assertThat(transaction.getCompletedAt()).isEqualTo(completed);
        assertThat(transaction.getCompletedAt()).isAfter(transaction.getInitiatedAt());
    }

    @Test
    void testTransactionBuilderDefaults() {
        // Act
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.TRANSFER)
                .amount(new BigDecimal("100.00"))
                .initiatedAt(LocalDateTime.now())
                .build();

        // Assert - Default values should be set
        assertThat(transaction.getCurrency()).isEqualTo(Currency.RON);
        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.PENDING);
    }

    @Test
    void testTransactionWithDescription() {
        // Arrange & Act
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.PAYMENT)
                .amount(new BigDecimal("50.00"))
                .currency(Currency.RON)
                .status(TransactionStatus.PENDING)
                .initiatedAt(LocalDateTime.now())
                .description("Payment for invoice #12345")
                .build();

        // Assert
        assertThat(transaction.getDescription()).isEqualTo("Payment for invoice #12345");
    }

    @Test
    void testTransactionWithAccountNumbers() {
        // Arrange & Act
        Transaction transaction = Transaction.builder()
                .fromAccountNumber("RO49AAAA1B31007593840000")
                .toAccountNumber("RO09BCYP0000001234567890")
                .transactionType(TransactionType.TRANSFER)
                .amount(new BigDecimal("100.00"))
                .currency(Currency.RON)
                .status(TransactionStatus.PENDING)
                .initiatedAt(LocalDateTime.now())
                .build();

        // Assert
        assertThat(transaction.getFromAccountNumber()).isEqualTo("RO49AAAA1B31007593840000");
        assertThat(transaction.getToAccountNumber()).isEqualTo("RO09BCYP0000001234567890");
    }

    @Test
    void testTransactionEquals() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Transaction transaction1 = Transaction.builder()
                .id(1L)
                .transactionId("TRX-123")
                .amount(new BigDecimal("100.00"))
                .currency(Currency.RON)
                .transactionType(TransactionType.TRANSFER)
                .status(TransactionStatus.PENDING)
                .initiatedAt(now)
                .build();

        Transaction transaction2 = Transaction.builder()
                .id(1L)
                .transactionId("TRX-123")
                .amount(new BigDecimal("100.00"))
                .currency(Currency.RON)
                .transactionType(TransactionType.TRANSFER)
                .status(TransactionStatus.PENDING)
                .initiatedAt(now)
                .build();

        // Assert
        assertThat(transaction1).isEqualTo(transaction2);
    }

    @Test
    void testTransactionHashCode() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Transaction transaction1 = Transaction.builder()
                .id(1L)
                .transactionId("TRX-123")
                .amount(new BigDecimal("100.00"))
                .currency(Currency.RON)
                .transactionType(TransactionType.TRANSFER)
                .status(TransactionStatus.PENDING)
                .initiatedAt(now)
                .build();

        Transaction transaction2 = Transaction.builder()
                .id(1L)
                .transactionId("TRX-123")
                .amount(new BigDecimal("100.00"))
                .currency(Currency.RON)
                .transactionType(TransactionType.TRANSFER)
                .status(TransactionStatus.PENDING)
                .initiatedAt(now)
                .build();

        // Assert
        assertThat(transaction1.hashCode()).isEqualTo(transaction2.hashCode());
    }
}

