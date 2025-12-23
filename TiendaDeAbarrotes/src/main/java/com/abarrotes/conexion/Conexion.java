package com.abarrotes.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {
    private static final String URL = "jdbc:sqlite:abarrotes_pos.db";
    private static Connection con = null;

    public static Connection conectar() {
        try {
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection(URL);
                crearTablas(); 
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar a SQLite: " + e.getMessage());
        }
        return con;
    }

    private static void crearTablas() {
        String[] consultas = {
            "CREATE TABLE IF NOT EXISTS usuarios (" +
            "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nombre_usuario TEXT NOT NULL UNIQUE, " +
            "contrasena TEXT NOT NULL, " +
            "rol TEXT CHECK(rol IN ('Administrador', 'Empleado')) NOT NULL);",

            "CREATE TABLE IF NOT EXISTS proveedores (" +
            "id_proveedor INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "empresa TEXT NOT NULL, " +
            "nombre_contacto TEXT, " +
            "telefono TEXT, " +
            "dia_visita TEXT);",

            "CREATE TABLE IF NOT EXISTS categorias (" +
            "id_categoria INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nombre_categoria TEXT NOT NULL);",

            "CREATE TABLE IF NOT EXISTS productos (" +
            "codigo_barras TEXT PRIMARY KEY, " +
            "nombre TEXT NOT NULL, " +
            "precio_compra REAL NOT NULL, " +
            "precio_venta REAL NOT NULL, " +
            "cantidad_stock REAL NOT NULL, " +
            "id_categoria INTEGER, " +
            "id_proveedor INTEGER, " +
            "FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria), " +
            "FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor));",

            "CREATE TABLE IF NOT EXISTS ventas (" +
            "id_venta INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "fecha_operacion DATETIME DEFAULT (datetime('now','localtime')), " +
            "total REAL NOT NULL, " +
            "pago_con REAL NOT NULL, " +
            "cambio REAL NOT NULL, " +
            "id_usuario INTEGER, " +
            "fecha DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            "FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario));",

            "CREATE TABLE IF NOT EXISTS detalle_ventas (" +
            "id_detalle INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "id_venta INTEGER, " +
            "codigo_barras TEXT, " +
            "cantidad REAL, " +
            "precio_venta_momento REAL, " +
            "subtotal REAL, " +
            "FOREIGN KEY (id_venta) REFERENCES ventas(id_venta), " +
            "FOREIGN KEY (codigo_barras) REFERENCES productos(codigo_barras));"
        };

        try (Statement stmt = con.createStatement()) {
            for (String sql : consultas) {
                stmt.execute(sql);
            }
            // Insertar admin por defecto
            stmt.execute("INSERT OR IGNORE INTO usuarios (nombre_usuario, contrasena, rol) VALUES ('admin', 'admin', 'Administrador')");
            System.out.println("Base de datos lista con usuario admin.");
        } catch (SQLException e) {
            System.err.println("Error al crear tablas: " + e.getMessage());
        }
    }
}