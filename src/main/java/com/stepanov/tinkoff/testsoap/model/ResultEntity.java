package com.stepanov.tinkoff.testsoap.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

/**
 * Persistence model, representation of the database "result" entity
 */
@Entity
@Table(name = "result")
public class ResultEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private String code;

  @Column(nullable = false)
  private Integer number;

  @Column(nullable = false, length = 400)
  private String fileNames;

  @Column
  private String error;

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(final String code) {
    this.code = code;
  }

  public Integer getNumber() {
    return number;
  }

  public void setNumber(final Integer number) {
    this.number = number;
  }

  public String getFileNames() {
    return fileNames;
  }

  public void setFileNames(final String fileNames) {
    this.fileNames = fileNames;
  }

  public String getError() {
    return error;
  }

  public void setError(final String error) {
    this.error = error;
  }

  public void setFileNames(final List<String> fileNames) {
    if (fileNames == null || fileNames.isEmpty()) {
      this.fileNames = "";
    } else {
      this.fileNames = String.join(",", fileNames);
    }
  }
}
