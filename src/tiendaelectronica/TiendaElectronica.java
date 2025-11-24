/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tiendaelectronica;

import Utilidades.ConexionBD;
import java.sql.Connection;

/**
 *
 * @author itsth
 */
public class TiendaElectronica {

    public static void main(String[] args) {
        ConexionBD conexionBD = ConexionBD.getInstancia();
        Connection conn = conexionBD.getConexion();
        
        if (conn != null) {
            System.out.println("Conexion exitosa");
        } else {
            System.out.println("Error de conexion");
        }
        
        conexionBD.cerrarConexion();
    }

}

