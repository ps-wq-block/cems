package com.cems.repository;

import com.cems.model.EventPhoto;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface EventPhotoRepository extends MongoRepository<EventPhoto, String> {
    List<EventPhoto> findByCategory(String category);
}
