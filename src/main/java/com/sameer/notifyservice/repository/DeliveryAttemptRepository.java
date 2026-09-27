package com.sameer.notifyservice.repository;

import com.sameer.notifyservice.model.DeliveryAttempt;
import com.sameer.notifyservice.model.NotificationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeliveryAttemptRepository
        extends JpaRepository<DeliveryAttempt, UUID> {

    List<DeliveryAttempt> findByRequestOrderByAttemptNumberAsc(NotificationRequest request);
}