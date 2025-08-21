/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.proyecto.desafio.nominas.model;

import java.time.LocalDate;

/**
 * Entidad que representa un empleado en el sistema de nómina.
 * @author cabra
 * @version 1.0
 * @since 2025-08-21
 */
public class Empleado {
    
    private String nombre;
    private String apellido;
    private String rut;
    private String cargo;
    private int salarioBase;
    private int bonos;
    private int descuentos;
    private LocalDate fechaIngreso;
    private int antiguedad;
    private int bonificacionAntiguedad;
    private int salarioFinal;
    private String motivoError;

    
    public Empleado() {
    }

    public Empleado(String nombre, String apellido, String rut, String cargo, int salarioBase, int bonos, int descuentos, LocalDate fechaIngreso) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.rut = rut;
        this.cargo = cargo;
        this.salarioBase = salarioBase;
        this.bonos = bonos;
        this.descuentos = descuentos;
        this.fechaIngreso = fechaIngreso;
    }

    public Empleado(String nombre, String apellido, String rut, String cargo, int salarioBase, int bonos, int descuentos, LocalDate fechaIngreso, String motivoError) {
        this(nombre, apellido, rut, cargo, salarioBase, bonos, descuentos, fechaIngreso);
        this.motivoError = motivoError;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public int getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(int salarioBase) {
        this.salarioBase = salarioBase;
    }

    public int getBonos() {
        return bonos;
    }

    public void setBonos(int bonos) {
        this.bonos = bonos;
    }

    public int getDescuentos() {
        return descuentos;
    }

    public void setDescuentos(int descuentos) {
        this.descuentos = descuentos;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public int getAntiguedad() {
        return antiguedad;
    }

    public void setAntiguedad(int antiguedad) {
        this.antiguedad = antiguedad;
    }

    public int getBonificacionAntiguedad() {
        return bonificacionAntiguedad;
    }

    public void setBonificacionAntiguedad(int bonificacionAntiguedad) {
        this.bonificacionAntiguedad = bonificacionAntiguedad;
    }

    public int getSalarioFinal() {
        return salarioFinal;
    }

    public void setSalarioFinal(int salarioFinal) {
        this.salarioFinal = salarioFinal;
    }

    public String getMotivoError() {
        return motivoError;
    }

    public void setMotivoError(String motivoError) {
        this.motivoError = motivoError;
    }

    @Override
    public String toString() {
        return "Empleado{" + "nombre=" + nombre + ", apellido=" + apellido + ", rut=" + rut + ", cargo=" + cargo + ", salarioBase=" + salarioBase + ", bonos=" + bonos + ", descuentos=" + descuentos + ", fechaIngreso=" + fechaIngreso + ", antiguedad=" + antiguedad + ", bonificacionAntiguedad=" + bonificacionAntiguedad + ", salarioFinal=" + salarioFinal + ", motivoError=" + motivoError + '}';
    }
    
    
    
}
