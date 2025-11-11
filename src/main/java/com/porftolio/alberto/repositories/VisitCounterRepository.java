package com.porftolio.alberto.repositories;

import com.porftolio.alberto.models.VisitCounter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VisitCounterRepository extends MongoRepository<VisitCounter, String> {
}
