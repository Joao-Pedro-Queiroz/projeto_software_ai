package br.insper.ai.ferramenta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ferramenta")
public class FerramentaController {

    @Autowired
    private FerramentaService ferramentaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RetornarFerramentaDTO cadastrarFerramenta(@RequestBody CadastrarFerramentaDTO dto, @RequestHeader(name = "email") String email) {
        return ferramentaService.cadastrarFerramenta(dto, email);
    }

    @GetMapping
    public List<Ferramenta> listarFerramentas() {
        return ferramentaService.listarFerramentas();
    }

    @GetMapping("/{id}")
    public Ferramenta buscarFerramenta(@PathVariable String id) {
        return ferramentaService.buscarFerramenta(id);
    }

    @DeleteMapping("/{id}")
    public void deletarFerramenta(@PathVariable String id, @RequestHeader(name = "email") String email) {
        ferramentaService.deletarFerramenta(id, email);
    }
}
