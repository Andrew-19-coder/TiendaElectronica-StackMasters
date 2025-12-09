/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Vista;
import Modelo.Usuario;
import Modelo.UsuarioDAO;
import Enums.RolUsuario;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Joan
 */
public class PanelUsuarios extends javax.swing.JInternalFrame {
    private UsuarioDAO usuarioDAO;
    private DefaultTableModel modeloTabla;
    /**
     * Creates new form PanelUsuarios
     */
    public PanelUsuarios() {
        initComponents();
        inicializar();
    }
    private void inicializar() {
        usuarioDAO = new UsuarioDAO();
        configurarTabla();
        cargarUsuarios();
        agregarListeners();
    }

    private void configurarTabla() {
        modeloTabla = (DefaultTableModel) tablaUsuarios.getModel();
        modeloTabla.setRowCount(0);

        tablaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaUsuarios.getColumnModel().getColumn(1).setPreferredWidth(120);
        tablaUsuarios.getColumnModel().getColumn(2).setPreferredWidth(200);
        tablaUsuarios.getColumnModel().getColumn(3).setPreferredWidth(120);
        tablaUsuarios.getColumnModel().getColumn(4).setPreferredWidth(100);
    }

    private void cargarUsuarios() {
        modeloTabla.setRowCount(0);
        ArrayList<Usuario> usuarios = usuarioDAO.obtenerTodos();

        for (Usuario usuario : usuarios) {
            Object[] fila = {
                usuario.getIdUsuario(),
                usuario.getNombreUsuario(),
                usuario.getNombreCompleto(),
                usuario.getRol().getDescripcion(),
                usuario.isEstado() ? "Activo" : "Inactivo"
            };
            modeloTabla.addRow(fila);
        }
    }

    private void agregarListeners() {
        btnAgregar.addActionListener(e -> agregarUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        btnRefrescar.addActionListener(e -> cargarUsuarios());
        btnBuscar.addActionListener(e -> buscarUsuario());
    }

    private void agregarUsuario() {
        DialogoUsuario dialogo = new DialogoUsuario(null, true);
    dialogo.setVisible(true);
    
    if (dialogo.isGuardado()) {
        Usuario nuevoUsuario = dialogo.obtenerUsuario();
        
        if (usuarioDAO.insertar(nuevoUsuario)) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Usuario agregado correctamente",
                "Éxito",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            cargarUsuarios();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Error al agregar usuario. Verifique que el nombre de usuario no exista.",
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    }

    private void editarUsuario() {
         int filaSeleccionada = tablaUsuarios.getSelectedRow();
    
    if (filaSeleccionada == -1) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Debe seleccionar un usuario de la tabla",
            "Advertencia",
            javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    int idUsuario = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
    Usuario usuarioEditar = usuarioDAO.obtenerPorId(idUsuario);
    
    if (usuarioEditar == null) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Error al cargar los datos del usuario",
            "Error",
            javax.swing.JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    DialogoUsuario dialogo = new DialogoUsuario(null, true, usuarioEditar);
    dialogo.setVisible(true);
    
    if (dialogo.isGuardado()) {
        Usuario usuarioActualizado = dialogo.obtenerUsuario();
        
        if (usuarioDAO.actualizar(usuarioActualizado)) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Usuario actualizado correctamente",
                "Éxito",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            cargarUsuarios();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Error al actualizar usuario",
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    }

    private void eliminarUsuario() {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();

        if (filaSeleccionada == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un usuario de la tabla",
                    "Advertencia",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idUsuario = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombreUsuario = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
        String rol = (String) modeloTabla.getValueAt(filaSeleccionada, 3);

        if (rol.equals("Administrador")) {
            long cantidadAdmins = usuarioDAO.obtenerTodos().stream()
                    .filter(u -> u.getRol() == RolUsuario.ADMINISTRADOR && u.isEstado())
                    .count();

            if (cantidadAdmins <= 1) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "No se puede eliminar el último administrador del sistema",
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int respuesta = javax.swing.JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar al usuario '" + nombreUsuario + "'?",
                "Confirmar eliminación",
                javax.swing.JOptionPane.YES_NO_OPTION);

        if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
            if (usuarioDAO.eliminar(idUsuario)) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Usuario eliminado correctamente",
                        "Éxito",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                cargarUsuarios();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Error al eliminar usuario",
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarUsuario() {
        String textoBuscar = txtBuscar.getText().trim();

        if (textoBuscar.isEmpty()) {
            cargarUsuarios();
            return;
        }

        modeloTabla.setRowCount(0);
        ArrayList<Usuario> usuarios = usuarioDAO.obtenerTodos();

        for (Usuario usuario : usuarios) {
            if (usuario.getNombreUsuario().toLowerCase().contains(textoBuscar.toLowerCase())
                    || usuario.getNombreCompleto().toLowerCase().contains(textoBuscar.toLowerCase())) {

                Object[] fila = {
                    usuario.getIdUsuario(),
                    usuario.getNombreUsuario(),
                    usuario.getNombreCompleto(),
                    usuario.getRol().getDescripcion(),
                    usuario.isEstado() ? "Activo" : "Inactivo"
                };
                modeloTabla.addRow(fila);
            }
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
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnRefrescar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaUsuarios = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Gestion de Usuarios");

        jPanel1.setBackground(new java.awt.Color(0, 204, 204));

        txtBuscar.setColumns(20);
        txtBuscar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        btnBuscar.setBackground(new java.awt.Color(51, 51, 255));
        btnBuscar.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscar.setText("Buscar");

        btnAgregar.setBackground(new java.awt.Color(0, 204, 51));
        btnAgregar.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        btnAgregar.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregar.setText("Agregar");

        btnEditar.setBackground(new java.awt.Color(255, 153, 0));
        btnEditar.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        btnEditar.setForeground(new java.awt.Color(255, 255, 255));
        btnEditar.setText("Editar");

        btnEliminar.setBackground(new java.awt.Color(255, 51, 51));
        btnEliminar.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("Eliminar");

        btnRefrescar.setBackground(new java.awt.Color(153, 0, 153));
        btnRefrescar.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        btnRefrescar.setForeground(new java.awt.Color(255, 255, 255));
        btnRefrescar.setText("Refrescar");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/floppy_disk_48.png"))); // NOI18N

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/Actualizar.png"))); // NOI18N

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/cancelar.png"))); // NOI18N

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/refrescar.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Gestion Usuarios");

        jScrollPane1.setForeground(new java.awt.Color(51, 204, 255));

        tablaUsuarios.setBackground(new java.awt.Color(51, 204, 255));
        tablaUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Usuario", "Nombre Completo", "Rol", "Estado"
            }
        ));
        tablaUsuarios.setShowGrid(true);
        jScrollPane1.setViewportView(tablaUsuarios);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAgregar)
                .addGap(16, 16, 16)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnEditar)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEliminar)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(btnRefrescar))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(btnBuscar)
                        .addGap(6, 6, 6)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 735, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(26, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnRefrescar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9))
                                .addGap(2, 2, 2))
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        getContentPane().add(jSeparator1, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnRefrescar;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable tablaUsuarios;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
