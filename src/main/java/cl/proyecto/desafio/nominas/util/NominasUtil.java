/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.proyecto.desafio.nominas.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;

/**
 * Clase utilitaria para validaciones relacionadas con el procesamiento de nóminas.
 * 
 * @author cabra
 * @version 1.0
 * @since 2025-08-21
 */
public class NominasUtil {
    
    /**
     * Valida todos los datos de un empleado según las reglas de negocio establecidas.
     * 
     * @param rut el RUT del empleado a validar
     * @param salarioBase el salario base en pesos chilenos
     * @param bonos los bonos adicionales en pesos chilenos
     * @param descuentos los descuentos aplicados en pesos chilenos
     * @param fechaIngresoStr la fecha de ingreso en formato String (yyyy-MM-dd)
     * @param ruts conjunto de RUTs ya procesados para verificar duplicados
     * @return null si todas las validaciones pasan, o un mensaje de error específico si alguna falla
     */
    public static String validarEmpleado(String rut, int salarioBase, int bonos, int descuentos, String fechaIngresoStr, Set<String> ruts) {
        if (ruts.contains(rut))
            return "RUT duplicado";
        if (salarioBase < 400000)
            return "Salario base menor al mínimo permitido";
        if (bonos > salarioBase * 0.5)
            return "Bonos superan el 50% del salario base";
        if (descuentos > salarioBase)
            return "Descuentos superan el salario base";
        LocalDate fechaIngreso;
        try {
            fechaIngreso = LocalDate.parse(fechaIngresoStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            return "Fecha de ingreso con formato inválido";
        }
        if (fechaIngreso.isAfter(LocalDate.now()))
            return "Fecha de ingreso futura";
        return null;
    }
}
