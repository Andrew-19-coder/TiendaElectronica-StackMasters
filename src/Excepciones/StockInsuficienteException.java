/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Excepciones;

/**
 *
 * @author Joan
 */
public class StockInsuficienteException extends Exception {
   private int stockDisponible;
    private int cantidadSolicitada;
    
    public StockInsuficienteException(String nombreProducto, int stockDisponible, int cantidadSolicitada) {
        super("Stock insuficiente para el producto '" + nombreProducto + 
              "'. Disponible: " + stockDisponible + ", Solicitado: " + cantidadSolicitada);
        this.stockDisponible = stockDisponible;
        this.cantidadSolicitada = cantidadSolicitada;
    }
    
    public int getStockDisponible() {
        return stockDisponible;
    }
    
    public int getCantidadSolicitada() {
        return cantidadSolicitada;
    }  
}
