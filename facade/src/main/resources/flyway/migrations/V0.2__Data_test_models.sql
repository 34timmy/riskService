insert into company_list (id, company_ids, descr)
VALUES ('1', '8602166992;5410786860', 'Список 1');
insert into company_list (id, company_ids, descr)
VALUES ('2', '8602166992;5410786860', 'Список 2');
insert into normative_parameters (param_name, value, descr)
VALUES ('FIIJJJ', '0.55', 'Testers gonna test');

INSERT INTO users(ID, NAME, EMAIL, PASSWORD)
values (1, 'user', 'user@gmail.com', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC');

INSERT INTO roles ( role, user_id)
values ( 'ROLE_USER', 1);
-- INSERT INTO COMPANY (ID, INN,descr)
-- values (8602166992, 10,'Comp 1'),
--        (5410786860, 20,'Comp 2');
--
INSERT INTO MODEL (ID, NAME, DESCR)
VALUES ('33416230-d0e5-43d5-9901-829572bec3b6', 'Model 1', 'Очень сложна модель 1'),
       ('741073e4-70d1-488c-828e-5000437df48c', 'Model 2', 'Очень сложна модель 2');

INSERT INTO MODEL_CALC (MODEL_ID, DESCR, NODE, PARENT_NODE, WEIGHT, LEVEL, IS_LEAF)
values ('33416230-d0e5-43d5-9901-829572bec3b6', 'Agr 1', '36edd6cc-1e68-46a6-82e0-3c8d2e643c51', null, 1, 1, 0),
       ('33416230-d0e5-43d5-9901-829572bec3b6', 'Agr 1.1', '52e1f310-9fb4-4944-9cc5-a297b149f980', '36edd6cc-1e68-46a6-82e0-3c8d2e643c51', 4, 2, 0),
       ('33416230-d0e5-43d5-9901-829572bec3b6', 'Agr 1.2', '46419c9e-7af9-4f6a-8408-dbf9faa7146c', '36edd6cc-1e68-46a6-82e0-3c8d2e643c51', 1, 2, 0),
       ('33416230-d0e5-43d5-9901-829572bec3b6', 'Formula 1', 'eab215a1-fa52-4a2e-a456-90a44c579b20', '52e1f310-9fb4-4944-9cc5-a297b149f980', 1, 2, 1),
       ('33416230-d0e5-43d5-9901-829572bec3b6', 'Formula 2', '501e6562-66a4-455a-8448-e1e97f23f468', '52e1f310-9fb4-4944-9cc5-a297b149f980', 1, 2, 1),
       ('741073e4-70d1-488c-828e-5000437df48c', 'Agr 2', '2d4f7e55-85fc-4780-9d70-8065d71db788', null, 1, 1, 0),
       ('741073e4-70d1-488c-828e-5000437df48c', 'Agr 2.1', '53ceb373-47a7-452c-8f40-cbb8fd390d83', '2d4f7e55-85fc-4780-9d70-8065d71db788', 1, 2, 0),
       ('741073e4-70d1-488c-828e-5000437df48c', 'Formula 3', '417275ff-a7a8-4360-b97a-81ac40fa3e8a', '53ceb373-47a7-452c-8f40-cbb8fd390d83', 1, 2, 1);

INSERT into FORMULA (id, DESCR, CALCULATION, FORMULA_TYPE, A, B, C, D, XB)
VALUES ('eab215a1-fa52-4a2e-a456-90a44c579b20', 'Formula 1', 'calc', 'S', 'A', 'B', 'C', 'D', 'XB'),
       ('501e6562-66a4-455a-8448-e1e97f23f468', 'Formula 2', 'calc', 'S', 'A', 'B', 'C', 'D', 'XB'),
       ('417275ff-a7a8-4360-b97a-81ac40fa3e8a', 'Formula 3', 'calc', 'S', 'A', 'B', 'C', 'D', 'XB');


INSERT INTO business_data (PARAM_CODE, DESCRIPTION)
VALUES (10, 'Code 10');
INSERT INTO FORMULA_PARAMS (NODE, PARAM_CODE)
VALUES ('501e6562-66a4-455a-8448-e1e97f23f468', 10);

INSERT INTO COMPANY_business_data (COMPANY_ID, PARAM_CODE, YEAR, PARAM_VALUE)
VALUES (1, 10, 2000, 'Val');

-- insert into company_list (id, company_ids)
-- VALUES ('1', '3241012505;7451346741');
-- insert into company_list (id, company_ids)
-- VALUES ('2', '3241012505;7451346741');