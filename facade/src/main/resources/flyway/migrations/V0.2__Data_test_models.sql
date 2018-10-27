-- INSERT INTO COMPANY (ID, INN)
-- values (1, 10),
--        (2, 20);
--
-- INSERT INTO MODEL (ID, DESCR)
-- VALUES (1, 'Model 1'),
--        (2, 'Model 2');
--
-- INSERT INTO MODEL_CALC (MODEL_ID, NODE, PARENT_NODE, WEIGHT, LEVEL, IS_LEAF)
-- values (1, 2, 3, 4, 5, 1);
--
-- INSERT into FORMULA (NODE, CALCULATION, FORMULA_TYPE, A, B, C, D, XB, COMMENTS)
-- VALUES (1, 'calc', 'type', 'A', 'B', 'C', 'D', 'XB', 'Comm');
-- INSERT INTO BUSINESS_PARAM (PARAM_CODE, DESCRIPTION)
-- VALUES (10, 'Code 10');
-- INSERT INTO FORMULA_PARAMS (NODE, PARAM_CODE)
-- VALUES (1, 10);
--
-- INSERT INTO COMPANY_BUSINESS_PARAMS (COMPANY_ID, PARAM_CODE, YEAR, PARAM_VALUE)
-- VALUES (1, 10, 2000, 'Val');

 insert into company_list (id, company_ids) VALUES ('1','3241012505;7451346741');
 insert into company_list (id, company_ids) VALUES ('2','3241012505;7451346741');