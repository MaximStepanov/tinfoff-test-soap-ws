package com.stepanov.tinkoff.testsoap.webservice;

import com.stepanov.tinkoff.testsoap.model.ResultEntity;
import com.stepanov.tinkoff.testsoap.repository.ResultsRepository;
import com.stepanov.tinkoff.testsoap.service.FileService;
import com.stepanov.tinkoff.testsoap.service.FileServiceException;
import com.stepanov.tinkoff.testsoap.utils.ResultCode;
import com.stepanov.tinkoff.testsoap.ws.GetResultRequest;
import com.stepanov.tinkoff.testsoap.ws.GetResultResponse;
import com.stepanov.tinkoff.testsoap.ws.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

/**
 * This class will handle all the incoming requests for the service
 * and will delegate the call to the finder method of the data repository
 */
@Endpoint
public class ResultEndpoint {

  private static final Logger logger = LogManager.getLogger(FileService.class);

  private static final String URL = "http://testsoap.tinkoff.stepanov.com/ws";

  private final ResultsRepository repository;

  private final FileService fileService;


  @Autowired
  public ResultEndpoint(final ResultsRepository repository, final FileService fileService) {
    this.repository = repository;
    this.fileService = fileService;
  }

  /**
   * Payload picks the handler method based on the message’s namespace and localPart.
   *
   * @param request - incoming message
   * @return - response payload
   */
  @PayloadRoot(namespace = URL, localPart = "getResultRequest")
  @ResponsePayload
  public GetResultResponse findNumber(@RequestPayload final GetResultRequest request) {
    final Result result = new Result();
    final int requestNumber = request.getNumber();

    try {
      final List<String> filesWhereNumberWasFound = fileService.findNumber(requestNumber);
      if (filesWhereNumberWasFound == null || filesWhereNumberWasFound.size() == 0) {
        result.setCode(ResultCode.NOT_FOUND);
        logger.info("Specified number was not found in any of files");
      } else {
        result.setCode(ResultCode.OK);
        result.getFileNames().addAll(filesWhereNumberWasFound);
        logger.info("Specified number was found in files");
      }
    } catch (final FileServiceException e) {
      result.setCode(ResultCode.ERROR);
      result.setError(e.getMessage());
      logger.info("Specified number has incorrect format of any other exception could be occurred");
    }

    save(result, requestNumber);
    logger.info("Result has been saved");

    final GetResultResponse response = new GetResultResponse();
    response.setResult(result);
    return response;
  }

  /**
   * Here this method maps {@link Result} to the {@link ResultEntity}
   * and then delegates saving of the entity to the Repository layer
   *
   * @param result - result of the {@link FileService} work
   * @param number - specified number by which the search was done
   */
  private void save(final Result result, final int number) {
    final ResultEntity resultEntity = new ResultEntity();
    resultEntity.setCode(result.getCode());
    resultEntity.setNumber(number);
    resultEntity.setFileNames(result.getFileNames());
    resultEntity.setError(result.getError());
    repository.save(resultEntity);
  }
}
