
INSERT INTO grupo (id, nome) VALUES (1, 'SuperUser');
INSERT INTO grupo (id, nome) VALUES (2, 'Administrador');
INSERT INTO grupo (id, nome) VALUES (3, 'Operador caixa');

INSERT INTO permissao VALUES (1, 'ROLE_SUPER_USER');
INSERT INTO permissao VALUES (2, 'ROLE_MANTER_PRODUTO');
INSERT INTO permissao VALUES (3, 'ROLE_MANTER_ENTRADA');
INSERT INTO permissao VALUES (4, 'ROLE_MANTER_PDV');
INSERT INTO permissao VALUES (5, 'ROLE_MANTER_FORNECEDOR');
INSERT INTO permissao VALUES (6, 'ROLE_ENVIAR_MENSAGEM');
INSERT INTO permissao VALUES (7, 'ROLE_LER_MENSAGEM');
INSERT INTO permissao VALUES (8, 'ROLE_FILTRAR_POR_USUARIO_MOVIMENTACAO_CAIXA');

INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (1, 1);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (1, 2);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (1, 3);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (1, 4);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (1, 5);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (1, 6);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (1, 7);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (1, 8);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (2, 2);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (2, 3);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (2, 4);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (2, 5);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (2, 6);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (2, 7);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (2, 8);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (3, 4);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (3, 7);

INSERT INTO cliente_sistema(acessar_tela_criar_login, bairro, cep, cidade, cpf_cnpj, data_atualizacao, data_cadastro, estado, logradouro, nome_comercio, primeiro_acesso, qtd_usuario, tenant)
VALUES (true,'Jardim Curitiba','61650005','Goiânia','77835168000141','2021-08-05 00:10:19.887','2021-08-05 00:10:19.887','Goiás','Quandra 45 lote 3','Distribuidora do SUD',true,3,'sud');
INSERT INTO usuario (id,nome, email, senha,proprietario, ativo, tenant) VALUES (1,'Anna', 'anna@storedrink.com', '$2a$10$rODOzMz60JmUUx5J39p9JOb5Ee7c2303YeH0kp42XJOW7Ch9qcBy2',true, true, (SELECT tenant FROM cliente_sistema WHERE tenant = 'sud'));
INSERT INTO usuario (id,nome, email, senha,proprietario, ativo, tenant) VALUES (2,'Marta', 'marta@storedrink.com', '$2a$10$rODOzMz60JmUUx5J39p9JOb5Ee7c2303YeH0kp42XJOW7Ch9qcBy2',false, true, (SELECT tenant FROM cliente_sistema WHERE tenant = 'sud'));
INSERT INTO usuario (id,nome, email, senha,proprietario, ativo, tenant) VALUES (3,'SUD', 'sud@storedrink.com', '$2a$10$rODOzMz60JmUUx5J39p9JOb5Ee7c2303YeH0kp42XJOW7Ch9qcBy2',true, true, (SELECT tenant FROM cliente_sistema WHERE tenant = 'sud'));
INSERT INTO usuario_grupo (id_usuario, id_grupo) VALUES ((SELECT id FROM usuario WHERE email = 'anna@storedrink.com'), 2);
INSERT INTO usuario_grupo (id_usuario, id_grupo) VALUES ((SELECT id FROM usuario WHERE email = 'marta@storedrink.com'), 3);
INSERT INTO usuario_grupo (id_usuario, id_grupo) VALUES ((SELECT id FROM usuario WHERE email = 'sud@storedrink.com'), 1);

INSERT INTO cliente_sistema(acessar_tela_criar_login, bairro, cep, cidade, cpf_cnpj, data_atualizacao, data_cadastro, estado, logradouro, nome_comercio, primeiro_acesso, qtd_usuario, tenant)
VALUES (true,'Setor bueno','61650005','Goiânia','77835168009890','2021-08-05 00:10:19.887','2021-08-05 00:10:19.887','Goiás','Quandra 45 lote 3','Distribuidora rei das bebidas',true,2,'sbn');
INSERT INTO usuario (id,nome, email, senha,proprietario, ativo, tenant) VALUES (4,'Joana', 'joana@storedrink.com', '$2a$10$rODOzMz60JmUUx5J39p9JOb5Ee7c2303YeH0kp42XJOW7Ch9qcBy2',false, true, (SELECT tenant FROM cliente_sistema WHERE tenant = 'sbn'));
INSERT INTO usuario (id,nome, email, senha,proprietario, ativo, tenant) VALUES (5,'Marcos', 'marcos@storedrink.com', '$2a$10$rODOzMz60JmUUx5J39p9JOb5Ee7c2303YeH0kp42XJOW7Ch9qcBy2',true, true, (SELECT tenant FROM cliente_sistema WHERE tenant = 'sbn'));
INSERT INTO usuario_grupo (id_usuario, id_grupo) VALUES ((SELECT id FROM usuario WHERE email = 'marcos@storedrink.com'), 2);
INSERT INTO usuario_grupo (id_usuario, id_grupo) VALUES ((SELECT id FROM usuario WHERE email = 'joana@storedrink.com'), 3);
