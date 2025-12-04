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

    public ProvedorDAO() {
        this.conexion = ConexionBD.getInstancia().getConexion();
    }

    public boolean insertar(Provedor provedor) {
        String sql = "INSERT INTO Provedores (nombre, contacto, telefono, email, direccion, estado, fecha_registro) VALUES (?, ?, ?, ?, ?, ?, NOW())";

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, provedor.getNombre());
            ps.setString(2, provedor.getContacto());
            ps.setString(3, provedor.getTelefono());
            ps.setString(4, provedor.getEmail());
            ps.setString(5, provedor.getDireccion());
            ps.setBoolean(6, provedor.isEstado());

            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar proveedor: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Provedor provedor) {
        String sql = "UPDATE Provedores SET nombre = ?, contacto = ?, telefono = ?, email = ?, direccion = ?, estado = ? WHERE id_provedor = ?";

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, provedor.getNombre());
            ps.setString(2, provedor.getContacto());
            ps.setString(3, provedor.getTelefono());
            ps.setString(4, provedor.getEmail());
            ps.setString(5, provedor.getDireccion());
            ps.setBoolean(6, provedor.isEstado());
            ps.setInt(7, provedor.getId());

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
        String sql = "SELECT id_provedor, nombre, contacto, telefono, email, direccion, estado, fecha_registro FROM Provedores";

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

    public ArrayList<Provedor> buscar(String criterio) {
        ArrayList<Provedor> provedores = new ArrayList<>();
        String sql = "SELECT id_provedor, nombre, contacto, telefono, email, direccion, estado FROM Provedores WHERE nombre LIKE ? OR contacto LIKE ? OR telefono LIKE ?";

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            String parametro = "%" + criterio + "%";
            ps.setString(1, parametro);
            ps.setString(2, parametro);
            ps.setString(3, parametro);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                provedores.add(construirProvedor(rs));
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.err.println("Error al buscar proveedores: " + e.getMessage());
        }

        return provedores;
    }

    private Provedor construirProvedor(ResultSet rs) throws SQLException {
        Provedor provedor = new Provedor();
        provedor.setId(rs.getInt("id_provedor"));
        provedor.setNombre(rs.getString("nombre"));
        provedor.setContacto(rs.getString("contacto"));
        provedor.setTelefono(rs.getString("telefono"));
        provedor.setEmail(rs.getString("email"));
        provedor.setDireccion(rs.getString("direccion"));
        provedor.setEstado(rs.getBoolean("estado"));
        return provedor;
    }
}
