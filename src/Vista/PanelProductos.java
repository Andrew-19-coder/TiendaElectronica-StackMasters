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
        inicializar();
    }

    private void inicializar() {
    productoDAO = new ProductoDAO();
    configurarTabla();
    cargarProductos();
    agregarListeners();
}

private void configurarTabla() {
    modeloTabla = (DefaultTableModel) tablaProductos.getModel();
    modeloTabla.setRowCount(0);
    
    tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(50);
    tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(100);
    tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(200);
    tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(100);
    tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(80);
    tablaProductos.getColumnModel().getColumn(5).setPreferredWidth(80);
    tablaProductos.getColumnModel().getColumn(6).setPreferredWidth(150);
    tablaProductos.getColumnModel().getColumn(7).setPreferredWidth(150);
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

        panelSuperior = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnRefrescar = new javax.swing.JButton();
        btnStockBajo = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Gestion de Productos");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Buscar:");

        txtBuscar.setColumns(20);

        btnBuscar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnBuscar.setText("Buscar");

        btnAgregar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnAgregar.setText("Agregar");

        btnEditar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEditar.setText("Editar");

        btnEliminar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEliminar.setText("Eliminar");

        btnRefrescar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnRefrescar.setText("Refrescar");

        btnStockBajo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnStockBajo.setText("Stock Bajo");

        javax.swing.GroupLayout panelSuperiorLayout = new javax.swing.GroupLayout(panelSuperior);
        panelSuperior.setLayout(panelSuperiorLayout);
        panelSuperiorLayout.setHorizontalGroup(
            panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSuperiorLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(btnBuscar)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAgregar)
                .addGap(18, 18, 18)
                .addComponent(btnEditar)
                .addGap(18, 18, 18)
                .addComponent(btnEliminar)
                .addGap(18, 18, 18)
                .addComponent(btnRefrescar)
                .addGap(18, 18, 18)
                .addComponent(btnStockBajo)
                .addContainerGap(58, Short.MAX_VALUE))
        );
        panelSuperiorLayout.setVerticalGroup(
            panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSuperiorLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar))
                .addGap(37, 37, 37)
                .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregar)
                    .addComponent(btnEditar)
                    .addComponent(btnEliminar)
                    .addComponent(btnRefrescar)
                    .addComponent(btnStockBajo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(57, Short.MAX_VALUE))
        );

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
        jScrollPane1.setViewportView(tablaProductos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jScrollPane1)
                    .addComponent(panelSuperior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnRefrescar;
    private javax.swing.JButton btnStockBajo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel panelSuperior;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
