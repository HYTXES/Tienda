package com.abarrotes.vistas;

import com.abarrotes.controladores.ProveedorDAO;
import com.abarrotes.modelos.Proveedor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelProveedores extends JPanel {
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtEmpresa, txtContacto, txtTel;
    private JComboBox<String> cbDia;
    private JButton btnGuardar, btnEliminar;
    private ProveedorDAO dao = new ProveedorDAO();

    public PanelProveedores() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- PANEL DE FORMULARIO (Diseño mejorado) ---
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Datos del Proveedor"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Margen entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Empresa:"), gbc);
        gbc.gridx = 1;
        txtEmpresa = new JTextField(15);
        panelForm.add(txtEmpresa, gbc);

        gbc.gridx = 2;
        panelForm.add(new JLabel("Nombre Contacto:"), gbc);
        gbc.gridx = 3;
        txtContacto = new JTextField(15);
        panelForm.add(txtContacto, gbc);

        // Fila 2
        gbc.gridx = 0; gbc.gridy = 1;
        panelForm.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        txtTel = new JTextField(15);
        panelForm.add(txtTel, gbc);

        gbc.gridx = 2;
        panelForm.add(new JLabel("Día de Visita:"), gbc);
        gbc.gridx = 3;
        cbDia = new JComboBox<>(new String[]{"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"});
        panelForm.add(cbDia, gbc);

        // Botón Guardar (Fila 3)
        gbc.gridx = 3; gbc.gridy = 2;
        gbc.insets = new Insets(15, 10, 10, 10);
        btnGuardar = new JButton("GUARDAR PROVEEDOR");
        btnGuardar.setBackground(new Color(0, 150, 136));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(150, 35));
        panelForm.add(btnGuardar, gbc);

        // --- TABLA ---
        modelo = new DefaultTableModel(new Object[]{"ID", "Empresa", "Contacto", "Teléfono", "Día Visita"}, 0);
        tabla = new JTable(modelo);
        tabla.setRowHeight(25); // Filas más altas para que se vea moderno
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        // --- PANEL INFERIOR (BOTÓN ELIMINAR) ---
        btnEliminar = new JButton("ELIMINAR SELECCIONADO");
        btnEliminar.setBackground(new Color(192, 57, 43));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setFont(new Font("SansSerif", Font.BOLD, 11));
        
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.setBackground(Color.WHITE);
        panelSur.add(btnEliminar);

        // Agregar todo al panel principal
        add(panelForm, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);

        // --- EVENTOS ---
        btnGuardar.addActionListener(e -> {
            if (txtEmpresa.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre de la empresa es obligatorio");
                return;
            }
            Proveedor p = new Proveedor(0, txtEmpresa.getText(), txtContacto.getText(), txtTel.getText(), cbDia.getSelectedItem().toString());
            if(dao.registrar(p)) {
                cargarDatos();
                limpiar();
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                int id = (int) tabla.getValueAt(fila, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este proveedor?");
                if (confirm == JOptionPane.YES_OPTION) {
                    if (dao.eliminar(id)) cargarDatos();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un proveedor de la tabla");
            }
        });

        cargarDatos();
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        List<Proveedor> lista = dao.listar();
        for (Proveedor p : lista) {
            modelo.addRow(new Object[]{p.getIdProveedor(), p.getEmpresa(), p.getContacto(), p.getTelefono(), p.getDiaVisita()});
        }
    }

    private void limpiar() {
        txtEmpresa.setText("");
        txtContacto.setText("");
        txtTel.setText("");
        cbDia.setSelectedIndex(0);
    }
}