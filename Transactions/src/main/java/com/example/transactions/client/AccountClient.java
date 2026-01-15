package com.example.transactions.client;

import com.example.transactions.dto.response.AccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "accountmanagement", path = "/api/accounts")
public interface AccountClient {

    @GetMapping("/fetch_general_data")
    ResponseEntity<AccountDto> fetchAccount(@RequestParam String accountNumber);
}
