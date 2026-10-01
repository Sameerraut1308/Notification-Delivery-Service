package com.sameer.notifyservice.dto;

import com.sameer.notifyservice.model.NotificationRequest.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNotificationRequest {

    @NotNull(message = "Type is required (EMAIL or SMS)")
    private NotificationType type;

    @NotBlank(message = "Recipient cannot be blank")
    private String recipient;

    private String subject; // Optional (mainly for EMAIL)

    @NotBlank(message = "Body cannot be blank")
    @Size(max = 1000, message = "Body cannot exceed 1000 characters")
    private String body;
}