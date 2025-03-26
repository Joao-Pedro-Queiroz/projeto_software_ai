package br.insper.ai.service;

import br.insper.ai.ferramenta.*;
import br.insper.ai.usuario.Usuario;
import br.insper.ai.usuario.UsuarioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FerramentaServiceTest {

    @InjectMocks
    private FerramentaService ferramentaService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private FerramentaRepository ferramentaRepository;

    @Test
    void test_saveFerramentaSuccesfully() {

        Ferramenta ferramenta = new Ferramenta();
        ferramenta.setId("123");
        ferramenta.setNome("Teste");
        ferramenta.setDescricao("Teste");
        ferramenta.setCategoria("Eletrônica");
        ferramenta.setNomeUsuario("Eduardo");
        ferramenta.setEmailUsuario("eduardo@gmail.com");

        Usuario usuario = new Usuario();
        usuario.setNome(ferramenta.getNome());
        usuario.setEmail(ferramenta.getEmailUsuario());
        usuario.setPapel("ADMIN");

        CadastrarFerramentaDTO postDTO = new CadastrarFerramentaDTO("Teste", "Teste", "Eletrônica");

        Mockito.when(usuarioService.getUsuario(ferramenta.getEmailUsuario())).thenReturn(usuario);

        Mockito.when(ferramentaRepository.save(ferramenta)).thenReturn(ferramenta);

        RetornarFerramentaDTO getDTO = ferramentaService.cadastrarFerramenta(postDTO, ferramenta.getEmailUsuario());

        Assertions.assertEquals("123", getDTO.id());
        Assertions.assertEquals("Teste", getDTO.nome());
        Assertions.assertEquals("Teste", getDTO.descricao());
        Assertions.assertEquals("Eletrônica", getDTO.categoria());
        Assertions.assertEquals("Eduardo", getDTO.nomeUsuario());
        Assertions.assertEquals("eduardo@gmail.com", getDTO.emailUsuario());
    }

    @Test
    void test_saveFerramentaErrorUsuarioNotFound() {
        CadastrarFerramentaDTO postDTO = new CadastrarFerramentaDTO("Teste", "Teste", "Eletrônica");
        String emailUsuario = "naoexiste@gmail.com";

        Mockito.when(usuarioService.getUsuario(emailUsuario))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> ferramentaService.cadastrarFerramenta(postDTO, emailUsuario));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void test_saveFerramentaErrorForbidden() {
        CadastrarFerramentaDTO postDTO = new CadastrarFerramentaDTO("Teste", "Teste", "Eletrônica");

        Usuario usuarioComum = new Usuario();
        usuarioComum.setNome("Usuário Comum");
        usuarioComum.setEmail("usuario_comum@gmail.com");
        usuarioComum.setPapel("USER");

        Mockito.when(usuarioService.getUsuario(usuarioComum.getEmail())).thenReturn(usuarioComum);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> ferramentaService.cadastrarFerramenta(postDTO, usuarioComum.getEmail()));

        Assertions.assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    @Test
    void test_findAllFerramentasWhenFerramentasIsEmpty() {

        Mockito.when(ferramentaRepository.findAll()).thenReturn(new ArrayList<>());

        List<Ferramenta> ferramentas = ferramentaService.listarFerramentas();

        Assertions.assertEquals(0, ferramentas.size());
    }

    @Test
    void test_findFerramentaByIdSuccesfully() {

        Ferramenta ferramenta = new Ferramenta();
        ferramenta.setId("123");
        ferramenta.setNome("Teste");
        ferramenta.setDescricao("Teste");
        ferramenta.setCategoria("Eletrônica");
        ferramenta.setNomeUsuario("Eduardo");
        ferramenta.setEmailUsuario("eduardo@gmail.com");

        Mockito.when(ferramentaRepository.findById(ferramenta.getId())).thenReturn(Optional.of(ferramenta));

        ferramenta = ferramentaService.buscarFerramenta(ferramenta.getId());

        Assertions.assertEquals("123", ferramenta.getId());
        Assertions.assertEquals("Teste", ferramenta.getNome());
        Assertions.assertEquals("Teste", ferramenta.getDescricao());
        Assertions.assertEquals("Eletrônica", ferramenta.getCategoria());
        Assertions.assertEquals("Eduardo", ferramenta.getNomeUsuario());
        Assertions.assertEquals("eduardo@gmail.com", ferramenta.getEmailUsuario());
    }

    @Test
    void test_findFerramentaByIdErrorNotFound() {

        String ferramentaId = "123";

        Mockito.when(ferramentaRepository.findById(ferramentaId)).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> ferramentaService.buscarFerramenta(ferramentaId));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void test_deleteFerramentaSuccesfully() {

        Ferramenta ferramenta = new Ferramenta();
        ferramenta.setId("123");

        Usuario usuario = new Usuario();
        usuario.setNome("Eduardo");
        usuario.setEmail("eduardo@gmail.com");
        usuario.setPapel("ADMIN");

        Mockito.when(usuarioService.getUsuario(usuario.getEmail())).thenReturn(usuario);

        Mockito.when(ferramentaRepository.findById(ferramenta.getId())).thenReturn(Optional.of(ferramenta));

        ferramentaService.deletarFerramenta(ferramenta.getId(), usuario.getEmail());

        Mockito.verify(ferramentaRepository, Mockito.times(1)).delete(ferramenta);
    }

    @Test
    void test_deleteFerramentaErrorForbidden() {
        Ferramenta ferramenta = new Ferramenta();
        ferramenta.setId("123");

        Usuario usuarioComum = new Usuario();
        usuarioComum.setNome("Usuário Comum");
        usuarioComum.setEmail("usuario_comum@gmail.com");
        usuarioComum.setPapel("USER");

        Mockito.when(usuarioService.getUsuario(usuarioComum.getEmail())).thenReturn(usuarioComum);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> ferramentaService.deletarFerramenta(ferramenta.getId(), usuarioComum.getEmail()));

        Assertions.assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }
}
