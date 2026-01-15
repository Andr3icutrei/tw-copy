package com.example.transactions.dto;

import com.example.transactions.dto.response.AccountDto;
import com.example.transactions.enums.AccountStatus;
import com.example.transactions.enums.AccountType;
import com.example.transactions.enums.Currency;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AccountDtoTests {

    @Test
    void testAccountDtoDefaultConstructor() {
        // Act
        AccountDto dto = new AccountDto();

        // Assert
        assertThat(dto).isNotNull();
    }

    @Test
    void testAccountDtoParameterizedConstructor() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime updated = now.plusDays(1);

        // Act
        AccountDto dto = new AccountDto(
                1L,
                "ACC123456789",
                100L,
                "John Doe",
                "john.doe@example.com",
                "+40123456789",
                AccountType.CHECKING,
                new BigDecimal("1000.00"),
                Currency.RON,
                AccountStatus.ACTIVE,
                true,
                now,
                updated
        );

        // Assert
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getAccountNumber()).isEqualTo("ACC123456789");
        assertThat(dto.getCustomerId()).isEqualTo(100L);
        assertThat(dto.getCustomerName()).isEqualTo("John Doe");
        assertThat(dto.getCustomerEmail()).isEqualTo("john.doe@example.com");
        assertThat(dto.getCustomerPhone()).isEqualTo("+40123456789");
        assertThat(dto.getAccountType()).isEqualTo(AccountType.CHECKING);
        assertThat(dto.getBalance()).isEqualByComparingTo(new BigDecimal("1000.00"));
        assertThat(dto.getCurrency()).isEqualTo(Currency.RON);
        assertThat(dto.getStatus()).isEqualTo(AccountStatus.ACTIVE);
        assertThat(dto.getIsVerified()).isTrue();
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(updated);
    }

    @Test
    void testAccountDtoSetters() {
        // Arrange
        AccountDto dto = new AccountDto();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime updated = now.plusHours(5);

        // Act
        dto.setId(2L);
        dto.setAccountNumber("ACC987654321");
        dto.setCustomerId(200L);
        dto.setCustomerName("Jane Smith");
        dto.setCustomerEmail("jane.smith@example.com");
        dto.setCustomerPhone("+40987654321");
        dto.setAccountType(AccountType.SAVINGS);
        dto.setBalance(new BigDecimal("5000.00"));
        dto.setCurrency(Currency.EUR);
        dto.setStatus(AccountStatus.BLOCKED);
        dto.setIsVerified(false);
        dto.setCreatedAt(now);
        dto.setUpdatedAt(updated);

        // Assert
        assertThat(dto.getId()).isEqualTo(2L);
        assertThat(dto.getAccountNumber()).isEqualTo("ACC987654321");
        assertThat(dto.getCustomerId()).isEqualTo(200L);
        assertThat(dto.getCustomerName()).isEqualTo("Jane Smith");
        assertThat(dto.getCustomerEmail()).isEqualTo("jane.smith@example.com");
        assertThat(dto.getCustomerPhone()).isEqualTo("+40987654321");
        assertThat(dto.getAccountType()).isEqualTo(AccountType.SAVINGS);
        assertThat(dto.getBalance()).isEqualByComparingTo(new BigDecimal("5000.00"));
        assertThat(dto.getCurrency()).isEqualTo(Currency.EUR);
        assertThat(dto.getStatus()).isEqualTo(AccountStatus.BLOCKED);
        assertThat(dto.getIsVerified()).isFalse();
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(updated);
    }

    @Test
    void testAccountDtoWithCheckingType() {
        // Arrange & Act
        AccountDto dto = new AccountDto(
                1L,
                "ACC123456789",
                100L,
                "John Doe",
                "john@example.com",
                "+40123456789",
                AccountType.CHECKING,
                new BigDecimal("1000.00"),
                Currency.RON,
                AccountStatus.ACTIVE,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Assert
        assertThat(dto.getAccountType()).isEqualTo(AccountType.CHECKING);
    }

    @Test
    void testAccountDtoWithSavingsType() {
        // Arrange & Act
        AccountDto dto = new AccountDto(
                1L,
                "ACC123456789",
                100L,
                "John Doe",
                "john@example.com",
                "+40123456789",
                AccountType.SAVINGS,
                new BigDecimal("5000.00"),
                Currency.EUR,
                AccountStatus.ACTIVE,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Assert
        assertThat(dto.getAccountType()).isEqualTo(AccountType.SAVINGS);
    }

    @Test
    void testAccountDtoWithBusinessType() {
        // Arrange & Act
        AccountDto dto = new AccountDto(
                1L,
                "ACC123456789",
                100L,
                "Business Corp",
                "business@example.com",
                "+40123456789",
                AccountType.BUSINESS,
                new BigDecimal("50000.00"),
                Currency.USD,
                AccountStatus.ACTIVE,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Assert
        assertThat(dto.getAccountType()).isEqualTo(AccountType.BUSINESS);
    }

    @Test
    void testAccountDtoWithActiveStatus() {
        // Arrange & Act
        AccountDto dto = new AccountDto(
                1L,
                "ACC123456789",
                100L,
                "John Doe",
                "john@example.com",
                "+40123456789",
                AccountType.CHECKING,
                new BigDecimal("1000.00"),
                Currency.RON,
                AccountStatus.ACTIVE,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Assert
        assertThat(dto.getStatus()).isEqualTo(AccountStatus.ACTIVE);
    }

    @Test
    void testAccountDtoWithBlockedStatus() {
        // Arrange & Act
        AccountDto dto = new AccountDto(
                1L,
                "ACC123456789",
                100L,
                "John Doe",
                "john@example.com",
                "+40123456789",
                AccountType.CHECKING,
                new BigDecimal("1000.00"),
                Currency.RON,
                AccountStatus.BLOCKED,
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Assert
        assertThat(dto.getStatus()).isEqualTo(AccountStatus.BLOCKED);
    }

    @Test
    void testAccountDtoWithPendingStatus() {
        // Arrange & Act
        AccountDto dto = new AccountDto(
                1L,
                "ACC123456789",
                100L,
                "John Doe",
                "john@example.com",
                "+40123456789",
                AccountType.CHECKING,
                new BigDecimal("0.00"),
                Currency.RON,
                AccountStatus.PENDING,
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Assert
        assertThat(dto.getStatus()).isEqualTo(AccountStatus.PENDING);
    }

    @Test
    void testAccountDtoWithClosedStatus() {
        // Arrange & Act
        AccountDto dto = new AccountDto(
                1L,
                "ACC123456789",
                100L,
                "John Doe",
                "john@example.com",
                "+40123456789",
                AccountType.CHECKING,
                new BigDecimal("0.00"),
                Currency.RON,
                AccountStatus.CLOSED,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Assert
        assertThat(dto.getStatus()).isEqualTo(AccountStatus.CLOSED);
    }

    @Test
    void testAccountDtoWithVerifiedTrue() {
        // Arrange & Act
        AccountDto dto = new AccountDto();
        dto.setIsVerified(true);

        // Assert
        assertThat(dto.getIsVerified()).isTrue();
    }

    @Test
    void testAccountDtoWithVerifiedFalse() {
        // Arrange & Act
        AccountDto dto = new AccountDto();
        dto.setIsVerified(false);

        // Assert
        assertThat(dto.getIsVerified()).isFalse();
    }

    @Test
    void testAccountDtoWithDifferentCurrencies() {
        // Test RON
        AccountDto ronDto = new AccountDto();
        ronDto.setCurrency(Currency.RON);
        ronDto.setBalance(new BigDecimal("1000.00"));
        assertThat(ronDto.getCurrency()).isEqualTo(Currency.RON);

        // Test EUR
        AccountDto eurDto = new AccountDto();
        eurDto.setCurrency(Currency.EUR);
        eurDto.setBalance(new BigDecimal("200.00"));
        assertThat(eurDto.getCurrency()).isEqualTo(Currency.EUR);

        // Test USD
        AccountDto usdDto = new AccountDto();
        usdDto.setCurrency(Currency.USD);
        usdDto.setBalance(new BigDecimal("300.00"));
        assertThat(usdDto.getCurrency()).isEqualTo(Currency.USD);
    }

    @Test
    void testAccountDtoWithZeroBalance() {
        // Arrange & Act
        AccountDto dto = new AccountDto();
        dto.setBalance(BigDecimal.ZERO);

        // Assert
        assertThat(dto.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void testAccountDtoWithLargeBalance() {
        // Arrange & Act
        AccountDto dto = new AccountDto();
        dto.setBalance(new BigDecimal("9999999999.99"));

        // Assert
        assertThat(dto.getBalance()).isEqualByComparingTo(new BigDecimal("9999999999.99"));
    }

    @Test
    void testAccountDtoWithDecimalBalance() {
        // Arrange & Act
        AccountDto dto = new AccountDto();
        dto.setBalance(new BigDecimal("1234.5678"));

        // Assert
        assertThat(dto.getBalance()).isEqualByComparingTo(new BigDecimal("1234.5678"));
    }

    @Test
    void testAccountDtoTimestamps() {
        // Arrange
        LocalDateTime created = LocalDateTime.of(2026, 1, 1, 10, 0, 0);
        LocalDateTime updated = LocalDateTime.of(2026, 1, 14, 15, 30, 0);

        AccountDto dto = new AccountDto();

        // Act
        dto.setCreatedAt(created);
        dto.setUpdatedAt(updated);

        // Assert
        assertThat(dto.getCreatedAt()).isEqualTo(created);
        assertThat(dto.getUpdatedAt()).isEqualTo(updated);
        assertThat(dto.getUpdatedAt()).isAfter(dto.getCreatedAt());
    }
}

