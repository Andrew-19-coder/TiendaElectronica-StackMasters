/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import Modelo.Usuario;
import Modelo.UsuarioDAO;
import Vista.LoginForm;
import Vista.MenuPrincipal;
import Utilidades.Validaciones;
/**
 *
 * @author Joan
 */
public class ControladorLogin {
      private LoginForm vista;
    private UsuarioDAO usuarioDAO;
    
    public ControladorLogin(LoginForm vista) {
        this.vista = vista;
        this.usuarioDAO = new UsuarioDAO();
    }
    
    public void iniciarSesion() {
        String usuario = vista.getUsuario();
        String password = vista.getPassword();
        
        if (!validarCampos(usuario, password)) {
            return;
        }
        
        Usuario usuarioAutenticado = usuarioDAO.autenticar(usuario, password);
        
        if (usuarioAutenticado != null) {
            vista.mostrarMensaje("Bienvenido " + usuarioAutenticado.getNombreCompleto());
            abrirMenuPrincipal(usuarioAutenticado);
            vista.dispose();
        } else {
            vista.mostrarError("Usuario o contraseña incorrectos");
            vista.limpiarCampos();
        }
    }
    
    private boolean validarCampos(String usuario, String password) {
        if (!Validaciones.campoNoVacio(usuario)) {
            vista.mostrarError("Debe ingresar un usuario");
            return false;
        }
        
        if (!Validaciones.campoNoVacio(password)) {
            vista.mostrarError("Debe ingresar una contraseña");
            return false;
        }
        
        return true;
    }
    
    private void abrirMenuPrincipal(Usuario usuario) {
        MenuPrincipal menu = new MenuPrincipal(usuario);
        menu.setVisible(true);
    }
}
