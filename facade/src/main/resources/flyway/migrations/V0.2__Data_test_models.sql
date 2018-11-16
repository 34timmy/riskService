insert into company_list (id, company_ids,descr)
VALUES ('1', '8602166992;5410786860','Список 1');
insert into company_list (id, company_ids,descr)
VALUES ('2', '8602166992;5410786860','Список 2');
insert into normative_parameters (param_name, value, descr)
VALUES ('FIIJJJ', '0.55', 'Testers gonna test');

-- INSERT INTO COMPANY (ID, INN,descr)
-- values (8602166992, 10,'Comp 1'),
--        (5410786860, 20,'Comp 2');
--
INSERT INTO MODEL (ID, DESCR)
VALUES (1, 'Model 1'),
       (2, 'Model 2');

INSERT INTO MODEL_CALC (MODEL_ID, DESCR, NODE, PARENT_NODE, WEIGHT, LEVEL, IS_LEAF)
values (1, 'Agr 1', 3, null, 4, 1, 0),
       (1, 'Agr 1.1', 5, 3, 4, 2, 0),
       (1, 'Agr 1.2', 7, 3, 4, 2, 0),
       (1, 'Formula 1', 8, 5, 4, 2, 1),
       (1, 'Formula 2', 9, 5, 4, 2, 1),
       (2, 'Agr 2', 4, null, 4, 1, 0),
       (2, 'Agr 2.1', 6, 4, 4, 2, 0),
       (2, 'Formula 3', 10, 6, 4, 2, 1);

INSERT into FORMULA (id, DESCR, CALCULATION, FORMULA_TYPE, A, B, C, D, XB, COMMENTS)
VALUES (8, 'Formula 1', 'calc', 'S', 'A', 'B', 'C', 'D', 'XB', 'Comm'),
       (9, 'Formula 2', 'calc', 'S', 'A', 'B', 'C', 'D', 'XB', 'Comm'),
       (10, 'Formula 3', 'calc', 'S', 'A', 'B', 'C', 'D', 'XB', 'Comm');



INSERT INTO BUSINESS_PARAM (PARAM_CODE, DESCRIPTION)
VALUES (10, 'Code 10');
INSERT INTO FORMULA_PARAMS (NODE, PARAM_CODE)
VALUES (8, 10);

INSERT INTO COMPANY_BUSINESS_PARAMS (COMPANY_ID, PARAM_CODE, YEAR, PARAM_VALUE)
VALUES (1, 10, 2000, 'Val');

-- insert into company_list (id, company_ids)
-- VALUES ('1', '3241012505;7451346741');
-- insert into company_list (id, company_ids)
-- VALUES ('2', '3241012505;7451346741');