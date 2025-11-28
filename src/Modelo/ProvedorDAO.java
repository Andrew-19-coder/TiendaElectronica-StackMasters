/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Utilidades.ConexionBD;
import java.sql.*;

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
        String sql = "INSERT INTO Provedores (id, nombre, contacto, direccion) VALUES (?, ?, ?, ?)";
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
        String sql = "UPDATE Provedores SET id = ?, nombre = ?, contacto = ?, direccion = ? WHERE id = ?";

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

    public boolean eliminar(int idProducto) {
        String sql = "DELETE FROM Provedores WHERE id=?";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idProducto);
            
            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar provedor: " + e.getMessage());
            return false;
        }
    }
}
