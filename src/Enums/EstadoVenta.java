/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Enums;

/**
 *
 * @author Joan
 */
public enum EstadoVenta {
     COMPLETADA("Completada"),
    CANCELADA("Cancelada");
    
    private final String descripcion;
    EstadoVenta(String descripcion) {
        this.descripcion = descripcion;
    }
   
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}
