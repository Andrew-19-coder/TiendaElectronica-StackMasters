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
 * @author Joan
 */
public class ProductoDAO {
 private Connection conexion;
    
    public ProductoDAO() {
        this.conexion = ConexionBD.getInstancia().getConexion();
    }
    
    public boolean insertar(Producto producto) {
        String sql = "INSERT INTO Productos (codigo, nombre, descripcion, precio, cantidad_disponible, `stock_mínimo`, id_categoria, id_provedor, estado, fecha_registro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getDescripcion());
            ps.setDouble(4, producto.getPrecio());
            ps.setInt(5, producto.getCantidadDisponible());
            ps.setInt(6, producto.getStockMinimo());
            ps.setInt(7, producto.getIdCategoria());
            ps.setInt(8, producto.getIdProveedor());
            ps.setBoolean(9, producto.isEstado());
            
            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }
    
    public boolean actualizar(Producto producto) {
        String sql = "UPDATE Productos SET codigo = ?, nombre = ?, descripcion = ?, precio = ?, cantidad_disponible = ?, `stock_mínimo` = ?, id_categoria = ?, id_provedor = ?, estado = ? WHERE id_producto = ?";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getDescripcion());
            ps.setDouble(4, producto.getPrecio());
            ps.setInt(5, producto.getCantidadDisponible());
            ps.setInt(6, producto.getStockMinimo());
            ps.setInt(7, producto.getIdCategoria());
            ps.setInt(8, producto.getIdProveedor());
            ps.setBoolean(9, producto.isEstado());
            ps.setInt(10, producto.getIdProducto());
            
            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminar(int idProducto) {
        String sql = "UPDATE Productos SET estado = false WHERE id_producto = ?";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idProducto);
            
            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }
    
    public Producto obtenerPorId(int idProducto) {
        String sql = "SELECT p.*, c.nombre_categoria, pr.nombre as nombre_proveedor " +
                     "FROM Productos p " +
                     "LEFT JOIN Categoria c ON p.id_categoria = c.id_categoria " +
                     "LEFT JOIN Provedores pr ON p.id_provedor = pr.id_provedor " +
                     "WHERE p.id_producto = ?";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Producto producto = construirProducto(rs);
                rs.close();
                ps.close();
                return producto;
            }
            
            rs.close();
            ps.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener producto: " + e.getMessage());
        }
        
        return null;
    }
    
    public ArrayList<Producto> obtenerTodos() {
        ArrayList<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre_categoria, pr.nombre as nombre_proveedor " +
                     "FROM Productos p " +
                     "LEFT JOIN Categoria c ON p.id_categoria = c.id_categoria " +
                     "LEFT JOIN Provedores pr ON p.id_provedor = pr.id_provedor " +
                     "WHERE p.estado = true ORDER BY p.nombre";
        
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                productos.add(construirProducto(rs));
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
        }
        
        return productos;
    }
    
    public ArrayList<Producto> obtenerBajoStock() {
        ArrayList<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre_categoria, pr.nombre as nombre_proveedor " +
                     "FROM Productos p " +
                     "LEFT JOIN Categoria c ON p.id_categoria = c.id_categoria " +
                     "LEFT JOIN Provedores pr ON p.id_provedor = pr.id_provedor " +
                     "WHERE p.estado = true AND p.cantidad_disponible <= p.`stock_mínimo` " +
                     "ORDER BY p.cantidad_disponible";
        
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                productos.add(construirProducto(rs));
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener productos con stock bajo: " + e.getMessage());
        }
        
        return productos;
    }
    
    public ArrayList<Producto> buscar(String criterio) {
        ArrayList<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre_categoria, pr.nombre as nombre_proveedor " +
                     "FROM Productos p " +
                     "LEFT JOIN Categoria c ON p.id_categoria = c.id_categoria " +
                     "LEFT JOIN Provedores pr ON p.id_provedor = pr.id_provedor " +
                     "WHERE p.estado = true AND (p.codigo LIKE ? OR p.nombre LIKE ?) " +
                     "ORDER BY p.nombre";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            String parametro = "%" + criterio + "%";
            ps.setString(1, parametro);
            ps.setString(2, parametro);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                productos.add(construirProducto(rs));
            }
            
            rs.close();
            ps.close();
            
        } catch (SQLException e) {
            System.err.println("Error al buscar productos: " + e.getMessage());
        }
        
        return productos;
    }
    
    private Producto construirProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setIdProducto(rs.getInt("id_producto"));
        producto.setCodigo(rs.getString("codigo"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setPrecio(rs.getDouble("precio"));
        producto.setCantidadDisponible(rs.getInt("cantidad_disponible"));
        producto.setStockMinimo(rs.getInt("stock_mínimo"));
        producto.setIdCategoria(rs.getInt("id_categoria"));
        producto.setNombreCategoria(rs.getString("nombre_categoria"));
        producto.setIdProveedor(rs.getInt("id_provedor"));
        producto.setNombreProveedor(rs.getString("nombre_proveedor"));
        producto.setEstado(rs.getBoolean("estado"));
        producto.setFechaRegistro(rs.getString("fecha_registro"));
        return producto;
    }
}
