/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author itsth
 */
public class Cliente {
   private int idCliente;
    private String cedula;
    private String nombreCompleto;
    private String telefono;
    private String email;
    private String direccion;
    private boolean estado;
    private String fechaRegistro;
    private int comprasRealizadas;
    
    public Cliente() {
    }
    
    public Cliente(int idCliente, String cedula, String nombreCompleto, String telefono, String email, String direccion, boolean estado, String fechaRegistro, int comprasRealizadas) {
        this.idCliente = idCliente;
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
        this.comprasRealizadas = comprasRealizadas;
    }
    
    public int getIdCliente() {
        return idCliente;
    }
    
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
    
    public String getCedula() {
        return cedula;
    }
    
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public boolean isEstado() {
        return estado;
    }
    
    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    public String getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    public int getComprasRealizadas() {
        return comprasRealizadas;
    }
    
    public void setComprasRealizadas(int comprasRealizadas) {
        this.comprasRealizadas = comprasRealizadas;
    }
    
    @Override
    public String toString() {
        return nombreCompleto + " (" + cedula + ")";
    }
}
