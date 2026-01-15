package com.example.transactions.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CurrencyTests {

    @Test
    void testCurrencyValues() {
        // Act & Assert
        assertThat(Currency.values()).hasSize(3);
        assertThat(Currency.values()).containsExactlyInAnyOrder(
                Currency.RON,
                Currency.EUR,
                Currency.USD
        );
    }

    @Test
    void testCurrencyRONValue() {
        // Assert
        assertThat(Currency.RON.getValue()).isEqualTo((byte) 1);
    }

    @Test
    void testCurrencyEURValue() {
        // Assert
        assertThat(Currency.EUR.getValue()).isEqualTo((byte) 5);
    }

    @Test
    void testCurrencyUSDValue() {
        // Assert
        assertThat(Currency.USD.getValue()).isEqualTo((byte) 4);
    }

    @Test
    void testCurrencyValueOf() {
        // Act & Assert
        assertThat(Currency.valueOf("RON")).isEqualTo(Currency.RON);
        assertThat(Currency.valueOf("EUR")).isEqualTo(Currency.EUR);
        assertThat(Currency.valueOf("USD")).isEqualTo(Currency.USD);
    }

    @Test
    void testCurrencyName() {
        // Assert
        assertThat(Currency.RON.name()).isEqualTo("RON");
        assertThat(Currency.EUR.name()).isEqualTo("EUR");
        assertThat(Currency.USD.name()).isEqualTo("USD");
    }

    @Test
    void testCurrencyOrdinal() {
        // Assert - Ordinals based on declaration order
        assertThat(Currency.RON.ordinal()).isEqualTo(0);
        assertThat(Currency.EUR.ordinal()).isEqualTo(1);
        assertThat(Currency.USD.ordinal()).isEqualTo(2);
    }

    @Test
    void testCurrencyEquality() {
        // Assert
        assertThat(Currency.RON).isEqualTo(Currency.RON);
        assertThat(Currency.EUR).isEqualTo(Currency.EUR);
        assertThat(Currency.USD).isEqualTo(Currency.USD);
        assertThat(Currency.RON).isNotEqualTo(Currency.EUR);
        assertThat(Currency.EUR).isNotEqualTo(Currency.USD);
    }
}

