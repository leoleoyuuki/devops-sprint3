package br.com.fiap.challenge3.controller;

import br.com.fiap.challenge3.model.DesempenhoFinanceiro;
import br.com.fiap.challenge3.model.Empresas;
import br.com.fiap.challenge3.repository.DesempenhoFinanceiroRepository;
import br.com.fiap.challenge3.repository.EmpresasRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/desempenho")
public class DesempenhoFinanceiroController {

    @Autowired
    private DesempenhoFinanceiroRepository desempenhoFinanceiroRepository;

    @Autowired
    private EmpresasRepository empresasRepository;

    @GetMapping
    public ModelAndView listarDesempenho() {
        List<DesempenhoFinanceiro> listaDesempenho = desempenhoFinanceiroRepository.findAll();
        ModelAndView mv = new ModelAndView("index_desempenho");
        mv.addObject("desempenhos", listaDesempenho);
        return mv;
    }

    @GetMapping("/novo")
    public ModelAndView novoDesempenhoPage(@RequestParam(value = "idEmpresa", required = false) Long idEmpresa) {
        ModelAndView mv = new ModelAndView("criar_desempenho");
        DesempenhoFinanceiro desempenho = new DesempenhoFinanceiro();

        if (idEmpresa != null) {
            Optional<Empresas> empresaOptional = empresasRepository.findById(idEmpresa);
            if (empresaOptional.isPresent()) {
                desempenho.setEmpresa(empresaOptional.get());
            } else {
                mv.setViewName("redirect:/desempenho");
                return mv;
            }
        }

        mv.addObject("desempenho", desempenho);
        mv.addObject("empresas", empresasRepository.findAll());
        return mv;
    }

    @PostMapping("/salvar")
    public ModelAndView salvarDesempenho(@Valid @ModelAttribute("desempenho") DesempenhoFinanceiro desempenho,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Adiciona logs aqui para verificação
            System.out.println("Erros de validação: " + bindingResult.getAllErrors());
            ModelAndView mv = new ModelAndView("criar_desempenho");
            mv.addObject("desempenho", desempenho);
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        }

        if (desempenho.getEmpresa() == null || desempenho.getEmpresa().getId() == null) {
            // Adiciona logs aqui para verificação
            System.out.println("Empresa não selecionada.");
            ModelAndView mv = new ModelAndView("criar_desempenho");
            mv.addObject("errorMessage", "Por favor, selecione uma empresa.");
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        }

        try {
            desempenhoFinanceiroRepository.save(desempenho);
            Long idEmpresa = desempenho.getEmpresa().getId();
            System.out.println("Redirecionando para /empresas/" + idEmpresa + "/detalhes");
            return new ModelAndView("redirect:/empresas/" + idEmpresa + "/detalhes?novoDesempenho=true");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    @GetMapping("/{id}/editar")
    public ModelAndView editarDesempenhoPage(@PathVariable Long id) {
        Optional<DesempenhoFinanceiro> op = desempenhoFinanceiroRepository.findById(id);

        if (op.isPresent()) {
            ModelAndView mv = new ModelAndView("editar_desempenho");
            mv.addObject("desempenho", op.get());
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        } else {
            return new ModelAndView("redirect:/desempenho");
        }
    }

    @PostMapping("/{id}/editar")
    public ModelAndView atualizarDesempenho(@PathVariable Long id, @Valid @ModelAttribute("desempenho") DesempenhoFinanceiro desempenho,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("editar_desempenho");
            mv.addObject("desempenho", desempenho);
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        }

        Optional<DesempenhoFinanceiro> op = desempenhoFinanceiroRepository.findById(id);

        if (op.isPresent()) {
            DesempenhoFinanceiro d = op.get();
            d.setReceita(desempenho.getReceita());
            d.setLucro(desempenho.getLucro());
            d.setCrescimento(desempenho.getCrescimento());
            d.setEmpresa(desempenho.getEmpresa());

            desempenhoFinanceiroRepository.save(d);

            Long idEmpresa = d.getEmpresa().getId();
            return new ModelAndView("redirect:/empresas/" + idEmpresa + "/detalhes");
        } else {
            return new ModelAndView("redirect:/desempenho");
        }
    }

    @PostMapping("/{id}/remover")
    public String removerDesempenho(@PathVariable Long id) {
        try {
            Optional<DesempenhoFinanceiro> desempenhoOpt = desempenhoFinanceiroRepository.findById(id);
            if (desempenhoOpt.isPresent()) {
                DesempenhoFinanceiro desempenho = desempenhoOpt.get();
                Long idEmpresa = desempenho.getEmpresa().getId();
                desempenhoFinanceiroRepository.deleteById(id);
                return "redirect:/empresas/" + idEmpresa + "/detalhes";
            } else {
                return "redirect:/empresas";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/empresas";
        }
    }

    @GetMapping("/empresa/{id}/detalhes")
    public ModelAndView detalhesEmpresa(@PathVariable Long id) {
        Optional<Empresas> empresaOpt = empresasRepository.findById(id);

        if (empresaOpt.isPresent()) {
            Empresas empresa = empresaOpt.get();
            List<DesempenhoFinanceiro> desempenhos = desempenhoFinanceiroRepository.findByEmpresaId(id);

            ModelAndView mv = new ModelAndView("detalhes_empresa");
            mv.addObject("empresa", empresa);
            mv.addObject("desempenhos", desempenhos);
            return mv;
        } else {
            return new ModelAndView("redirect:/empresas");
        }
    }
}

