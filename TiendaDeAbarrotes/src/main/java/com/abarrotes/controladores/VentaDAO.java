package com.abarrotes.controladores;

import com.abarrotes.conexion.Conexion;
import java.sql.*;

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
}