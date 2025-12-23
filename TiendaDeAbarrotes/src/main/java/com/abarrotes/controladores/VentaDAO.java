package com.abarrotes.controladores;

import com.abarrotes.conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {
    
    public int registrarVenta(double total, double pago, double cambio, int idUsuario) {
        String sql = "INSERT INTO ventas (total, pago_con, cambio, id_usuario) VALUES (?,?,?,?)";
        try (Connection con = Conexion.conectar(); 
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, total);
            ps.setDouble(2, pago);
            ps.setDouble(3, cambio);
            ps.setInt(4, idUsuario);
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1); // Retorna el ID de la venta para los detalles
        } catch (SQLException e) { System.err.println(e.getMessage()); }
        return -1;
    }

    public void registrarDetalle(int idVenta, String codigo, double cant, double precio, double subtotal) {
        String sql = "INSERT INTO detalle_ventas (id_venta, codigo_barras, cantidad, precio_venta_momento, subtotal) VALUES (?,?,?,?,?)";
        String updateStock = "UPDATE productos SET cantidad_stock = cantidad_stock - ? WHERE codigo_barras = ?";
        
        try (Connection con = Conexion.conectar()) {
            // Insertar detalle
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idVenta);
                ps.setString(2, codigo);
                ps.setDouble(3, cant);
                ps.setDouble(4, precio);
                ps.setDouble(5, subtotal);
                ps.execute();
            }
            // Actualizar Stock
            try (PreparedStatement psUpdate = con.prepareStatement(updateStock)) {
                psUpdate.setDouble(1, cant);
                psUpdate.setString(2, codigo);
                psUpdate.execute();
            }
        } catch (SQLException e) { System.err.println(e.getMessage()); }
    }
    
    public List<Object[]> obtenerReporteDiario(String filtroUsuario) {
        List<Object[]> lista = new ArrayList<>();
        // Usamos LIKE para el filtro de nombre de usuario
        String sql = "SELECT v.id_venta, p.nombre AS producto, dv.cantidad, dv.precio_venta_momento, " +
                     "(dv.precio_venta_momento - p.precio_compra) * dv.cantidad AS ganancia_neta, " +
                     "u.nombre_usuario AS cajero " +
                     "FROM detalle_ventas dv " +
                     "JOIN ventas v ON dv.id_venta = v.id_venta " +
                     "JOIN productos p ON dv.codigo_barras = p.codigo_barras " +
                     "JOIN usuarios u ON v.id_usuario = u.id_usuario " + // Join con usuarios
                     "WHERE date(v.fecha) = date('now', 'localtime') " +
                     "AND u.nombre_usuario LIKE ?"; 

        try (Connection con = Conexion.conectar(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
        
            ps.setString(1, "%" + filtroUsuario + "%");
            ResultSet rs = ps.executeQuery();
        
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id_venta"),
                    rs.getString("producto"),
                    rs.getDouble("cantidad"),
                    rs.getDouble("precio_venta_momento"),
                    rs.getDouble("ganancia_neta"),
                    rs.getString("cajero") // <--- Nueva columna
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}