/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;


/**
 *
 * @author Joan
 */
public class Validaciones {
  public static boolean campoNoVacio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }
    
    public static boolean longitudMinima(String texto, int longitudMinima) {
        return campoNoVacio(texto) && texto.trim().length() >= longitudMinima;
    }
    
    public static boolean longitudMaxima(String texto, int longitudMaxima) {
        return texto == null || texto.trim().length() <= longitudMaxima;
    }
    
    public static boolean emailValido(String email) {
        if (!campoNoVacio(email)) {
            return false;
        }
        return email.contains("@") && email.contains(".");
    }
    
    public static boolean telefonoValido(String telefono) {
        if (!campoNoVacio(telefono)) {
            return false;
        }
        return telefono.matches("[0-9]{4}-[0-9]{4}");
    }
    
    public static boolean cedulaValida(String cedula) {
        if (!campoNoVacio(cedula)) {
            return false;
        }
        return cedula.matches("[0-9]-[0-9]{4}-[0-9]{4}");
    }
    
    public static boolean soloNumeros(String texto) {
        if (!campoNoVacio(texto)) {
            return false;
        }
        for (int i = 0; i < texto.length(); i++) {
            if (!Character.isDigit(texto.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean soloLetras(String texto) {
        if (!campoNoVacio(texto)) {
            return false;
        }
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (!Character.isLetter(c) && c != ' ') {
                return false;
            }
        }
        return true;
    }
    
    public static boolean numeroPositivo(double numero) {
        return numero > 0;
    }
    
    public static boolean numeroPositivoOCero(double numero) {
        return numero >= 0;
    }
    
    public static boolean numeroEnRango(double numero, double min, double max) {
        return numero >= min && numero <= max;
    }
    
    public static boolean esEntero(String texto) {
        if (!campoNoVacio(texto)) {
            return false;
        }
        try {
            Integer.parseInt(texto.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean esDecimal(String texto) {
        if (!campoNoVacio(texto)) {
            return false;
        }
        try {
            Double.parseDouble(texto.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean codigoProductoValido(String codigo) {
        if (!campoNoVacio(codigo)) {
            return false;
        }
        return codigo.matches("[A-Z]{3}-[0-9]{3,}");
    }
    
    public static String sanitizarTexto(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace("'", "").replace("\"", "").replace(";", "").trim();
    }
}
