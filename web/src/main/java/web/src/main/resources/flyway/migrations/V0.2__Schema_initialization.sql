CREATE TABLE company
(
  id   integer primary key identity,
  INN  varchar(250),
  name varchar(100),
  year integer,
  sb_1 varchar(250),
  sb_2 varchar(250)
);

CREATE TABLE model