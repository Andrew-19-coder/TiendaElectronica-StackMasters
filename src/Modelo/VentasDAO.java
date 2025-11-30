/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Enums.EstadoVenta;
import Utilidades.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author Braya
 */
public class VentasDAO {
    private Connection conexion;

    public VentasDAO() {
        this.conexion = ConexionBD.getInstancia().getConexion();
    }
    
    public boolean guardarVenta(Ventas venta, List<DetalleVentas> list) throws SQLException {
        try {
            Connection cn = ConexionBD.getInstancia().getConexion();
            PreparedStatement ps = cn.prepareStatement("INSERT INTO Ventas(fecha_venta,id_cliente,id_usuario,observaciones,estado) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, venta.getFechaVentas());
            ps.setInt(2, venta.getIdCliente());
            ps.setInt(3, venta.getIdUsuario());
            ps.setString(4, venta.getObservaciones());
            ps.setString(5, venta.getEstado().name());

            int filasAfectadas = ps.executeUpdate();
            
            if(filasAfectadas >0){
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()){
                    int idGenerado = rs.getInt(1);
                    venta.setIdVenta(idGenerado);
                }
                rs.close();
            }
            ps.close();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al guardar una venta producto: " + e.getMessage());
            return false;
        }
    }
    
    public boolean cancelarVenta(int idVenta) throws SQLException {
        try {
            Connection cn = ConexionBD.getInstancia().getConexion();
            PreparedStatement ps = cn.prepareStatement("UPDATE Ventas SET estado = ? WHERE id_venta = ?");
            ps.setString(1, EstadoVenta.CANCELADA.name());
            ps.setInt(2, idVenta);
            
            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al cancelar la venta producto: " + e.getMessage());
            return false;
        }
    }

    public Ventas buscar(int idventa) throws SQLException {
        try {
            Connection cn = ConexionBD.getInstancia().getConexion();
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM Ventas WHERE id_venta = ?");
            ps.setInt(1, idventa);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return construirVentas(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar venta: " + e.getMessage());
        }

        return null;
    }
    
    public List<Ventas> listarVentas() {
        List<Ventas> lista = new ArrayList<>();
        try {
            Connection cn = ConexionBD.getInstancia().getConexion();
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM Ventas ORDER BY id_venta DESC");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(construirVentas(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar venta: " + e.getMessage());
        }

        return lista;
    }
    
    private Ventas construirVentas(ResultSet rs) throws SQLException {
        Ventas venta = new Ventas();
        venta.setIdVenta(rs.getInt("id_venta"));
        venta.setFechaVentas(rs.getString("fecha_venta"));
        venta.setIdCliente(rs.getInt("id_cliente"));
        venta.setIdUsuario(rs.getInt("id_usuario"));
        venta.setObservaciones(rs.getString("observaciones"));
        String estadoTexto =  rs.getString("estado");
        if(estadoTexto != null){
            venta.setEstado(EstadoVenta.valueOf(estadoTexto));
        }
        return venta;
    }
    
}
