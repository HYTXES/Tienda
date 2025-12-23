package com.abarrotes.controladores;

import com.abarrotes.conexion.Conexion;
import com.abarrotes.modelos.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario validarUsuario(String user, String pass) {
        Usuario u = null;
        // Agregamos 'nombre' a la consulta si es que existe en tu tabla
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ? AND contrasena = ?";
        
        try (Connection con = Conexion.conectar(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, user);
            ps.setString(2, pass);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    u = new Usuario();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setNombreUsuario(rs.getString("nombre_usuario"));
                    // Si tienes columna 'nombre' en la BD, descomenta la siguiente línea:
                    // u.setNombre(rs.getString("nombre")); 
                    u.setRol(rs.getString("rol"));
                }
            }
        } catch (SQLException e) { 
            System.err.println("Error en Login DAO: " + e.getMessage()); 
        }
        return u;
    }

    public boolean registrar(Usuario user) {
        // Asegúrate de que los nombres de las columnas coincidan con tu DB
        String sql = "INSERT INTO usuarios (nombre_usuario, contrasena, rol) VALUES (?,?,?)";
        try (Connection con = Conexion.conectar(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getNombreUsuario());
            ps.setString(2, user.getContrasena());
            ps.setString(3, user.getRol());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false; 
        }
    }
    
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection con = Conexion.conectar(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Connection con = Conexion.conectar(); 
             Statement st = con.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                // Usamos el constructor que definimos en la clase Usuario
                lista.add(new Usuario(
                    rs.getInt("id_usuario"), 
                    rs.getString("nombre_usuario"), 
                    rs.getString("contrasena"), 
                    rs.getString("rol")
                ));
            }
        } catch (SQLException e) { 
            System.err.println("Error al listar usuarios: " + e.getMessage()); 
        }
        return lista;
    }
}