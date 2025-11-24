/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Excepciones;

/**
 *
 * @author Joan
 */
public class ProductoNoEncontradoException extends Exception {

    public ProductoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    public ProductoNoEncontradoException(int idProducto) {
        super("No se encontró el producto con ID: " + idProducto);
    }

    public ProductoNoEncontradoException(String campo, String valor) {
        super("No se encontró el producto con " + campo + ": " + valor);
    }
}
