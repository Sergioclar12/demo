package com.gestion.empleados.gestion_empleados_backend.controlador;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.empleados.gestion_empleados_backend.excepciones.ResourceNotFoundException;
import com.gestion.empleados.gestion_empleados_backend.modelo.Empleado;
import com.gestion.empleados.gestion_empleados_backend.repositorio.EmpleadoRepositorio;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin(origins = "*")
public class EmpleadoControlador {

    @Autowired
    private EmpleadoRepositorio repositorio;

    // Este método sirve para listar todos los empleados
    @GetMapping("/empleados")
    public List<Empleado> listarTodosLosEmpleados() {
        return repositorio.findAll();
    }

    // Este método sirve para guardar un empleado
    @PostMapping("/empleados")
    public Empleado guardarEmpleado(@RequestBody Empleado empleado) {
        return repositorio.save(empleado);
    }

    // Este método sirve para obtener un empleado
    @GetMapping("/empleados/{id}")
    public ResponseEntity<Empleado> obtenerEmpleadoPorId(@PathVariable Long id) {
        Empleado empleado = repositorio.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No existe el empleado con el ID : " + id));
        return ResponseEntity.ok(empleado);
    }

    // Este método sirve para actualizar un empleado
    @PutMapping("/empleados/{id}")
    public ResponseEntity<Empleado> actualizarEmpleado(@PathVariable Long id, @RequestBody Empleado detallesEmpleado) {
        Empleado empleado = repositorio.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No existe el empleado con el ID : " + id));

        empleado.setNombre(detallesEmpleado.getNombre());
        empleado.setApellido(detallesEmpleado.getApellido());
        empleado.setEmail(detallesEmpleado.getEmail());

        Empleado empleadoActualizado = repositorio.save(empleado);
        return ResponseEntity.ok(empleadoActualizado);
    }

    // Este método sirve para eliminar un empleado
    @DeleteMapping("/empleados/{id}")
    public ResponseEntity<Map<String, Boolean>> eliminarEmpleado(@PathVariable Long id) {
        Empleado empleado = repositorio.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No existe el empleado con el ID : " + id));

        repositorio.delete(empleado);
        Map<String, Boolean> respuesta = new HashMap<>();
        respuesta.put("eliminar", Boolean.TRUE);
        return ResponseEntity.ok(respuesta);
    }

    // Método para exportar la lista de empleados a un archivo Word
    @GetMapping("/empleados/export-word")
    public ResponseEntity<InputStreamResource> exportarEmpleadosAWord() throws IOException {
        List<Empleado> empleados = repositorio.findAll();
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("Lista de Empleados");

            for (Empleado empleado : empleados) {
                paragraph = document.createParagraph();
                run = paragraph.createRun();
                run.setText("ID: " + empleado.getId() + ", Nombre: " + empleado.getNombre() + ", Apellido: " + empleado.getApellido() + ", Email: " + empleado.getEmail());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.write(out);
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=empleados.docx");

            return new ResponseEntity<>(new InputStreamResource(in), headers, HttpStatus.OK);
        }
    }
}
