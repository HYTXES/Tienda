package com.abarrotes.vistas;

import javax.swing.*;
import java.awt.*;

public class DashboardEmpleado extends JFrame {
    private JPanel panelCentro; // Atributo para poder cambiar el contenido

    public DashboardEmpleado() {
        setTitle("PUNTO DE VENTA - Cajero");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- PANEL LATERAL (MENÚ) ---
        JPanel menu = new JPanel(new GridLayout(6, 1, 10, 10));
        menu.setBackground(new Color(34, 47, 62));
        menu.setPreferredSize(new Dimension(200, 700));

        JLabel lblRol = new JLabel(" CAJERO", SwingConstants.CENTER);
        lblRol.setForeground(Color.WHITE);
        lblRol.setFont(new Font("SansSerif", Font.BOLD, 18));

        JButton btnVentas = crearBotonMenu("Nueva Venta   🛒");
        JButton btnConsultar = crearBotonMenu("Ver Precio   🔍");
        JButton btnCerrar = crearBotonMenu("Cerrar Sesión   🚪");

        menu.add(lblRol);
        menu.add(btnVentas);
        menu.add(btnConsultar);
        menu.add(new JLabel("")); // Espacio
        menu.add(new JLabel("")); // Espacio
        menu.add(btnCerrar);

        // --- PANEL CENTRAL ---
        panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(Color.WHITE);
        panelCentro.add(new JLabel("Bienvenido. Presione 'Nueva Venta' para comenzar.", SwingConstants.CENTER));

        // --- EVENTOS ---
        btnVentas.addActionListener(e -> mostrarPanel(new PanelVentas()));
        
        btnConsultar.addActionListener(e -> {
            // Aquí llamaremos al panel de consultar precio (Punto 7)
            // Por ahora podemos reusar un JOptionPane rápido o crear el panel
            mostrarVerificador();
        });

        btnCerrar.addActionListener(e -> {
            new Login().setVisible(true);
            this.dispose();
        });

        add(menu, BorderLayout.WEST);
        add(panelCentro, BorderLayout.CENTER);
    }

    private JButton crearBotonMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(52, 73, 94));
        btn.setFocusPainted(false);
        return btn;
    }

    private void mostrarPanel(JPanel p) {
        panelCentro.removeAll();
        panelCentro.add(p, BorderLayout.CENTER);
        panelCentro.revalidate();
        panelCentro.repaint();
    }

    private void mostrarVerificador() {
        String codigo = JOptionPane.showInputDialog(this, "Escanee o digite el código:");
        if (codigo != null && !codigo.isEmpty()) {
            com.abarrotes.controladores.ProductoDAO dao = new com.abarrotes.controladores.ProductoDAO();
            com.abarrotes.modelos.Producto p = dao.buscarPorCodigo(codigo);
            if (p != null) {
                JOptionPane.showMessageDialog(this, 
                    "PRODUCTO: " + p.getNombre() + "\nPRECIO: $" + p.getPrecioVenta(),
                    "Verificador de Precios", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Producto no encontrado");
            }
        }
    }
}