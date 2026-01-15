package com.example.transactions.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionTypeTests {

    @Test
    void testTransactionTypeValues() {
        // Act & Assert
        assertThat(TransactionType.values()).hasSize(4);
        assertThat(TransactionType.values()).containsExactlyInAnyOrder(
                TransactionType.TRANSFER,
                TransactionType.DEPOSIT,
                TransactionType.WITHDRAWAL,
                TransactionType.PAYMENT
        );
    }

    @Test
    void testTransactionTypeValueOf() {
        // Act & Assert
        assertThat(TransactionType.valueOf("TRANSFER")).isEqualTo(TransactionType.TRANSFER);
        assertThat(TransactionType.valueOf("DEPOSIT")).isEqualTo(TransactionType.DEPOSIT);
        assertThat(TransactionType.valueOf("WITHDRAWAL")).isEqualTo(TransactionType.WITHDRAWAL);
        assertThat(TransactionType.valueOf("PAYMENT")).isEqualTo(TransactionType.PAYMENT);
    }

    @Test
    void testTransactionTypeName() {
        // Assert
        assertThat(TransactionType.TRANSFER.name()).isEqualTo("TRANSFER");
        assertThat(TransactionType.DEPOSIT.name()).isEqualTo("DEPOSIT");
        assertThat(TransactionType.WITHDRAWAL.name()).isEqualTo("WITHDRAWAL");
        assertThat(TransactionType.PAYMENT.name()).isEqualTo("PAYMENT");
    }

    @Test
    void testTransactionTypeOrdinal() {
        // Assert - Ordinals based on declaration order
        assertThat(TransactionType.TRANSFER.ordinal()).isEqualTo(0);
        assertThat(TransactionType.DEPOSIT.ordinal()).isEqualTo(1);
        assertThat(TransactionType.WITHDRAWAL.ordinal()).isEqualTo(2);
        assertThat(TransactionType.PAYMENT.ordinal()).isEqualTo(3);
    }

    @Test
    void testTransactionTypeEquality() {
        // Assert
        assertThat(TransactionType.TRANSFER).isEqualTo(TransactionType.TRANSFER);
        assertThat(TransactionType.DEPOSIT).isEqualTo(TransactionType.DEPOSIT);
        assertThat(TransactionType.WITHDRAWAL).isEqualTo(TransactionType.WITHDRAWAL);
        assertThat(TransactionType.PAYMENT).isEqualTo(TransactionType.PAYMENT);
        assertThat(TransactionType.TRANSFER).isNotEqualTo(TransactionType.DEPOSIT);
        assertThat(TransactionType.WITHDRAWAL).isNotEqualTo(TransactionType.PAYMENT);
    }
}

