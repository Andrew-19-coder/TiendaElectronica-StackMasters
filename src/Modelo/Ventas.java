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
    private double subtotal;
    private double impuesto;
    private double total;
    
    public Ventas() {
    }
    
    public Ventas(int idVenta, String fechaVentas, int idCliente, int idUsuario, String observaciones, EstadoVenta estado) {
        this.idVenta = idVenta;
        this.fechaVentas = fechaVentas;
        this.idCliente = idCliente;
        this.idUsuario = idUsuario;
        this.observaciones = observaciones;
        this.estado = estado;
    }
    
    public int getIdVenta() {
        return idVenta;
    }
    
    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }
    
    public String getFechaVentas() {
        return fechaVentas;
    }
    
    public void setFechaVentas(String fechaVentas) {
        this.fechaVentas = fechaVentas;
    }
    
    public int getIdCliente() {
        return idCliente;
    }
    
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
    
    public int getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public EstadoVenta getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoVenta estado) {
        this.estado = estado;
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    public double getImpuesto() {
        return impuesto;
    }
    
    public void setImpuesto(double impuesto) {
        this.impuesto = impuesto;
    }
    
    public double getTotal() {
        return total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
}
