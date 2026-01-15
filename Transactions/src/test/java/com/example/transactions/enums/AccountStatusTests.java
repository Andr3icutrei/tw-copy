package com.example.transactions.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountStatusTests {

    @Test
    void testAccountStatusValues() {
        // Act & Assert
        assertThat(AccountStatus.values()).hasSize(4);
        assertThat(AccountStatus.values()).containsExactlyInAnyOrder(
                AccountStatus.ACTIVE,
                AccountStatus.BLOCKED,
                AccountStatus.PENDING,
                AccountStatus.CLOSED
        );
    }

    @Test
    void testAccountStatusValueOf() {
        // Act & Assert
        assertThat(AccountStatus.valueOf("ACTIVE")).isEqualTo(AccountStatus.ACTIVE);
        assertThat(AccountStatus.valueOf("BLOCKED")).isEqualTo(AccountStatus.BLOCKED);
        assertThat(AccountStatus.valueOf("PENDING")).isEqualTo(AccountStatus.PENDING);
        assertThat(AccountStatus.valueOf("CLOSED")).isEqualTo(AccountStatus.CLOSED);
    }

    @Test
    void testAccountStatusName() {
        // Assert
        assertThat(AccountStatus.ACTIVE.name()).isEqualTo("ACTIVE");
        assertThat(AccountStatus.BLOCKED.name()).isEqualTo("BLOCKED");
        assertThat(AccountStatus.PENDING.name()).isEqualTo("PENDING");
        assertThat(AccountStatus.CLOSED.name()).isEqualTo("CLOSED");
    }

    @Test
    void testAccountStatusOrdinal() {
        // Assert - Ordinals based on declaration order
        assertThat(AccountStatus.ACTIVE.ordinal()).isEqualTo(0);
        assertThat(AccountStatus.BLOCKED.ordinal()).isEqualTo(1);
        assertThat(AccountStatus.PENDING.ordinal()).isEqualTo(2);
        assertThat(AccountStatus.CLOSED.ordinal()).isEqualTo(3);
    }

    @Test
    void testAccountStatusEquality() {
        // Assert
        assertThat(AccountStatus.ACTIVE).isEqualTo(AccountStatus.ACTIVE);
        assertThat(AccountStatus.BLOCKED).isEqualTo(AccountStatus.BLOCKED);
        assertThat(AccountStatus.PENDING).isEqualTo(AccountStatus.PENDING);
        assertThat(AccountStatus.CLOSED).isEqualTo(AccountStatus.CLOSED);
        assertThat(AccountStatus.ACTIVE).isNotEqualTo(AccountStatus.BLOCKED);
        assertThat(AccountStatus.PENDING).isNotEqualTo(AccountStatus.CLOSED);
    }
}

