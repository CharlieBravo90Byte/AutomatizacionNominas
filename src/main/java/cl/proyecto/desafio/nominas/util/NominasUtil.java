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
 *
 * @author cabra
 */
public class NominasUtil {
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
