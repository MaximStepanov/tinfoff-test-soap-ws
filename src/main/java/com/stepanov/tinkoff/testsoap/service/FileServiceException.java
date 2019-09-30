package com.stepanov.tinkoff.testsoap.service;

/**
 * Custom exception wrapper used for handle specific file processing exceptions
 */
public class FileServiceException extends Exception {

  FileServiceException(final String message, final Throwable exception) {
    super(message, exception);
  }
}
