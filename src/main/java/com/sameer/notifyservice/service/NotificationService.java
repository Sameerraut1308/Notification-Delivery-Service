package com.sameer.notifyservice.service;

import com.sameer.notifyservice.dto.CreateNotificationRequest;
import com.sameer.notifyservice.dto.NotificationResponse;
import com.sameer.notifyservice.exception.BadRequestException;
import com.sameer.notifyservice.exception.ResourceNotFoundException;
import com.sameer.notifyservice.model.NotificationRequest;
import com.sameer.notifyservice.model.NotificationRequest.NotificationStatus;
import com.sameer.notifyservice.model.NotificationRequest.NotificationType;
import com.sameer.notifyservice.model.User;
import com.sameer.notifyservice.repository.NotificationRequestRepository;
import com.sameer.notifyservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRequestRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public NotificationResponse createNotification(CreateNotificationRequest request, String userEmail) {
        // 1. Fetch authenticated user
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        // 2. Extra business validation: EMAIL requires a subject
        if (request.getType() == NotificationType.EMAIL && (request.getSubject() == null || request.getSubject().isBlank())) {
            throw new BadRequestException("Subject is required for EMAIL notifications");
        }

        // 3. Build entity with initial QUEUED status
        NotificationRequest notification = NotificationRequest.builder()
                .type(request.getType())
                .recipient(request.getRecipient().trim())
                .subject(request.getSubject() != null ? request.getSubject().trim() : null)
                .body(request.getBody().trim())
                .status(NotificationStatus.QUEUED)
                .retryCount(0)
                .createdBy(user)
                .build();

        NotificationRequest saved = notificationRepository.save(notification);

        return mapToResponse(saved);
    }

    public NotificationResponse mapToResponse(NotificationRequest n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .type(n.getType())
                .recipient(n.getRecipient())
                .subject(n.getSubject())
                .body(n.getBody())
                .status(n.getStatus())
                .retryCount(n.getRetryCount())
                .createdByEmail(n.getCreatedBy().getEmail())
                .createdAt(n.getCreatedAt())
                .updatedAt(n.getUpdatedAt())
                .build();
    }
}