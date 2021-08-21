
package com.store.drinks.controller;

import com.store.drinks.controller.page.PageWrapper;
import com.store.drinks.entidade.Fornecedor;
import com.store.drinks.repository.querys.fornecedor.FornecedorFilter;
import com.store.drinks.execption.NegocioException;
import com.store.drinks.service.FornecedorService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("fornecedor")
public class FornecedorController {
    
    @Autowired
    private FornecedorService fornecedorService;
    
    @GetMapping
    public ModelAndView pageIndex(Fornecedor fornecedor) {
        return new ModelAndView("fornecedor/Novo");
    }
    
    @GetMapping("novo")
    public ModelAndView pageNovo(Fornecedor fornecedor) {
        return new ModelAndView("fornecedor/Novo");
    }
    
    @PreAuthorize("hasRole('MANTER_FORNECEDOR')")
    @PostMapping("salvar")
    public ModelAndView salvar(@Valid Fornecedor fornecedor, BindingResult result, Model model, RedirectAttributes attributes) {
        try {
            if (result.hasErrors()) {
                return pageIndex(fornecedor);
            }
            fornecedorService.salvar(fornecedor);
        } catch (NegocioException ex) {
            ObjectError error = new ObjectError("erro", ex.getMessage());
            result.addError(error);
            return pageIndex(fornecedor);
        }
        attributes.addFlashAttribute("mensagem", "Fornecedor salvo com sucesso!");
        return new ModelAndView("redirect:/fornecedor", HttpStatus.CREATED);
    }
    
    @GetMapping("pesquisar")
    public ModelAndView pesqisar(FornecedorFilter fornecedorFilter, BindingResult result, @PageableDefault(size = 10) Pageable pageable, HttpServletRequest httpServletRequest) {
        ModelAndView mv = new ModelAndView("fornecedor/Pesquisar");
        PageWrapper<Fornecedor> paginaWrapper = new PageWrapper<>(fornecedorService.filtrar(fornecedorFilter, pageable),httpServletRequest);
        mv.addObject("pagina", paginaWrapper);
        return mv;
    }
}