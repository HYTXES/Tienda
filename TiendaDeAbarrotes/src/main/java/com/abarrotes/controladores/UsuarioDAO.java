package com.abarrotes.controladores;

import com.abarrotes.conexion.Conexion;
import com.abarrotes.modelos.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public String login(String user, String pass) {
        String rol = "";
        String sql = "SELECT rol FROM usuarios WHERE nombre_usuario = ? AND contrasena = ?";
        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setString(2, pass);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { rol = rs.getString("rol"); }
            }
        } catch (SQLException e) { System.err.println(e.getMessage()); }
        return rol;
    }

    public boolean registrar(Usuario user) {
        String sql = "INSERT INTO usuarios (nombre_usuario, contrasena, rol) VALUES (?,?,?)";
        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getNombre());
            ps.setString(2, user.getContrasena());
            ps.setString(3, user.getRol());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
    
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
        return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Connection con = Conexion.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Usuario(rs.getInt("id_usuario"), rs.getString("nombre_usuario"), 
                          rs.getString("contrasena"), rs.getString("rol")));
            }
        } catch (SQLException e) { System.err.println(e.getMessage()); }
        return lista;
    }
}