package com.example.transactions.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionStatusTests {

    @Test
    void testTransactionStatusValues() {
        // Act & Assert
        assertThat(TransactionStatus.values()).hasSize(4);
        assertThat(TransactionStatus.values()).containsExactlyInAnyOrder(
                TransactionStatus.PENDING,
                TransactionStatus.COMPLETED,
                TransactionStatus.FAILED,
                TransactionStatus.CANCELLED
        );
    }

    @Test
    void testTransactionStatusValueOf() {
        // Act & Assert
        assertThat(TransactionStatus.valueOf("PENDING")).isEqualTo(TransactionStatus.PENDING);
        assertThat(TransactionStatus.valueOf("COMPLETED")).isEqualTo(TransactionStatus.COMPLETED);
        assertThat(TransactionStatus.valueOf("FAILED")).isEqualTo(TransactionStatus.FAILED);
        assertThat(TransactionStatus.valueOf("CANCELLED")).isEqualTo(TransactionStatus.CANCELLED);
    }

    @Test
    void testTransactionStatusName() {
        // Assert
        assertThat(TransactionStatus.PENDING.name()).isEqualTo("PENDING");
        assertThat(TransactionStatus.COMPLETED.name()).isEqualTo("COMPLETED");
        assertThat(TransactionStatus.FAILED.name()).isEqualTo("FAILED");
        assertThat(TransactionStatus.CANCELLED.name()).isEqualTo("CANCELLED");
    }

    @Test
    void testTransactionStatusOrdinal() {
        // Assert - Ordinals based on declaration order
        assertThat(TransactionStatus.PENDING.ordinal()).isEqualTo(0);
        assertThat(TransactionStatus.COMPLETED.ordinal()).isEqualTo(1);
        assertThat(TransactionStatus.FAILED.ordinal()).isEqualTo(2);
        assertThat(TransactionStatus.CANCELLED.ordinal()).isEqualTo(3);
    }

    @Test
    void testTransactionStatusEquality() {
        // Assert
        assertThat(TransactionStatus.PENDING).isEqualTo(TransactionStatus.PENDING);
        assertThat(TransactionStatus.COMPLETED).isEqualTo(TransactionStatus.COMPLETED);
        assertThat(TransactionStatus.FAILED).isEqualTo(TransactionStatus.FAILED);
        assertThat(TransactionStatus.CANCELLED).isEqualTo(TransactionStatus.CANCELLED);
        assertThat(TransactionStatus.PENDING).isNotEqualTo(TransactionStatus.COMPLETED);
        assertThat(TransactionStatus.FAILED).isNotEqualTo(TransactionStatus.CANCELLED);
    }
}

