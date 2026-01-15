package com.example.transactions.controller;

import com.example.transactions.dto.request.PostTransactionDto;
import com.example.transactions.dto.request.PutTransactionDto;
import com.example.transactions.dto.response.ModifyTransactionCurrencyDto;
import com.example.transactions.dto.response.TransactionDto;
import com.example.transactions.enums.Currency;
import com.example.transactions.enums.TransactionStatus;
import com.example.transactions.enums.TransactionType;
import com.example.transactions.service.ITransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransactionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ITransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private TransactionDto transactionDto;

    @BeforeEach
    void setUp() {
        transactionDto = new TransactionDto(
                "acc-002",
                "TRX123",
                "acc-001",
                "1234567890",
                "0987654321",
                TransactionType.TRANSFER,
                new BigDecimal("100.00"),
                Currency.RON,
                "Test transaction",
                TransactionStatus.PENDING,
                null
        );
    }

    @Test
    void postTransaction_ShouldReturnCreated() throws Exception {
        PostTransactionDto createDto = new PostTransactionDto(
                "0987654321",
                "acc-001",
                "acc-002",
                "1234567890",
                TransactionType.TRANSFER,
                new BigDecimal("100.00"),
                Currency.RON,
                "Test transaction"
        );

        given(transactionService.postTransaction(any(PostTransactionDto.class))).willReturn(transactionDto);

        mockMvc.perform(post("/api/transactions/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value("TRX123"));
    }

    @Test
    void fetchTransaction_ShouldReturnTransaction() throws Exception {
        given(transactionService.fetchTransactionById("TRX123")).willReturn(transactionDto);

        mockMvc.perform(get("/api/transactions/get/TRX123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("TRX123"));
    }

    @Test
    void putTransaction_ShouldReturnSuccess() throws Exception {
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

        given(transactionService.putTransaction(any(PutTransactionDto.class), anyString())).willReturn(true);

        mockMvc.perform(put("/api/transactions/put/TRX123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction put successfully"));
    }

    @Test
    void putTransaction_ShouldReturnNotFound() throws Exception {
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

        given(transactionService.putTransaction(any(PutTransactionDto.class), anyString())).willReturn(false);

        mockMvc.perform(put("/api/transactions/put/INVALID")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Transaction not found"));
    }

    @Test
    void cancelTransaction_ShouldReturnSuccess() throws Exception {
        given(transactionService.cancelTransactionById("TRX123")).willReturn(true);

        mockMvc.perform(delete("/api/transactions/close/TRX123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction canceled successfully"));
    }

    @Test
    void cancelTransaction_ShouldReturnNotFound() throws Exception {
        given(transactionService.cancelTransactionById("INVALID")).willReturn(false);

        mockMvc.perform(delete("/api/transactions/close/INVALID"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Transaction not found"));
    }

    @Test
    void modifyTransactionType_ShouldReturnSuccess() throws Exception {
        given(transactionService.modifyTransactionType("TRX123", TransactionType.DEPOSIT)).willReturn(true);

        mockMvc.perform(patch("/api/transactions/modify-transaction-type/TRX123")
                        .param("newType", "DEPOSIT"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction type modified successfully"));
    }

    @Test
    void modifyTransactionType_ShouldReturnNotFound() throws Exception {
        given(transactionService.modifyTransactionType("INVALID", TransactionType.DEPOSIT)).willReturn(false);

        mockMvc.perform(patch("/api/transactions/modify-transaction-type/INVALID")
                        .param("newType", "DEPOSIT"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Transaction not found"));
    }

    @Test
    void modifyTransactionCurrency_ShouldReturnSuccess() throws Exception {
        ModifyTransactionCurrencyDto modifyDto = new ModifyTransactionCurrencyDto(
                "TRX123",
                Currency.EUR,
                new BigDecimal("20.00")
        );

        given(transactionService.modifyTransactionCurrency("TRX123", Currency.EUR)).willReturn(modifyDto);

        mockMvc.perform(patch("/api/transactions/modify-currency/TRX123")
                        .param("currency", "EUR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("TRX123"))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void modifyTransactionCurrency_ShouldReturnNotFound() throws Exception {
        given(transactionService.modifyTransactionCurrency("INVALID", Currency.EUR)).willReturn(null);

        mockMvc.perform(patch("/api/transactions/modify-currency/INVALID")
                        .param("currency", "EUR"))
                .andExpect(status().isNotFound());
    }

    @Test
    void completePayment_ShouldReturnSuccess() throws Exception {
        given(transactionService.executePaymentByTransactionId("TRX123")).willReturn(true);

        mockMvc.perform(patch("/api/transactions/complete-payment/TRX123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction completed successfully"));
    }

    @Test
    void completePayment_ShouldReturnNotFound() throws Exception {
        given(transactionService.executePaymentByTransactionId("INVALID")).willReturn(false);

        mockMvc.perform(patch("/api/transactions/complete-payment/INVALID"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Transaction not found"));
    }

    @Test
    void calculateFees_ShouldReturnFees() throws Exception {
        given(transactionService.calculateTransactionAmount("TRX123")).willReturn(new BigDecimal("2.50"));

        mockMvc.perform(get("/api/transactions/calculate-fees/TRX123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Calculated fees: 2.50"));
    }

    @Test
    void antiFraudCheck_ShouldReturnScore() throws Exception {
        given(transactionService.antiFraudCheck("TRX123")).willReturn("LOW");

        mockMvc.perform(get("/api/transactions/anti-fraud-check/TRX123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Anti fraud score:LOW"));
    }

    @Test
    void createWithAccountDetails_ShouldReturnCreated() throws Exception {
        PostTransactionDto createDto = new PostTransactionDto(
                "0987654321",
                "acc-001",
                "acc-002",
                "1234567890",
                TransactionType.TRANSFER,
                new BigDecimal("100.00"),
                Currency.RON,
                "Test"
        );

        String response = "Transaction created successfully with account details: customer nameJohn Doe to Jane Smith";
        given(transactionService.postTransactionWithAccountDetails(any(PostTransactionDto.class))).willReturn(response);

        mockMvc.perform(post("/api/transactions/create_with_account_details")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string(response));
    }

    @Test
    void getWithAccountDetails_ShouldReturnDetails() throws Exception {
        String response = "Transaction created successfully with account details: customer nameJohn Doe to Jane Smith";
        given(transactionService.fetchTransactionWithAccountDetailsById("TRX123")).willReturn(response);

        mockMvc.perform(get("/api/transactions/get_with_account_details/TRX123"))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }

    @Test
    void createWithNotification_ShouldReturnCreated() throws Exception {
        PostTransactionDto createDto = new PostTransactionDto(
                "0987654321",
                "acc-001",
                "acc-002",
                "1234567890",
                TransactionType.TRANSFER,
                new BigDecimal("100.00"),
                Currency.RON,
                "Test"
        );

        String response = "Transaction created successfully with new notification: Transaction Created";
        given(transactionService.postTransactionWithNotification(any(PostTransactionDto.class))).willReturn(response);

        mockMvc.perform(post("/api/transactions/create_with_notification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string(response));
    }
}

