/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Excepciones;

/**
 *
 * @author Joan
 */
public class VentaInvalidaException extends Exception {

    public VentaInvalidaException(String mensaje) {
        super(mensaje);
    }

    public VentaInvalidaException(String campo, String razon) {
        super("Venta inválida - " + campo + ": " + razon);
    }
}
