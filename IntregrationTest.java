package controller;
import com.consultas.proyecto.dto.UsuarioDTO;
import com.consultas.proyecto.controller.UsuarioController;

import com.consultas.proyecto.model.Usuario;
import com.consultas.proyecto.service.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
public class IntregrationTest {
    /*
     * Testear un Metodo Get y verificar contenido de la respuesta
     *
     * clase: reservaController, metodo: getReservas
     *
     *
     * */

    @Autowired
    MockMvc mockMvc;
    private RoleService usuarioService;

    public IntegrationTest() {
    }

    @Test
    void intTest() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/getAll",
                new Object[0])).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nombre", new Object[0])
                        .value("Pepe")).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testGetReservasPathVariableOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/{idUsuario}",
                new Object[]{1111})).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", new Object[0])
                        .value("Pepe Torre "));
    }

    @Test
    void integracionPostCreateUserTest() throws Exception {
        UsuarioDTO userDto = new UsuarioDTO(01, "Martin", "Marquez", "contraseña", "mm@mail.com", "11-1111-1111", true, null);
        ObjectWriter writer = (new ObjectMapper()).configure(SerializationFeature.WRAP_ROOT_VALUE, false).writer();
        String payloadDto = writer.writeValueAsString(userDto);
        Usuario usuario = usuarioService.create(userDto);
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/usuarios/"+usuario.getIdUsuario(), new Object[0])
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadDto))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType("application/json"))
                .andReturn();
        Assertions.assertEquals(payloadDto, mvcResult.getResponse().getContentAsString());
    }





}
