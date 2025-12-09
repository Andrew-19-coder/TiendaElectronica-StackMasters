/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Vista;

import Modelo.Producto;
import Modelo.ProductoDAO;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Joan
 */
public class PanelProductos extends javax.swing.JInternalFrame {
    private ProductoDAO productoDAO;
    private DefaultTableModel modeloTabla;

    /**
     * Creates new form PanelProductos
     */
    public PanelProductos() {
        initComponents();
        this.modeloTabla = (DefaultTableModel) tablaProductos.getModel();
        inicializar();
    }

    private void inicializar() {
        productoDAO = new ProductoDAO();
        cargarProductos();
        agregarListeners();
    }

    private void cargarProductos() {
        modeloTabla.setRowCount(0);
        ArrayList<Producto> productos = productoDAO.obtenerTodos();

        for (Producto producto : productos) {
            Object[] fila = {
                producto.getIdProducto(),
                producto.getCodigo(),
                producto.getNombre(),
                String.format("₡%.2f", producto.getPrecio()),
                producto.getCantidadDisponible(),
                producto.getStockMinimo(),
                producto.getNombreCategoria() != null ? producto.getNombreCategoria() : "Sin categoría",
                producto.getNombreProveedor() != null ? producto.getNombreProveedor() : "Sin proveedor"
            };
            modeloTabla.addRow(fila);
        }
    }

