/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author Joan
 */
public class ConexionBD {
   private static ConexionBD instancia;
    private Connection conexion;
  
    private static final String URL = "jdbc:mysql://localhost:3307/TiendaElectronica";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "Root123@";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    
    
      private ConexionBD() {
        try {
            Class.forName(DRIVER);
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver MySQL no encontrado - " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al conectar con la BD - " + e.getMessage());
        }
    }
   
    public static ConexionBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }
  
    public Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Error al reconectar - " + e.getMessage());
        }
        return conexion;
    }
  
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión - " + e.getMessage());
        }
    }
}
