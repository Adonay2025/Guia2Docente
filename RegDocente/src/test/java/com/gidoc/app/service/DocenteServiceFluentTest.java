package com.gidoc.app.service;

import com.gidoc.app.service.DocenteService;
import com.gidoc.domain.Docente;
import com.gidoc.repo.DocenteRepository;
import com.gidoc.web.dto.DocenteRequest;
import com.gidoc.web.dto.DocenteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DocenteServiceFluentTest {

    @Mock
    private DocenteRepository docenteRepository;

    @InjectMocks
    private DocenteService docenteService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearDocente_DeberiaRetornarResponseCorrecta() {
        // Given
        DocenteRequest request = new DocenteRequest();
        request.setNombre("Luis");
        request.setApellido("Hernández");
        request.setEmail("luis@test.com");
        request.setEspecialidad("Física");
        request.setTelefono("1122334455");

        when(docenteRepository.existsByEmail("luis@test.com")).thenReturn(false);
        when(docenteRepository.save(any(Docente.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        DocenteResponse response = docenteService.crearDocente(request);

        // Then
        assertThat(response)
                .isNotNull()
                .extracting(DocenteResponse::getEmail)
                .isEqualTo("luis@test.com");

        assertThat(response.getEspecialidad())
                .isEqualTo("Física");

        verify(docenteRepository).save(any(Docente.class));
    }

    @Test
    void desactivarDocente_DeberiaRetornarFalseSiNoExiste() {
        when(docenteRepository.findById(99L)).thenReturn(Optional.empty());

        boolean resultado = docenteService.desactivarDocente(99L);

        assertThat(resultado).isFalse();
    }
}
