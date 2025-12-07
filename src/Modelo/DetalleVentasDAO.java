/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
import Utilidades.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Joan
 */
public class DetalleVentasDAO {
    
    private Connection conexion;
    
    public DetalleVentasDAO() {
        this.conexion = ConexionBD.getInstancia().getConexion();
    }
    
    public boolean insertar(DetalleVentas detalle) {
        String sql = "INSERT INTO DetalleVentas (id_venta, id_producto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, detalle.getIdVenta());
            ps.setInt(2, detalle.getIdProducto());
            ps.setInt(3, detalle.getCantidad());
            ps.setDouble(4, detalle.getPrecioUnitario());
            
            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar detalle venta: " + e.getMessage());
            return false;
        }
    }
    
    public List<DetalleVentas> obtenerPorVenta(int idVenta) {
        List<DetalleVentas> detalles = new ArrayList<>();
        String sql = "SELECT * FROM DetalleVentas WHERE id_venta = ?";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idVenta);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                detalles.add(construirDetalle(rs));
            }
            
            rs.close();
            ps.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener detalles: " + e.getMessage());
        }
        
        return detalles;
    }
    
    private DetalleVentas construirDetalle(ResultSet rs) throws SQLException {
        DetalleVentas detalle = new DetalleVentas();
        detalle.setIdDetalle(rs.getInt("id_detalle"));
        detalle.setIdVenta(rs.getInt("id_venta"));
        detalle.setIdProducto(rs.getInt("id_producto"));
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));
        return detalle;
    }
} 

