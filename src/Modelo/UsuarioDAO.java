/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
import Enums.RolUsuario;
import Utilidades.ConexionBD;
import Utilidades.Encriptacion;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author Joan
 */
public class UsuarioDAO {

     private Connection conexion;
    
    public UsuarioDAO() {
        this.conexion = ConexionBD.getInstancia().getConexion();
    }
    
    public boolean insertar(Usuario usuario) {
        String sql = "INSERT INTO Usuarios (nombre_usuario, contraseña, nombre_completo, rol, estado, `fecha creación`) VALUES (?, ?, ?, ?, ?, NOW())";
    
    try {
        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, usuario.getNombreUsuario());
        ps.setString(2, Encriptacion.encriptarSHA256(usuario.getContrasena()));
        ps.setString(3, usuario.getNombreCompleto());
        ps.setString(4, usuario.getRol().name());
        ps.setBoolean(5, usuario.isEstado());
        
        int filasAfectadas = ps.executeUpdate();
        ps.close();
        return filasAfectadas > 0;
        
    } catch (SQLException e) {
        System.err.println("Error al insertar usuario: " + e.getMessage());
        return false;
        }
    }
    
    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE Usuarios SET nombre_usuario = ?, nombre_completo = ?, rol = ?, estado = ? WHERE id_usuario = ?";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getNombreCompleto());
            ps.setString(3, usuario.getRol().name());
            ps.setBoolean(4, usuario.isEstado());
            ps.setInt(5, usuario.getIdUsuario());
            
            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }
    
    public boolean cambiarContrasena(int idUsuario, String nuevaContrasena) {
        String sql = "UPDATE Usuarios SET contraseña = ? WHERE id_usuario = ?";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, Encriptacion.encriptarSHA256(nuevaContrasena));
            ps.setInt(2, idUsuario);
            
            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al cambiar contraseña: " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminar(int idUsuario) {
        String sql = "UPDATE Usuarios SET estado = false WHERE id_usuario = ?";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            
            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
    
    public Usuario obtenerPorId(int idUsuario) {
        String sql = "SELECT * FROM Usuarios WHERE id_usuario = ?";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Usuario usuario = construirUsuario(rs);
                rs.close();
                ps.close();
                return usuario;
            }
            
            rs.close();
            ps.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario: " + e.getMessage());
        }
        
        return null;
    }
    
    public ArrayList<Usuario> obtenerTodos() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios WHERE estado = true ORDER BY nombre_completo";
        
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                usuarios.add(construirUsuario(rs));
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios: " + e.getMessage());
        }
        
        return usuarios;
    }
    
    public Usuario autenticar(String nombreUsuario, String contrasena) {
        String sql = "SELECT * FROM Usuarios WHERE nombre_usuario = ? AND estado = true";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String contrasenaEncriptada = rs.getString("contraseña");
                
                if (Encriptacion.verificarPassword(contrasena, contrasenaEncriptada)) {
                    Usuario usuario = construirUsuario(rs);
                    actualizarUltimoAcceso(usuario.getIdUsuario());
                    rs.close();
                    ps.close();
                    return usuario;
                }
            }
            
            rs.close();
            ps.close();
            
        } catch (SQLException e) {
            System.err.println("Error al autenticar: " + e.getMessage());
        }
        
        return null;
    }
    
    private void actualizarUltimoAcceso(int idUsuario) {
        String sql = "UPDATE Usuarios SET ultimo_acceso = NOW() WHERE id_usuario = ?";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Error al actualizar ultimo acceso: " + e.getMessage());
        }
    }
    
    private Usuario construirUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setNombreUsuario(rs.getString("nombre_usuario"));
        usuario.setContrasena(rs.getString("contraseña"));
        usuario.setNombreCompleto(rs.getString("nombre_completo"));
       usuario.setRol(RolUsuario.valueOf(rs.getString("rol").toUpperCase()));
        usuario.setEstado(rs.getBoolean("estado"));
        
        String fechaCreacion = rs.getString("fecha creación");
        usuario.setFechaCreacion(fechaCreacion != null ? fechaCreacion : "");
        
        String ultimoAcceso = rs.getString("ultimo_acceso");
        usuario.setUltimoAcceso(ultimoAcceso != null ? ultimoAcceso : "");
        
        return usuario;
    }
}
