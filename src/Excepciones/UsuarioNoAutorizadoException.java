/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Excepciones;

/**
 *
 * @author Joan
 */
public class UsuarioNoAutorizadoException extends Exception {

    public UsuarioNoAutorizadoException(String mensaje) {
        super(mensaje);
    }

    public UsuarioNoAutorizadoException(String accion, String rolRequerido) {
        super("Acción no autorizada: '" + accion + "'. Se requiere rol: " + rolRequerido);
    }
}
