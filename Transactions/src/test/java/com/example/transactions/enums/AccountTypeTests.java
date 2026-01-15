package com.example.transactions.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTypeTests {

    @Test
    void testAccountTypeValues() {
        // Act & Assert
        assertThat(AccountType.values()).hasSize(3);
        assertThat(AccountType.values()).containsExactlyInAnyOrder(
                AccountType.SAVINGS,
                AccountType.CHECKING,
                AccountType.BUSINESS
        );
    }

    @Test
    void testAccountTypeValueOf() {
        // Act & Assert
        assertThat(AccountType.valueOf("SAVINGS")).isEqualTo(AccountType.SAVINGS);
        assertThat(AccountType.valueOf("CHECKING")).isEqualTo(AccountType.CHECKING);
        assertThat(AccountType.valueOf("BUSINESS")).isEqualTo(AccountType.BUSINESS);
    }

    @Test
    void testAccountTypeName() {
        // Assert
        assertThat(AccountType.SAVINGS.name()).isEqualTo("SAVINGS");
        assertThat(AccountType.CHECKING.name()).isEqualTo("CHECKING");
        assertThat(AccountType.BUSINESS.name()).isEqualTo("BUSINESS");
    }

    @Test
    void testAccountTypeOrdinal() {
        // Assert - Ordinals based on declaration order
        assertThat(AccountType.SAVINGS.ordinal()).isEqualTo(0);
        assertThat(AccountType.CHECKING.ordinal()).isEqualTo(1);
        assertThat(AccountType.BUSINESS.ordinal()).isEqualTo(2);
    }

    @Test
    void testAccountTypeEquality() {
        // Assert
        assertThat(AccountType.SAVINGS).isEqualTo(AccountType.SAVINGS);
        assertThat(AccountType.CHECKING).isEqualTo(AccountType.CHECKING);
        assertThat(AccountType.BUSINESS).isEqualTo(AccountType.BUSINESS);
        assertThat(AccountType.SAVINGS).isNotEqualTo(AccountType.CHECKING);
        assertThat(AccountType.CHECKING).isNotEqualTo(AccountType.BUSINESS);
    }
}

