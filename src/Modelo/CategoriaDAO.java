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
 * @author oscar
 */
public class CategoriaDAO {
    private Connection conexion;
    
    public CategoriaDAO() {
        this.conexion = ConexionBD.getInstancia().getConexion();
    }
    
   
    public boolean insertar(Categoria cate) {
        String sql = "INSERT INTO Categoria (nombre_categoria, descripcion) VALUES (?, ?)";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, cate.getNombre());        // ← índice 1
            ps.setString(2, cate.getDescripcion());   // ← índice 2
            
            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar categoria: " + e.getMessage());
            return false;
        }
    }
    
   
    public boolean actualizar(Categoria cate) {
        String sql = "UPDATE Categoria SET nombre_categoria = ?, descripcion = ? WHERE id_categoria = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, cate.getNombre());           
            ps.setString(2, cate.getDescripcion());     
            ps.setInt(3, cate.getId());         
            
            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar categoria: " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminar(int idCate) {
        String sql = "DELETE FROM Categoria WHERE id_categoria = ?";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idCate);
            
            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar categoria: " + e.getMessage());
            return false;
        }
    }
    
   
    private Categoria construirCategoria(ResultSet rs) throws SQLException {
        Categoria cate = new Categoria();
        cate.setId(rs.getInt("id_categoria"));
        cate.setNombre(rs.getString("nombre_categoria"));
        cate.setDescripcion(rs.getString("descripcion"));
        return cate;
    }
    
    public ArrayList<Categoria> obtenerTodos() {
        ArrayList<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre_categoria, descripcion FROM Categoria";
        
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                categorias.add(construirCategoria(rs));
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener Categorias: " + e.getMessage());
        }
        
        return categorias;
    }
    
    
    public ArrayList<Categoria> buscar(String criterio) {
        ArrayList<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre_categoria, descripcion FROM Categoria " +
                     "WHERE nombre_categoria LIKE ? OR descripcion LIKE ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            String patron = "%" + criterio + "%";
            ps.setString(1, patron);
            ps.setString(2, patron);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                categorias.add(construirCategoria(rs));
            }
            
            rs.close();
            ps.close();
            
        } catch (SQLException e) {
            System.err.println("Error al buscar categoria: " + e.getMessage());
        }
        
        return categorias;
    }
    
   
    public Categoria obtenerPorId(int id) {
        String sql = "SELECT id_categoria, nombre_categoria, descripcion FROM Categoria WHERE id_categoria = ?";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Categoria cate = construirCategoria(rs);
                rs.close();
                ps.close();
                return cate;
            }
            
            rs.close();
            ps.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener categoria por ID: " + e.getMessage());
        }
        
        return null;
    }
}
