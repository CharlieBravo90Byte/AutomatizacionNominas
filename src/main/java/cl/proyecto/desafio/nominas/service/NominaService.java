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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import cl.proyecto.desafio.nominas.model.Empleado;
import cl.proyecto.desafio.nominas.util.NominasUtil;
import java.time.format.DateTimeFormatter;



/**
 * Servicio encargado del procesamiento de nóminas de empleados.
 * 
 * Esta clase utiliza validaciones para RUT, rangos salariales y formatos de fecha,
 * además de calcular automáticamente la antigüedad y bonificaciones correspondientes.
 * 
 * @author cabra
 * @version 1.0
 * @since 2025-08-21
 */
@Service
public class NominaService {
    
    /** Ruta del archivo de entrada con los datos de empleados */
    private static final String INPUT_PATH = "src/main/resources/input/empleados.csv";
    
    /** Ruta del archivo de salida para empleados válidos */
    private static final String OUTPUT_VALIDOS = "src/main/resources/output/empleados_validos.csv";
    
    /** Ruta del archivo de salida para empleados inválidos */
    private static final String OUTPUT_INVALIDOS = "src/main/resources/output/empleados_invalidos.csv";

    /** Lista de empleados que pasaron todas las validaciones */
    private List<Empleado> empleadosValidos = new ArrayList<>();
    
    /** Lista de empleados que no pasaron las validaciones */
    private List<Empleado> empleadosInvalidos = new ArrayList<>();
    
    /** Conjunto de RUTs ya procesados para evitar duplicados */
    private Set<String> ruts = new HashSet<>();

    /**
     * Método principal que procesa la nómina completa.
     * 
     */
    public void procesarNomina() {
        empleadosValidos.clear();
        empleadosInvalidos.clear();
        ruts.clear();
        leerArchivo();
        escribirArchivosSalida();
    }

    /**
     * Lee y procesa el archivo CSV de entrada línea por línea.
     * 
     */
    private void leerArchivo() {
    try (BufferedReader br = new BufferedReader(new FileReader(INPUT_PATH))) {
        String linea;
        boolean primeraLinea = true;
        String separador = null;
        
        while ((linea = br.readLine()) != null) {
            if (primeraLinea) { 
                separador = detectarSeparador(linea);
                System.out.println("Separador: " + (separador.equals(",") ? "coma (,)" : "punto y coma (;)"));
                primeraLinea = false; 
                continue; 
            }
            
            String[] datos = linea.split(separador);
            if (datos.length != 8) {
                empleadosInvalidos.add(new Empleado("", "", "", "", 0, 0, 0, null, "Cantidad de columnas incorrecta"));
                continue;
            }
            
            String nombre = datos[0].trim();
            String apellido = datos[1].trim();
            String rut = datos[2].trim();
            String cargo = datos[3].trim();
            String fechaIngresoStr = datos[7].trim();
            
            int salarioBase, bonos, descuentos;
            
            try {
                salarioBase = Integer.parseInt(datos[4].trim());
                bonos = Integer.parseInt(datos[5].trim());
                descuentos = Integer.parseInt(datos[6].trim());
            } catch (NumberFormatException e) {
                empleadosInvalidos.add(new Empleado(nombre, apellido, rut, cargo, 0, 0, 0, null, "Error de formato en salario, bonos o descuentos"));
                continue;
            }
            String motivoError = NominasUtil.validarEmpleado(rut, salarioBase, bonos, descuentos, fechaIngresoStr, ruts);
            if (motivoError == null) {
                LocalDate fechaIngreso = LocalDate.parse(fechaIngresoStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
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

    /**
     * Calcula la antigüedad del empleado y su salario final.
     * 
     * @param empleado el empleado al cual calcular la antigüedad y salario final
     */
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
     * Escribe los archivos de salida con los empleados válidos e inválidos.
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
    
    /**
 * Cuenta las ocurrencias de un carácter en una cadena.
 * 
 * @param cadena cadena donde buscar
 * @param caracter carácter a contar
 * @return número de ocurrencias
 */
private int contarOcurrencias(String cadena, char caracter) {
    int contador = 0;
    for (int i = 0; i < cadena.length(); i++) {
        if (cadena.charAt(i) == caracter) {
            contador++;
        }
    }
    return contador;
}
    
    /**
 * Detecta automáticamente el separador utilizado en el archivo CSV.
 * Analiza la primera línea y determina si usa comas o punto y coma.
 * 
 * @param primeraLinea línea de encabezados del archivo CSV
 * @return separador detectado ("," o ";")
 */
private String detectarSeparador(String primeraLinea) {
    // Contar ocurrencias de cada separador
    int cantidadComas = contarOcurrencias(primeraLinea, ',');
    int cantidadPuntoYComa = contarOcurrencias(primeraLinea, ';');
    
    // El separador correcto debería generar exactamente 7 separaciones (8 columnas)
    if (cantidadComas == 7) {
        return ",";
    } else if (cantidadPuntoYComa == 7) {
        return ";";
    }
    
    // Si no hay exactamente 7, usar el que tenga más ocurrencias
    if (cantidadPuntoYComa > cantidadComas) {
        return ";";
    } else {
        return ",";
    }
}
    

    /**
     * Genera un resumen estadístico del procesamiento de la nómina.
     * 
     * @return una cadena de texto formateada con el resumen completo del procesamiento
     */
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
