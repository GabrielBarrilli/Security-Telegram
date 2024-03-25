INSERT INTO usuarios (id, username, password, role) VALUES (100, 'admin@email.com', '$2a$12$WdgjjHlDFBqtQkM/pHku/OtimfZL.SuLv5vXxxWsZigY8j5j5JbAm', 'ROLE_ADMIN');
INSERT INTO usuarios (id, username, password, role) VALUES (101, 'lara@email.com', '$2a$12$WdgjjHlDFBqtQkM/pHku/OtimfZL.SuLv5vXxxWsZigY8j5j5JbAm', 'ROLE_CLIENTE');
INSERT INTO usuarios (id, username, password, role) VALUES (102, 'gabriel@email.com', '$2a$12$WdgjjHlDFBqtQkM/pHku/OtimfZL.SuLv5vXxxWsZigY8j5j5JbAm', 'ROLE_CLIENTE');
INSERT INTO usuarios (id, username, password, role) VALUES (103, 'toby@email.com', '$2a$12$WdgjjHlDFBqtQkM/pHku/OtimfZL.SuLv5vXxxWsZigY8j5j5JbAm', 'ROLE_CLIENTE');

INSERT INTO clientes (id, nome, cpf, id_usuario) VALUES (10, 'Lara Letícia', '02836171014', 101);
INSERT INTO clientes (id, nome, cpf, id_usuario) VALUES (20, 'Gabriel Barrilli', '38318819012', 102);
