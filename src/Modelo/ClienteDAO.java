/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author itsth
 */
public class ClienteDAO {
    private Connection conexion;
    
    public boolean insertar(Cliente cliente) {
        String sql = "INSERT INTO Clientes (cedula, nombreCompleto, telefono, correo) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, cliente.getCedula());
            ps.setString(2, cliente.getNombreCompleto());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getCorreo());

            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar cliente: " + e.getMessage());
            return false;
        }
    }
    
    public boolean actualizar(Cliente cliente) {
        String sql = "UPDATE Clientes SET cedula = ?, nombre_completo = ?, telefono = ?, correo = ? WHERE cedula = ?";

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, cliente.getCedula());
            ps.setString(2, cliente.getNombreCompleto());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getCorreo());

            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminar(String cedula) {
        String sql = "DELETE FROM Clientes WHERE cedula = ?";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, cedula);
            
            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar provedor: " + e.getMessage());
            return false;
        }
    }
    
    public ArrayList<Cliente> obtenerTodos() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT cedula, nombre_completo, telefono, correo FROM Clientes";
        
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
    
    private Cliente construirCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setCedula(rs.getString("cedula"));
        cliente.setNombreCompleto(rs.getString("nombre_completo"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setCorreo(rs.getString("correo"));
        return cliente;
    }
}
