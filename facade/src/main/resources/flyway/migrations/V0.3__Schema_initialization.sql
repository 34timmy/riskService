DROP TABLE IF EXISTS rule;

CREATE TABLE rule
(
  id       varchar(255) NOT NULL,
  name     varchar(255),
  model_id varchar(255)
);

ALTER TABLE rule
  ADD CONSTRAINT rule_pk PRIMARY KEY (id);

ALTER TABLE rule
  ADD CONSTRAINT model_fk FOREIGN KEY (model_id) REFERENCES MODEL (id);

ALTER TABLE FORMULA
  ADD CONSTRAINT rule_fk FOREIGN KEY (rule_id) REFERENCES rule (id);


