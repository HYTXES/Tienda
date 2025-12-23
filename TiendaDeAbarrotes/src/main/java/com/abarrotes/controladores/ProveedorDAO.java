package com.abarrotes.controladores;

import com.abarrotes.conexion.Conexion;
import com.abarrotes.modelos.Proveedor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    public boolean registrar(Proveedor prov) {
        String sql = "INSERT INTO proveedores (empresa, nombre_contacto, telefono, dia_visita) VALUES (?,?,?,?)";
        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, prov.getEmpresa());
            ps.setString(2, prov.getContacto());
            ps.setString(3, prov.getTelefono());
            ps.setString(4, prov.getDiaVisita());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
    
    public boolean eliminar(int id) {
    // Asegúrate de que el nombre de la columna sea id_proveedor
        String sql = "DELETE FROM proveedores WHERE id_proveedor = ?";
        try (Connection con = Conexion.conectar(); 
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    public List<Proveedor> listar() {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM proveedores";
        try (Connection con = Conexion.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Proveedor(
                    rs.getInt("id_proveedor"),
                    rs.getString("empresa"),
                    rs.getString("nombre_contacto"),
                    rs.getString("telefono"),
                    rs.getString("dia_visita")
                ));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return lista;
    }
}