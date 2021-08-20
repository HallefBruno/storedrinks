
package com.store.drinks.repository.querys.fornecedor;

import com.store.drinks.entidade.Fornecedor;
import com.store.drinks.execption.NegocioException;
import com.store.drinks.repository.util.Multitenancy;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;


public class FornecedorRepositoryImpl implements FornecedorRepositoryCustom {
    
    @PersistenceContext
    private EntityManager manager;
    
    @Autowired
    private Multitenancy multitenancy;
    
    @Override
    public void verificarExistenciaFornecedor(Fornecedor fornecedor) {
        Query query = manager.createNamedQuery("find fornecedor");
        query.setParameter(1, fornecedor.getNome());
        query.setParameter(2, fornecedor.getCpfCnpj());
        query.setParameter(3, multitenancy.getTenantValue());
        List<Fornecedor> resultado = query.getResultList();
        if(!resultado.isEmpty()) {
            if(resultado.size() == 1 && Objects.isNull(fornecedor.getId())) {
                String msg = String.format("Encontra-se no sistema caracteristica desse fornecedor: %s, %s", fornecedor.getNome(),  fornecedor.getCpfCnpj());
                throw new NegocioException(msg);
            } else if (resultado.size() > 1) {
                String msg = String.format("Encontra-se no sistema caracteristica desse fornecedor: %s, %s", fornecedor.getNome(), fornecedor.getCpfCnpj());
                throw new NegocioException(msg);
            }
        }
    }
}
