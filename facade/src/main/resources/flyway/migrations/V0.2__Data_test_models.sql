insert into company_list (id, company_ids)
VALUES ('1', '8602166992;5410786860');
insert into company_list (id, company_ids)
VALUES ('2', '8602166992;5410786860');
insert into normative_parameters (param_name, value, descr)
VALUES ('FIIJJJ', '0.55', 'Testers gonna test');

-- INSERT INTO COMPANY (ID, INN)
-- values (1, 10),
--        (2, 20);
--
INSERT INTO MODEL (ID, DESCR)
VALUES (1, 'Model 1'),
       (2, 'Model 2');

INSERT INTO MODEL_CALC (MODEL_ID, DESCR, NODE, PARENT_NODE, WEIGHT, LEVEL, IS_LEAF)
values (1, 'Calc 1', 3, null, 4, 1, 1),
       (1, 'Calc 2', 4, 3, 4, 2, 1),
       (1, 'Calc 3', 5, 3, 4, 2, 1);
--
-- INSERT INTO rule (ID, NAME, MODEL_ID)
-- VALUES (1, 'rule 1', '1'),
--        (2, 'rule_2', '2'),
--        (3, 'rule_3', '2');
INSERT into FORMULA (id, DESCR, CALCULATION, FORMULA_TYPE, A, B, C, D, XB, COMMENTS, RULE_ID, model_calc_id)
VALUES (6, 'Formula1', 'calc', 'S', 'A', 'B', 'C', 'D', 'XB', 'Comm', 1, 3),
       (7, 'Formula2', 'calc', 'S', 'A', 'B', 'C', 'D', 'XB', 'Comm', 2, 4)
;
-- INSERT INTO BUSINESS_PARAM (PARAM_CODE, DESCRIPTION)
-- VALUES (10, 'Code 10');
-- INSERT INTO FORMULA_PARAMS (NODE, PARAM_CODE)
-- VALUES (1, 10);
--
-- INSERT INTO COMPANY_BUSINESS_PARAMS (COMPANY_ID, PARAM_CODE, YEAR, PARAM_VALUE)
-- VALUES (1, 10, 2000, 'Val');
--
-- insert into company_list (id, company_ids)
-- VALUES ('1', '3241012505;7451346741');
-- insert into company_list (id, company_ids)
-- VALUES ('2', '3241012505;7451346741');