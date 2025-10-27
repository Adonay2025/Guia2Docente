package com.gidoc.app.service;

import com.gidoc.domain.Docente;
import com.gidoc.repo.DocenteRepository;
import com.gidoc.web.dto.DocenteRequest;
import com.gidoc.web.dto.DocenteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocenteServiceTest {

    @Mock
    private DocenteRepository docenteRepository;

    @InjectMocks
    private DocenteService docenteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearDocente_DeberiaGuardarYRetornarDocenteResponse() {
        // Arrange
        DocenteRequest request = new DocenteRequest();
        request.setNombre("Josue");
        request.setApellido("Escobar");
        request.setEmail("josue@test.com");
        request.setEspecialidad("Matemáticas");
        request.setTelefono("1234567890");

        when(docenteRepository.existsByEmail("josue@test.com")).thenReturn(false);

        Docente docenteGuardado = new Docente("Josue", "Escobar", "josue@test.com", "Matemáticas", "1234567890");
        when(docenteRepository.save(any(Docente.class))).thenReturn(docenteGuardado);

        // Act
        DocenteResponse response = docenteService.crearDocente(request);

        // Assert
        assertNotNull(response);
        assertEquals("Josue", response.getNombre());
        verify(docenteRepository, times(1)).save(any(Docente.class));
    }

    @Test
    void crearDocente_DeberiaLanzarExcepcionSiEmailYaExiste() {
        // Arrange
        DocenteRequest request = new DocenteRequest();
        request.setEmail("repetido@test.com");
        when(docenteRepository.existsByEmail("repetido@test.com")).thenReturn(true);

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> docenteService.crearDocente(request));

        assertEquals("El email ya está registrado", ex.getMessage());
    }

    @Test
    void desactivarDocente_DeberiaDesactivarDocenteExistente() {
        Docente docente = new Docente("Ana", "López", "ana@test.com", "Biología", "9876543210");
        docente.setId(1L);

        when(docenteRepository.findById(1L)).thenReturn(Optional.of(docente));

        boolean resultado = docenteService.desactivarDocente(1L);

        assertTrue(resultado);
        assertFalse(docente.getActivo());
        verify(docenteRepository).save(docente);
    }
}
