/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;

/**
 *
 * @author Joan
 */
public class Encriptacion {

    public static String encriptarSHA256(String texto) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(texto.getBytes());
            String resultado = "";

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    resultado += '0';
                }
                resultado += hex;
            }

            return resultado;
        } catch (Exception e) {
            System.err.println("Error al encriptar: " + e.getMessage());
            return null;
        }
    }

    public static boolean verificarPassword(String passwordIngresado, String passwordEncriptado) {
        String passwordIngresadoEncriptado = encriptarSHA256(passwordIngresado);
        return passwordIngresadoEncriptado != null && passwordIngresadoEncriptado.equals(passwordEncriptado);
    }
}
