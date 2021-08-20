INSERT INTO cliente_sistema(acessar_tela_criar_login, bairro, cep, cidade, cpf_cnpj, data_atualizacao, data_cadastro, estado, logradouro, nome_comercio, primeiro_acesso, qtd_usuario, tenant)
VALUES (true,'Jardim Curitiba','61650005','Goiânia','77835168000141','2021-08-05 00:10:19.887','2021-08-05 00:10:19.887','Goiás','Quandra 45 lote 3','Loja sud',true,1,'sud');

INSERT INTO grupo (id, nome) VALUES (1, 'SuperUser');
INSERT INTO grupo (id, nome) VALUES (2, 'Administrador');
INSERT INTO grupo (id, nome) VALUES (3, 'Operador caixa');

INSERT INTO usuario (id,nome, email, senha,proprietario, ativo, tenant) VALUES (1,'Admin', 'admin@storedrink.com', '$2a$10$rODOzMz60JmUUx5J39p9JOb5Ee7c2303YeH0kp42XJOW7Ch9qcBy2',true, true, (SELECT tenant FROM cliente_sistema WHERE tenant = 'sud'));

INSERT INTO permissao VALUES (1, 'ROLE_SUPER_USER');
INSERT INTO permissao VALUES (2, 'ROLE_MANTER_PRODUTO');
INSERT INTO permissao VALUES (3, 'ROLE_MANTER_ENTRADA');
INSERT INTO permissao VALUES (4, 'ROLE_MANTER_PDV');
INSERT INTO permissao VALUES (5, 'ROLE_MANTER_FORNECEDOR');

INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (1, 1);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (1, 2);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (1, 3);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (1, 4);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (1, 5);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (2, 2);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (2, 3);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (2, 4);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (2, 5);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (3, 4);

INSERT INTO usuario_grupo (id_usuario, id_grupo) VALUES ((SELECT id FROM usuario WHERE email = 'admin@storedrink.com'), 1);

INSERT INTO cliente_sistema(acessar_tela_criar_login, bairro, cep, cidade, cpf_cnpj, data_atualizacao, data_cadastro, estado, logradouro, nome_comercio, primeiro_acesso, qtd_usuario, tenant)
VALUES (true,'Vila finsocial','61650005','Goiânia','77835168009890','2021-08-05 00:10:19.887','2021-08-05 00:10:19.887','Goiás','Quandra 45 lote 3','Loja VFS',true,1,'vfs');
INSERT INTO usuario (id,nome, email, senha,proprietario, ativo, tenant) VALUES (2,'usertest', 'usertest@storedrink.com', '$2a$10$rODOzMz60JmUUx5J39p9JOb5Ee7c2303YeH0kp42XJOW7Ch9qcBy2',true, true, (SELECT tenant FROM cliente_sistema WHERE tenant = 'vfs'));
INSERT INTO usuario_grupo (id_usuario, id_grupo) VALUES ((SELECT id FROM usuario WHERE email = 'usertest@storedrink.com'), 2);


INSERT INTO cliente_sistema(acessar_tela_criar_login, bairro, cep, cidade, cpf_cnpj, data_atualizacao, data_cadastro, estado, logradouro, nome_comercio, primeiro_acesso, qtd_usuario, tenant)
VALUES (true,'Setor bueno','61650005','Goiânia','77835168009880','2021-08-20 00:10:19.887','2021-08-05 00:10:19.887','Goiás','Quandra 45 lote 3','Loja das bebidas',true,1,'lb');
INSERT INTO usuario (id,nome, email, senha,proprietario, ativo, tenant) VALUES (3,'lojabtest', 'lojabtest@storedrink.com', '$2a$10$rODOzMz60JmUUx5J39p9JOb5Ee7c2303YeH0kp42XJOW7Ch9qcBy2',true, true, (SELECT tenant FROM cliente_sistema WHERE tenant = 'lb'));
INSERT INTO usuario_grupo (id_usuario, id_grupo) VALUES ((SELECT id FROM usuario WHERE email = 'lojabtest@storedrink.com'), 3);
