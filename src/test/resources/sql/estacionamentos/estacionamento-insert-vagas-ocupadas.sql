INSERT INTO usuarios (id, username, password, role) VALUES (100, 'admin@email.com', '$2a$12$WdgjjHlDFBqtQkM/pHku/OtimfZL.SuLv5vXxxWsZigY8j5j5JbAm', 'ROLE_ADMIN');
INSERT INTO usuarios (id, username, password, role) VALUES (101, 'lara@email.com', '$2a$12$WdgjjHlDFBqtQkM/pHku/OtimfZL.SuLv5vXxxWsZigY8j5j5JbAm', 'ROLE_CLIENTE');
INSERT INTO usuarios (id, username, password, role) VALUES (102, 'gabriel@email.com', '$2a$12$WdgjjHlDFBqtQkM/pHku/OtimfZL.SuLv5vXxxWsZigY8j5j5JbAm', 'ROLE_CLIENTE');
INSERT INTO usuarios (id, username, password, role) VALUES (103, 'joao@email.com', '$2a$12$WdgjjHlDFBqtQkM/pHku/OtimfZL.SuLv5vXxxWsZigY8j5j5JbAm', 'ROLE_CLIENTE');

INSERT INTO clientes (id, nome, cpf, id_usuario) VALUES (10, 'Lara Leticia', '02836171014', 101);
INSERT INTO clientes (id, nome, cpf, id_usuario) VALUES (20, 'Gabriel Barrilli', '38318819012', 102);
INSERT INTO clientes (id, nome, cpf, id_usuario) VALUES (30, 'Joao Barrilli', '37421518002', 103);

insert into vagas (id, codigo, status) values (100, 'A-01', 'OCUPADA');
insert into vagas (id, codigo, status) values (200, 'A-02', 'OCUPADA');
insert into vagas (id, codigo, status) values (300, 'A-03', 'OCUPADA');
insert into vagas (id, codigo, status) values (400, 'A-04', 'OCUPADA');
insert into vagas (id, codigo, status) values (500, 'A-05', 'OCUPADA');

insert into clientes_tem_vagas (numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
values ('20230313-101300', 'FIT-1020', 'FIAT', 'PALIO', 'VERDE', '2023-03-13 10:15:00', 10, 100);

insert into clientes_tem_vagas (numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
values ('20230314-101400', 'SIE-1020', 'FIAT', 'SIENA', 'BRANCO', '2023-03-14 10:15:00', 20, 200);

insert into clientes_tem_vagas (numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
values ('20230315-101500', 'FIT-1020', 'FIAT', 'PALIO', 'VERDE', '2023-03-14 10:15:00', 30, 300);

insert into clientes_tem_vagas (numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
values ('20230316-101600', 'SIE-1040', 'FIAT', 'SIENA', 'VERDE', '2023-03-14 10:15:00', 10, 400);

insert into clientes_tem_vagas (numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
values ('20230317-101700', 'SIE-1050', 'FIAT', 'SIENA', 'VERDE', '2023-03-14 10:15:00', 20, 500);