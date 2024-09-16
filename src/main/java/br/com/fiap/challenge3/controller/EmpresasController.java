package br.com.fiap.challenge3.controller;

import br.com.fiap.challenge3.model.*;
import br.com.fiap.challenge3.repository.*;
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
@RequestMapping("/empresas")
public class EmpresasController {

    @Autowired
    private EmpresasRepository empresasRepository;
    @Autowired
    private TendenciasGastosRepository tendenciasGastosRepository;
    @Autowired
    private DesempenhoFinanceiroRepository desempenhoFinanceiroRepository;
    @Autowired
    private HistoricoInteracoesRepository historicoInteracoesRepository;
    @Autowired
    private ComportamentoNegociosRepository comportamentoNegociosRepository;

    @GetMapping
    public ModelAndView listarEmpresas() {
        List<Empresas> listaEmpresas = empresasRepository.findAll();
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("empresas", listaEmpresas);
        return mv;
    }

    @GetMapping("/nova")
    public ModelAndView novaEmpresaPage() {
        ModelAndView mv = new ModelAndView("nova_empresa");
        mv.addObject("empresa", new Empresas());
        mv.addObject("lista_setores", OpcoesSetor.values());
        mv.addObject("lista_tipos", OpcoesTipo.values());
        mv.addObject("lista_tamanhos", OpcoesTamanho.values());
        return mv;
    }

    @PostMapping("/salvar")
    public ModelAndView salvarEmpresa(@Valid @ModelAttribute("empresa") Empresas empresa, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("nova_empresa");
            mv.addObject("empresa", empresa);
            mv.addObject("lista_setores", OpcoesSetor.values());
            mv.addObject("lista_tipos", OpcoesTipo.values());
            mv.addObject("lista_tamanhos", OpcoesTamanho.values());
            return mv;
        } else {
            empresasRepository.save(empresa);
            return new ModelAndView("redirect:/empresas");
        }
    }

    @GetMapping("/{id}/editar")
    public ModelAndView retornaPaginaEdicao(@PathVariable Long id) {
        Optional<Empresas> op = empresasRepository.findById(id);

        if (op.isPresent()) {
            ModelAndView mv = new ModelAndView("editar_empresa");
            mv.addObject("empresa", op.get());
            mv.addObject("lista_setores", OpcoesSetor.values());
            mv.addObject("lista_tipos", OpcoesTipo.values());
            mv.addObject("lista_tamanhos", OpcoesTamanho.values());
            return mv;
        } else {
            return new ModelAndView("redirect:/empresas");
        }
    }


    @PostMapping("/{id}/editar")
    public ModelAndView atualizarEmpresa(@PathVariable Long id, @Valid @ModelAttribute("empresa") Empresas empresa,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("editar_empresa");
            mv.addObject("empresa", empresa);
            mv.addObject("lista_setores", OpcoesSetor.values());
            mv.addObject("lista_tipos", OpcoesTipo.values());
            mv.addObject("lista_tamanhos", OpcoesTamanho.values());
            return mv;
        } else {
            Optional<Empresas> op = empresasRepository.findById(id);

            if (op.isPresent()) {
                Empresas emp = op.get();
                emp.setNome(empresa.getNome());
                emp.setSetor(empresa.getSetor());
                emp.setTipoEmpresa(empresa.getTipoEmpresa());
                emp.setTamanho(empresa.getTamanho());
                emp.setLocalizacaoGeografica(empresa.getLocalizacaoGeografica());
                emp.setNumeroFuncionarios(empresa.getNumeroFuncionarios());
                emp.setCliente(empresa.getCliente());

                empresasRepository.save(emp);
                return new ModelAndView("redirect:/empresas");
            } else {
                return new ModelAndView("redirect:/empresas");
            }
        }
    }

    @PostMapping("/{id}/remover")
    public String removeEmpresa(@PathVariable Long id) {
        empresasRepository.deleteById(id);
        return "redirect:/empresas";
    }

    @GetMapping("/{id}/detalhes")
    public ModelAndView detalhesEmpresa(@PathVariable Long id,
                                        @RequestParam(value = "novoComportamento", required = false) Boolean novoComportamento) {
        System.out.println("Accessing detalhesEmpresa with id: " + id);

        Optional<Empresas> empresaOpt = empresasRepository.findById(id);

        if (empresaOpt.isPresent()) {
            Empresas empresa = empresaOpt.get();

            List<TendenciasGastos> tendencias = tendenciasGastosRepository.findByEmpresaId(id);
            List<DesempenhoFinanceiro> desempenhos = desempenhoFinanceiroRepository.findByEmpresaId(id);
            List<HistoricoInteracoes> historicos = historicoInteracoesRepository.findByEmpresaId(id);
            List<ComportamentoNegocios> comportamentos = comportamentoNegociosRepository.findByEmpresaId(id);

            ModelAndView mv = new ModelAndView("detalhes_empresa");
            mv.addObject("empresa", empresa);
            mv.addObject("tendencias", tendencias);
            mv.addObject("desempenhos", desempenhos);
            mv.addObject("historicos", historicos);
            mv.addObject("comportamentos", comportamentos);

            boolean showCreateButtonTendencia = tendencias.isEmpty();
            mv.addObject("showCreateButtonTendencia", showCreateButtonTendencia);

            boolean showCreateButtonDesempenho = desempenhos.isEmpty();
            mv.addObject("showCreateButtonDesempenho", showCreateButtonDesempenho);

            boolean showCreateButtonHistorico = historicos.isEmpty();
            mv.addObject("showCreateButtonHistorico", showCreateButtonHistorico);

            boolean showCreateButtonComportamento = comportamentos.isEmpty();
            mv.addObject("showCreateButtonComportamento", showCreateButtonComportamento);

            return mv;
        } else {
            System.out.println("Empresa not found with id: " + id);
            return new ModelAndView("redirect:/empresas");
        }
    }


}
