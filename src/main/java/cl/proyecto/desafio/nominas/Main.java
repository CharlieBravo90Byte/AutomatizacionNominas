/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.proyecto.desafio.nominas;

import cl.proyecto.desafio.nominas.service.NominaService;
import java.util.Scanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author cabra
 */


@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Main.class, args);
        NominaService nominaService = ctx.getBean(NominaService.class);

        Scanner scanner = new Scanner(System.in);
        System.out.println("---------------------------------");
        System.out.println("Procesador de Nomina");
        System.out.println("---------------------------------");
        boolean salir = false;
        while (!salir) {
            
            System.out.println("\nOpciones:");
            System.out.println("1. Procesar nomina");
            System.out.println("2. Mostrar resumen");
            System.out.println("3. Salir");
            System.out.println("Seleccione una opcion: ");

            
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    System.out.print("Procesando nómina");
                    for (int i = 0; i < 3; i++) {
                        try { Thread.sleep(500); } catch (InterruptedException e) {}
                        System.out.print(".");
                    }
                    System.out.println();
                    long inicio = System.currentTimeMillis();
                    nominaService.procesarNomina();
                    long fin = System.currentTimeMillis();
                    System.out.println("¡Listo! Nómina procesada.");
                    System.out.println("Tiempo de ejecución: " + ((fin-inicio)/1000.0) + " segundos.");
                    break;
                case "2":
                    System.out.println(nominaService.obtenerResumen());
                    break;
                case "3":
                    salir = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
        System.out.println("¡Hasta luego!");
        SpringApplication.exit(ctx);
    }
}
