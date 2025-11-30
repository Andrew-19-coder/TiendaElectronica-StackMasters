/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Enums.EstadoVenta;
import java.time.LocalDate;

/**
 *
 * @author Braya
 */
public class Ventas {
    private int idVenta;
    private String fechaVentas;
    private int idCliente;
    private int idUsuario;
    private String observaciones;
    private EstadoVenta estado;
    
    public Ventas(){
        
    }

    public Ventas(int idVenta, String fechaVentas, int idCliente, int idUsuario, String observaciones, EstadoVenta estado) {
        this.idVenta = idVenta;
        this.fechaVentas = fechaVentas;
        this.idCliente = idCliente;
        this.idUsuario = idUsuario;
        this.observaciones = observaciones;
        this.estado = EstadoVenta.COMPLETADA;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public String getFechaVentas() {
        return fechaVentas;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public EstadoVenta getEstado() {
        return estado;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public void setFechaVentas(String fechaVentas) {
        this.fechaVentas = fechaVentas;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setEstado(EstadoVenta estado) {
        this.estado = estado;
    }
    
    
    
    
}
