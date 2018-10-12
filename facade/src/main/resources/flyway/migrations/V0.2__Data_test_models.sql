-- TODO
delete
from COMPANY;
delete
from FORMULA;
DELETE
from RULE;
delete
from MODEL_CALC;
delete
from MODEL;


INSERT INTO COMPANY (ID, INN)
values (1, 10),
       (2, 20);

INSERT INTO MODEL (ID, NAME)
VALUES (1, 'DESCRIPTION'),
       (2, 'D2');

INSERT INTO MODEL_CALC (MODEL_ID, NODE, PARENT_NODE, WEIGHT, LEVEL, IS_LEAF)
values (1, 2, 3, 4, 5, 1);

INSERT INTO rule (ID, NAME, MODEL_ID)
VALUES (1, 'rule 1', '1');
INSERT into FORMULA (NODE, NAME, CALCULATION, FORMULA_TYPE, A, B, C, D, XB, COMMENTS, RULE_ID)
VALUES (1, 'Formula 1', 'calc', 'type', 'A', 'B', 'C', 'D', 'XB', 'Comm', 1)

