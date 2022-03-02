# csv 파일 db에 import 하기

1. 계정 연결 할 때 CONNECTION - ADVANCED - OTHERS 에 `OPT_LOCAL_INFILE=1` 입력하고 연결하기
2. 테이블 생성(컬럼순서유의)
```mysql
CREATE TABLE places (
id int auto_increment primary key,
name text(1000),
type text(1000),
subtype text(1000),
address text(1000),
lat text(1000),
lng text(1000)
);
```
3. 임포트
```mysql
LOAD DATA LOCAL 
INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/gyunggi_202112_new.csv'
INTO TABLE places
CHARACTER SET utf8
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(name,maintype,subtype,address,lat,lng);
```
4. 추가 컬럼
```mysql
ALTER TABLE places ADD hit INT;
ALTER TABLE places ADD imgsrc VARCHAR(1000);
```
