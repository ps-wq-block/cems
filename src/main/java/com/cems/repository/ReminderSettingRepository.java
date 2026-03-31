package com.cems.repository;

import com.cems.model.ReminderSetting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReminderSettingRepository extends MongoRepository<ReminderSetting, String> {
    Optional<ReminderSetting> findByEventName(String eventName);
}
