package com.gestion.empleados.gestion_empleados_backend.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.empleados.gestion_empleados_backend.modelo.Empleado;

@Repository
public interface EmpleadoRepositorio extends JpaRepository <Empleado, Long> {



}
