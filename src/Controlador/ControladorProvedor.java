/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Provedor;
import Modelo.ProvedorDAO;
import Vista.DialogoProvedor;
import Vista.PanelProvedores;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author itsth
 */
public class ControladorProvedor {
    private ProvedorDAO provedorDAO;
    private PanelProvedores vista;
    
    public ControladorProvedor(PanelProvedores vista) {
        this.vista = vista;
        this.provedorDAO = new ProvedorDAO();
    }
    
    public void cargarProvedores() {
        ArrayList<Provedor> provedores = provedorDAO.obtenerTodos();
        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaProvedores().getModel();
        modelo.setRowCount(0);
        
        for (Provedor p : provedores) {
            Object[] fila = {
                p.getId(),
                p.getNombre(),
                p.getContacto(),
                p.getTelefono(),
                p.getEmail(),
                p.getDireccion(),
                p.isEstado() ? "Activo" : "Inactivo"
            };
            modelo.addRow(fila);
        }
    }
    
    public void agregarProvedor() {
        DialogoProvedor dialogo = new DialogoProvedor(null, true);
        dialogo.setVisible(true);
        
        if (dialogo.isConfirmado()) {
            Provedor provedor = dialogo.getProvedor();
            if (validarDatos(provedor)) {
                if (provedorDAO.insertar(provedor)) {
                    JOptionPane.showMessageDialog(vista, "Proveedor registrado exitosamente");
                    cargarProvedores();
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al registrar el proveedor", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    public void actualizarProvedor() {
        int fila = vista.getTablaProvedores().getSelectedRow();
        
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar un proveedor");
            return;
        }
        
        int id = (int) vista.getTablaProvedores().getValueAt(fila, 0);
        Provedor provedorActual = obtenerProvedorPorId(id);
        
        if (provedorActual != null) {
            DialogoProvedor dialogo = new DialogoProvedor(null, true, provedorActual);
            dialogo.setVisible(true);
            
            if (dialogo.isConfirmado()) {
                Provedor provedorActualizado = dialogo.getProvedor();
                provedorActualizado.setId(id);
                
                if (validarDatos(provedorActualizado)) {
                    if (provedorDAO.actualizar(provedorActualizado)) {
                        JOptionPane.showMessageDialog(vista, "Proveedor actualizado exitosamente");
                        cargarProvedores();
                    } else {
                        JOptionPane.showMessageDialog(vista, "Error al actualizar", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
    
    public void eliminarProvedor() {
        int fila = vista.getTablaProvedores().getSelectedRow();
        
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar un proveedor");
            return;
        }
        
        int id = (int) vista.getTablaProvedores().getValueAt(fila, 0);
        String nombre = (String) vista.getTablaProvedores().getValueAt(fila, 1);
        
        int confirmacion = JOptionPane.showConfirmDialog(vista, 
            "¿Está seguro de eliminar a: " + nombre + "?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (provedorDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(vista, "Proveedor eliminado");
                cargarProvedores();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al eliminar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void filtrarDisponibles() {
        ArrayList<Provedor> provedores = provedorDAO.obtenerDisponibles();
        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaProvedores().getModel();
        modelo.setRowCount(0);
        
        for (Provedor p : provedores) {
            Object[] fila = {
                p.getId(),
                p.getNombre(),
                p.getContacto(),
                p.getTelefono(),
                p.getEmail(),
                p.getDireccion(),
                "Activo"
            };
            modelo.addRow(fila);
        }
    }
    
    private Provedor obtenerProvedorPorId(int id) {
        ArrayList<Provedor> todos = provedorDAO.obtenerTodos();
        for (Provedor p : todos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
    
    private boolean validarDatos(Provedor provedor) {
        if (provedor.getNombre().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El nombre es obligatorio");
            return false;
        }
        
        if (provedor.getTelefono().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El teléfono es obligatorio");
            return false;
        }
        
        if (!provedor.getEmail().isEmpty() && !validarEmail(provedor.getEmail())) {
            JOptionPane.showMessageDialog(vista, "El formato del email no es válido");
            return false;
        }
        
        return true;
    }
    
    private boolean validarEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    public void buscarProvedor(String criterio) {
        if (criterio.isEmpty()) {
            cargarProvedores();
            return;
        }
        
        ArrayList<Provedor> provedores = provedorDAO.buscar(criterio);
        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaProvedores().getModel();
        modelo.setRowCount(0);
        
        for (Provedor p : provedores) {
            Object[] fila = {
                p.getId(),
                p.getNombre(),
                p.getContacto(),
                p.getTelefono(),
                p.getEmail(),
                p.getDireccion(),
                p.isEstado() ? "Activo" : "Inactivo"
            };
            modelo.addRow(fila);
        }
        
        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(vista, "No se encontraron resultados");
        }
    }
}
