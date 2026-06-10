/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tiendaelectronica;
import Vista.LoginForm;
/**
 *
 * @author itsth
 */
public class TiendaElectronica {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            LoginForm login = new LoginForm(null, true);
            login.setVisible(true);
        });
    }

}

