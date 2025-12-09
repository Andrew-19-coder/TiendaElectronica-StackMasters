/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Categoria;
import Modelo.CategoriaDAO;
import Vista.DialogoCategoria;
import Vista.panelCategoria;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Braya
 */
public class ControladorCategoria {
    private CategoriaDAO categoria;
    private panelCategoria vista;
    

    public ControladorCategoria(panelCategoria vista) {
        this.categoria = new CategoriaDAO();
        this.vista = vista;
    }
    
    public void cargarCategoria() {
        ArrayList<Categoria> categorias = categoria.obtenerTodos();
        DefaultTableModel modelo = (DefaultTableModel) vista.getTable().getModel();
        modelo.setRowCount(0);

        for (Categoria p : categorias) {
            Object[] fila = {
                p.getNombre(),
                p.getDescripcion()
            };
            modelo.addRow(fila);
        }
    }
    
    public void agregarCategoria() {
        DialogoCategoria dialogo = new DialogoCategoria(null, true);
        dialogo.setVisible(true);

        if (dialogo.isConfirmado()) {
            Categoria cate = dialogo.getCate();
            if (validarDatos(cate)) {
                if (categoria.insertar(cate)) {
                    JOptionPane.showMessageDialog(vista, "Categoria registrada exitosamente");
                    cargarCategoria();
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al registrar la categoria", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private boolean validarDatos(Categoria cate) {
        if (cate.getNombre().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El nombre es obligatorio");
            return false;
        }

        if (!cate.getDescripcion().isEmpty()) {
            JOptionPane.showMessageDialog(vista, " Faltan Datos requeridos");
            return false;
        }

        return true;
    }
    
    
    
    public void actualizarCategoria() {
        int fila = vista.getTable().getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar una categoria");
            return;
        }

        int id = (int) vista.getTable().getValueAt(fila, 0);
        Categoria categoriaActual = obtenerCategoriaPorId(id);

        if (categoriaActual != null) {
            DialogoCategoria dialogo = new DialogoCategoria(null, true);
            dialogo.setVisible(true);

            if (dialogo.isConfirmado()) {
                Categoria categoriaActualizado = dialogo.getCate();
                categoriaActualizado.setId(id);

                if (validarDatos(categoriaActualizado)) {
                    if (categoria.actualizar(categoriaActualizado)) {
                        JOptionPane.showMessageDialog(vista, "Categoria actualizada exitosamente");
                        cargarCategoria();
                    } else {
                        JOptionPane.showMessageDialog(vista, "Error al actualizar", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
    
    private Categoria obtenerCategoriaPorId(int id) {
        ArrayList<Categoria> todos = categoria.obtenerTodos();
        for (Categoria p : todos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
    
    public void eliminarCategoria() {
        int fila = vista.getTable().getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar una categoria de la tabla");
            return;
        }

        int id = (int) vista.getTable().getValueAt(fila, 0);
        String nombre = (String) vista.getTable().getValueAt(fila, 1);

        int confirmacion = JOptionPane.showConfirmDialog(vista,
                "¿Está seguro de eliminar a: " + nombre + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (categoria.eliminar(id)) {
                DefaultTableModel modelo = (DefaultTableModel) vista.getTable().getModel();
                modelo.removeRow(fila);

                JOptionPane.showMessageDialog(vista, "categoria desactivado exitosamente");
            } else {
                JOptionPane.showMessageDialog(vista, "Error al desactivar la categoria", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void mostrarTodosCategorias() {
        ArrayList<Categoria> categorias = categoria.obtenerTodos();
        DefaultTableModel modelo = (DefaultTableModel) vista.getTable().getModel();
        modelo.setRowCount(0);

        for (Categoria p : categorias) {
            Object[] fila = {
                p.getId(),
                p.getNombre(),
                p.getDescripcion()
            };
            modelo.addRow(fila);
        }
    }
    
    public void buscarCategoria(String criterio) {
        if (criterio.isEmpty()) {
            cargarCategoria();
            return;
        }

        ArrayList<Categoria> categorias = categoria.buscar(criterio);
        DefaultTableModel modelo = (DefaultTableModel) vista.getTable().getModel();
        modelo.setRowCount(0);

        for (Categoria p : categorias) {
            Object[] fila = {
                p.getId(),
                p.getNombre(),
                p.getDescripcion()
            };
            modelo.addRow(fila);
        }

        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(vista, "No se encontraron resultados");
        }
    }
 
}
