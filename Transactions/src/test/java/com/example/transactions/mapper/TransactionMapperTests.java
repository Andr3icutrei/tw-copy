package com.example.transactions.mapper;

import com.example.transactions.dto.request.PostTransactionDto;
import com.example.transactions.dto.request.PutTransactionDto;
import com.example.transactions.dto.response.TransactionDto;
import com.example.transactions.entity.Transaction;
import com.example.transactions.enums.Currency;
import com.example.transactions.enums.TransactionStatus;
import com.example.transactions.enums.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionMapperTests {

    @Test
    void testToEntityFromPostTransactionDto() {
        // Arrange
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

        // Act
        Transaction entity = TransactionMapper.ToEntity(dto);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(entity.getCurrency()).isEqualTo(Currency.RON);
        assertThat(entity.getTransactionType()).isEqualTo(TransactionType.TRANSFER);
        assertThat(entity.getStatus()).isEqualTo(TransactionStatus.PENDING);
        assertThat(entity.getDescription()).isEqualTo("Test transaction");
        assertThat(entity.getFromAccountId()).isEqualTo("acc-001");
        assertThat(entity.getToAccountId()).isEqualTo("acc-002");
        assertThat(entity.getInitiatedAt()).isNotNull();
    }

    @Test
    void testToEntityFromPutTransactionDto() {
        // Arrange
        PutTransactionDto dto = new PutTransactionDto(
                TransactionType.PAYMENT,
                "acc-001",
                "acc-002",
                "1234567890",
                "0987654321",
                new BigDecimal("150.00"),
                Currency.EUR,
                "Updated transaction",
                TransactionStatus.COMPLETED
        );
        Long id = 1L;
        String transactionId = "TRX123";

        // Act
        Transaction entity = TransactionMapper.ToEntity(dto, id, transactionId);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getTransactionId()).isEqualTo("TRX123");
        assertThat(entity.getAmount()).isEqualByComparingTo(new BigDecimal("150.00"));
        assertThat(entity.getCurrency()).isEqualTo(Currency.EUR);
        assertThat(entity.getTransactionType()).isEqualTo(TransactionType.PAYMENT);
        assertThat(entity.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
        assertThat(entity.getDescription()).isEqualTo("Updated transaction");
        assertThat(entity.getFromAccountId()).isEqualTo("acc-001");
        assertThat(entity.getToAccountId()).isEqualTo("acc-002");
    }

    @Test
    void testToDtoFromTransaction() {
        // Arrange
        Transaction transaction = Transaction.builder()
                .id(1L)
                .transactionId("TRX123")
                .fromAccountId("acc-001")
                .toAccountId("acc-002")
                .fromAccountNumber("1234567890")
                .toAccountNumber("0987654321")
                .transactionType(TransactionType.TRANSFER)
                .amount(new BigDecimal("100.00"))
                .currency(Currency.RON)
                .description("Test transaction")
                .status(TransactionStatus.PENDING)
                .failureReason(null)
                .initiatedAt(LocalDateTime.now())
                .build();

        // Act
        TransactionDto dto = TransactionMapper.ToDto(transaction);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getTransactionId()).isEqualTo("TRX123");
        assertThat(dto.getFromAccountId()).isEqualTo("acc-001");
        assertThat(dto.getToAccountId()).isEqualTo("acc-002");
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
    void testToDtoFromTransactionWithFailureReason() {
        // Arrange
        Transaction transaction = Transaction.builder()
                .id(1L)
                .transactionId("TRX123")
                .fromAccountId("acc-001")
                .toAccountId("acc-002")
                .fromAccountNumber("1234567890")
                .toAccountNumber("0987654321")
                .transactionType(TransactionType.TRANSFER)
                .amount(new BigDecimal("100.00"))
                .currency(Currency.RON)
                .description("Failed transaction")
                .status(TransactionStatus.FAILED)
                .failureReason("Insufficient funds")
                .initiatedAt(LocalDateTime.now())
                .build();

        // Act
        TransactionDto dto = TransactionMapper.ToDto(transaction);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getStatus()).isEqualTo(TransactionStatus.FAILED);
        assertThat(dto.getFailureReason()).isEqualTo("Insufficient funds");
    }

    @Test
    void testToEntityFromPostTransactionDtoWithDeposit() {
        // Arrange
        PostTransactionDto dto = new PostTransactionDto(
                "0987654321",
                "acc-001",
                "acc-002",
                "1234567890",
                TransactionType.DEPOSIT,
                new BigDecimal("500.00"),
                Currency.USD,
                "Deposit transaction"
        );

        // Act
        Transaction entity = TransactionMapper.ToEntity(dto);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getTransactionType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(entity.getCurrency()).isEqualTo(Currency.USD);
        assertThat(entity.getAmount()).isEqualByComparingTo(new BigDecimal("500.00"));
    }

    @Test
    void testToEntityFromPostTransactionDtoWithWithdrawal() {
        // Arrange
        PostTransactionDto dto = new PostTransactionDto(
                "0987654321",
                "acc-001",
                "acc-002",
                "1234567890",
                TransactionType.WITHDRAWAL,
                new BigDecimal("200.00"),
                Currency.EUR,
                "Withdrawal transaction"
        );

        // Act
        Transaction entity = TransactionMapper.ToEntity(dto);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getTransactionType()).isEqualTo(TransactionType.WITHDRAWAL);
        assertThat(entity.getCurrency()).isEqualTo(Currency.EUR);
        assertThat(entity.getAmount()).isEqualByComparingTo(new BigDecimal("200.00"));
    }

    @Test
    void testToEntityFromPutTransactionDtoWithCancelledStatus() {
        // Arrange
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

        // Act
        Transaction entity = TransactionMapper.ToEntity(dto, 1L, "TRX123");

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getStatus()).isEqualTo(TransactionStatus.CANCELLED);
    }

    @Test
    void testMappingPreservesAmountPrecision() {
        // Arrange
        PostTransactionDto dto = new PostTransactionDto(
                "0987654321",
                "acc-001",
                "acc-002",
                "1234567890",
                TransactionType.TRANSFER,
                new BigDecimal("123.456789"),
                Currency.RON,
                "Precision test"
        );

        // Act
        Transaction entity = TransactionMapper.ToEntity(dto);

        // Build a complete transaction for reverse mapping
        Transaction completeTransaction = Transaction.builder()
                .id(1L)
                .transactionId("TRX123")
                .fromAccountId(entity.getFromAccountId())
                .toAccountId(entity.getToAccountId())
                .fromAccountNumber("1234567890")
                .toAccountNumber("0987654321")
                .transactionType(entity.getTransactionType())
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .initiatedAt(LocalDateTime.now())
                .build();

        TransactionDto resultDto = TransactionMapper.ToDto(completeTransaction);

        // Assert
        assertThat(resultDto.getAmount()).isEqualByComparingTo(new BigDecimal("123.456789"));
    }

    @Test
    void testToEntityFromPutTransactionDtoPreservesAllFields() {
        // Arrange
        PutTransactionDto dto = new PutTransactionDto(
                TransactionType.PAYMENT,
                "from-account-123",
                "to-account-456",
                "FROM123456",
                "TO987654",
                new BigDecimal("999.99"),
                Currency.USD,
                "Complete field test",
                TransactionStatus.PENDING
        );

        // Act
        Transaction entity = TransactionMapper.ToEntity(dto, 42L, "TRX-CUSTOM-999");

        // Assert
        assertThat(entity.getId()).isEqualTo(42L);
        assertThat(entity.getTransactionId()).isEqualTo("TRX-CUSTOM-999");
        assertThat(entity.getFromAccountId()).isEqualTo("from-account-123");
        assertThat(entity.getToAccountId()).isEqualTo("to-account-456");
        assertThat(entity.getTransactionType()).isEqualTo(TransactionType.PAYMENT);
        assertThat(entity.getAmount()).isEqualByComparingTo(new BigDecimal("999.99"));
        assertThat(entity.getCurrency()).isEqualTo(Currency.USD);
        assertThat(entity.getDescription()).isEqualTo("Complete field test");
        assertThat(entity.getStatus()).isEqualTo(TransactionStatus.PENDING);
    }
}

