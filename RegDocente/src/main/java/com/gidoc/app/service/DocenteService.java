package com.gidoc.app.service;

import com.gidoc.domain.Docente;
import com.gidoc.repo.DocenteRepository;
import com.gidoc.web.dto.DocenteRequest;
import com.gidoc.web.dto.DocenteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocenteService {

    @Autowired
    private DocenteRepository docenteRepository;

    public DocenteResponse crearDocente(DocenteRequest request) {
        // Verificar si el email ya existe
        if (docenteRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        Docente docente = new Docente();
        docente.setNombre(request.getNombre());
        docente.setApellido(request.getApellido());
        docente.setEmail(request.getEmail());
        docente.setEspecialidad(request.getEspecialidad());
        docente.setTelefono(request.getTelefono());

        Docente docenteGuardado = docenteRepository.save(docente);
        return convertirAResponse(docenteGuardado);
    }

    public List<DocenteResponse> obtenerTodosLosDocentes() {
        return docenteRepository.findByActivoTrue()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public Optional<DocenteResponse> obtenerDocentePorId(Long id) {
        return docenteRepository.findById(id)
                .filter(Docente::getActivo)
                .map(this::convertirAResponse);
    }

    public Optional<DocenteResponse> actualizarDocente(Long id, DocenteRequest request) {
        return docenteRepository.findById(id)
                .filter(Docente::getActivo)
                .map(docente -> {
                    // Verificar si el nuevo email ya existe en otro docente
                    if (!docente.getEmail().equals(request.getEmail()) &&
                            docenteRepository.existsByEmail(request.getEmail())) {
                        throw new RuntimeException("El email ya está registrado en otro docente");
                    }

                    docente.setNombre(request.getNombre());
                    docente.setApellido(request.getApellido());
                    docente.setEmail(request.getEmail());
                    docente.setEspecialidad(request.getEspecialidad());
                    docente.setTelefono(request.getTelefono());

                    Docente docenteActualizado = docenteRepository.save(docente);
                    return convertirAResponse(docenteActualizado);
                });
    }

    public boolean desactivarDocente(Long id) {
        return docenteRepository.findById(id)
                .filter(Docente::getActivo)
                .map(docente -> {
                    docente.setActivo(false);
                    docenteRepository.save(docente);
                    return true;
                })
                .orElse(false);
    }

    public List<DocenteResponse> obtenerDocentesPorEspecialidad(String especialidad) {
        return docenteRepository.findByEspecialidad(especialidad)
                .stream()
                .filter(Docente::getActivo)
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    private DocenteResponse convertirAResponse(Docente docente) {
        return new DocenteResponse(
                docente.getId(),
                docente.getNombre(),
                docente.getApellido(),
                docente.getEmail(),
                docente.getEspecialidad(),
                docente.getTelefono(),
                docente.getFechaRegistro(),
                docente.getActivo()
        );
    }
}