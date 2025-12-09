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
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaUsuarios = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        btnRefrescar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Gestion de Usuarios");
        setPreferredSize(new java.awt.Dimension(740, 400));

        jScrollPane1.setForeground(new java.awt.Color(51, 204, 255));
        jScrollPane1.setViewportBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tablaUsuarios.setBackground(new java.awt.Color(51, 204, 255));
        tablaUsuarios.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
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

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);
        getContentPane().add(jSeparator1, java.awt.BorderLayout.PAGE_END);

        jPanel2.setBackground(new java.awt.Color(0, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setPreferredSize(new java.awt.Dimension(1070, 135));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Gestion Usuarios");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 32, 0, 0);
        jPanel2.add(jLabel2, gridBagConstraints);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/floppy_disk_48.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 10, 0, 0);
        jPanel2.add(jLabel3, gridBagConstraints);

        btnBuscar.setBackground(new java.awt.Color(51, 51, 255));
        btnBuscar.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscar.setText("Buscar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 16;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = -2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel2.add(btnBuscar, gridBagConstraints);

        btnRefrescar.setBackground(new java.awt.Color(153, 0, 153));
        btnRefrescar.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        btnRefrescar.setForeground(new java.awt.Color(255, 255, 255));
        btnRefrescar.setText("Refrescar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(23, 12, 0, 0);
        jPanel2.add(btnRefrescar, gridBagConstraints);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/cancelar.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 20, 0, 0);
        jPanel2.add(jLabel5, gridBagConstraints);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/Actualizar.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipady = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 27, 0, 0);
        jPanel2.add(jLabel4, gridBagConstraints);

        txtBuscar.setColumns(20);
        txtBuscar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipadx = 145;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 48, 0, 40);
        jPanel2.add(txtBuscar, gridBagConstraints);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/refrescar.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 18, 0, 0);
        jPanel2.add(jLabel9, gridBagConstraints);

        btnEliminar.setBackground(new java.awt.Color(255, 51, 51));
        btnEliminar.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("Eliminar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(23, 12, 0, 0);
        jPanel2.add(btnEliminar, gridBagConstraints);

        btnAgregar.setBackground(new java.awt.Color(0, 204, 51));
        btnAgregar.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        btnAgregar.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregar.setText("Agregar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(23, 10, 0, 0);
        jPanel2.add(btnAgregar, gridBagConstraints);

        btnEditar.setBackground(new java.awt.Color(255, 153, 0));
        btnEditar.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        btnEditar.setForeground(new java.awt.Color(255, 255, 255));
        btnEditar.setText("Editar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(23, 12, 0, 0);
        jPanel2.add(btnEditar, gridBagConstraints);

        jLabel1.setBackground(new java.awt.Color(153, 0, 153));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 0, 153));
        jLabel1.setText("Buscar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 12;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 38, 0, 0);
        jPanel2.add(jLabel1, gridBagConstraints);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/VistaIconos/Usuario.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 50, 0, 0);
        jPanel2.add(jLabel6, gridBagConstraints);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnRefrescar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable tablaUsuarios;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
