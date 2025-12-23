package com.abarrotes; // El paquete debe ser solo com.abarrotes

import com.abarrotes.vistas.DashboardAdmin;
import com.abarrotes.vistas.Login;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Ejecutamos la interfaz en el hilo correcto
        SwingUtilities.invokeLater(() -> {
            Login lg = new Login();
            lg.setVisible(true);
        });
    }
}