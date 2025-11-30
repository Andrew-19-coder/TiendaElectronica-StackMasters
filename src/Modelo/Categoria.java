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
    private int id;
    private String nombre;
    private String Descripcion;

    public int getId() {return id;}

    public String getNombre() {return nombre;}

    public String getDescripcion() {return Descripcion;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public void setDescripcion(String Descripcion) {this.Descripcion = Descripcion;}

    public void setId(int id) {this.id = id; }
    
    public Categoria(int id, String nombre, String Descripcion) {
        this.id = id;this.nombre = nombre;this.Descripcion = Descripcion;
    }
  
}