    private void agregarListeners() {
        btnAgregar.addActionListener(e -> agregarProducto());
        btnEditar.addActionListener(e -> editarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnRefrescar.addActionListener(e -> cargarProductos());
        btnBuscar.addActionListener(e -> buscarProducto());
        btnStockBajo.addActionListener(e -> mostrarStockBajo());
    }

    private void agregarProducto() {
        DialogoProducto dialogo = new DialogoProducto(null, true);
        dialogo.setVisible(true);

        if (dialogo.isGuardado()) {
            Producto nuevoProducto = dialogo.obtenerProducto();

            if (productoDAO.insertar(nuevoProducto)) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Producto agregado correctamente",
                        "Éxito",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                cargarProductos();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Error al agregar producto. Verifique que el código no exista.",
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarProducto() {
        int filaSeleccionada = tablaProductos.getSelectedRow();

        if (filaSeleccionada == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un producto de la tabla",
                    "Advertencia",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idProducto = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        Producto productoEditar = productoDAO.obtenerPorId(idProducto);

        if (productoEditar == null) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error al cargar los datos del producto",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        DialogoProducto dialogo = new DialogoProducto(null, true, productoEditar);
        dialogo.setVisible(true);

        if (dialogo.isGuardado()) {
            Producto productoActualizado = dialogo.obtenerProducto();

            if (productoDAO.actualizar(productoActualizado)) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Producto actualizado correctamente",
                        "Éxito",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                cargarProductos();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Error al actualizar producto",
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarProducto() {
        int filaSeleccionada = tablaProductos.getSelectedRow();

        if (filaSeleccionada == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un producto de la tabla",
                    "Advertencia",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idProducto = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombreProducto = (String) modeloTabla.getValueAt(filaSeleccionada, 2);

        int respuesta = javax.swing.JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el producto '" + nombreProducto + "'?",
                "Confirmar eliminación",
                javax.swing.JOptionPane.YES_NO_OPTION);

        if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
            if (productoDAO.eliminar(idProducto)) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Producto eliminado correctamente",
                        "Éxito",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                cargarProductos();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Error al eliminar producto",
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarProducto() {
        String textoBuscar = txtBuscar.getText().trim();

        if (textoBuscar.isEmpty()) {
            cargarProductos();
            return;
        }

        modeloTabla.setRowCount(0);
        ArrayList<Producto> productos = productoDAO.buscar(textoBuscar);

        for (Producto producto : productos) {
            Object[] fila = {
                producto.getIdProducto(),
                producto.getCodigo(),
                producto.getNombre(),
                String.format("₡%.2f", producto.getPrecio()),
                producto.getCantidadDisponible(),
                producto.getStockMinimo(),
                producto.getNombreCategoria() != null ? producto.getNombreCategoria() : "Sin categoría",
                producto.getNombreProveedor() != null ? producto.getNombreProveedor() : "Sin proveedor"
            };
            modeloTabla.addRow(fila);
        }

        if (productos.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "No se encontraron productos con ese criterio",
                    "Sin resultados",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void mostrarStockBajo() {
        modeloTabla.setRowCount(0);
        ArrayList<Producto> productos = productoDAO.obtenerBajoStock();

        for (Producto producto : productos) {
            Object[] fila = {
                producto.getIdProducto(),
                producto.getCodigo(),
                producto.getNombre(),
                String.format("₡%.2f", producto.getPrecio()),
                producto.getCantidadDisponible(),
                producto.getStockMinimo(),
                producto.getNombreCategoria() != null ? producto.getNombreCategoria() : "Sin categoría",
                producto.getNombreProveedor() != null ? producto.getNombreProveedor() : "Sin proveedor"
            };
            modeloTabla.addRow(fila);
        }

        if (productos.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "No hay productos con stock bajo",
                    "Stock OK",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            cargarProductos();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Se encontraron " + productos.size() + " producto(s) con stock bajo",
                    "Alerta de Stock",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panelSuperior = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnRefrescar = new javax.swing.JButton();
        btnStockBajo = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Gestion de Productos");

        panelSuperior.setBackground(new java.awt.Color(0, 204, 204));
        panelSuperior.setPreferredSize(new java.awt.Dimension(606, 140));
        panelSuperior.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 204));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Buscar:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 0);
        panelSuperior.add(jLabel1, gridBagConstraints);

        txtBuscar.setColumns(20);
        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipadx = 125;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 14, 0, 16);
        panelSuperior.add(txtBuscar, gridBagConstraints);

        btnBuscar.setBackground(new java.awt.Color(0, 102, 204));
        btnBuscar.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscar.setText("Buscar");
        panelSuperior.add(btnBuscar, new java.awt.GridBagConstraints());

        btnAgregar.setBackground(new java.awt.Color(0, 204, 0));
        btnAgregar.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnAgregar.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregar.setText("Agregar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(24, 70, 0, 0);
        panelSuperior.add(btnAgregar, gridBagConstraints);

        btnEditar.setBackground(new java.awt.Color(255, 102, 0));
        btnEditar.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnEditar.setForeground(new java.awt.Color(255, 255, 255));
        btnEditar.setText("Editar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(24, 0, 0, 0);
        panelSuperior.add(btnEditar, gridBagConstraints);

        btnEliminar.setBackground(new java.awt.Color(255, 0, 0));
        btnEliminar.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("Eliminar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(24, 10, 0, 0);
        panelSuperior.add(btnEliminar, gridBagConstraints);

        btnRefrescar.setBackground(new java.awt.Color(204, 153, 0));
        btnRefrescar.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnRefrescar.setForeground(new java.awt.Color(255, 255, 255));
        btnRefrescar.setText("Refrescar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(24, 2, 0, 0);
        panelSuperior.add(btnRefrescar, gridBagConstraints);

        btnStockBajo.setBackground(new java.awt.Color(153, 0, 153));
        btnStockBajo.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnStockBajo.setForeground(new java.awt.Color(255, 255, 255));
        btnStockBajo.setText("Stock Bajo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(24, 12, 0, 0);
        panelSuperior.add(btnStockBajo, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Gestion Productos");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        panelSuperior.add(jLabel2, gridBagConstraints);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/computadora.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.ipady = -20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 60, 0, 0);
        panelSuperior.add(jLabel3, gridBagConstraints);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/Guardar.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 10, 10, 0);
        panelSuperior.add(jLabel4, gridBagConstraints);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/Actualizar.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipadx = 12;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 35, 10, 0);
        panelSuperior.add(jLabel5, gridBagConstraints);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/cancelar.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 28, 10, 0);
        panelSuperior.add(jLabel6, gridBagConstraints);

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/stock.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 22, 0, 0);
        panelSuperior.add(jLabel8, gridBagConstraints);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/refrescar.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 14, 0, 0);
        panelSuperior.add(jLabel9, gridBagConstraints);

        jScrollPane1.setBackground(new java.awt.Color(0, 204, 255));

        tablaProductos.setBackground(new java.awt.Color(0, 204, 255));
        tablaProductos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Codigo", "Nombre", "Precio", "Stock", "Stock min", "Categoria", "Proveedor"
            }
        ));
        tablaProductos.setShowGrid(true);
        jScrollPane1.setViewportView(tablaProductos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator1)
                        .addContainerGap())
                    .addComponent(panelSuperior, javax.swing.GroupLayout.DEFAULT_SIZE, 850, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnRefrescar;
    private javax.swing.JButton btnStockBajo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel panelSuperior;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
