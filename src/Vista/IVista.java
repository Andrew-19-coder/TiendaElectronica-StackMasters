/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Vista;

/**
 *
 * @author Braya
 */
public interface IVista {
    public void mostrarMensaje(String msg, String titulo);
    public void mostrarError(String msg);
    public boolean confirmar(String msg, String titulo);
    public String solicitar(String msg, String titulo);
}
