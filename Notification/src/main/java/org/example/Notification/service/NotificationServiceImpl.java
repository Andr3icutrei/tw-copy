package org.example.Notification.service;

import org.example.Notification.dto.request.NotificationCreateDto;
import org.example.Notification.dto.request.NotificationUpdateDto;
import org.example.Notification.dto.response.NotificationDto;
import org.example.Notification.dto.response.NotificationStatusDto;
import org.example.Notification.entity.Notification;
import org.example.Notification.enums.NotificationStatus;
import org.example.Notification.mapper.NotificationMapper;
import org.example.Notification.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements INotificationService {

    @Autowired
    private NotificationRepository notificationRepository;



    @Override
    @Transactional
    public NotificationDto createNotification(NotificationCreateDto notificationCreateDto) {
        Notification notification = NotificationMapper.toEntity(notificationCreateDto);
        notification.setStatus(NotificationStatus.PENDING);
        Notification savedNotification = notificationRepository.save(notification);
        return NotificationMapper.toDto(savedNotification);
    }

    @Override
    public NotificationDto fetchNotification(String notificationId) {
        Notification notification = notificationRepository.findByNotificationId(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + notificationId));
        return NotificationMapper.toDto(notification);
    }

    @Override
    @Transactional
    public NotificationDto updateNotificationSettings(NotificationUpdateDto notificationUpdateDto) {
        Notification notification = notificationRepository.findByNotificationId(notificationUpdateDto.getNotificationId())
                .orElseThrow(() -> new RuntimeException("Notification not found: " + notificationUpdateDto.getNotificationId()));
        NotificationMapper.updateEntityFromDto(notification, notificationUpdateDto);
        Notification updatedNotification = notificationRepository.save(notification);
        return NotificationMapper.toDto(updatedNotification);
    }

    @Override
    @Transactional
    public boolean deleteExpiredNotifications(int retentionDays) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(retentionDays);
        List<Notification> oldNotifications = notificationRepository.findByCreatedAtBefore(cutoffDate);
        if (oldNotifications.isEmpty()) {
            return false;
        }
        notificationRepository.deleteOldNotifications(cutoffDate);
        return true;
    }

    @Override
    @Transactional
    public String resendFailedNotification(String notificationId) {
        Notification notification = notificationRepository.findByNotificationId(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + notificationId));

        if (notification.getStatus() != NotificationStatus.FAILED) {
            throw new RuntimeException("Notification is not in FAILED status");
        }

        if (notification.getRetryCount() >= notification.getMaxRetries()) {
            throw new RuntimeException("Maximum retry attempts reached");
        }

        notification.setRetryCount(notification.getRetryCount() + 1);
        notification.setStatus(NotificationStatus.PENDING);
        notification.setFailureReason(null);
        notification.setFailedAt(null);

        notificationRepository.save(notification);

        return "Notification resent successfully. Retry count: " + notification.getRetryCount();
    }

    @Override
    @Transactional
    public NotificationDto scheduleNotification(NotificationCreateDto notificationCreateDto) {
        if (notificationCreateDto.getScheduledAt() == null) {
            throw new RuntimeException("Scheduled time must be provided");
        }

        if (notificationCreateDto.getScheduledAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Scheduled time must be in the future");
        }

        Notification notification = NotificationMapper.toEntity(notificationCreateDto);
        notification.setStatus(NotificationStatus.SCHEDULED);
        Notification savedNotification = notificationRepository.save(notification);
        return NotificationMapper.toDto(savedNotification);
    }

    @Override
    @Transactional
    public String sendSmsNotification(String notificationId) {
        Notification notification = notificationRepository.findByNotificationId(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + notificationId));

        if (notification.getNotificationType() != org.example.Notification.enums.NotificationType.SMS) {
            throw new RuntimeException("Notification is not of type SMS");
        }

        if (notification.getRecipientPhone() == null || notification.getRecipientPhone().isEmpty()) {
            throw new RuntimeException("Recipient phone number is missing");
        }

        try {
            System.out.println("Sending SMS to: " + notification.getRecipientPhone());
            System.out.println("Message: " + notification.getMessage());

            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
            notification.setDeliveredAt(LocalDateTime.now());

            notificationRepository.save(notification);

            return "SMS sent successfully to " + notification.getRecipientPhone();

        } catch (Exception e) {
            notification.setStatus(NotificationStatus.FAILED);
            notification.setFailedAt(LocalDateTime.now());
            notification.setFailureReason("SMS sending failed: " + e.getMessage());
            notificationRepository.save(notification);

            throw new RuntimeException("Failed to send SMS: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public String sendEmailNotification(String notificationId) {
        Notification notification = notificationRepository.findByNotificationId(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + notificationId));

        if (notification.getNotificationType() != org.example.Notification.enums.NotificationType.EMAIL) {
            throw new RuntimeException("Notification is not of type EMAIL");
        }

        if (notification.getRecipientEmail() == null || notification.getRecipientEmail().isEmpty()) {
            throw new RuntimeException("Recipient email is missing");
        }

        try {
            System.out.println("Sending Email to: " + notification.getRecipientEmail());
            System.out.println("Subject: " + notification.getSubject());
            System.out.println("Message: " + notification.getMessage());

            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
            notification.setDeliveredAt(LocalDateTime.now());

            notificationRepository.save(notification);

            return "Email sent successfully to " + notification.getRecipientEmail();

        } catch (Exception e) {
            notification.setStatus(NotificationStatus.FAILED);
            notification.setFailedAt(LocalDateTime.now());
            notification.setFailureReason("Email sending failed: " + e.getMessage());
            notificationRepository.save(notification);

            throw new RuntimeException("Failed to send Email: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public String markAsRead(String notificationId) {
        Notification notification = notificationRepository.findByNotificationId(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + notificationId));

        if (notification.getStatus() == NotificationStatus.READ) {
            return "Notification already marked as read";
        }

        notification.setStatus(NotificationStatus.READ);
        notificationRepository.save(notification);

        return "Notification marked as read successfully";
    }

    @Override
    public NotificationStatusDto getNotificationStatus(String notificationId) {
        Notification notification = notificationRepository.findByNotificationId(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + notificationId));
        return NotificationMapper.toStatusDto(notification);
    }

    @Override
    public List<NotificationDto> getNotificationHistory(Long recipientId) {
        List<Notification> notifications = notificationRepository.findByRecipientId(recipientId);
        return notifications.stream()
                .map(NotificationMapper::toDto)
                .collect(Collectors.toList());
    }
}