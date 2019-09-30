package com.stepanov.tinkoff.testsoap;

import com.stepanov.tinkoff.testsoap.service.FileService;
import com.stepanov.tinkoff.testsoap.service.FileServiceException;

/**
 * The only purpose of this class is to run generateRandomNumbersTextFiles(int) method
 * Refer to {@link FileService#generateRandomNumbersTextFiles} for the details
 */
public class TextFilesGenerator {

  public static void main(final String[] args) throws FileServiceException {
    final FileService fileService = new FileService();
    fileService.generateRandomNumbersTextFiles(125000000);
  }
}
