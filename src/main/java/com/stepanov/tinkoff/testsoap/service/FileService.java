package com.stepanov.tinkoff.testsoap.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Service for the files processing
 * <p>
 * Responsible for generating 20 text files and each of them ~ 1Gb
 */
@Component
public class FileService {

  private static final Logger logger = LogManager.getLogger(FileService.class);

  private static final String TEXT_FILES_SOURCE = "src\\main\\resources\\text_files\\";
  private final Random random = new Random();

  /**
   * The main method of {@link FileService}.
   * <p>
   * Process all the files in the "resources/text_files" directory
   * and returns file names which contain specified number
   *
   * @param number - specified number
   * @return {@link List<String>} object containing file names
   * @throws FileServiceException - throws if any exception occurs during files processing
   */
  public List<String> findNumber(final int number) throws FileServiceException {
    final List<String> fileNames = getTextFileNames();
    final List<String> resultList = new ArrayList<>();

    for (final String fileName : fileNames) {
      if (findInSpecifiedFile(fileName, number) != null) {
        resultList.add(fileName);
      }
    }
    return resultList;
  }

  /**
   * Finds a specified number in a text file and returns its name if number was found at least 1 time
   *
   * @param fileName - file where we are searching for the number
   * @param number   - specified number
   * @return - {@link String} value of the fileName if a specified file contains specified number
   * @throws FileServiceException - throws if any exception occurs during the file processing
   */
  private String findInSpecifiedFile(final String fileName, final int number) throws FileServiceException {
    try (final FileReader fileReader = new FileReader(TEXT_FILES_SOURCE + fileName);
        final BufferedReader reader = new BufferedReader(fileReader)) {
      final StreamTokenizer tokenizer = new StreamTokenizer(reader);
      while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
        final int currentNumber = (int) tokenizer.nval;
        if (number == currentNumber) {
          logger.info("Found number in the file : {}", fileName);
          return fileName;
        }
        tokenizer.nextToken();
      }
    } catch (final IOException ex) {
      logger.error("Error find number in the file {}", fileName);
      throw new FileServiceException("System encountered an error while processing number search in file", ex);
    }
    return null;
  }

  /**
   * Returns the list of file names represented in "resources/text_files" directory
   *
   * @return {@link List<String>} containing file names
   * @throws FileServiceException - throws if any exception occurs during files processing
   */
  private List<String> getTextFileNames() throws FileServiceException {
    try {
      return Files.walk(Paths.get(TEXT_FILES_SOURCE))
          .filter(Files::isRegularFile)
          .map(file -> file.toFile().getName())
          .collect(Collectors.toList());
    } catch (final IOException e) {
      logger.error("Error while getting files' names");
      throw new FileServiceException("System encountered an error while reading file names", e);
    }
  }

  /**
   * Generates 20 text files in the "resources/text_files" directory
   * which contain random (range 0 - 42.000.000) numbers separated by comma
   * <p>
   * Should be used only one time to generify all the files, to prevent additional calls
   * there is a check if the files are already exist
   *
   * @param amountOfNumbersInEachFile - number that specifies amount of numbers that will be written into the file
   * @throws FileServiceException - throws if any exception occurs during the generation
   */
  public void generateRandomNumbersTextFiles(final int amountOfNumbersInEachFile) throws FileServiceException {
    final File folder = new File(TEXT_FILES_SOURCE);
    final File[] files = folder.listFiles();
    if (files != null && files.length > 0) {
      return;
    }

    for (int i = 0; i < 20; i++) {
      final File file = createTextFile(i);
      try (final FileWriter fileWriter = new FileWriter(file);
           final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int k = amountOfNumbersInEachFile; k > 0; k--) {
          stringBuilder.append(random.nextInt(42000000)).append(",");
        }
        bufferedWriter.write(stringBuilder.toString());
        logger.info(i + "'s text file has been created");
      } catch (final IOException e) {
        logger.error("Error occurred while creating files");
        throw new FileServiceException("System encountered an error while generating text files", e);
      }
    }
    logger.info("20 text files have been created successfully!");
  }

  /**
   * Returns a {@link File} named by adding counter int value
   *
   * @param i - index
   * @return - File with specified name
   */
  private static File createTextFile(final int i) {
    return new File(String.format(TEXT_FILES_SOURCE + "textFile_%d.txt", i));
  }
}
