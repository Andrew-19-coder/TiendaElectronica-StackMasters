/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;
import Modelo.Producto;
import Modelo.ProductoDAO;
import Vista.MenuPrincipal;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

/**
 *
 * @author Joan
 */
public class MonitorStockBajo extends Thread {
  private MenuPrincipal menuPrincipal;
    private ProductoDAO productoDAO;
    private boolean ejecutando;
    private int intervaloSegundos;
    private boolean alertaMostrada = false;
    
    
     public MonitorStockBajo(MenuPrincipal menuPrincipal, int intervaloSegundos) {
        this.menuPrincipal = menuPrincipal;
        this.productoDAO = new ProductoDAO();
        this.intervaloSegundos = intervaloSegundos;
        this.ejecutando = true;
        
        setDaemon(true);
        setName("Monitor-Stock-Bajo");
    }
    
    @Override
    public void run() {
        System.out.println("✅ MonitorStockBajo iniciado (cada " + intervaloSegundos + " segundos)");
        
        while (ejecutando) {
            try {
                // 1. Consultar productos con stock bajo
                ArrayList<Producto> productosBajos = productoDAO.obtenerBajoStock();
                final int cantidad = productosBajos.size();
                
                // 2. Mostrar popup solo la primera vez que detecta productos bajos
                if (cantidad > 0 && !alertaMostrada) {
                    SwingUtilities.invokeLater(() -> {
                        javax.swing.JOptionPane.showMessageDialog(
                            menuPrincipal,
                            "⚠️ Hay " + cantidad + " producto(s) con stock bajo.\n\n" +
                            "Por favor, revisar el módulo de Productos para reabastecer.",
                            "⚠️ Alerta de Stock Bajo",
                            javax.swing.JOptionPane.WARNING_MESSAGE
                        );
                    });
                    alertaMostrada = true;
                    System.out.println("🔔 Alerta visual mostrada al usuario");
                }
                
                // 3. Log en consola (siempre)
                if (cantidad > 0) {
                    System.out.println("⚠️ [Monitor] " + cantidad + " producto(s) bajo stock detectados");
                } else {
                    System.out.println("✅ [Monitor] Stock OK - Todos los productos tienen stock suficiente");
                }
                
                // 4. Dormir antes de la próxima revisión
                Thread.sleep(intervaloSegundos * 1000);
                
            } catch (InterruptedException e) {
                System.out.println("⏹️ MonitorStockBajo interrumpido");
                ejecutando = false;
            } catch (Exception e) {
                System.err.println("❌ Error en MonitorStockBajo: " + e.getMessage());
            }
        }
        
        System.out.println("🛑 MonitorStockBajo detenido");
    }
    
    /**
     * Detiene el hilo de forma segura
     */
    public void detener() {
        ejecutando = false;
        interrupt();
    }
    
    /**
     * Resetea la alerta para que vuelva a mostrarse
     * (útil si se reabastecen productos)
     */
    public void resetearAlerta() {
        alertaMostrada = false;
    }
}
