/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Excepciones;

/**
 *
 * @author Joan
 */
public class ConexionBDException extends Exception {
      public ConexionBDException(String mensaje) {
        super(mensaje);
    }
    
    public ConexionBDException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
