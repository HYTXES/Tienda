package com.abarrotes.vistas;

import com.abarrotes.controladores.UsuarioDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private JLabel lblTitulo;

    public Login() {
        // Configuración de la ventana
        setTitle("Acceso al Sistema - Abarrotes");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla
        setLayout(new BorderLayout());

        // Panel Principal con fondo oscuro
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(new Color(45, 45, 45));
        panelPrincipal.setLayout(null);

        // Título
        lblTitulo = new JLabel("INICIAR SESIÓN", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setBounds(0, 50, 400, 40);
        panelPrincipal.add(lblTitulo);

        // Campo Usuario
        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setForeground(Color.LIGHT_GRAY);
        lblUser.setBounds(50, 150, 100, 30);
        panelPrincipal.add(lblUser);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(50, 180, 300, 40);
        txtUsuario.setBackground(new Color(60, 60, 60));
        txtUsuario.setForeground(Color.WHITE);
        txtUsuario.setCaretColor(Color.WHITE);
        txtUsuario.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        panelPrincipal.add(txtUsuario);

        // Campo Password
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setForeground(Color.LIGHT_GRAY);
        lblPass.setBounds(50, 240, 100, 30);
        panelPrincipal.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(50, 270, 300, 40);
        txtPassword.setBackground(new Color(60, 60, 60));
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setCaretColor(Color.WHITE);
        txtPassword.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        panelPrincipal.add(txtPassword);

        // Botón Ingresar
        btnIngresar = new JButton("INGRESAR");
        btnIngresar.setBounds(50, 360, 300, 50);
        btnIngresar.setBackground(new Color(0, 150, 136)); // Color verde azulado
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnIngresar.setBorder(null);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelPrincipal.add(btnIngresar);

        add(panelPrincipal, BorderLayout.CENTER);

        // Evento del botón
        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarAcceso();
            }
        });

        // Soporte para entrar con la tecla Enter
        txtPassword.addActionListener(e -> validarAcceso());
    }

    private void validarAcceso() {
        String user = txtUsuario.getText().trim();
        String pass = new String(txtPassword.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        String rol = dao.login(user, pass);

        if (rol != null && !rol.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bienvenido " + user);
            
            // Redirección según el rol
            if (rol.equalsIgnoreCase("Administrador")) {
                new DashboardAdmin().setVisible(true);
            } else {
                new DashboardEmpleado().setVisible(true);
            }
            
            this.dispose(); // Cerrar el login
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error de acceso", JOptionPane.ERROR_MESSAGE);
        }
    }
}