package br.com.fiap.challenge3.controller;

import br.com.fiap.challenge3.model.Empresas;
import br.com.fiap.challenge3.model.HistoricoInteracoes;
import br.com.fiap.challenge3.model.OpcoesContrato;
import br.com.fiap.challenge3.repository.HistoricoInteracoesRepository;
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
@RequestMapping("/historico")
public class HistoricoInteracoesController {

    @Autowired
    private HistoricoInteracoesRepository historicoInteracoesRepository;

    @Autowired
    private EmpresasRepository empresasRepository;

    @GetMapping
    public ModelAndView listarHistoricos() {
        List<HistoricoInteracoes> listaHistoricos = historicoInteracoesRepository.findAll();
        ModelAndView mv = new ModelAndView("index_historico");
        mv.addObject("historicos", listaHistoricos);
        return mv;
    }

    @GetMapping("/novo")
    public ModelAndView novoHistoricoPage(@RequestParam(value = "idEmpresa", required = false) Long idEmpresa) {
        ModelAndView mv = new ModelAndView("criar_historico");
        HistoricoInteracoes historico = new HistoricoInteracoes();

        if (idEmpresa != null) {
            Optional<Empresas> empresaOptional = empresasRepository.findById(idEmpresa);
            if (empresaOptional.isPresent()) {
                historico.setEmpresa(empresaOptional.get());
            } else {
                mv.setViewName("redirect:/historico");
                return mv;
            }
        }

        mv.addObject("historico", historico);
        mv.addObject("empresas", empresasRepository.findAll());
        mv.addObject("opcoesContrato", OpcoesContrato.values()); // Adicionando o Enum ao modelo
        return mv;
    }

    @PostMapping("/salvar")
    public ModelAndView salvarHistorico(@Valid @ModelAttribute("historico") HistoricoInteracoes historico,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Adiciona logs aqui para verificação
            System.out.println("Erros de validação: " + bindingResult.getAllErrors());

            ModelAndView mv = new ModelAndView("criar_historico");
            mv.addObject("historico", historico);
            mv.addObject("empresas", empresasRepository.findAll());
            mv.addObject("opcoesContrato", OpcoesContrato.values());
            return mv;
        }

        if (historico.getEmpresa() == null || historico.getEmpresa().getId() == null) {
            // Adiciona logs aqui para verificação
            System.out.println("Empresa não selecionada.");

            ModelAndView mv = new ModelAndView("criar_historico");
            mv.addObject("errorMessage", "Por favor, selecione uma empresa.");
            mv.addObject("empresas", empresasRepository.findAll());
            mv.addObject("opcoesContrato", OpcoesContrato.values());
            return mv;
        }

        try {
            historicoInteracoesRepository.save(historico);
            Long idEmpresa = historico.getEmpresa().getId();
            System.out.println("Redirecionando para /empresas/" + idEmpresa + "/detalhes");
            return new ModelAndView("redirect:/empresas/" + idEmpresa + "/detalhes?novaInteracao=true");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/{id}/editar")
    public ModelAndView editarHistoricoPage(@PathVariable Long id) {
        Optional<HistoricoInteracoes> op = historicoInteracoesRepository.findById(id);

        if (op.isPresent()) {
            ModelAndView mv = new ModelAndView("editar_historico");
            mv.addObject("historico", op.get());
            mv.addObject("empresas", empresasRepository.findAll());
            mv.addObject("opcoesContrato", OpcoesContrato.values());  // Adicionando o Enum ao modelo
            return mv;
        } else {
            return new ModelAndView("redirect:/historico");
        }
    }

    @PostMapping("/{id}/editar")
    public ModelAndView atualizarHistorico(@PathVariable Long id, @Valid @ModelAttribute("historico") HistoricoInteracoes historico,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("editar_historico");
            mv.addObject("historico", historico);
            mv.addObject("empresas", empresasRepository.findAll());
            mv.addObject("opcoesContrato", OpcoesContrato.values());  // Correção para opcoesContrato
            return mv;
        }

        Optional<HistoricoInteracoes> op = historicoInteracoesRepository.findById(id);

        if (op.isPresent()) {
            HistoricoInteracoes h = op.get();
            h.setPropostaNegocio(historico.getPropostaNegocio());
            h.setContratoAssinado(historico.getContratoAssinado());
            h.setFeedbackServicosProdutos(historico.getFeedbackServicosProdutos());
            h.setEmpresa(historico.getEmpresa());

            historicoInteracoesRepository.save(h);

            return new ModelAndView("redirect:/empresas/{id}/detalhes", "id", h.getEmpresa().getId());
        } else {
            return new ModelAndView("redirect:/historico");
        }
    }

    @PostMapping("/{id}/remover")
    public String removerHistorico(@PathVariable Long id) {
        Optional<HistoricoInteracoes> historicoOpt = historicoInteracoesRepository.findById(id);
        if (historicoOpt.isPresent()) {
            HistoricoInteracoes historico = historicoOpt.get();
            Long idEmpresa = historico.getEmpresa().getId();
            historicoInteracoesRepository.deleteById(id);
            return "redirect:/empresas/" + idEmpresa + "/detalhes";
        } else {
            return "redirect:/historico";
        }
    }

    @GetMapping("/{id}/detalhes")
    public ModelAndView detalhesHistorico(@PathVariable Long id) {
        Optional<Empresas> empresaOpt = empresasRepository.findById(id);

        if (empresaOpt.isPresent()) {
            Empresas empresa = empresaOpt.get();
            List<HistoricoInteracoes> historicos = historicoInteracoesRepository.findByEmpresaId(id);

            ModelAndView mv = new ModelAndView("detalhes_empresa_historico");
            mv.addObject("empresa", empresa);
            mv.addObject("historicos", historicos);
            return mv;
        } else {
            return new ModelAndView("redirect:/empresas");
        }
    }

}
