/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Vista;
import Modelo.Producto;
import Modelo.ProductoDAO;
import Utilidades.Validaciones;
import java.sql.*;
import Utilidades.ConexionBD;
import javax.swing.JOptionPane;
/**
 *
 * @author Joan
 */
public class DialogoProducto extends javax.swing.JDialog {
    private Producto productoEditar;
    private boolean esEdicion;
    private boolean guardado;
    /**
     * Creates new form DialogoProducto
     */
    public DialogoProducto(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        inicializar();
        this.esEdicion = false;
        this.guardado = false;
    }

    public DialogoProducto(java.awt.Frame parent, boolean modal, Producto producto) {
        super(parent, modal);
        initComponents();
        inicializar();
        this.productoEditar = producto;
        this.esEdicion = true;
        this.guardado = false;
        cargarDatos();
    }
    
    
    private void inicializar() {
    setLocationRelativeTo(null);
    cargarCategorias();
    cargarProveedores();
    agregarListeners();
}

private void cargarCategorias() {
    cboCategoria.removeAllItems();
    Connection conn = ConexionBD.getInstancia().getConexion();
    
    try {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id_categoria, nombre_categoria FROM Categoria ORDER BY nombre_categoria");
        
        while (rs.next()) {
            ((javax.swing.DefaultComboBoxModel) cboCategoria.getModel()).addElement(new ComboItem(rs.getInt("id_categoria"), rs.getString("nombre_categoria")));
        }
        
        rs.close();
        stmt.close();
        
    } catch (SQLException e) {
        System.err.println("Error al cargar categorías: " + e.getMessage());
    }
}

private void cargarProveedores() {
    cboProveedor.removeAllItems();
    Connection conn = ConexionBD.getInstancia().getConexion();
    
    try {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id_provedor, nombre FROM Provedores WHERE estado = true ORDER BY nombre");
        
        while (rs.next()) {
            ((javax.swing.DefaultComboBoxModel) cboProveedor.getModel()).addElement(new ComboItem(rs.getInt("id_provedor"), rs.getString("nombre")));
        }
        
        rs.close();
        stmt.close();
        
    } catch (SQLException e) {
        System.err.println("Error al cargar proveedores: " + e.getMessage());
    }
}

private void cargarDatos() {
    txtCodigo.setText(productoEditar.getCodigo());
    txtCodigo.setEnabled(false);
    txtNombre.setText(productoEditar.getNombre());
    txtDescripcion.setText(productoEditar.getDescripcion());
    txtPrecio.setText(String.valueOf(productoEditar.getPrecio()));
    spnCantidad.setValue(productoEditar.getCantidadDisponible());
    spnStockMinimo.setValue(productoEditar.getStockMinimo());
    
    for (int i = 0; i < cboCategoria.getItemCount(); i++) {
        Object obj = cboCategoria.getItemAt(i);
        if (obj instanceof ComboItem) {
            ComboItem item = (ComboItem) obj;
            if (item.getId() == productoEditar.getIdCategoria()) {
                cboCategoria.setSelectedIndex(i);
                break;
            }
        }
    }
    
    for (int i = 0; i < cboProveedor.getItemCount(); i++) {
        Object obj = cboProveedor.getItemAt(i);
        if (obj instanceof ComboItem) {
            ComboItem item = (ComboItem) obj;
            if (item.getId() == productoEditar.getIdProveedor()) {
                cboProveedor.setSelectedIndex(i);
                break;
            }
        }
    }
    
    chkEstado.setSelected(productoEditar.isEstado());
}

private void agregarListeners() {
    btnGuardar.addActionListener(e -> guardar());
    btnCancelar.addActionListener(e -> cancelar());
}

private void guardar() {
    if (!validarCampos()) {
        return;
    }
    
    guardado = true;
    dispose();
}

private boolean validarCampos() {
    if (!Validaciones.campoNoVacio(txtCodigo.getText())) {
        JOptionPane.showMessageDialog(this, "Debe ingresar un código", "Validación", JOptionPane.WARNING_MESSAGE);
        txtCodigo.requestFocus();
        return false;
    }
    
    if (!esEdicion && !Validaciones.codigoProductoValido(txtCodigo.getText())) {
        JOptionPane.showMessageDialog(this, "El código debe tener el formato XXX-### (ej: LAP-001)", "Validación", JOptionPane.WARNING_MESSAGE);
        txtCodigo.requestFocus();
        return false;
    }
    
    if (!Validaciones.campoNoVacio(txtNombre.getText())) {
        JOptionPane.showMessageDialog(this, "Debe ingresar un nombre", "Validación", JOptionPane.WARNING_MESSAGE);
        txtNombre.requestFocus();
        return false;
    }
    
    if (!Validaciones.campoNoVacio(txtPrecio.getText())) {
        JOptionPane.showMessageDialog(this, "Debe ingresar un precio", "Validación", JOptionPane.WARNING_MESSAGE);
        txtPrecio.requestFocus();
        return false;
    }
    
    if (!Validaciones.esDecimal(txtPrecio.getText())) {
        JOptionPane.showMessageDialog(this, "El precio debe ser un número válido", "Validación", JOptionPane.WARNING_MESSAGE);
        txtPrecio.requestFocus();
        return false;
    }
    
    if (!Validaciones.numeroPositivo(Double.parseDouble(txtPrecio.getText()))) {
        JOptionPane.showMessageDialog(this, "El precio debe ser mayor a cero", "Validación", JOptionPane.WARNING_MESSAGE);
        txtPrecio.requestFocus();
        return false;
    }
    
    if (cboCategoria.getSelectedIndex() == -1) {
        JOptionPane.showMessageDialog(this, "Debe seleccionar una categoría", "Validación", JOptionPane.WARNING_MESSAGE);
        return false;
    }
    
    if (cboProveedor.getSelectedIndex() == -1) {
        JOptionPane.showMessageDialog(this, "Debe seleccionar un proveedor", "Validación", JOptionPane.WARNING_MESSAGE);
        return false;
    }
    
    return true;
}

private void cancelar() {
    guardado = false;
    dispose();
}

public Producto obtenerProducto() {
    Producto producto = esEdicion ? productoEditar : new Producto();
    
    producto.setCodigo(txtCodigo.getText().trim());
    producto.setNombre(txtNombre.getText().trim());
    producto.setDescripcion(txtDescripcion.getText().trim());
    producto.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
    producto.setCantidadDisponible((Integer) spnCantidad.getValue());
    producto.setStockMinimo((Integer) spnStockMinimo.getValue());
    
    ComboItem categoriaSeleccionada = (ComboItem) cboCategoria.getSelectedItem();
    producto.setIdCategoria(categoriaSeleccionada.getId());
    
    ComboItem proveedorSeleccionado = (ComboItem) cboProveedor.getSelectedItem();
    producto.setIdProveedor(proveedorSeleccionado.getId());
    
    producto.setEstado(chkEstado.isSelected());
    
    return producto;
}

public boolean isGuardado() {
    return guardado;
}

private class ComboItem {
    private int id;
    private String nombre;
    
