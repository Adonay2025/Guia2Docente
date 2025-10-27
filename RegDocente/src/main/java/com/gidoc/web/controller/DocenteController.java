package com.gidoc.web.controller;

import com.gidoc.app.service.DocenteService;
import com.gidoc.web.dto.DocenteRequest;
import com.gidoc.web.dto.DocenteResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/docentes")
@CrossOrigin(origins = "*")
public class DocenteController {

    @Autowired
    private DocenteService docenteService;

    @PostMapping
    public ResponseEntity<?> crearDocente(@Valid @RequestBody DocenteRequest request) {
        try {
            DocenteResponse docente = docenteService.crearDocente(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(docente);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<DocenteResponse>> obtenerTodosLosDocentes() {
        List<DocenteResponse> docentes = docenteService.obtenerTodosLosDocentes();
        return ResponseEntity.ok(docentes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocenteResponse> obtenerDocentePorId(@PathVariable Long id) {
        Optional<DocenteResponse> docente = docenteService.obtenerDocentePorId(id);
        return docente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<DocenteResponse>> obtenerDocentesPorEspecialidad(
            @PathVariable String especialidad) {
        List<DocenteResponse> docentes = docenteService.obtenerDocentesPorEspecialidad(especialidad);
        return ResponseEntity.ok(docentes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarDocente(
            @PathVariable Long id,
            @Valid @RequestBody DocenteRequest request) {
        try {
            Optional<DocenteResponse> docente = docenteService.actualizarDocente(id, request);
            return docente.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarDocente(@PathVariable Long id) {
        boolean desactivado = docenteService.desactivarDocente(id);
        return desactivado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}