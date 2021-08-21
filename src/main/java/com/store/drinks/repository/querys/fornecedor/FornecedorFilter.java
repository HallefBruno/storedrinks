
package com.store.drinks.repository.querys.fornecedor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FornecedorFilter {
    
    private String nomeFornecedor;
    private String cpfCnpj;
    private String telefone;
    
}

