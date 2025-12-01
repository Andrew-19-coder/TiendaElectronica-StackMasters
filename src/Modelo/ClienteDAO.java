/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
import Utilidades.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author itsth
 */
public class ClienteDAO {
    
     private Connection conexion;
    
    public ClienteDAO() {
        this.conexion = ConexionBD.getInstancia().getConexion();
    }
    
    public boolean insertar(Cliente cliente) {
        String sql = "INSERT INTO Clientes (cedula, nombre_completo, telefono, email, direccion, estado, fecha_registro) VALUES (?, ?, ?, ?, ?, ?, NOW())";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, cliente.getCedula());
            ps.setString(2, cliente.getNombreCompleto());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getEmail());
            ps.setString(5, cliente.getDireccion());
            ps.setBoolean(6, cliente.isEstado());
            
            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar cliente: " + e.getMessage());
            return false;
        }
    }
    
    public boolean actualizar(Cliente cliente) {
        String sql = "UPDATE Clientes SET cedula = ?, nombre_completo = ?, telefono = ?, email = ?, direccion = ?, estado = ? WHERE id_cliente = ?";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, cliente.getCedula());
            ps.setString(2, cliente.getNombreCompleto());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getEmail());
            ps.setString(5, cliente.getDireccion());
            ps.setBoolean(6, cliente.isEstado());
            ps.setInt(7, cliente.getIdCliente());
            
            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminar(int idCliente) {
        String sql = "UPDATE Clientes SET estado = false WHERE id_cliente = ?";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idCliente);
            
            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }
    
    public Cliente obtenerPorId(int idCliente) {
        String sql = "SELECT id_cliente, cedula, nombre_completo, telefono, email, direccion, estado, fecha_registro FROM Clientes WHERE id_cliente = ?";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Cliente cliente = construirCliente(rs);
                rs.close();
                ps.close();
                return cliente;
            }
            
            rs.close();
            ps.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener cliente: " + e.getMessage());
        }
        
        return null;
    }
    
    public Cliente obtenerPorCedula(String cedula) {
        String sql = "SELECT id_cliente, cedula, nombre_completo, telefono, email, direccion, estado, fecha_registro FROM Clientes WHERE cedula = ?";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Cliente cliente = construirCliente(rs);
                rs.close();
                ps.close();
                return cliente;
            }
            
            rs.close();
            ps.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener cliente: " + e.getMessage());
        }
        
        return null;
    }
    
    public ArrayList<Cliente> obtenerTodos() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id_cliente, cedula, nombre_completo, telefono, email, direccion, estado, fecha_registro FROM Clientes WHERE estado = true ORDER BY nombre_completo";
        
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                clientes.add(construirCliente(rs));
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener clientes: " + e.getMessage());
        }
        
        return clientes;
    }
    
    public ArrayList<Cliente> buscar(String criterio) {
        ArrayList<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id_cliente, cedula, nombre_completo, telefono, email, direccion, estado, fecha_registro FROM Clientes WHERE estado = true AND (cedula LIKE ? OR nombre_completo LIKE ?) ORDER BY nombre_completo";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            String parametro = "%" + criterio + "%";
            ps.setString(1, parametro);
            ps.setString(2, parametro);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                clientes.add(construirCliente(rs));
            }
            
            rs.close();
            ps.close();
            
        } catch (SQLException e) {
            System.err.println("Error al buscar clientes: " + e.getMessage());
        }
        
        return clientes;
    }
    
    private Cliente construirCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("id_cliente"));
        cliente.setCedula(rs.getString("cedula"));
        cliente.setNombreCompleto(rs.getString("nombre_completo"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setEmail(rs.getString("email"));
        cliente.setDireccion(rs.getString("direccion"));
        cliente.setEstado(rs.getBoolean("estado"));
        cliente.setFechaRegistro(rs.getString("fecha_registro"));
        
       
        int compras = contarComprasCliente(rs.getString("cedula"));
        cliente.setComprasRealizadas(compras);
        
        return cliente;
    }
    
    private int contarComprasCliente(String cedula) {
        String sql = "SELECT COUNT(*) as total FROM Venta WHERE cedula_cliente = ?";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                int total = rs.getInt("total");
                rs.close();
                ps.close();
                return total;
            }
            
            rs.close();
            ps.close();
            
        } catch (SQLException e) {
          
            return 0;
        }
        
        return 0;
    }
}
