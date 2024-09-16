package br.com.fiap.challenge3.controller;

import br.com.fiap.challenge3.model.ComportamentoNegocios;
import br.com.fiap.challenge3.model.Empresas;
import br.com.fiap.challenge3.model.OpcoesRecursos;
import br.com.fiap.challenge3.repository.ComportamentoNegociosRepository;
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
@RequestMapping("/comportamento")
public class ComportamentoNegociosController {

    @Autowired
    private ComportamentoNegociosRepository comportamentoNegociosRepository;

    @Autowired
    private EmpresasRepository empresasRepository;

    @GetMapping
    public ModelAndView listarComportamentos() {
        List<ComportamentoNegocios> listaComportamentos = comportamentoNegociosRepository.findAll();
        ModelAndView mv = new ModelAndView("index_comportamento");
        mv.addObject("comportamentos", listaComportamentos);
        return mv;
    }

    @GetMapping("/novo")
    public ModelAndView novoComportamentoPage(@RequestParam(value = "idEmpresa", required = false) Long idEmpresa) {
        ModelAndView mv = new ModelAndView("criar_comportamento");
        ComportamentoNegocios comportamento = new ComportamentoNegocios();

        if (idEmpresa != null) {
            Optional<Empresas> empresaOptional = empresasRepository.findById(idEmpresa);
            if (empresaOptional.isPresent()) {
                comportamento.setEmpresa(empresaOptional.get());
            } else {
                mv.setViewName("redirect:/comportamento");
                return mv;
            }
        }

        mv.addObject("comportamento", comportamento);
        mv.addObject("empresas", empresasRepository.findAll());
        mv.addObject("opcoesRecursos", OpcoesRecursos.values()); // Passa os valores do Enum para o template
        return mv;
    }

    @PostMapping("/salvar")
    public ModelAndView salvarComportamento(@Valid @ModelAttribute("comportamento") ComportamentoNegocios comportamento,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("criar_comportamento");
            mv.addObject("comportamento", comportamento);
            mv.addObject("empresas", empresasRepository.findAll());
            mv.addObject("opcoesRecursos", OpcoesRecursos.values());
            return mv;
        }

        if (comportamento.getEmpresa() == null || comportamento.getEmpresa().getId() == null) {
            ModelAndView mv = new ModelAndView("criar_comportamento");
            mv.addObject("errorMessage", "Por favor, selecione uma empresa.");
            mv.addObject("empresas", empresasRepository.findAll());
            mv.addObject("opcoesRecursos", OpcoesRecursos.values());
            return mv;
        }

        try {
            comportamentoNegociosRepository.save(comportamento);
            Long idEmpresa = comportamento.getEmpresa().getId();
            return new ModelAndView("redirect:/empresas/" + idEmpresa + "/detalhes?novoComportamento=true");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/{id}/editar")
    public ModelAndView editarComportamentoPage(@PathVariable Long id) {
        Optional<ComportamentoNegocios> comportamentoOpt = comportamentoNegociosRepository.findById(id);
        if (comportamentoOpt.isPresent()) {
            ModelAndView mv = new ModelAndView("editar_comportamento");
            mv.addObject("comportamento", comportamentoOpt.get());
            mv.addObject("empresas", empresasRepository.findAll());
            mv.addObject("opcoesRecursos", OpcoesRecursos.values());
            return mv;
        } else {
            return new ModelAndView("redirect:/comportamento");
        }
    }

    @PostMapping("/{id}/editar")
    public ModelAndView atualizarComportamento(@PathVariable Long id, @Valid @ModelAttribute("comportamento") ComportamentoNegocios comportamento,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("editar_comportamento");
            mv.addObject("comportamento", comportamento);
            mv.addObject("empresas", empresasRepository.findAll());
            mv.addObject("opcoesRecursos", OpcoesRecursos.values());
            return mv;
        }

        // Recupera o comportamento existente do banco de dados pelo ID
        Optional<ComportamentoNegocios> comportamentoOpt = comportamentoNegociosRepository.findById(id);

        if (comportamentoOpt.isPresent()) {
            ComportamentoNegocios comportamentoExistente = comportamentoOpt.get();

            // Atualiza os valores do comportamento existente com os valores do formulário
            comportamentoExistente.setInteracoesPlataforma(comportamento.getInteracoesPlataforma());
            comportamentoExistente.setFrequenciaUso(comportamento.getFrequenciaUso());
            comportamentoExistente.setFeedback(comportamento.getFeedback());
            comportamentoExistente.setUsoRecursosEspecificos(comportamento.getUsoRecursosEspecificos());

            // Verifica se o comportamento possui uma empresa associada
            if (comportamento.getEmpresa() != null && comportamento.getEmpresa().getId() != null) {
                comportamentoExistente.setEmpresa(comportamento.getEmpresa());
            } else {
                // Caso não tenha, redireciona para uma página de erro
                return new ModelAndView("redirect:/error");
            }

            // Salva o comportamento atualizado
            comportamentoNegociosRepository.save(comportamentoExistente);

            Long idEmpresa = comportamentoExistente.getEmpresa().getId();
            return new ModelAndView("redirect:/empresas/" + idEmpresa + "/detalhes");
        } else {
            // Se o comportamento não for encontrado, redireciona para a lista de comportamentos
            return new ModelAndView("redirect:/comportamento");
        }
    }

    @PostMapping("/{id}/remover")
    public String removerComportamento(@PathVariable Long id) {
        try {
            Optional<ComportamentoNegocios> comportamentoOpt = comportamentoNegociosRepository.findById(id);
            if (comportamentoOpt.isPresent()) {
                ComportamentoNegocios comportamento = comportamentoOpt.get();
                Long idEmpresa = comportamento.getEmpresa().getId();
                comportamentoNegociosRepository.deleteById(id);
                return "redirect:/empresas/" + idEmpresa + "/detalhes";
            } else {
                return "redirect:/comportamento";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/comportamento";
        }
    }

    @GetMapping("/empresa/{id}/detalhes")
    public ModelAndView detalhesEmpresa(@PathVariable Long id) {
        Optional<Empresas> empresaOpt = empresasRepository.findById(id);

        if (empresaOpt.isPresent()) {
            Empresas empresa = empresaOpt.get();
            List<ComportamentoNegocios> comportamentos = comportamentoNegociosRepository.findByEmpresaId(id);

            ModelAndView mv = new ModelAndView("detalhes_empresa");
            mv.addObject("empresa", empresa);
            mv.addObject("comportamentos", comportamentos);
            return mv;
        } else {
            return new ModelAndView("redirect:/empresas");
        }
    }
}
