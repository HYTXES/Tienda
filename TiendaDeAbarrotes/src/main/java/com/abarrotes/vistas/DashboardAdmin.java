package com.abarrotes.vistas;

import javax.swing.*;
import java.awt.*;

public class DashboardAdmin extends JFrame {
    private JPanel panelCentro;
    private Color colorFondoMenu = new Color(33, 37, 41); // Gris casi negro
    private Color colorBoton = new Color(52, 58, 64);     // Gris oscuro
    private Color colorAcento = new Color(0, 150, 136);   // Verde azulado (Teal)

    public DashboardAdmin() {
        setTitle("ADMINISTRACIÓN - POS Abarrotes");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- PANEL LATERAL (MENÚ) ---
        JPanel menu = new JPanel();
        menu.setBackground(colorFondoMenu);
        menu.setPreferredSize(new Dimension(220, 750));
        menu.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5)); // Botones uno tras otro

        // Logo o Título en el menú
        JLabel lblLogo = new JLabel("ADMINISTRADOR");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblLogo.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        menu.add(lblLogo);

        // Creación de botones con estilo plano
        JButton btnVentas = crearBotonMenu("NUEVA VENTA   ➕");
        JButton btnProds = crearBotonMenu("INVENTARIO   📦");
        JButton btnProv = crearBotonMenu("PROVEEDORES   🚚");
        JButton btnUsers = crearBotonMenu("USUARIOS   👥");
        JButton btnReportes = crearBotonMenu("REPORTE DIARIO   📃");
        JButton btnGanancias = crearBotonMenu("GANANCIAS   💰");

        JButton btnSalir = crearBotonMenu("CERRAR SESIÓN   🚪");

        // Agregar botones al panel
        menu.add(btnVentas);
        menu.add(btnProds);
        menu.add(btnProv);
        menu.add(btnUsers);
        menu.add(btnReportes);
        menu.add(btnGanancias);
        menu.add(btnSalir);

        // --- PANEL CENTRAL ---
        panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(Color.WHITE);
        JLabel lblBienvenida = new JLabel("Bienvenido Administrador", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("SansSerif", Font.ITALIC, 20));
        lblBienvenida.setForeground(Color.LIGHT_GRAY);
        panelCentro.add(lblBienvenida);

        // --- EVENTOS ---
        btnVentas.addActionListener(e -> mostrarPanel(new PanelVentas()));
        btnProds.addActionListener(e -> mostrarPanel(new PanelProductos()));
        btnProv.addActionListener(e -> mostrarPanel(new PanelProveedores()));
        btnUsers.addActionListener(e -> mostrarPanel(new PanelUsuarios()));
        btnReportes.addActionListener(e -> mostrarPanel(new PanelReportes()));
        
        btnSalir.addActionListener(e -> {
            new Login().setVisible(true);
            this.dispose();
        });

        add(menu, BorderLayout.WEST);
        add(panelCentro, BorderLayout.CENTER);
    }

    // El secreto de la estética está aquí:
    private JButton crearBotonMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(220, 50)); // Ancho completo del menú
        btn.setForeground(Color.WHITE);
        btn.setBackground(colorBoton);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        // Quitar bordes y efectos feos de Java
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(73, 80, 87))); // Línea sutil abajo
        btn.setContentAreaFilled(true);
        
        // Efecto Hover (Cambio de color al pasar el mouse)
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(colorAcento);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!btn.getBackground().equals(new Color(192, 57, 43))) { // Si no es el de ganancias
                    btn.setBackground(colorBoton);
                } else {
                    btn.setBackground(new Color(192, 57, 43));
                }
            }
        });

        return btn;
    }

    private void mostrarPanel(JPanel p) {
        panelCentro.removeAll();
        panelCentro.add(p, BorderLayout.CENTER);
        panelCentro.revalidate();
        panelCentro.repaint();
    }
}