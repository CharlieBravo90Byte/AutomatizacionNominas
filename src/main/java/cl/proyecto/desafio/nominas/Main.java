/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.proyecto.desafio.nominas;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import cl.proyecto.desafio.nominas.service.NominaService;

/**
 * Clase principal de la aplicación de procesamiento de nóminas.
 * 
 * @author cabra
 * @version 1.0
 * @since 2025-08-21
 */
@SpringBootApplication
public class Main {
    
    /**
     * Método principal que inicia la aplicación Spring Boot y ejecuta el menú interactivo.
     * 
     * @param args argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Main.class, args);
        NominaService nominaService = ctx.getBean(NominaService.class);

        // Scanner para manejar la entrada del usuario desde consola
        // Se mantiene abierto durante toda la ejecución de la aplicación
        Scanner scanner = new Scanner(System.in);
        System.out.println("---------------------------------");
        System.out.println("Procesador de Nomina");
        System.out.println("---------------------------------");
        boolean salir = false;
        
        // Bucle principal del menú interactivo
        while (!salir) {
            
            System.out.println("\nOpciones:");
            System.out.println("1. Procesar nomina");
            System.out.println("2. Mostrar resumen");
            System.out.println("3. Salir");
            System.out.println("Seleccione una opcion: ");

            
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    // Procesamiento de nómina con indicador visual de progreso
                    System.out.print("Procesando nómina");
                    for (int i = 0; i < 3; i++) {
                        try { Thread.sleep(500); } catch (InterruptedException e) {}
                        System.out.print(".");
                    }
                    System.out.println();
                    
                    // Medición del tiempo de ejecución del procesamiento
                    long inicio = System.currentTimeMillis();
                    nominaService.procesarNomina();
                    long fin = System.currentTimeMillis();
                    
                    System.out.println("¡Listo! Nómina procesada.");
                    System.out.println("Tiempo de ejecución: " + ((fin-inicio)/1000.0) + " segundos.");
                    break;
                case "2":
                    // Mostrar resumen estadístico del último procesamiento
                    System.out.println(nominaService.obtenerResumen());
                    break;
                case "3":
                    // Salir de la aplicación
                    salir = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
        
        System.out.println("¡Hasta luego!");
        scanner.close(); // Cerrar el scanner antes de salir
        SpringApplication.exit(ctx);
    }
}
