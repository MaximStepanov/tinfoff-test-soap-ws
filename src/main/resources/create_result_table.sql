CREATE TABLE result (
     id NUMBER(*,0) NOT NULL PRIMARY KEY,
     code VARCHAR2(50 BYTE) NOT NULL,
     number NUMBER(*,0) NOT NULL,
     file_names VARCHAR2(100 BYTE),
     error VARCHAR2(100 BYTE)
);