CREATE TABLE company (
    id varchar2(255) NOT NULL,
    inn varchar2(255)
);
ALTER TABLE company ADD CONSTRAINT company_pk PRIMARY KEY (id);

CREATE TABLE business_param (
    param_code varchar2(255) NOT NULL,
    description varchar2(4000)
);
ALTER TABLE business_param ADD CONSTRAINT business_param_pk PRIMARY KEY (param_code);

CREATE TABLE company_business_params (
    company_id varchar2(255) NOT NULL,
    param_code varchar2(4000) NOT NULL,
    year integer NOT NULL,
    param_value varchar2(4000)
);
ALTER TABLE company_business_params ADD CONSTRAINT company_bus_param_pk PRIMARY KEY (company_id, param_code, year);
--Нужны ли эти констрейнты? ну нет описания и нет - рассчитать-то всё можем.
-- ALTER TABLE company_business_params ADD CONSTRAINT company_fk FOREIGN KEY (company_id) REFERENCES company (id);
-- ALTER TABLE company_business_params ADD CONSTRAINT param_fk FOREIGN KEY (param_code) REFERENCES business_param (param_code);


CREATE TABLE model (
    id varchar2(255) NOT NULL,
    descr varchar2(250)
);
ALTER TABLE model ADD CONSTRAINT model_pk PRIMARY KEY (id);

CREATE TABLE model_calc (
    model_id VARCHAR2(255) NOT NULL,
    node VARCHAR2(255) NOT NULL,
    parent_node VARCHAR2(255),
    weight DOUBLE PRECISION,
    level INTEGER,
    is_leaf INTEGER
);
ALTER TABLE model_calc ADD CONSTRAINT model_calc_pk PRIMARY KEY (model_id, node);
ALTER TABLE model_calc ADD CONSTRAINT model_calc_model_fk FOREIGN KEY (model_id) REFERENCES model (id);
ALTER TABLE model_calc ADD CONSTRAINT weight_val_check CHECK (weight BETWEEN 0 and 100);
ALTER TABLE model_calc ADD CONSTRAINT leaf_val_check CHECK (is_leaf BETWEEN 0 and 1);

CREATE TABLE formula (
    node VARCHAR2(255) NOT NULL,
    descr VARCHAR2(4000) NOT NULL,
    calculation VARCHAR2(4000) NOT NULL,
    formula_type VARCHAR2(255) NOT NULL,
    a varchar2(4000) NOT NULL,
    b varchar2(4000) NOT NULL,
    c varchar2(4000) NOT NULL,
    d varchar2(4000) NOT NULL,
    xb VARCHAR2(4000) NOT NULL,
    comments VARCHAR2(4000) NOT NULL
);
ALTER TABLE formula ADD CONSTRAINT formula_pk PRIMARY KEY (node);
-- ALTER TABLE formula ADD CONSTRAINT node_fk FOREIGN KEY (node) REFERENCES model_calc (node);

CREATE TABLE formula_params(
    node VARCHAR2(255) NOT NULL,
    param_code VARCHAR2(4000) NOT NULL,
    year_shift INTEGER DEFAULT 0
);
ALTER TABLE formula_params ADD CONSTRAINT formula_params_pk PRIMARY KEY (node, param_code);
CREATE INDEX formula_params_node_idx ON formula_params(node);
ALTER TABLE formula_params ADD CONSTRAINT formula_params_formula_fk FOREIGN KEY (node) REFERENCES formula (node);
ALTER TABLE formula_params ADD CONSTRAINT formula_params_bus_param_fk FOREIGN KEY (param_code) REFERENCES business_param (param_code);


CREATE TABLE company_list(
    id VARCHAR2(255) NOT NULL,
    company_ids VARCHAR2(4000) NOT NULL
);
ALTER TABLE company_list ADD CONSTRAINT company_list_pk PRIMARY KEY (id);

CREATE TABLE result_data_mapper(
    model_id VARCHAR2(255) NOT NULL,
    company_list_id VARCHAR2(255) NOT NULL,
    table_name VARCHAR2(4000) NOT NULL
);
ALTER TABLE result_data_mapper ADD CONSTRAINT result_data_mapper_pk PRIMARY KEY (model_id, company_list_id);
ALTER TABLE result_data_mapper ADD CONSTRAINT res_data_mapper_model_fk FOREIGN KEY (model_id) REFERENCES model (id);
ALTER TABLE result_data_mapper ADD CONSTRAINT company_list_fk FOREIGN KEY (company_list_id) REFERENCES company_list (id);

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