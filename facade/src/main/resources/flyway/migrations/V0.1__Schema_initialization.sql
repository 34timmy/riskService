CREATE SEQUENCE SEQ_ID
  start with 1;


CREATE TABLE company (
  id  VARCHAR2 (255) NOT NULL,
  inn VARCHAR2 (255)
);
ALTER TABLE company
  ADD CONSTRAINT company_pk PRIMARY KEY (id);

CREATE TABLE business_param (
  param_code  VARCHAR2 (255) NOT NULL,
  description VARCHAR2 (4000)
);
ALTER TABLE business_param
  ADD CONSTRAINT business_param_pk PRIMARY KEY (param_code);

CREATE TABLE company_business_params (
  company_id  VARCHAR2 (255) NOT NULL,
  param_code  VARCHAR2 (4000) NOT NULL,
  year        INTEGER NOT NULL,
  param_value VARCHAR2 (4000)
);
ALTER TABLE company_business_params
  ADD CONSTRAINT company_bus_param_pk PRIMARY KEY (company_id, param_code, year);
--Нужны ли эти констрейнты? ну нет описания и нет - рассчитать-то всё можем.
-- ALTER TABLE company_business_params ADD CONSTRAINT company_fk FOREIGN KEY (company_id) REFERENCES company (id);
-- ALTER TABLE company_business_params ADD CONSTRAINT param_fk FOREIGN KEY (param_code) REFERENCES business_param (param_code);


CREATE TABLE model (
  id    VARCHAR2 (255) default SEQ_ID.nextval NOT NULL,
  descr VARCHAR2 (4000)
);
ALTER TABLE model
  ADD CONSTRAINT model_pk PRIMARY KEY (id);

CREATE TABLE model_calc (
  model_id    VARCHAR2 (255) NOT NULL,
  descr       VARCHAR2 (255),
  node        VARCHAR2 (255) default SEQ_ID.nextval NOT NULL,
  parent_node VARCHAR2 (255),
  weight      DOUBLE PRECISION,
  level       INTEGER,
  is_leaf     INTEGER
);
ALTER TABLE model_calc
  ADD CONSTRAINT model_calc_pk PRIMARY KEY (model_id, node);
ALTER TABLE model_calc
  ADD CONSTRAINT model_calc_model_fk FOREIGN KEY (model_id) REFERENCES model (id);
ALTER TABLE model_calc
  ADD CONSTRAINT weight_val_check CHECK (weight BETWEEN 0 AND 100);
ALTER TABLE model_calc
  ADD CONSTRAINT leaf_val_check CHECK (is_leaf BETWEEN 0 AND 1);

CREATE TABLE rule
(
  id       VARCHAR(255) NOT NULL,
  name     VARCHAR(255),
  model_id VARCHAR(255)
);
ALTER TABLE rule
  ADD CONSTRAINT rule_pk PRIMARY KEY (id);

ALTER TABLE rule
  ADD CONSTRAINT model_fk FOREIGN KEY (model_id) REFERENCES model (id)
  ON DELETE SET NULL;

CREATE TABLE formula (
  id            VARCHAR2 (255)default SEQ_ID.nextval NOT NULL,
  descr         VARCHAR2 (4000),
  calculation   VARCHAR2 (4000) NOT NULL,
  formula_type  VARCHAR2 (255) NOT NULL,
  a             VARCHAR2 (4000) NOT NULL,
  b             VARCHAR2 (4000) NOT NULL,
  c             VARCHAR2 (4000) NOT NULL,
  d             VARCHAR2 (4000) NOT NULL,
  xb            VARCHAR2 (4000) NOT NULL,
  comments      VARCHAR2 (4000) NOT NULL,
  rule_id       VARCHAR2 (255)
);
ALTER TABLE formula
  ADD CONSTRAINT formula_pk PRIMARY KEY (id);
-- ALTER TABLE formula ADD CONSTRAINT node_fk FOREIGN KEY (node) REFERENCES model_calc (node);
-- ALTER TABLE formula
--   ADD CONSTRAINT rule_fk FOREIGN KEY (rule_id) REFERENCES rule (id);

CREATE TABLE formula_params (
  node       VARCHAR2 (255) NOT NULL,
  param_code VARCHAR2 (4000) NOT NULL,
  year_shift INTEGER NOT NULL DEFAULT 0
);
ALTER TABLE formula_params
  ADD CONSTRAINT formula_params_pk PRIMARY KEY (node, param_code, year_shift);
CREATE INDEX formula_params_node_idx
  ON formula_params (node);
ALTER TABLE formula_params
  ADD CONSTRAINT formula_params_formula_fk FOREIGN KEY (node) REFERENCES formula (id);
-- ALTER TABLE formula_params ADD CONSTRAINT formula_params_bus_param_fk FOREIGN KEY (param_code) REFERENCES business_param (param_code);

CREATE TABLE company_list (
  id          VARCHAR2 (255) NOT NULL,
  company_ids VARCHAR2 (4000) NOT NULL,
  descr       VARCHAR2 (4000)
);
ALTER TABLE company_list
  ADD CONSTRAINT company_list_pk PRIMARY KEY (id);

CREATE TABLE result_data_mapper (
  model_id            VARCHAR2 (255) NOT NULL,
  company_list_id     VARCHAR2 (255) NOT NULL,
  all_company_list_id VARCHAR2 (255) NOT NULL,
  year                INTEGER NOT NULL,
  table_name          VARCHAR2 (4000) NOT NULL
);
ALTER TABLE result_data_mapper
  ADD CONSTRAINT res_data_mapper_model_fk FOREIGN KEY (model_id) REFERENCES model (id);
ALTER TABLE result_data_mapper
  ADD CONSTRAINT company_list_fk FOREIGN KEY (company_list_id)
REFERENCES company_list (id);
ALTER TABLE result_data_mapper
  ADD CONSTRAINT all_company_list_id_fk FOREIGN KEY (all_company_list_id)
REFERENCES company_list (id);


CREATE TABLE normative_parameters (
  param_name VARCHAR2 (4000) NOT NULL,
  descr      VARCHAR2 (4000),
  value      DOUBLE PRECISION
)
-- h2 не поддерживает хранимки. Закоменчено до тестов на оракле
-- CREATE OR REPLACE PROCEDURE create_temp_result_table(table_name VARCHAR2) IS
--     v_column VARCHAR2(30);
--     BEGIN
--         EXECUTE IMMEDIATE 'CREATE TABLE ' || table_name ||  '(' ||
--                           'node VARCHAR2(255) NOT NULL,' ||
--                           'parent_node VARCHAR2(255),' ||
--                           'weight INTEGER,' ||
--                           'is_leaf INTEGER,' ||
--                           'value DOUBLE,' ||
--                           ')';
--         EXECUTE IMMEDIATE 'ALTER TABLE ' || table_name || ' ADD CONSTRAINT ' || table_name || '_pk PRIMARY KEY (node)';
--         EXECUTE IMMEDIATE 'ALTER TABLE ' || table_name || ' ADD CONSTRAINT ' || table_name || '_weight_val_check CHECK (weight BETWEEN 0 and 100);';
--         EXECUTE IMMEDIATE 'ALTER TABLE ' || table_name || ' ADD CONSTRAINT ' || table_name || '_leaf_val_check CHECK (is_leaf BETWEEN 0 and 1)';
--     END create_temp_result_table;