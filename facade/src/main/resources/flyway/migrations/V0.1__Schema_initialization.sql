CREATE TABLE public.industry (
  id         VARCHAR2 (255) NOT NULL,
  name       VARCHAR2 (255) NOT NULL,
  descr VARCHAR2(4000)
);
ALTER TABLE public.industry
  ADD CONSTRAINT industry_pk PRIMARY KEY (id);

CREATE TABLE public.company (
  id        VARCHAR2 (255) NOT NULL,
  inn       VARCHAR2 (255),
  industry  VARCHAR2 (255),
  descr     VARCHAR2 (255)
);


ALTER TABLE public.company
  ADD CONSTRAINT company_pk PRIMARY KEY (id);
ALTER TABLE public.company
  ADD CONSTRAINT company_industry_fk FOREIGN KEY (industry) REFERENCES industry (id)
  ON DELETE SET NULL
  ON UPDATE CASCADE;

CREATE TABLE public.users (
  id         VARCHAR2 (255) NOT NULL,
  firstName       VARCHAR2 (255) NOT NULL,
  lastName       VARCHAR2 (255) NOT NULL,
  email      VARCHAR2 (255) NOT NULL,
  password   VARCHAR2 (255) NOT NULL,
  enabled    boolean  default true,
  registered DATETIME default now()
);

ALTER TABLE public.users
  ADD CONSTRAINT users_pk PRIMARY KEY (id);

CREATE TABLE public.role (
  id          VARCHAR2 (255) NOT NULL,
  desc        VARCHAR2 (255),
  parent_role VARCHAR2 (255)
);
ALTER TABLE public.role
  ADD CONSTRAINT role_pk PRIMARY KEY (id);

CREATE TABLE public.user_roles (
  role_id    varchar(255) NOT NULL,
  user_id varchar(255) NOT NULL
);

ALTER TABLE public.user_roles
  ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) references users (id)
  ON DELETE CASCADE;

ALTER TABLE public.user_roles
  ADD CONSTRAINT role_id_fk FOREIGN KEY (role_id) references role (id)
  ON DELETE CASCADE;

ALTER TABLE public.user_roles
  ADD CONSTRAINT user_roles_idx UNIQUE (user_id, role_id);

CREATE TABLE public.business_data (
  param_code  VARCHAR2 (255) NOT NULL,
  description VARCHAR2 (4000)
);
ALTER TABLE public.business_data
  ADD CONSTRAINT business_data_pk PRIMARY KEY (param_code);

CREATE TABLE public.company_business_data (
  company_id  VARCHAR2 (255) NOT NULL,
  param_code  VARCHAR2 (4000) NOT NULL,
  year        INTEGER NOT NULL,
  param_value VARCHAR2 (4000)
);
ALTER TABLE public.company_business_data
  ADD CONSTRAINT company_bus_param_pk PRIMARY KEY (company_id, param_code, year);
--Нужны ли эти констрейнты? ну нет описания и нет - рассчитать-то всё можем.
<<<<<<< HEAD
-- TODO ALTER TABLE public.company_business_data ADD CONSTRAINT company_fk FOREIGN KEY (company_id) REFERENCES company (id) ON DELETE CASCADE;
-- TODO ALTER TABLE public.company_business_data ADD CONSTRAINT param_fk FOREIGN KEY (param_code) REFERENCES business_data (param_code) ON DELETE CASCADE;
=======
-- ALTER TABLE public.company_business_data ADD CONSTRAINT company_fk FOREIGN KEY (company_id) REFERENCES company (id) ON DELETE CASCADE;
-- ALTER TABLE public.company_business_data ADD CONSTRAINT param_fk FOREIGN KEY (param_code) REFERENCES business_data (param_code) ON DELETE CASCADE;
>>>>>>> refs/remotes/origin/master


CREATE TABLE public.model (
  id      VARCHAR2 (255) NOT NULL,
  name    VARCHAR2 (500),
  descr   VARCHAR2 (4000),
  role_id VARCHAR2 (255)
);
ALTER TABLE public.model
  ADD CONSTRAINT model_pk PRIMARY KEY (id);

