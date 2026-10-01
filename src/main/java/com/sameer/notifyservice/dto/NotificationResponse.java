package com.sameer.notifyservice.dto;

import com.sameer.notifyservice.model.NotificationRequest.NotificationStatus;
import com.sameer.notifyservice.model.NotificationRequest.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private UUID id;
    private NotificationType type;
    private String recipient;
    private String subject;
    private String body;
    private NotificationStatus status;
    private Integer retryCount;
    private String createdByEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}