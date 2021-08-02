INSERT INTO grupo (id, nome) VALUES (1, 'Administrador');
INSERT INTO grupo (id, nome) VALUES (2, 'Vendedor');
INSERT INTO grupo (id, nome) VALUES (3, 'SuperUser');

INSERT INTO usuario (id,nome, email, senha, ativo) VALUES (1,'Admin', 'admin@storedrink.com', '$2a$10$rODOzMz60JmUUx5J39p9JOb5Ee7c2303YeH0kp42XJOW7Ch9qcBy2', true);

INSERT INTO permissao VALUES (1, 'ROLE_MANTER_PRODUTO');
INSERT INTO permissao VALUES (2, 'ROLE_MANTER_ENTRADA');
INSERT INTO permissao VALUES (3, 'ROLE_MANTER_PDV');
INSERT INTO permissao VALUES (4, 'ROLE_SUPER_USER');

INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (1, 1);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (1, 2);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (1, 3);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (2, 3);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (3, 1);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (3, 2);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (3, 3);
INSERT INTO grupo_permissao (id_grupo, id_permissao) VALUES (3, 4);

INSERT INTO usuario_grupo (id_usuario, id_grupo) VALUES (
(SELECT id FROM usuario WHERE email = 'admin@storedrink.com'), 3);