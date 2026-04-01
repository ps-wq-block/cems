package com.cems.repository;

import com.cems.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    Optional<Notification> findByTitleAndReceiver(String title, String receiver);
}
