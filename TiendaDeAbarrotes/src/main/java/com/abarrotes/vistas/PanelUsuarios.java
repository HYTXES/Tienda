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
        setLayout(new BorderLayout());

        // Formulario
        JPanel panelForm = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtUser = new JTextField(10);
        txtPass = new JPasswordField(10);
        cbRol = new JComboBox<>(new String[]{"Administrador", "Empleado"});
        btnGuardar = new JButton("Registrar");

        panelForm.add(new JLabel("Usuario:")); panelForm.add(txtUser);
        panelForm.add(new JLabel("Clave:")); panelForm.add(txtPass);
        panelForm.add(new JLabel("Rol:")); panelForm.add(cbRol);
        panelForm.add(btnGuardar);

        // Tabla
        modelo = new DefaultTableModel(new Object[]{"ID", "Usuario", "Rol"}, 0);
        tabla = new JTable(modelo);
        
        // Botón Eliminar (Panel Sur)
        btnEliminar = new JButton("Eliminar Usuario Seleccionado");
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.add(btnEliminar);

        add(panelForm, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);

        // Eventos
        btnGuardar.addActionListener(e -> {
            if(txtUser.getText().isEmpty()) return;
            Usuario u = new Usuario(0, txtUser.getText(), new String(txtPass.getPassword()), cbRol.getSelectedItem().toString());
            if(dao.registrar(u)) {
                JOptionPane.showMessageDialog(this, "Usuario Creado");
                txtUser.setText(""); txtPass.setText("");
                cargarDatos();
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla");
                return;
            }
            int id = (int) tabla.getValueAt(fila, 0);
            String nombre = tabla.getValueAt(fila, 1).toString();

            if (nombre.equals("admin")) {
                JOptionPane.showMessageDialog(this, "No se puede eliminar al admin principal");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar a " + nombre + "?");
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
            modelo.addRow(new Object[]{u.getId(), u.getNombre(), u.getRol()});
        }
    }
}