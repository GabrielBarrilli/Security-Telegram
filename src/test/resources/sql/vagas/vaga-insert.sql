INSERT INTO usuarios (id, username, password, role) VALUES (100, 'admin@email.com', '$2a$12$WdgjjHlDFBqtQkM/pHku/OtimfZL.SuLv5vXxxWsZigY8j5j5JbAm', 'ROLE_ADMIN');
INSERT INTO usuarios (id, username, password, role) VALUES (101, 'lara@email.com', '$2a$12$WdgjjHlDFBqtQkM/pHku/OtimfZL.SuLv5vXxxWsZigY8j5j5JbAm', 'ROLE_CLIENTE');
INSERT INTO usuarios (id, username, password, role) VALUES (102, 'gabriel@email.com', '$2a$12$WdgjjHlDFBqtQkM/pHku/OtimfZL.SuLv5vXxxWsZigY8j5j5JbAm', 'ROLE_CLIENTE');

INSERT INTO vagas(id, codigo, status) values(10, 'A-01', 'LIVRE');
INSERT INTO vagas(id, codigo, status) values(20, 'A-02', 'LIVRE');
INSERT INTO vagas(id, codigo, status) values(30, 'A-03', 'OCUPADA');
INSERT INTO vagas(id, codigo, status) values(40, 'A-04', 'LIVRE');