/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.proyecto.desafio.nominas.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cl.proyecto.desafio.nominas.model.Empleado;
import cl.proyecto.desafio.nominas.util.NominasUtil;
import org.springframework.stereotype.Service;



/**
 *
 * @author cabra
 */
@Service
public class NominaService {
    private static final String INPUT_PATH = "src/main/resources/input/empleados.csv";
    private static final String OUTPUT_VALIDOS = "src/main/resources/output/empleados_validos.csv";
    private static final String OUTPUT_INVALIDOS = "src/main/resources/output/empleados_invalidos.csv";

    private List<Empleado> empleadosValidos = new ArrayList<>();
    private List<Empleado> empleadosInvalidos = new ArrayList<>();
    private Set<String> ruts = new HashSet<>();

    public void procesarNomina() {
        empleadosValidos.clear();
        empleadosInvalidos.clear();
        ruts.clear();
        leerArchivo();
        escribirArchivosSalida();
    }

    private void leerArchivo() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_PATH))) {
            String linea;
            boolean primeraLinea = true;
            while ((linea = br.readLine()) != null) {
                if (primeraLinea) { primeraLinea = false; continue; }
                String[] datos = linea.split(",");
                if (datos.length != 8) {
                    empleadosInvalidos.add(new Empleado("", "", "", "", 0, 0, 0, null, "Cantidad de columnas incorrecta"));
                    continue;
                }
                String nombre = datos[0], apellido = datos[1], rut = datos[2], cargo = datos[3];
                int salarioBase, bonos, descuentos;
                String fechaIngresoStr = datos[7];
                try {
                    salarioBase = Integer.parseInt(datos[4]);
                    bonos = Integer.parseInt(datos[5]);
                    descuentos = Integer.parseInt(datos[6]);
                } catch (NumberFormatException e) {
                    empleadosInvalidos.add(new Empleado(nombre, apellido, rut, cargo, 0, 0, 0, null, "Error de formato en salario, bonos o descuentos"));
                    continue;
                }
                String motivoError = NominasUtil.validarEmpleado(rut, salarioBase, bonos, descuentos, fechaIngresoStr, ruts);
                LocalDate fechaIngreso = null;
                if (motivoError == null) {
                    fechaIngreso = LocalDate.parse(fechaIngresoStr, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    ruts.add(rut);
                    Empleado empleado = new Empleado(nombre, apellido, rut, cargo, salarioBase, bonos, descuentos, fechaIngreso);
                    calcularAntiguedadYSalarioFinal(empleado);
                    empleadosValidos.add(empleado);
                } else {
                    empleadosInvalidos.add(new Empleado(nombre, apellido, rut, cargo, salarioBase, bonos, descuentos, null, motivoError));
                }
            }
        } catch (IOException e) {
            empleadosInvalidos.add(new Empleado("", "", "", "", 0, 0, 0, null, "Error al leer el archivo de entrada: " + e.getMessage()));
        }
    }

    private void calcularAntiguedadYSalarioFinal(Empleado empleado) {
        int años = Period.between(empleado.getFechaIngreso(), LocalDate.now()).getYears();
        empleado.setAntiguedad(años);
        int bonificacion = 0;
        if (años > 5) bonificacion = (int)(empleado.getSalarioBase() * 0.10);
        else if (años >= 3) bonificacion = (int)(empleado.getSalarioBase() * 0.05);
        empleado.setBonificacionAntiguedad(bonificacion);
        int salarioFinal = empleado.getSalarioBase() + empleado.getBonos() + bonificacion - empleado.getDescuentos();
        empleado.setSalarioFinal(salarioFinal);
    }
    /**
     * 
     */
    private void escribirArchivosSalida() {
        try {
            Files.createDirectories(Paths.get("src/main/resources/output/"));
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_VALIDOS))) {
                bw.write("Nombre;Apellido;RUT;Cargo;SalarioBase;Bonos;BonificacionAntiguedad;Descuentos;SalarioFinal;Antiguedad\n");
                for (Empleado e : empleadosValidos) {
                    bw.write(String.format("%s;%s;%s;%s;%d;%d;%d;%d;%d;%d\n",
                            e.getNombre(), e.getApellido(), e.getRut(), e.getCargo(), e.getSalarioBase(),
                            e.getBonos(), e.getBonificacionAntiguedad(), e.getDescuentos(), e.getSalarioFinal(), e.getAntiguedad()));
                }
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_INVALIDOS))) {
                bw.write("Nombre;Apellido;RUT;Cargo;MotivoError\n");
                for (Empleado e : empleadosInvalidos) {
                    bw.write(String.format("%s;%s;%s;%s;%s\n",
                            e.getNombre(), e.getApellido(), e.getRut(), e.getCargo(),
                            e.getMotivoError()));
                }
            }
        } catch (IOException e) {
        }
    }

    public String obtenerResumen() {
        int totalValidos = empleadosValidos.size();
        int totalInvalidos = empleadosInvalidos.size();
        double promedioSalario = empleadosValidos.stream().mapToInt(Empleado::getSalarioFinal).average().orElse(0.0);
        double promedioAntiguedad = empleadosValidos.stream().mapToInt(Empleado::getAntiguedad).average().orElse(0.0);

        StringBuilder sb = new StringBuilder();
        sb.append("----- Resumen de Nomina -----\n");
        sb.append("Total empleados validos: ").append(totalValidos).append("\n");
        sb.append("Total empleados invalidos: ").append(totalInvalidos).append("\n");
        sb.append("Promedio salario final: $").append(String.format("%.2f", promedioSalario)).append("\n");
        sb.append("Antiguedad promedio: ").append(String.format("%.2f", promedioAntiguedad)).append(" años\n");
        sb.append("Archivos generados en: src/main/resources/output/\n");
        return sb.toString();
    }
}
