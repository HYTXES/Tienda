package com.abarrotes.vistas;

import com.abarrotes.controladores.UsuarioDAO;
import com.abarrotes.modelos.SesionUsuario;
import com.abarrotes.modelos.Usuario;
import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;

    public Login() {
        setTitle("Acceso al Sistema");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel Principal con un diseño de Caja (BoxLayout) para evitar que se pierdan elementos
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(new Color(33, 37, 41)); // Gris oscuro moderno
        panelPrincipal.setLayout(new GridBagLayout()); // GridBagLayout es el más estable
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 30, 10, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- TÍTULO ---
        JLabel lblTitulo = new JLabel("INICIAR SESIÓN", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelPrincipal.add(lblTitulo, gbc);

        // --- USUARIO ---
        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setForeground(new Color(173, 181, 189));
        gbc.gridy = 1; gbc.gridwidth = 1;
        panelPrincipal.add(lblUser, gbc);

        txtUsuario = new JTextField(20);
        txtUsuario.setPreferredSize(new Dimension(300, 40));
        txtUsuario.setBackground(new Color(52, 58, 64));
        txtUsuario.setForeground(Color.WHITE);
        txtUsuario.setCaretColor(Color.WHITE); // Esto hace que el cursor sea blanco y visible
        txtUsuario.setBorder(BorderFactory.createLineBorder(new Color(73, 80, 87)));
        txtUsuario.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridy = 2;
        panelPrincipal.add(txtUsuario, gbc);

        // --- PASSWORD ---
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setForeground(new Color(173, 181, 189));
        gbc.gridy = 3;
        panelPrincipal.add(lblPass, gbc);

        txtPassword = new JPasswordField(20);
        txtPassword.setPreferredSize(new Dimension(300, 40));
        txtPassword.setBackground(new Color(52, 58, 64));
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setCaretColor(Color.WHITE);
        txtPassword.setBorder(BorderFactory.createLineBorder(new Color(73, 80, 87)));
        gbc.gridy = 4;
        panelPrincipal.add(txtPassword, gbc);

        // --- BOTÓN ---
        btnIngresar = new JButton("INGRESAR");
        btnIngresar.setPreferredSize(new Dimension(300, 50));
        btnIngresar.setBackground(new Color(0, 150, 136));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 5;
        gbc.insets = new Insets(30, 30, 10, 30);
        panelPrincipal.add(btnIngresar, gbc);

        add(panelPrincipal);

        // Eventos
        btnIngresar.addActionListener(e -> validarAcceso());
        txtPassword.addActionListener(e -> validarAcceso());
    }

    private void validarAcceso() {
        String user = txtUsuario.getText().trim();
        String pass = new String(txtPassword.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor llene los campos");
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        Usuario u = dao.validarUsuario(user, pass);

        if (u != null) {
            SesionUsuario.setUsuario(u);
            if (u.getRol().equalsIgnoreCase("Administrador")) {
                new DashboardAdmin().setVisible(true);
            } else {
                new DashboardEmpleado().setVisible(true);
            }
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas");
        }
    }
}