CREATE TABLE public.model_calc (
  model_id      VARCHAR2 (255) NOT NULL,
  descr         VARCHAR2 (4000),
  node          VARCHAR2 (255) NOT NULL,
  parent_id   VARCHAR2 (255),
  weight        DOUBLE PRECISION,
  expert_value  DOUBLE PRECISION,
  level         INTEGER,
  is_leaf       INTEGER,
  comments      text
);
ALTER TABLE public.model_calc
  ADD CONSTRAINT model_calc_pk PRIMARY KEY (model_id, node);
ALTER TABLE public.model_calc
  ADD CONSTRAINT model_calc_model_fk FOREIGN KEY (model_id) REFERENCES model (id)
  ON DELETE CASCADE;
ALTER TABLE public.model_calc
  ADD CONSTRAINT weight_val_check CHECK (weight BETWEEN 0 AND 100);
ALTER TABLE public.model_calc
  ADD CONSTRAINT node_unique UNIQUE(node);
ALTER TABLE public.model_calc
  ADD CONSTRAINT leaf_val_check CHECK (is_leaf BETWEEN 0 AND 1);

CREATE TABLE public.formula (
  id           VARCHAR2 (255) NOT NULL,
  descr        VARCHAR2 (4000),
  calculation  VARCHAR2 (4000) NOT NULL,
  formula_type VARCHAR2 (255) NOT NULL,
  a            VARCHAR2 (4000) NOT NULL,
  b            VARCHAR2 (4000) NOT NULL,
  c            VARCHAR2 (4000) NOT NULL,
  d            VARCHAR2 (4000) NOT NULL,
  xb           VARCHAR2 (4000) NOT NULL
);
ALTER TABLE public.formula
  ADD CONSTRAINT formula_pk PRIMARY KEY (id);
-- ALTER TABLE public.formula ADD CONSTRAINT node_fk FOREIGN KEY (node) REFERENCES model_calc (node) ON DELETE CASCADE;


CREATE TABLE public.formula_params (
  node       VARCHAR2 (255) NOT NULL,
  param_code VARCHAR2 (4000) NOT NULL,
  year_shift INTEGER NOT NULL DEFAULT 0
);
ALTER TABLE public.formula_params
  ADD CONSTRAINT formula_params_pk PRIMARY KEY (node, param_code, year_shift);
CREATE INDEX formula_params_node_idx
  ON formula_params (node);
ALTER TABLE public.formula_params
  ADD CONSTRAINT formula_params_formula_fk FOREIGN KEY (node) REFERENCES formula (id)
  ON DELETE CASCADE;
-- ALTER TABLE public.formula_params ADD CONSTRAINT formula_params_bus_param_fk FOREIGN KEY (param_code) REFERENCES business_data (param_code) ON DELETE CASCADE;

CREATE TABLE public.company_list (
  id          VARCHAR2 (255) NOT NULL,
  company_ids VARCHAR2 (4000) NOT NULL,
  descr       VARCHAR2 (4000)
);
ALTER TABLE public.company_list
  ADD CONSTRAINT company_list_pk PRIMARY KEY (id);

CREATE TABLE public.result_data_mapper (
  model_id            VARCHAR2 (255) NOT NULL,
  company_list_id     VARCHAR2 (255) NOT NULL,
  all_company_list_id VARCHAR2 (255) NOT NULL,
  year                INTEGER NOT NULL,
  table_name          VARCHAR2 (4000) NOT NULL,
  calculated_time     TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);
ALTER TABLE public.result_data_mapper
  ADD CONSTRAINT res_data_mapper_model_fk FOREIGN KEY (model_id) REFERENCES model (id)
  ON DELETE CASCADE;
ALTER TABLE public.result_data_mapper
  ADD CONSTRAINT company_list_fk FOREIGN KEY (company_list_id)
REFERENCES company_list (id)
  ON DELETE CASCADE;
ALTER TABLE public.result_data_mapper
  ADD CONSTRAINT all_company_list_id_fk FOREIGN KEY (all_company_list_id)
REFERENCES company_list (id)
  ON DELETE CASCADE;


CREATE TABLE public.normative_parameters (
  param_name VARCHAR2 (4000) NOT NULL,
  descr      VARCHAR2 (4000),
  value      DOUBLE PRECISION
)
