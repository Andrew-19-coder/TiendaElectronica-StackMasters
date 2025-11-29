/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author oscar
 */
public class Categoria {
    private String id;
    private String nombre;
    private String Descripcion;
    private boolean activa;

    public String getId() {return id;}

    public String getNombre() {return nombre;}

    public String getDescripcion() {return Descripcion;}

    public boolean isActiva() {return activa;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public void setDescripcion(String Descripcion) {this.Descripcion = Descripcion;}

    public void setActiva(boolean activa) {this.activa = activa;}

    public Categoria(String id, String nombre, String Descripcion, boolean activa) {
        this.id = id;this.nombre = nombre;this.Descripcion = Descripcion;this.activa = activa;
    }
  
}
