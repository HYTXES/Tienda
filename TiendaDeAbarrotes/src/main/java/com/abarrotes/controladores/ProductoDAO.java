package com.abarrotes.controladores;

import com.abarrotes.conexion.Conexion;
import com.abarrotes.modelos.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public boolean registrar(Producto p) {
        String sql = "INSERT INTO productos (codigo_barras, nombre, precio_compra, precio_venta, cantidad_stock, id_proveedor) VALUES (?,?,?,?,?,?)";
        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setDouble(3, p.getPrecioCompra());
            ps.setDouble(4, p.getPrecioVenta());
            ps.setDouble(5, p.getStock());
            ps.setInt(6, p.getIdProveedor());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar producto: " + e.getMessage());
            return false;
        }
    }
    
    public boolean actualizarPrecios(String codigo, double pCompra, double pVenta) {
        String sql = "UPDATE productos SET precio_compra = ?, precio_venta = ? WHERE codigo_barras = ?";
        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, pCompra);
            ps.setDouble(2, pVenta);
            ps.setString(3, codigo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar precios: " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminar(String codigo) {
        String sql = "DELETE FROM productos WHERE codigo_barras = ?";
        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try (Connection con = Conexion.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Producto(
                    rs.getString("codigo_barras"), rs.getString("nombre"),
                    rs.getDouble("precio_compra"), rs.getDouble("precio_venta"),
                    rs.getDouble("cantidad_stock"), rs.getInt("id_proveedor")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
    
    public Producto buscarPorCodigo(String codigo) {
        Producto p = null;
        String sql = "SELECT * FROM productos WHERE codigo_barras = ?";
    
        try (Connection con = Conexion.conectar(); 
            PreparedStatement ps = con.prepareStatement(sql)) {
        
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Producto();
                    p.setCodigo(rs.getString("codigo_barras"));
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecioCompra(rs.getDouble("precio_compra"));
                    p.setPrecioVenta(rs.getDouble("precio_venta"));
                    p.setStock(rs.getDouble("cantidad_stock"));
                    p.setIdProveedor(rs.getInt("id_proveedor"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar producto por código: " + e.getMessage());
        }
        return p;
    }
    
    public boolean agregarStock(String codigo, double cantidad) {
    String sql = "UPDATE productos SET cantidad_stock = cantidad_stock + ? WHERE codigo_barras = ?";
    try (Connection con = Conexion.conectar(); 
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setDouble(1, cantidad);
        ps.setString(2, codigo);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Error al subir stock: " + e.getMessage());
        return false;
    }
}
    
}