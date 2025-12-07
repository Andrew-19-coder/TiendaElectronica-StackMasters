/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Enums.EstadoVenta;
import Modelo.DetalleVentas;
import Modelo.DetalleVentasDAO;
import Modelo.Producto;
import Modelo.ProductoDAO;
import Modelo.Ventas;
import Modelo.VentasDAO;
import Vista.IVista;
import java.util.List;

/**
 *
 * @author Braya
 */
public class ControladorVentas {
    private VentasDAO ventasDAO;
    private DetalleVentasDAO detalleDAO;
    private ProductoDAO productoDAO;
    private IVista vista;

    public ControladorVentas(IVista vista) {
        this.ventasDAO = new VentasDAO();
        this.detalleDAO = new DetalleVentasDAO();
        this.productoDAO = new ProductoDAO();
        this.vista = vista;
    }

    public boolean guardar(Ventas venta, List<DetalleVentas> detalles) {
        try {

            if (detalles == null || detalles.isEmpty()) {
                vista.mostrarError("Debe agregar al menos un producto");
                return false;
            }

            for (DetalleVentas detalle : detalles) {

                Producto producto = productoDAO.obtenerPorId(detalle.getIdProducto());

                if (producto == null) {
                    vista.mostrarError("El producto con ID " + detalle.getIdProducto() + " no fue encontrado en el inventario.");
                    return false;
                }

                if (producto.getCantidadDisponible() < detalle.getCantidad()) {
                    vista.mostrarError("Stock insuficiente para: " + producto.getNombre());
                    return false;
                }
            }

            boolean resultado = ventasDAO.guardarVenta(venta, detalles);
            if (resultado) {
                vista.mostrarMensaje("Venta registrada correctamente", "Éxito");
            } else {
                vista.mostrarError("Error al registrar la venta");
            }
            return resultado;
            
        } catch (Exception ex) {
            vista.mostrarError("Error: " + ex.getMessage());
            return false;
        }
    }

    public boolean cancelarVentas(int idVentas){
        try {
            Ventas venta = ventasDAO.buscar(idVentas);
            
            if(venta==null){
                vista.mostrarError("Venta no encontrada");
                return false;
            }
            
            if (venta.getEstado() == EstadoVenta.CANCELADA) {
                vista.mostrarError("La venta ya esta cancelada");
                return false;
            }

            boolean confirmar = vista.confirmar("¿Esta seguro de que desea cancelar la venta?", "Confirmar cancelacion");
            if (!confirmar) {
                return false;
            }

            boolean resultado = ventasDAO.cancelarVenta(idVentas);
            if (resultado) {
                vista.mostrarMensaje("Venta cancelada correctamente", "Exito");
            }
            return resultado;

        } catch (Exception ex) {
            vista.mostrarError("Error: " + ex.getMessage());
            return false;
        }
    }
    
    public List<Ventas> obtenerTodasLasVentas(){
        return ventasDAO.listarVentas();
    }
    
    public Ventas buscarVentas(int idVenta){
        try {
            return ventasDAO.buscar(idVenta);
        } catch (Exception ex) {
            vista.mostrarError("Error: " + ex.getMessage());
            return null;
        }
    }

    public boolean actualizarObservaciones(int idVenta, String observaciones) {
        try {
            if (observaciones == null || observaciones.trim().isEmpty()) {
                vista.mostrarError("Las observaciones no pueden estar vacías");
                return false;
            }

            Ventas venta = ventasDAO.buscar(idVenta);

            if (venta == null) {
                vista.mostrarError("Venta no encontrada");
                return false;
            }

            if (venta.getEstado() == EstadoVenta.CANCELADA) {
                vista.mostrarError("No se puede actualizar una venta cancelada");
                return false;
            }

            boolean resultado = ventasDAO.actualizarObservaciones(idVenta, observaciones);

            if (resultado) {
                vista.mostrarMensaje("Observaciones actualizadas correctamente", "Éxito");
            }

            return resultado;

        } catch (Exception ex) {
            vista.mostrarError("Error: " + ex.getMessage());
            return false;
        }
    }

    public List<DetalleVentas> obtenerDetallesVenta(int idVenta) {
        return detalleDAO.obtenerPorVenta(idVenta);
    }
}
