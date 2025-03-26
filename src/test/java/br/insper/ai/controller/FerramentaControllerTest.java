package br.insper.ai.controller;

import br.insper.ai.ferramenta.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class FerramentaControllerTest {

    @InjectMocks
    private FerramentaController ferramentaController;

    @Mock
    private FerramentaService ferramentaService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(ferramentaController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void test_PostFerramenta() throws Exception {

        CadastrarFerramentaDTO postDTO = new CadastrarFerramentaDTO("Teste", "Teste", "Eletrônica");
        String emailUsuario = "eduardo@gmail.com";

        RetornarFerramentaDTO getDTO = new RetornarFerramentaDTO("123","Teste", "Teste", "Eletrônica", "Eduardo", "eduardo@gmail.com");

        ObjectMapper objectMapper = new ObjectMapper();

        Mockito.when(ferramentaService.cadastrarFerramenta(postDTO, emailUsuario)).thenReturn(getDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/ferramenta")
                        .content(objectMapper.writeValueAsString(postDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("email", emailUsuario)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(getDTO)));
    }

    @Test
    void test_GetFerramentasSemFiltro() throws Exception {

        Ferramenta ferramenta = new Ferramenta();
        ferramenta.setId("123");
        ferramenta.setNome("Teste");
        ferramenta.setDescricao("Teste");
        ferramenta.setCategoria("Eletrônica");
        ferramenta.setNomeUsuario("Eduardo");
        ferramenta.setEmailUsuario("eduardo@gmail.com");

        List<Ferramenta> ferramentas = Arrays.asList(ferramenta);

        ObjectMapper objectMapper = new ObjectMapper();

        Mockito.when(ferramentaService.listarFerramentas())
                .thenReturn(ferramentas);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/ferramenta"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(ferramentas)));
    }

    @Test
    void test_GetFerramenta() throws Exception {

        Ferramenta ferramenta = new Ferramenta();
        ferramenta.setId("123");
        ferramenta.setNome("Teste");
        ferramenta.setDescricao("Teste");
        ferramenta.setCategoria("Eletrônica");
        ferramenta.setNomeUsuario("Eduardo");
        ferramenta.setEmailUsuario("eduardo@gmail.com");

        ObjectMapper objectMapper = new ObjectMapper();

        Mockito.when(ferramentaService.buscarFerramenta(ferramenta.getId()))
                .thenReturn(ferramenta);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/ferramenta/"+ ferramenta.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(ferramenta)));
    }

}
