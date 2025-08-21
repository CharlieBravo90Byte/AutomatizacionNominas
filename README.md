# Proyecto Desafío Nóminas

¡Bienvenido!  
Este proyecto es un **procesador de nóminas de empleados** hecho en Java y Spring Boot.  
Procesa un archivo CSV con datos de empleados, valida la información, calcula antigüedad, bonificaciones y genera archivos de salida con el resumen de nómina.  
Incluye una interfaz **por consola**, así que puedes ejecutarlo en cualquier PC.

---

## ¿Qué hace?

- Lee los empleados desde `src/main/resources/input/empleados.csv`
- Valida los datos (RUT, salario, fechas, etc.)
- Calcula la antigüedad y bonificaciones por años trabajados
- Genera dos archivos:
  - Empleados válidos: `src/main/resources/output/empleados_validos.csv`
  - Empleados inválidos: `src/main/resources/output/empleados_invalidos.csv`
- Muestra un resumen por consola: cantidad de empleados, promedio de salarios, etc.
- Importante que el archivo este en delimitador son las comas (;).

---

## Instalación y ejecución

1. **Clona el repositorio**
   ```bash
   git clone https://github.com/CharlieBravo90Byte/AutomatizacionNominas.git
   cd desafio-nominas
   ```

2. **Asegúrate de tener Java 8 instalado**  
   (o superior, pero funciona en Java 8)

3. **Instala Maven**  
   Si no lo tienes: [Descargar Maven](https://maven.apache.org/download.cgi)

4. **Pon tu archivo de empleados**
   - Ubica el CSV en `src/main/resources/input/empleados.csv`
   - El formato esperado es:  
     ```
     Nombre;Apellido;RUT;Cargo;SalarioBase;Bonos;Descuentos;FechaIngreso
     Maria;Muñoz;12345678-9;QA;800000;100000;50000;2018-03-20
     ```

5. **Compila y ejecuta**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   O, si prefieres:
   ```bash
   mvn exec:java -Dexec.mainClass="cl.proyecto.desafio.nominas.Main"
   ```

---

## ¿Cómo se usa?

Cuando corre, verás un menú por consola:
```
Opciones:
1. Procesar nómina
2. Mostrar resumen
3. Salir
Seleccione una opción:
```
- Opción 1: Procesa el archivo y genera los CSV de salida
- Opción 2: Muestra el resumen de la nómina en pantalla
- Opción 3: Cierra el programa

---

## Estructura del proyecto

```
src/main/java/cl/proyecto/desafio/nominas/
    Main.java
    service/NominaService.java
    model/Empleado.java
    util/NominasUtil.java
    view/Pantalla.java (solo si quieres interfaz gráfica)
src/main/resources/input/empleados.csv
src/main/resources/output/empleados_validos.csv
src/main/resources/output/empleados_invalidos.csv
pom.xml
README.md
