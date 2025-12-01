/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Enums.EstadoVenta;
import Utilidades.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author Braya
 */
public class VentasDAO {

    private Connection conexion;
    private DetalleVentasDAO detalleDAO;
    private ProductoDAO productoDAO;

    public VentasDAO() {
        this.conexion = ConexionBD.getInstancia().getConexion();
        this.detalleDAO = new DetalleVentasDAO();
        this.productoDAO = new ProductoDAO();
    }

    public boolean guardarVenta(Ventas venta, List<DetalleVentas> detalles) throws SQLException {

        if (detalles == null || detalles.isEmpty()) {
            System.err.println("Error: No hay productos en la venta");
            return false;
        }
        try {
            conexion.setAutoCommit(false);

            String sqlVenta = "INSERT INTO Ventas (fecha_venta, id_cliente, id_usuario, observaciones, estado) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psVenta = conexion.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
            psVenta.setString(1, venta.getFechaVentas());
            psVenta.setInt(2, venta.getIdCliente());
            psVenta.setInt(3, venta.getIdUsuario());
            psVenta.setString(4, venta.getObservaciones());
            psVenta.setString(5, venta.getEstado().name());

            int filasAfectadas = psVenta.executeUpdate();

            if (filasAfectadas == 0) {
                conexion.rollback();
                return false;
            }

            ResultSet rs = psVenta.getGeneratedKeys();
            int idVentaGenerado = 0;
            if (rs.next()) {
                idVentaGenerado = rs.getInt(1);
                venta.setIdVenta(idVentaGenerado);
            }
            rs.close();
            psVenta.close();

            for (DetalleVentas detalle : detalles) {
                detalle.setIdVenta(idVentaGenerado);

                if (!detalleDAO.insertar(detalle)) {
                    conexion.rollback();
                    return false;
                }

                if (!descontarStock(detalle.getIdProducto(), detalle.getCantidad())) {
                    conexion.rollback();
                    return false;
                }
            }

            conexion.commit();
            conexion.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            conexion.rollback();
            conexion.setAutoCommit(true);
            System.err.println("Error al guardar venta: " + e.getMessage());
            return false;
        }
    }

    private boolean descontarStock(int idProducto, int cantidad) {
        String sql = "UPDATE Productos SET cantidad_disponible = cantidad_disponible - ? WHERE id_producto = ?";

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, cantidad);
            ps.setInt(2, idProducto);

            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al descontar stock: " + e.getMessage());
            return false;
        }
    }

    public boolean cancelarVenta(int idVenta) throws SQLException {
        try {

            List<DetalleVentas> detalles = detalleDAO.obtenerPorVenta(idVenta);

            for (DetalleVentas detalle : detalles) {
                restaurarStock(detalle.getIdProducto(), detalle.getCantidad());
            }

            PreparedStatement ps = conexion.prepareStatement("UPDATE Ventas SET estado = ? WHERE id_venta = ?");
            ps.setString(1, EstadoVenta.CANCELADA.name());
            ps.setInt(2, idVenta);

            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al cancelar venta: " + e.getMessage());
            return false;
        }
    }

    private boolean restaurarStock(int idProducto, int cantidad) {
        String sql = "UPDATE Productos SET cantidad_disponible = cantidad_disponible + ? WHERE id_producto = ?";

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, cantidad);
            ps.setInt(2, idProducto);

            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al restaurar stock: " + e.getMessage());
            return false;
        }
    }

    public Ventas buscar(int idVenta) throws SQLException {
        try {
            PreparedStatement ps = conexion.prepareStatement("SELECT * FROM Ventas WHERE id_venta = ?");
            ps.setInt(1, idVenta);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Ventas venta = construirVentas(rs);
                rs.close();
                ps.close();
                return venta;
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.err.println("Error al buscar venta: " + e.getMessage());
        }

        return null;
    }

    public List<Ventas> listarVentas() {
        List<Ventas> lista = new ArrayList<>();

        try {
            PreparedStatement ps = conexion.prepareStatement("SELECT * FROM Ventas ORDER BY id_venta DESC");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(construirVentas(rs));
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.err.println("Error al listar ventas: " + e.getMessage());
        }

        return lista;
    }

    private Ventas construirVentas(ResultSet rs) throws SQLException {
        Ventas venta = new Ventas();
        venta.setIdVenta(rs.getInt("id_venta"));
        venta.setFechaVentas(rs.getString("fecha_venta"));
        venta.setIdCliente(rs.getInt("id_cliente"));
        venta.setIdUsuario(rs.getInt("id_usuario"));
        venta.setObservaciones(rs.getString("observaciones"));

        String estadoTexto = rs.getString("estado");
        if (estadoTexto != null) {
            venta.setEstado(EstadoVenta.valueOf(estadoTexto));
        }

        calcularTotales(venta);

        return venta;
    }

    private void calcularTotales(Ventas venta) {
        try {
            List<DetalleVentas> detalles = detalleDAO.obtenerPorVenta(venta.getIdVenta());

            double subtotal = 0;
            for (DetalleVentas detalle : detalles) {
                subtotal += detalle.getCantidad() * detalle.getPrecioUnitario();
            }

            double impuesto = subtotal * 0.13;
            double total = subtotal + impuesto;

            venta.setSubtotal(subtotal);
            venta.setImpuesto(impuesto);
            venta.setTotal(total);

        } catch (Exception e) {
            System.err.println("Error al calcular totales: " + e.getMessage());
        }
    }
}
