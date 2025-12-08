/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Cliente;
import Modelo.ClienteDAO;
import Vista.DialogoCliente;
import Vista.PanelClientes;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author itsth
 */
public class ControladorCliente {
    private ClienteDAO clienteDAO;
    private PanelClientes vista;
    
     public ControladorCliente(PanelClientes vista) {
        this.vista = vista;
        this.clienteDAO = new ClienteDAO();
    }
    
    public void cargarClientes() {
        ArrayList<Cliente> clientes = clienteDAO.obtenerTodos();
        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaClientes().getModel();
        modelo.setRowCount(0);
        
        for (Cliente c : clientes) {
            Object[] fila = {
                c.getIdCliente(),
                c.getCedula(),
                c.getNombreCompleto(),
                c.getTelefono(),
                c.getEmail(),
                c.getDireccion(),
                c.isEstado() ? "Activo" : "Inactivo",
                c.getFechaRegistro()
            };
            modelo.addRow(fila);
        }
    }
    
    public void agregarCliente() {
        DialogoCliente dialogo = new DialogoCliente(null, true);
        dialogo.setVisible(true);
        
        if (dialogo.isConfirmado()) {
            Cliente cliente = dialogo.getCliente();
            if (validarDatos(cliente)) {
                if (clienteDAO.insertar(cliente)) {
                    JOptionPane.showMessageDialog(vista, "Cliente registrado exitosamente");
                    cargarClientes();
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al registrar el cliente", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    public void actualizarCliente() {
        int fila = vista.getTablaClientes().getSelectedRow();
        
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar un cliente");
            return;
        }
        
        int id = (int) vista.getTablaClientes().getValueAt(fila, 0);
        Cliente clienteActual = clienteDAO.obtenerPorId(id);
        
        if (clienteActual != null) {
            DialogoCliente dialogo = new DialogoCliente(null, true, clienteActual);
            dialogo.setVisible(true);
            
            if (dialogo.isConfirmado()) {
                Cliente clienteActualizado = dialogo.getCliente();
                clienteActualizado.setIdCliente(id);
                
                if (validarDatos(clienteActualizado)) {
                    if (clienteDAO.actualizar(clienteActualizado)) {
                        JOptionPane.showMessageDialog(vista, "Cliente actualizado exitosamente");
                        cargarClientes();
                    } else {
                        JOptionPane.showMessageDialog(vista, "Error al actualizar", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
    
    public void eliminarCliente() {
        int fila = vista.getTablaClientes().getSelectedRow();
        
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar un cliente de la tabla");
            return;
        }
        
        int id = (int) vista.getTablaClientes().getValueAt(fila, 0);
        String nombre = (String) vista.getTablaClientes().getValueAt(fila, 2);
        
        int confirmacion = JOptionPane.showConfirmDialog(vista, 
            "¿Está seguro de desactivar a: " + nombre + "?", 
            "Confirmar desactivación", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (clienteDAO.eliminar(id)) {
                DefaultTableModel modelo = (DefaultTableModel) vista.getTablaClientes().getModel();
                modelo.removeRow(fila);
                JOptionPane.showMessageDialog(vista, "Cliente desactivado exitosamente");
            } else {
                JOptionPane.showMessageDialog(vista, "Error al desactivar el cliente", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void buscarCliente(String criterio) {
        if (criterio.isEmpty()) {
            cargarClientes();
            return;
        }
        
        ArrayList<Cliente> clientes = clienteDAO.buscar(criterio);
        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaClientes().getModel();
        modelo.setRowCount(0);
        
        for (Cliente c : clientes) {
            Object[] fila = {
                c.getIdCliente(),
                c.getCedula(),
                c.getNombreCompleto(),
                c.getTelefono(),
                c.getEmail(),
                c.getDireccion(),
                c.getFechaRegistro(),
                c.getComprasRealizadas()
            };
            modelo.addRow(fila);
        }
        
        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(vista, "No se encontraron resultados");
        }
    }
    
    private boolean validarDatos(Cliente cliente) {
        if (cliente.getCedula().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "La cédula es obligatoria");
            return false;
        }
        
        if (cliente.getNombreCompleto().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El nombre completo es obligatorio");
            return false;
        }
        
        if (cliente.getTelefono().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El teléfono es obligatorio");
            return false;
        }
        
        if (!cliente.getEmail().isEmpty() && !validarEmail(cliente.getEmail())) {
            JOptionPane.showMessageDialog(vista, "El formato del email no es válido");
            return false;
        }
        
        return true;
    }
    
    private boolean validarEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}
