INSERT INTO grupo (id, nome) VALUES (1, 'Administrador');
INSERT INTO grupo (id, nome) VALUES (2, 'Vendedor');

INSERT INTO usuario (id,nome, email, senha, ativo) VALUES (1,'Admin', 'admin@storedrink.com', '$2a$10$rODOzMz60JmUUx5J39p9JOb5Ee7c2303YeH0kp42XJOW7Ch9qcBy2', true);

INSERT INTO permissao VALUES (1, 'ROLE_CADASTRAR_PRODUTO');
INSERT INTO permissao VALUES (2, 'ROLE_CADASTRAR_ENTRADA');

INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (1, 1);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (1, 2);

INSERT INTO usuario_grupo (id_usuario, id_grupo) VALUES (
(SELECT id FROM usuario WHERE email = 'admin@storedrink.com'), 1);