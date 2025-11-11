package com.porftolio.alberto.repositories;

import com.porftolio.alberto.models.ContactaConmigo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ContactaConmigoRepository extends MongoRepository<ContactaConmigo, String> {
    List<ContactaConmigo> findAllByProcessed(boolean processed);
}
