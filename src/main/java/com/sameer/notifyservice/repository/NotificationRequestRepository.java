package com.sameer.notifyservice.repository;

import com.sameer.notifyservice.model.NotificationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationRequestRepository
        extends JpaRepository<NotificationRequest, UUID> {
}