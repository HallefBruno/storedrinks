CREATE OR REPLACE FUNCTION process_produto()
  RETURNS trigger AS
$BODY$
DECLARE
    verificador integer := 0;
    result RECORD;
    BEGIN
        IF (TG_OP = 'INSERT') THEN
	    select descricao_produto,codigo_barra, codigo_produto into result from produto where (descricao_produto = new.descricao_produto or codigo_barra = new.codigo_barra or codigo_produto = new.codigo_produto) and tenant = new.tenant;
	    IF (result.descricao_produto IS NOT NULL) THEN
		RAISE EXCEPTION 'Encontra-se no sistema caracteristica desse produto: %, %, %',result.descricao_produto, result.codigo_barra, result.codigo_produto;
	    END IF;
            RETURN NEW;
        END IF;
        RETURN NULL;
    END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION process_produto()
  OWNER TO postgres;



-- Trigger: produto on produto

-- DROP TRIGGER produto ON produto;

CREATE TRIGGER produto
  BEFORE INSERT
  ON produto
  FOR EACH ROW
  EXECUTE PROCEDURE process_produto();