    public ComboItem(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    
    public int getId() {
        return id;
    }
    
    @Override
    public String toString() {
        return nombre;
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

        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        spnStockMinimo = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        spnCantidad = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        cboCategoria = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        cboProveedor = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        chkEstado = new javax.swing.JCheckBox();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Productos");
        setModal(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Añadir Producto");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/producto.png"))); // NOI18N
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 77, 75));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 102, 0));
        jLabel1.setText("Codigo:");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 6, 69, 26));

        txtCodigo.setColumns(15);
        txtCodigo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(txtCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 38, 256, 30));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 0, 204));
        jLabel2.setText("Nombre:");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 84, 60, 25));

        txtNombre.setColumns(30);
        txtNombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 115, 256, 30));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 204, 204));
        jLabel3.setText("Descripcion:");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 155, 87, 29));

        txtDescripcion.setColumns(30);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setRows(3);
        txtDescripcion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane1.setViewportView(txtDescripcion);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 196, 256, 59));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 0, 204));
        jLabel4.setText("Precio:");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 267, 97, 25));

        txtPrecio.setColumns(15);
        txtPrecio.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(txtPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 298, 256, 30));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 0, 102));
        jLabel6.setText("Stock Disponible:");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 340, 125, 28));

        spnStockMinimo.setModel(new javax.swing.SpinnerNumberModel(5, 1, 100, 1));
        spnStockMinimo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(spnStockMinimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 380, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 102, 102));
        jLabel5.setText("Cantidad Disponible:");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 340, 148, 28));

        spnCantidad.setModel(new javax.swing.SpinnerNumberModel(0, 0, 10000, 1));
        spnCantidad.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(spnCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 380, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 204, 153));
        jLabel7.setText("Categoria:");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 420, 110, 24));

        cboCategoria.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(cboCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 450, -1, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 204, 204));
        jLabel8.setText("Proveedor:");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 420, 118, -1));

        cboProveedor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(cboProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 450, -1, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 51, 51));
        jLabel11.setText("Estado");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 490, 72, -1));

        chkEstado.setSelected(true);
        chkEstado.setText("Activo");
        chkEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkEstadoActionPerformed(evt);
            }
        });
        jPanel2.add(chkEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 510, 78, -1));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/codigo.png"))); // NOI18N
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 12, 55, -1));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/Nombre.png"))); // NOI18N
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 89, 43, 50));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/descripcion.png"))); // NOI18N
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, 80));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/precio.png"))); // NOI18N
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, -1, 50));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/cantidaddisponible.png"))); // NOI18N
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, -1, 50));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/estock.png"))); // NOI18N
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 350, -1, 50));

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/categoria2.png"))); // NOI18N
        jPanel2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 420, -1, 60));

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/provedor2.png"))); // NOI18N
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 420, -1, 50));

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/estado2.png"))); // NOI18N
        jPanel2.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 490, -1, 40));

        jPanel3.setBackground(new java.awt.Color(51, 153, 255));

        btnGuardar.setBackground(new java.awt.Color(0, 153, 0));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("Guardar");

        btnCancelar.setBackground(new java.awt.Color(204, 0, 0));
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("Cancelar");

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/Guardar.png"))); // NOI18N

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/cancelar.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(btnGuardar)
                        .addGap(33, 33, 33))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(btnCancelar)
                        .addGap(32, 32, 32))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 538, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chkEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkEstadoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DialogoProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogoProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogoProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogoProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogoProducto dialog = new DialogoProducto(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JComboBox<String> cboCategoria;
    private javax.swing.JComboBox<String> cboProveedor;
    private javax.swing.JCheckBox chkEstado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner spnCantidad;
    private javax.swing.JSpinner spnStockMinimo;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecio;
    // End of variables declaration//GEN-END:variables
}
