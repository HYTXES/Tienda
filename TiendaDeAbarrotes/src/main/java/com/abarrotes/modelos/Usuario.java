package com.abarrotes.modelos;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String nombreUsuario;
    private String contrasena; // Cambiado de password a contrasena para coincidir con el DAO
    private String rol;

    // Constructor vacío
    public Usuario() {}

    // Constructor completo
    public Usuario(int idUsuario, String nombreUsuario, String contrasena, String rol) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    // --- GETTERS Y SETTERS ---
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    // Cambiado a getContrasena para que el DAO lo encuentre
    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}