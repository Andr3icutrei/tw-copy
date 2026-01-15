package com.example.transactions.service;

import com.example.transactions.client.AccountClient;
import com.example.transactions.client.NotificationClient;
import com.example.transactions.dto.request.PostTransactionDto;
import com.example.transactions.dto.request.PutTransactionDto;
import com.example.transactions.dto.response.AccountDto;
import com.example.transactions.dto.response.ModifyTransactionCurrencyDto;
import com.example.transactions.dto.response.NotificationDto;
import com.example.transactions.dto.response.TransactionDto;
import com.example.transactions.entity.Transaction;
import com.example.transactions.enums.Currency;
import com.example.transactions.enums.TransactionStatus;
import com.example.transactions.enums.TransactionType;
import com.example.transactions.repository.ITransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTests {

    @Mock
    private ITransactionRepository transactionRepository;

    @Mock
    private NotificationClient notificationClient;

    @Mock
    private AccountClient accountClient;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction transaction;
    private Transaction transaction2;

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setTransactionId("TRX123");
        transaction.setFromAccountId("acc-001");
        transaction.setToAccountId("acc-002");
        transaction.setFromAccountNumber("1234567890");
        transaction.setToAccountNumber("0987654321");
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setCurrency(Currency.RON);
        transaction.setDescription("Test transaction");
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setInitiatedAt(LocalDateTime.now());

        transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setTransactionId("TRX456");
        transaction2.setFromAccountId("acc-003");
        transaction2.setToAccountId("acc-004");
        transaction2.setFromAccountNumber("1111111111");
        transaction2.setToAccountNumber("2222222222");
        transaction2.setTransactionType(TransactionType.PAYMENT);
        transaction2.setAmount(new BigDecimal("200.00"));
        transaction2.setCurrency(Currency.EUR);
        transaction2.setStatus(TransactionStatus.COMPLETED);
        transaction2.setInitiatedAt(LocalDateTime.now());
    }

    @Test
    void postTransaction_Success() {
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

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionDto result = transactionService.postTransaction(dto);

        assertNotNull(result);
        assertNotNull(result.getTransactionId());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void fetchTransactionById_Success() {
        when(transactionRepository.findTransactionByTransactionId("TRX123")).thenReturn(Optional.of(transaction));

        TransactionDto result = transactionService.fetchTransactionById("TRX123");

        assertEquals("TRX123", result.getTransactionId());
    }

    @Test
    void fetchTransactionById_NotFound() {
        when(transactionRepository.findTransactionByTransactionId("INVALID")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> transactionService.fetchTransactionById("INVALID"));
    }

    @Test
    void putTransaction_Success() {
        PutTransactionDto updateDto = new PutTransactionDto(
                TransactionType.PAYMENT,
                "acc-001",
                "acc-002",
                "1234567890",
                "0987654321",
                new BigDecimal("150.00"),
                Currency.EUR,
                "Updated transaction",
                TransactionStatus.PENDING
        );

        when(transactionRepository.findTransactionByTransactionId("TRX123")).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        boolean result = transactionService.putTransaction(updateDto, "TRX123");

        assertTrue(result);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void cancelTransactionById_Success() {
        when(transactionRepository.findTransactionByTransactionId("TRX123")).thenReturn(Optional.of(transaction));

        boolean result = transactionService.cancelTransactionById("TRX123");

        assertTrue(result);
        assertEquals(TransactionStatus.CANCELLED, transaction.getStatus());
        verify(transactionRepository).save(transaction);
    }

    @Test
    void executePaymentByTransactionId_Success() {
        when(transactionRepository.findTransactionByTransactionId("TRX123")).thenReturn(Optional.of(transaction));

        boolean result = transactionService.executePaymentByTransactionId("TRX123");

        assertTrue(result);
        assertEquals(TransactionStatus.COMPLETED, transaction.getStatus());
        verify(transactionRepository).save(transaction);
    }

    @Test
    void modifyTransactionType_Success() {
        when(transactionRepository.findTransactionByTransactionId("TRX123")).thenReturn(Optional.of(transaction));

        boolean result = transactionService.modifyTransactionType("TRX123", TransactionType.DEPOSIT);

        assertTrue(result);
        assertEquals(TransactionType.DEPOSIT, transaction.getTransactionType());
        verify(transactionRepository).save(transaction);
    }

    @Test
    void modifyTransactionCurrency_Success() {
        when(transactionRepository.findTransactionByTransactionId("TRX123")).thenReturn(Optional.of(transaction));

        ModifyTransactionCurrencyDto result = transactionService.modifyTransactionCurrency("TRX123", Currency.EUR);

        assertNotNull(result);
        assertEquals("TRX123", result.getTransactionId());
        assertEquals(Currency.EUR, result.getCurrency());
        verify(transactionRepository).save(transaction);
    }

    @Test
    void calculateTransactionAmount_Transfer() {
        transaction.setTransactionType(TransactionType.TRANSFER);
        when(transactionRepository.findTransactionByTransactionId("TRX123")).thenReturn(Optional.of(transaction));

        BigDecimal result = transactionService.calculateTransactionAmount("TRX123");

        assertEquals(0, new BigDecimal("1.00").compareTo(result));
    }

    @Test
    void calculateTransactionAmount_Withdrawal() {
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        when(transactionRepository.findTransactionByTransactionId("TRX123")).thenReturn(Optional.of(transaction));

        BigDecimal result = transactionService.calculateTransactionAmount("TRX123");

        assertEquals(new BigDecimal("2.50"), result);
    }

    @Test
    void calculateTransactionAmount_Deposit() {
        transaction.setTransactionType(TransactionType.DEPOSIT);
        when(transactionRepository.findTransactionByTransactionId("TRX123")).thenReturn(Optional.of(transaction));

        BigDecimal result = transactionService.calculateTransactionAmount("TRX123");

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void calculateTransactionAmount_Payment() {
        transaction.setTransactionType(TransactionType.PAYMENT);
        when(transactionRepository.findTransactionByTransactionId("TRX123")).thenReturn(Optional.of(transaction));

        BigDecimal result = transactionService.calculateTransactionAmount("TRX123");

        assertEquals(0, new BigDecimal("1.50").compareTo(result));
    }

    @Test
    void calculateTransactionAmount_NotFound() {
        when(transactionRepository.findTransactionByTransactionId("INVALID")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> transactionService.calculateTransactionAmount("INVALID"));
    }

    @Test
    void antiFraudCheck_Success() {
        transaction.setStatus(TransactionStatus.COMPLETED);
        when(transactionRepository.findTransactionByTransactionId("TRX123")).thenReturn(Optional.of(transaction));

        String result = transactionService.antiFraudCheck("TRX123");

        assertNotNull(result);
    }

    @Test
    void antiFraudCheck_NotCompleted() {
        transaction.setStatus(TransactionStatus.PENDING);
        when(transactionRepository.findTransactionByTransactionId("TRX123")).thenReturn(Optional.of(transaction));

        assertThrows(RuntimeException.class, () -> transactionService.antiFraudCheck("TRX123"));
    }

    @Test
    void postTransactionWithAccountDetails_Success() {
        PostTransactionDto dto = new PostTransactionDto(
                "0987654321",
                "acc-001",
                "acc-002",
                "1234567890",
                TransactionType.TRANSFER,
                new BigDecimal("100.00"),
                Currency.RON,
                "Test"
        );

        AccountDto fromAccount = new AccountDto();
        fromAccount.setCustomerName("John Doe");
        AccountDto toAccount = new AccountDto();
        toAccount.setCustomerName("Jane Smith");

        when(accountClient.fetchAccount("1234567890")).thenReturn(ResponseEntity.ok(fromAccount));
        when(accountClient.fetchAccount("0987654321")).thenReturn(ResponseEntity.ok(toAccount));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        String result = transactionService.postTransactionWithAccountDetails(dto);

        assertNotNull(result);
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("Jane Smith"));
        verify(accountClient).fetchAccount("1234567890");
        verify(accountClient).fetchAccount("0987654321");
    }

    @Test
    void postTransactionWithAccountDetails_Failure() {
        PostTransactionDto dto = new PostTransactionDto(
                "0987654321",
                "acc-001",
                "acc-002",
                "1234567890",
                TransactionType.TRANSFER,
                new BigDecimal("100.00"),
                Currency.RON,
                "Test"
        );

        when(accountClient.fetchAccount(anyString())).thenThrow(new RuntimeException("Account not found"));

        assertThrows(RuntimeException.class, () -> transactionService.postTransactionWithAccountDetails(dto));
    }

    @Test
    void fetchTransactionWithAccountDetailsById_Success() {
        AccountDto fromAccount = new AccountDto();
        fromAccount.setCustomerName("John Doe");
        AccountDto toAccount = new AccountDto();
        toAccount.setCustomerName("Jane Smith");

        when(transactionRepository.findTransactionByTransactionId("TRX123")).thenReturn(Optional.of(transaction));
        when(accountClient.fetchAccount("1234567890")).thenReturn(ResponseEntity.ok(fromAccount));
        when(accountClient.fetchAccount("0987654321")).thenReturn(ResponseEntity.ok(toAccount));

        String result = transactionService.fetchTransactionWithAccountDetailsById("TRX123");

        assertNotNull(result);
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("Jane Smith"));
    }

    @Test
    void fetchTransactionWithAccountDetailsById_NotFound() {
        when(transactionRepository.findTransactionByTransactionId("INVALID")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> transactionService.fetchTransactionWithAccountDetailsById("INVALID"));
    }

    @Test
    void postTransactionWithNotification_Success() {
        PostTransactionDto dto = new PostTransactionDto(
                "0987654321",
                "acc-001",
                "acc-002",
                "1234567890",
                TransactionType.TRANSFER,
                new BigDecimal("100.00"),
                Currency.RON,
                "Test"
        );

        AccountDto fromAccount = new AccountDto();
        fromAccount.setCustomerName("John Doe");
        fromAccount.setCustomerId(1L);
        fromAccount.setCustomerEmail("john@test.com");
        fromAccount.setCustomerPhone("0712345678");

        AccountDto toAccount = new AccountDto();
        toAccount.setCustomerName("Jane Smith");
        toAccount.setCustomerId(2L);
        toAccount.setCustomerEmail("jane@test.com");
        toAccount.setCustomerPhone("0787654321");

        NotificationDto notification = new NotificationDto();
        notification.setMessage("Transaction Created");

        when(accountClient.fetchAccount("1234567890")).thenReturn(ResponseEntity.ok(fromAccount));
        when(accountClient.fetchAccount("0987654321")).thenReturn(ResponseEntity.ok(toAccount));
        when(notificationClient.createNotification(any())).thenReturn(ResponseEntity.ok(notification));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        String result = transactionService.postTransactionWithNotification(dto);

        assertNotNull(result);
        assertTrue(result.contains("Transaction Created"));
        verify(notificationClient).createNotification(any());
    }

    @Test
    void postTransactionWithNotification_Failure() {
        PostTransactionDto dto = new PostTransactionDto(
                "0987654321",
                "acc-001",
                "acc-002",
                "1234567890",
                TransactionType.TRANSFER,
                new BigDecimal("100.00"),
                Currency.RON,
                "Test"
        );

        when(accountClient.fetchAccount(anyString())).thenThrow(new RuntimeException("Account not found"));

        assertThrows(RuntimeException.class, () -> transactionService.postTransactionWithNotification(dto));
    }
}
