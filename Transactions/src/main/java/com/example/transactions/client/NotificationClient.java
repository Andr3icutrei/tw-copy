package com.example.transactions.client;

import com.example.transactions.dto.request.NotificationCreateDto;
import com.example.transactions.dto.response.NotificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "notification", path = "/api/notifications")
public interface NotificationClient {

    @PostMapping("/create")
    ResponseEntity<NotificationDto> createNotification(@RequestBody NotificationCreateDto notificationCreateDto);
}
