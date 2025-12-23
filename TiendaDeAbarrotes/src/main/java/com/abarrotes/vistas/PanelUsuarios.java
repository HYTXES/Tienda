package com.abarrotes.vistas;

import com.abarrotes.controladores.UsuarioDAO;
import com.abarrotes.modelos.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelUsuarios extends JPanel {
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JComboBox<String> cbRol;
    private JButton btnGuardar, btnEliminar;
    private UsuarioDAO dao = new UsuarioDAO();

    public PanelUsuarios() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- PANEL DE FORMULARIO (Estilizado) ---
        JPanel panelForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createTitledBorder("Registro de Usuarios"));

        txtUser = new JTextField(12);
        txtPass = new JPasswordField(12);
        cbRol = new JComboBox<>(new String[]{"Administrador", "Empleado"});
        
        btnGuardar = new JButton("REGISTRAR");
        btnGuardar.setBackground(new Color(0, 150, 136));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 12));

        panelForm.add(new JLabel("Usuario:")); 
        panelForm.add(txtUser);
        panelForm.add(new JLabel("Clave:")); 
        panelForm.add(txtPass);
        panelForm.add(new JLabel("Rol:")); 
        panelForm.add(cbRol);
        panelForm.add(btnGuardar);

        // --- TABLA ---
        modelo = new DefaultTableModel(new Object[]{"ID", "Usuario", "Rol"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(25);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(Color.WHITE);

        // --- BOTÓN ELIMINAR ---
        btnEliminar = new JButton("ELIMINAR SELECCIONADO");
        btnEliminar.setBackground(new Color(192, 57, 43));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.setBackground(Color.WHITE);
        panelSur.add(btnEliminar);

        add(panelForm, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);

        // --- EVENTOS ---
        btnGuardar.addActionListener(e -> {
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword()).trim();
            
            if(user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos");
                return;
            }

            // CORRECCIÓN: Usamos el constructor de Usuario ajustado
            Usuario u = new Usuario(0, user, pass, cbRol.getSelectedItem().toString());
            
            if(dao.registrar(u)) {
                JOptionPane.showMessageDialog(this, "Usuario Creado");
                txtUser.setText(""); 
                txtPass.setText("");
                cargarDatos();
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla");
                return;
            }
            
            // CORRECCIÓN: Convertir ID de forma segura
            int id = Integer.parseInt(tabla.getValueAt(fila, 0).toString());
            String nombre = tabla.getValueAt(fila, 1).toString();

            if (nombre.equalsIgnoreCase("admin")) {
                JOptionPane.showMessageDialog(this, "No se puede eliminar al admin principal por seguridad");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar a " + nombre + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.eliminar(id)) {
                    cargarDatos();
                }
            }
        });

        cargarDatos();
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        List<Usuario> lista = dao.listar();
        for (Usuario u : lista) {
            // CORRECCIÓN: Nombres de métodos según la clase Usuario corregida
            modelo.addRow(new Object[]{
                u.getIdUsuario(), 
                u.getNombreUsuario(), 
                u.getRol()
            });
        }
    }
}