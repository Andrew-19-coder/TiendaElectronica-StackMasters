/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Vista;

import Controlador.ControladorVentas;
import Enums.EstadoVenta;
import Modelo.DetalleVentas;
import Modelo.Ventas;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import Utilidades.ConexionBD;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Braya
 */
public class DialogoVentas extends javax.swing.JDialog implements IVista {

    private ControladorVentas controlador;
    private List<DetalleVentas> detalles;

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DialogoVentas.class.getName());

    /**
     * Creates new form DialogoVentas
     */
    public DialogoVentas(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.controlador = new ControladorVentas(this);
        this.detalles = new ArrayList<>();

        cargarClientes();
        cargarUsuarios();
        cargarProductos();
        cmbEstado.removeAllItems();
        for (Enums.EstadoVenta t : Enums.EstadoVenta.values()) {
            cmbEstado.addItem(t.name());
        }

        txtFecha.setText(LocalDate.now().toString());
    }

    public void cargarClientes() {
        cmbCliente.removeAllItems();
        Connection conn = ConexionBD.getInstancia().getConexion();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id_cliente, nombre_completo FROM Clientes ORDER BY nombre_completo");

            while (rs.next()) {
                ((javax.swing.DefaultComboBoxModel) cmbCliente.getModel()).addElement(new ComboItem(rs.getInt("id_cliente"), rs.getString("nombre_completo")));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            mostrarError("Error al cargar clientes: " + e.getMessage());
        }
    }

    private void cargarUsuarios() {
        cmbUsuario.removeAllItems();
        Connection conn = ConexionBD.getInstancia().getConexion();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id_usuario, nombre_usuario FROM Usuarios ORDER BY nombre_usuario");

            while (rs.next()) {
                ((javax.swing.DefaultComboBoxModel) cmbUsuario.getModel()).addElement(new ComboItem(rs.getInt("id_usuario"), rs.getString("nombre_usuario")));
            }

            rs.close();
            stmt.close();

        } catch (SQLException ex) {
            mostrarError("Error al cargar usuarios: " + ex.getMessage());
        }
    }

    private void cargarProductos() {
        cmbProducto.removeAllItems();
        Connection conn = ConexionBD.getInstancia().getConexion();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id_producto, nombre, precio, cantidad_disponible FROM Productos WHERE cantidad_disponible > 0 ORDER BY nombre");

            while (rs.next()) {
                int id = rs.getInt("id_producto");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio"); // ← ESTO FALTABA
                int stock = rs.getInt("cantidad_disponible"); // ← ESTO FALTABA

                String display = nombre + " - $" + String.format("%.2f", precio) + " (Stock: " + stock + ")";
                ((javax.swing.DefaultComboBoxModel) cmbProducto.getModel()).addElement(
                        new ProductoComboItem(id, display, precio, stock)
                );
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            mostrarError("Error al cargar productos: " + e.getMessage());
        }
    }
    
    private void actualizarTablaProductos() {
        DefaultTableModel modelo = (DefaultTableModel) TablaProductos.getModel();
        modelo.setRowCount(0);

        Connection conn = ConexionBD.getInstancia().getConexion();

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT nombre FROM Productos WHERE id_producto = ?");

            for (DetalleVentas detalle : detalles) {
                ps.setInt(1, detalle.getIdProducto());
                ResultSet rs = ps.executeQuery();

                String nombreProducto = "";
                if (rs.next()) {
                    nombreProducto = rs.getString("nombre");
                }

                double subtotal = detalle.getCantidad() * detalle.getPrecioUnitario();

                modelo.addRow(new Object[]{
                    nombreProducto,
                    detalle.getCantidad(),
                    String.format("$%.2f", detalle.getPrecioUnitario()),
                    String.format("$%.2f", subtotal)
                });

                rs.close();
            }

            ps.close();

        } catch (SQLException e) {
            mostrarError("Error al actualizar tabla: " + e.getMessage());
        }
    }

    private void calcularTotales() {
        double subtotal = 0;
        for (DetalleVentas d : detalles) {
            subtotal += d.getCantidad() * d.getPrecioUnitario();
        }
        
        double iva = subtotal * 0.13;
        double total = subtotal + iva;
        
        lblsubtotal.setText("$" + String.format("%.2f", subtotal));
        lbliva.setText("$" + String.format("%.2f", iva));
        lbltotal.setText("$" + String.format("%.2f", total));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelTitulo = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        panelContenido = new javax.swing.JPanel();
        panelInfo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbCliente = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cmbUsuario = new javax.swing.JComboBox<>();
        panelAgregar = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cmbProducto = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        spnCantidad = new javax.swing.JSpinner();
        btnAgregarProducto = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        panelTabla = new javax.swing.JPanel();
        scrollTabla = new javax.swing.JScrollPane();
        TablaProductos = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btnQuitarProducto = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        lblSubtotal = new javax.swing.JLabel();
        lblsubtotal = new javax.swing.JLabel();
        lblIVA = new javax.swing.JLabel();
        lbliva = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lbltotal = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        cmbEstado = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        txtObservaciones = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Venta Producto");
        setModal(true);
        setPreferredSize(new java.awt.Dimension(525, 750));
        setResizable(false);

        panelTitulo.setBackground(new java.awt.Color(102, 204, 255));
        panelTitulo.setPreferredSize(new java.awt.Dimension(700, 50));

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(0, 0, 102));
        lblTitulo.setText("Nueva Venta");
        panelTitulo.add(lblTitulo);

        getContentPane().add(panelTitulo, java.awt.BorderLayout.PAGE_START);

        panelContenido.setLayout(new javax.swing.BoxLayout(panelContenido, javax.swing.BoxLayout.LINE_AXIS));

        panelInfo.setBackground(new java.awt.Color(255, 255, 255));
        panelInfo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelInfo.setPreferredSize(new java.awt.Dimension(680, 80));
        panelInfo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 2, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 204, 0));
        jLabel1.setText("Cliente");
        panelInfo.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        cmbCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelInfo.add(cmbCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 110, 30));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 2, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 204, 204));
        jLabel2.setText("Fecha");
        panelInfo.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, -1, -1));

        txtFecha.setEditable(false);
        txtFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaActionPerformed(evt);
            }
        });
        panelInfo.add(txtFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, 110, 30));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 2, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 102, 0));
        jLabel3.setText("Usuario");
        panelInfo.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, -1, -1));

        cmbUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelInfo.add(cmbUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, 110, 30));

        panelAgregar.setBackground(new java.awt.Color(255, 255, 255));
        panelAgregar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelAgregar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 2, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 51, 204));
        jLabel4.setText("Producto");
        panelAgregar.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        cmbProducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelAgregar.add(cmbProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 110, 30));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 2, 20)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(204, 0, 0));
        jLabel5.setText("Cantidad");
        panelAgregar.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, -1, -1));

        spnCantidad.setModel(new javax.swing.SpinnerNumberModel(1, 1, 999, 1));
        panelAgregar.add(spnCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 20, 90, 30));

        btnAgregarProducto.setBackground(new java.awt.Color(0, 204, 0));
        btnAgregarProducto.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnAgregarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarProducto.setText("Agregar");
        btnAgregarProducto.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                btnAgregarProductoComponentHidden(evt);
            }
        });
        btnAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductoActionPerformed(evt);
            }
        });
        panelAgregar.add(btnAgregarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 60, -1, -1));

        jSeparator2.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator2.setForeground(new java.awt.Color(102, 102, 102));
        panelAgregar.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 520, 20));

        panelTabla.setLayout(new java.awt.BorderLayout());
        panelAgregar.add(panelTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 510, 230));

        panelInfo.add(panelAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 510, 110));

        TablaProductos.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        TablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Producto", "Cantidad", "Precio Unit.", "Subtotal"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaProductos.setGridColor(new java.awt.Color(224, 224, 224));
        TablaProductos.setSelectionBackground(new java.awt.Color(0, 187, 222));
        TablaProductos.setSelectionForeground(new java.awt.Color(255, 255, 255));
        TablaProductos.setShowGrid(true);
        scrollTabla.setViewportView(TablaProductos);

        panelInfo.add(scrollTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 220, 440, 170));

        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(160, 40));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        btnQuitarProducto.setBackground(new java.awt.Color(255, 0, 0));
        btnQuitarProducto.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnQuitarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnQuitarProducto.setText("Quitar Producto");
        btnQuitarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarProductoActionPerformed(evt);
            }
        });
        jPanel2.add(btnQuitarProducto);

        panelInfo.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 390, 170, 40));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblSubtotal.setFont(new java.awt.Font("Segoe UI", 2, 20)); // NOI18N
        lblSubtotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSubtotal.setText("Subtotal:");
        jPanel3.add(lblSubtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, -1, -1));

        lblsubtotal.setFont(new java.awt.Font("Segoe UI", 2, 20)); // NOI18N
        lblsubtotal.setText("$0.00");
        jPanel3.add(lblsubtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 10, 140, -1));

        lblIVA.setFont(new java.awt.Font("Segoe UI", 2, 20)); // NOI18N
        lblIVA.setText("IVA (13%):");
        jPanel3.add(lblIVA, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 40, -1, -1));

        lbliva.setFont(new java.awt.Font("Segoe UI", 2, 20)); // NOI18N
        lbliva.setText("$0.00");
        jPanel3.add(lbliva, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 40, 140, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 2, 20)); // NOI18N
        jLabel6.setText("TOTAL:");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 70, -1, -1));

        lbltotal.setFont(new java.awt.Font("Segoe UI", 2, 20)); // NOI18N
        lbltotal.setForeground(new java.awt.Color(0, 102, 0));
        lbltotal.setText("$0.00");
        jPanel3.add(lbltotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 70, 140, -1));

        jSeparator4.setForeground(new java.awt.Color(102, 102, 102));
        jPanel3.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 140, 540, 10));

        panelInfo.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 510, 510, 100));

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnGuardar.setBackground(new java.awt.Color(0, 153, 51));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("Guardar Venta");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setBackground(new java.awt.Color(255, 0, 0));
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("Cancelar Venta");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 103, Short.MAX_VALUE)
                .addComponent(btnCancelar)
                .addGap(58, 58, 58))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelInfo.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 610, 510, 80));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 2, 20)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(153, 0, 153));
        jLabel8.setText("Estado");
        panelInfo.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 60, -1, -1));

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelInfo.add(cmbEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 60, 110, 30));

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtObservaciones.setOpaque(true);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 2, 20)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(204, 0, 204));
        jLabel7.setText("Observaciones");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        panelInfo.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 430, 510, 80));

        jSeparator3.setForeground(new java.awt.Color(102, 102, 102));
        panelInfo.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 520, 510, 20));

        panelContenido.add(panelInfo);

        getContentPane().add(panelContenido, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaActionPerformed

    private void btnQuitarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarProductoActionPerformed
        int fila = TablaProductos.getSelectedRow();
        
        if (fila >= 0) {
            detalles.remove(fila);
            actualizarTablaProductos();
            calcularTotales();
        } else {
            mostrarError("Seleccione un producto para quitar");
        }

    }//GEN-LAST:event_btnQuitarProductoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        if (detalles.isEmpty()) {
            dispose();
            return;
        }

        boolean confirma = confirmar("¿Está seguro de cancelar? Se perderán los datos ingresados", "Confirmar");
        if (confirma) {
            dispose();
        }
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        if (detalles.isEmpty()) {
            mostrarError("Debe agregar al menos un producto");
            return;
        }

        ComboItem cliente = (ComboItem) cmbCliente.getSelectedItem();
        if (cliente == null) {
            mostrarError("Seleccione un cliente");
            return;
        }

        ComboItem usuario = (ComboItem) cmbUsuario.getSelectedItem();
        if (usuario == null) {
            mostrarError("Seleccione un usuario");
            return;
        }

        if (txtObservaciones.getText().trim().isEmpty()) {
            mostrarError("Debe escribir las observaciones");
            return;
        }

        Ventas venta = new Ventas();
        venta.setFechaVentas(txtFecha.getText());
        venta.setIdCliente(cliente.getId());
        venta.setIdUsuario(usuario.getId());
        venta.setObservaciones(txtObservaciones.getText());
        String estadoSeleccionado = (String) cmbEstado.getSelectedItem();
        EstadoVenta estado = EstadoVenta.valueOf(estadoSeleccionado);
        venta.setEstado(estado);

        boolean exito = controlador.guardar(venta, detalles);

        if (exito) {
            mostrarMensaje("Venta registrada correctamente. ID: " + venta.getIdVenta(), "Éxito");
            dispose();
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnAgregarProductoComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_btnAgregarProductoComponentHidden
        
    }//GEN-LAST:event_btnAgregarProductoComponentHidden

    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoActionPerformed
        ProductoComboItem producto = (ProductoComboItem) cmbProducto.getSelectedItem();
        
        if (producto == null) {
            mostrarError("Seleccione un producto");
            return;
        }
        
        int cantidad = (Integer) spnCantidad.getValue();
        
        if (cantidad <= 0) {
            mostrarError("La cantidad debe ser mayor a 0");
            return;
        }
        
        if (cantidad > producto.getStock()) {
            mostrarError("Stock insuficiente. Disponible: " + producto.getStock());
            return;
        }
        
        DetalleVentas detalle = new DetalleVentas();
        detalle.setIdProducto(producto.getId());
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(producto.getPrecio());
        
        detalles.add(detalle);
        actualizarTablaProductos();
        calcularTotales();
        
        spnCantidad.setValue(1);
    }//GEN-LAST:event_btnAgregarProductoActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                DialogoVentas dialog = new DialogoVentas(new javax.swing.JFrame(), true);
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
    private javax.swing.JTable TablaProductos;
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnQuitarProducto;
    private javax.swing.JComboBox<String> cmbCliente;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JComboBox<String> cmbProducto;
    private javax.swing.JComboBox<String> cmbUsuario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel lblIVA;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lbliva;
    private javax.swing.JLabel lblsubtotal;
    private javax.swing.JLabel lbltotal;
    private javax.swing.JPanel panelAgregar;
    private javax.swing.JPanel panelContenido;
    private javax.swing.JPanel panelInfo;
    private javax.swing.JPanel panelTabla;
    private javax.swing.JPanel panelTitulo;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JSpinner spnCantidad;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtObservaciones;
    // End of variables declaration//GEN-END:variables

    @Override
    public void mostrarMensaje(String msg, String titulo) {
        javax.swing.JOptionPane.showMessageDialog(this, msg, titulo, javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mostrarError(String msg) {
        javax.swing.JOptionPane.showMessageDialog(this, msg, "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public boolean confirmar(String msg, String titulo) {
        int result = javax.swing.JOptionPane.showConfirmDialog(this, msg, titulo, javax.swing.JOptionPane.YES_NO_OPTION);
        return result == javax.swing.JOptionPane.YES_OPTION;
    }

    @Override
    public String solicitar(String msg, String titulo) {
        return javax.swing.JOptionPane.showInputDialog(this, msg, titulo, javax.swing.JOptionPane.QUESTION_MESSAGE);
    }

    public class ComboItem {

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
            return nombre; // Esto es lo que muestra el ComboBox
        }
    }

    private class ProductoComboItem extends ComboItem {
        private double precio;
        private int stock;

        public ProductoComboItem(int id, String nombre, double precio, int stock) {
            super(id, nombre);
            this.precio = precio;
            this.stock = stock;
        }

        public double getPrecio() {
            return precio;
        }

        public int getStock() {
            return stock;
        }
    }
}
