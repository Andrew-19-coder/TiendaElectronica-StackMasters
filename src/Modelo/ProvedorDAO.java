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
public class ProvedorDAO {

    private Connection conexion;

    public ProvedorDAO(Connection conexion) {
        this.conexion = ConexionBD.getInstancia().getConexion();
    }

    public boolean insertar(Provedor provedor) {
        String sql = "INSERT INTO Provedores (id_provedor, nombre, contacto, direccion) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, provedor.getId());
            ps.setString(2, provedor.getNombre());
            ps.setString(3, provedor.getContacto());
            ps.setString(4, provedor.getDireccion());

            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar proveedor: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Provedor provedor) {
        String sql = "UPDATE Provedores SET id_provedor = ?, nombre = ?, contacto = ?, direccion = ? WHERE id_provedor = ?";

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, provedor.getId());
            ps.setString(2, provedor.getNombre());
            ps.setString(3, provedor.getContacto());
            ps.setString(4, provedor.getDireccion());

            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar provedor: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM Provedores WHERE id_provedor = ?";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            
            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar provedor: " + e.getMessage());
            return false;
        }
    }
    
    public ArrayList<Provedor> obtenerTodos() {
        ArrayList<Provedor> provedores = new ArrayList<>();
        String sql = "SELECT id_provedor, nombre, contacto, direccion FROM Provedores";
        
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                provedores.add(construirProvedor(rs));
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener provedores: " + e.getMessage());
        }
        
        return provedores;
    }
    
    private Provedor construirProvedor(ResultSet rs) throws SQLException {
        Provedor provedor = new Provedor();
        provedor.setId(rs.getInt("id"));
        provedor.setNombre(rs.getString("nombre"));
        provedor.setContacto(rs.getString("contacto"));
        provedor.setDireccion(rs.getString("direccion"));
        return provedor;
    }
}
