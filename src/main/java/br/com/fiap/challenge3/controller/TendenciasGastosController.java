package br.com.fiap.challenge3.controller;

import br.com.fiap.challenge3.model.Empresas;
import br.com.fiap.challenge3.model.TendenciasGastos;
import br.com.fiap.challenge3.repository.EmpresasRepository;
import br.com.fiap.challenge3.repository.TendenciasGastosRepository;
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
@RequestMapping("/tendencias")
public class TendenciasGastosController {

    @Autowired
    private TendenciasGastosRepository tendenciasGastosRepository;

    @Autowired
    private EmpresasRepository empresasRepository;

    @GetMapping
    public ModelAndView listarTendencias() {
        List<TendenciasGastos> listaTendencias = tendenciasGastosRepository.findAll();
        ModelAndView mv = new ModelAndView("index_tendencias");
        mv.addObject("tendencias", listaTendencias);
        return mv;
    }

    @GetMapping("/nova")
    public ModelAndView novaTendenciaPage(@RequestParam(value = "idEmpresa", required = false) Long idEmpresa) {
        ModelAndView mv = new ModelAndView("criar_tendencia");
        TendenciasGastos tendencia = new TendenciasGastos();

        if (idEmpresa != null) {
            Optional<Empresas> empresaOptional = empresasRepository.findById(idEmpresa);
            if (empresaOptional.isPresent()) {
                tendencia.setEmpresa(empresaOptional.get());
            } else {
                mv.setViewName("redirect:/tendencias");
                return mv;
            }
        }

        mv.addObject("tendencia", tendencia);
        mv.addObject("empresas", empresasRepository.findAll());
        return mv;
    }

    @PostMapping("/salvar")
    public ModelAndView salvarTendencia(@Valid @ModelAttribute("tendencia") TendenciasGastos tendencia,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("criar_tendencia");
            mv.addObject("tendencia", tendencia);
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        }

        if (tendencia.getEmpresa() == null || tendencia.getEmpresa().getId() == null) {
            // Caso a empresa não esteja associada corretamente
            ModelAndView mv = new ModelAndView("criar_tendencia");
            mv.addObject("errorMessage", "Por favor, selecione uma empresa.");
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        }

        try {
            TendenciasGastos salvaTendencia = tendenciasGastosRepository.save(tendencia);
            Long idEmpresa = tendencia.getEmpresa().getId(); // Certifique-se de que a empresa não seja nula

            // Redireciona para a página de detalhes da empresa com um parâmetro indicando que uma nova tendência foi criada
            return new ModelAndView("redirect:/empresas/" + idEmpresa + "/detalhes?novaTendencia=true");
        } catch (Exception e) {
            // Registra o erro e mostra uma mensagem amigável para o usuário
            e.printStackTrace();
            ModelAndView mv = new ModelAndView("criar_tendencia");
            mv.addObject("tendencia", tendencia);
            mv.addObject("empresas", empresasRepository.findAll());
            mv.addObject("errorMessage", "Ocorreu um erro ao salvar a tendência de gastos. Tente novamente.");
            return mv;
        }
    }



    @GetMapping("/{id}/editar")
    public ModelAndView editarTendenciaPage(@PathVariable Long id) {
        Optional<TendenciasGastos> op = tendenciasGastosRepository.findById(id);

        if (op.isPresent()) {
            ModelAndView mv = new ModelAndView("editar_tendencia");
            mv.addObject("tendencia", op.get()); // Certifique-se de que o campo "empresa" está presente no objeto
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        } else {
            return new ModelAndView("redirect:/tendencias");
        }
    }


    @PostMapping("/{id}/editar")
    public ModelAndView atualizarTendencia(@PathVariable Long id, @Valid @ModelAttribute("tendencia") TendenciasGastos tendencia,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("editar_tendencia");
            mv.addObject("tendencia", tendencia);
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        }

        Optional<TendenciasGastos> op = tendenciasGastosRepository.findById(id);

        if (op.isPresent()) {
            TendenciasGastos t = op.get();
            t.setAno(tendencia.getAno());
            t.setGastoMarketing(tendencia.getGastoMarketing());
            t.setGastoAutomacao(tendencia.getGastoAutomacao());
            t.setEmpresa(tendencia.getEmpresa()); // Verifique se a empresa está presente

            tendenciasGastosRepository.save(t);

            Long idEmpresa = t.getEmpresa().getId();
            return new ModelAndView("redirect:/empresas/" + idEmpresa + "/detalhes");
        } else {
            return new ModelAndView("redirect:/tendencias");
        }
    }

    @PostMapping("/{id}/remover")
    public String removeTendencia(@PathVariable Long id) {
        Optional<TendenciasGastos> tendenciaOpt = tendenciasGastosRepository.findById(id);
        if (tendenciaOpt.isPresent()) {
            TendenciasGastos tendencia = tendenciaOpt.get();
            Long idEmpresa = tendencia.getEmpresa().getId();
            tendenciasGastosRepository.deleteById(id);
            return "redirect:/empresas/" + idEmpresa + "/detalhes";
        } else {
            return "redirect:/tendencias";
        }
    }

    @GetMapping("/{id}/detalhes")
    public ModelAndView detalhesEmpresa(@PathVariable Long id) {
        Optional<Empresas> empresaOpt = empresasRepository.findById(id);

        if (empresaOpt.isPresent()) {
            Empresas empresa = empresaOpt.get();
            List<TendenciasGastos> tendencias = tendenciasGastosRepository.findByEmpresaId(id);

            ModelAndView mv = new ModelAndView("detalhes_empresa");
            mv.addObject("empresa", empresa);
            mv.addObject("tendencias", tendencias);
            return mv;
        } else {
            return new ModelAndView("redirect:/empresas");
        }
    }
}
