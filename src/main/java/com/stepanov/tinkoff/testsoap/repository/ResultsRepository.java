package com.stepanov.tinkoff.testsoap.repository;

import com.stepanov.tinkoff.testsoap.model.ResultEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository layer for {@link ResultEntity} model
 */
@Repository
public interface ResultsRepository extends CrudRepository<ResultEntity, Long> {
}
