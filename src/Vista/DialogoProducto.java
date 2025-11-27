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

        jLabel1 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        spnCantidad = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        spnStockMinimo = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        cboCategoria = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        cboProveedor = new javax.swing.JComboBox<>();
        chkEstado = new javax.swing.JCheckBox();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Productos");
        setModal(true);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Codigo:");

        txtCodigo.setColumns(15);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Nombre:");

        txtNombre.setColumns(30);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Descripcion:");

        txtDescripcion.setColumns(30);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setRows(3);
        jScrollPane1.setViewportView(txtDescripcion);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Precio:");

        txtPrecio.setColumns(15);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Cantidad Disponible:");

        spnCantidad.setModel(new javax.swing.SpinnerNumberModel(0, 0, 10000, 1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Stock Disponible:");

        spnStockMinimo.setModel(new javax.swing.SpinnerNumberModel(5, 1, 100, 1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Categoria:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Proveedor:");

        chkEstado.setSelected(true);
        chkEstado.setText("Activo");

        btnGuardar.setText("Guardar");

        btnCancelar.setText("Cancelar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(txtPrecio))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spnCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(56, 56, 56)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spnStockMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(chkEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7))
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spnCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnStockMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(chkEstado)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner spnCantidad;
    private javax.swing.JSpinner spnStockMinimo;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecio;
    // End of variables declaration//GEN-END:variables
}
