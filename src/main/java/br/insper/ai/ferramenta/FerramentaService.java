package br.insper.ai.ferramenta;

import br.insper.ai.usuario.Usuario;
import br.insper.ai.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FerramentaService {

    @Autowired
    private FerramentaRepository ferramentaRepository;

    @Autowired
    private UsuarioService usuarioService;

    public RetornarFerramentaDTO cadastrarFerramenta(CadastrarFerramentaDTO dto, String emailUsuario) {
        Usuario usuario = usuarioService.getUsuario(emailUsuario);

        if (!usuario.getPapel().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Ferramenta ferramenta = new Ferramenta();
        ferramenta.setNome(dto.nome());
        ferramenta.setDescricao(dto.descricao());
        ferramenta.setCategoria(dto.categoria());
        ferramenta.setNomeUsuario(usuario.getNome());
        ferramenta.setEmailUsuario(usuario.getEmail());

        ferramenta = ferramentaRepository.save(ferramenta);
        return new RetornarFerramentaDTO(ferramenta.getId(), ferramenta.getNome(), ferramenta.getDescricao(), ferramenta.getCategoria(), ferramenta.getNomeUsuario(), ferramenta.getEmailUsuario());
    }

    public List<Ferramenta> listarFerramentas() {
        return ferramentaRepository.findAll();
    }

    public Ferramenta buscarFerramenta(String id) {
        return ferramentaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void deletarFerramenta(String id, String emailUsuario) {
        Usuario usuario = usuarioService.getUsuario(emailUsuario);

        if (!usuario.getPapel().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Ferramenta ferramenta = buscarFerramenta(id);
        ferramentaRepository.delete(ferramenta);
    }
}
