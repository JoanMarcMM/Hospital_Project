package com.example.hospital_project;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NurseController.class)
class NurseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NurseRepository nurseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Nurse sampleNurse;

    @BeforeEach
    void setUp() {
        sampleNurse = new Nurse();
        // sampleNurse.setId(1L);  // <-- QUITADO
        sampleNurse.setName("Juan");
        sampleNurse.setLastname("Pérez");
        sampleNurse.setUser("juanp");
        sampleNurse.setPw("Password1");
    }

    // =============== POST /nurse/new ===============

    @Test
    void createNurse_validData_returnsCreated() throws Exception {
        when(nurseRepository.save(any(Nurse.class))).thenReturn(sampleNurse);

        mockMvc.perform(post("/nurse/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleNurse)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Juan"));
    }

    @Test
    void createNurse_missingFields_returnsBadRequest() throws Exception {
        Nurse invalid = new Nurse();
        invalid.setName(""); // obligatorio
        invalid.setLastname("Pérez");
        invalid.setUser("user");
        invalid.setPw("Password1");

        mockMvc.perform(post("/nurse/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Faltan campos obligatorios."));
    }

    @Test
    void createNurse_invalidPassword_returnsBadRequest() throws Exception {
        Nurse invalid = new Nurse();
        invalid.setName("Juan");
        invalid.setLastname("Pérez");
        invalid.setUser("user");
        invalid.setPw("short"); // no cumple regex

        mockMvc.perform(post("/nurse/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("La contraseña debe tener al menos 8 caracteres, una letra mayúscula y un número."));
    }

    // =============== GET /nurse/{id} ===============

    @Test
    void getNurse_existingId_returnsOk() throws Exception {
        when(nurseRepository.findById(1L)).thenReturn(Optional.of(sampleNurse));

        mockMvc.perform(get("/nurse/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getNurse_nonExistingId_returnsNotFound() throws Exception {
        when(nurseRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/nurse/99"))
                .andExpect(status().isNotFound());
    }

    // =============== DELETE /nurse/{id} ===============

    @Test
    void deleteNurse_existingId_returnsOk() throws Exception {
        when(nurseRepository.existsById(1L)).thenReturn(true);
        doNothing().when(nurseRepository).deleteById(1L);

        mockMvc.perform(delete("/nurse/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Nurse with ID 1 deleted successfully."));

        verify(nurseRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteNurse_nonExistingId_returnsNotFound() throws Exception {
        when(nurseRepository.existsById(1L)).thenReturn(false);

        mockMvc.perform(delete("/nurse/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Nurse with ID 1 not found."));
    }

    // =============== GET /nurse/index ===============

    @Test
    void getAll_returnsListOfNurses() throws Exception {
        List<Nurse> list = Arrays.asList(sampleNurse);
        when(nurseRepository.findAll()).thenReturn(list);

        mockMvc.perform(get("/nurse/index"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Juan"));
    }

    @Test
    void getAll_empty_returnsEmptyList() throws Exception {
        when(nurseRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/nurse/index"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    // =============== POST /nurse/login ===============

    @Test
    void login_validCredentials_returnsOkTrue() throws Exception {
        Nurse another = new Nurse();
        // another.setId(2L);  // <-- QUITADO
        another.setName("Ana");
        another.setLastname("López");
        another.setUser("anal");
        another.setPw("Password2");

        when(nurseRepository.findAll()).thenReturn(Arrays.asList(sampleNurse, another));

        Nurse loginRequest = new Nurse();
        loginRequest.setUser("juanp");
        loginRequest.setPw("Password1");

        mockMvc.perform(post("/nurse/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void login_invalidCredentials_returnsUnauthorizedFalse() throws Exception {
        when(nurseRepository.findAll()).thenReturn(Arrays.asList(sampleNurse));

        Nurse loginRequest = new Nurse();
        loginRequest.setUser("otro");
        loginRequest.setPw("mal");

        mockMvc.perform(post("/nurse/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("false"));
    }

    // =============== PUT /nurse/{id} ===============

    @Test
    void updateNurse_existingId_validFields_returnsUpdated() throws Exception {
        when(nurseRepository.findById(1L)).thenReturn(Optional.of(sampleNurse));
        when(nurseRepository.save(any(Nurse.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Nurse updateBody = new Nurse();
        updateBody.setName("Pedro");
        updateBody.setLastname("García");
        updateBody.setUser("pedrog");
        updateBody.setPw("NewPass1");

        mockMvc.perform(put("/nurse/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pedro"))
                .andExpect(jsonPath("$.lastname").value("García"))
                .andExpect(jsonPath("$.user").value("pedrog"))
                .andExpect(jsonPath("$.pw").value("NewPass1"));
    }

    @Test
    void updateNurse_nonExistingId_returnsNotFound() throws Exception {
        when(nurseRepository.findById(1L)).thenReturn(Optional.empty());

        Nurse updateBody = new Nurse();
        updateBody.setName("Pedro");

        mockMvc.perform(put("/nurse/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBody)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateNurse_invalidName_returnsBadRequestWithErrors() throws Exception {
        when(nurseRepository.findById(1L)).thenReturn(Optional.of(sampleNurse));

        Nurse updateBody = new Nurse();
        updateBody.setName("Pepe123"); // no pasa el regex

        mockMvc.perform(put("/nurse/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value("name no puede contener números ni caracteres raros"));
    }

    // =============== GET /nurse/name/{name} ===============

    @Test
    void findByName_existing_returnsList() throws Exception {
        when(nurseRepository.findByNameContainingIgnoreCase("juan"))
                .thenReturn(Arrays.asList(sampleNurse));

        mockMvc.perform(get("/nurse/name/juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Juan"));
    }

    @Test
    void findByName_notExisting_returnsNotFound() throws Exception {
        when(nurseRepository.findByNameContainingIgnoreCase("xxx"))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/nurse/name/xxx"))
                .andExpect(status().isNotFound());
    }
}
