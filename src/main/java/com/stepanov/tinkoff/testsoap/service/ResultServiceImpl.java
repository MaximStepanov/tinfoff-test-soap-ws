package com.stepanov.tinkoff.testsoap.service;

import com.stepanov.tinkoff.testsoap.model.ResultEntity;
import com.stepanov.tinkoff.testsoap.repository.ResultsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Result service layer
 */
@Service
public class ResultServiceImpl implements ResultService {

  private static final Logger logger = LogManager.getLogger(FileService.class);

  private final ResultsRepository resultsRepository;

  @Autowired
  public ResultServiceImpl(final ResultsRepository repository) {
    this.resultsRepository = repository;
  }

  /**
   * Saves {@link ResultEntity} to the database
   *
   * @param resultEntity - model representation of {@link com.stepanov.tinkoff.testsoap.ws.Result}
   */
  @Override
  public void save(final ResultEntity resultEntity) {
    logger.info("Saving Result into the database...");
    resultsRepository.save(resultEntity);
  }
}
