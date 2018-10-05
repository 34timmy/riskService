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
    param_code varchar2(4000),
    param_value varchar2(4000)
);
ALTER TABLE company_business_params ADD CONSTRAINT company_bus_param_pk PRIMARY KEY (company_id);
ALTER TABLE company_business_params ADD CONSTRAINT company_fk FOREIGN KEY (company_id) REFERENCES company (id);
ALTER TABLE company_business_params ADD CONSTRAINT param_fk FOREIGN KEY (param_code) REFERENCES business_param (param_code);


CREATE TABLE model (
    id varchar2(255) NOT NULL,
    a2 varchar2(250)
);
ALTER TABLE model ADD CONSTRAINT model_pk PRIMARY KEY (id);

