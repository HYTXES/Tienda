package com.abarrotes.vistas;

import com.abarrotes.controladores.ProductoDAO;
import com.abarrotes.controladores.ProveedorDAO;
import com.abarrotes.modelos.Producto;
import com.abarrotes.modelos.Proveedor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelProductos extends JPanel {
    private JTextField txtCodigo, txtNombre, txtPCompra, txtPVenta, txtStock;
    private JComboBox<Proveedor> cbProveedor;
    private JTable tabla;
    private DefaultTableModel modelo;
    private ProductoDAO proDao = new ProductoDAO();
    private ProveedorDAO provDao = new ProveedorDAO();

    public PanelProductos() {
        setLayout(new BorderLayout());

        // Panel Formulario
        JPanel panelForm = new JPanel(new GridLayout(4, 4, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Gestión de Inventario"));

        txtCodigo = new JTextField();
        txtNombre = new JTextField();
        txtPCompra = new JTextField();
        txtPVenta = new JTextField();
        txtStock = new JTextField();
        cbProveedor = new JComboBox<>();
        
        // Llenar proveedores
        cargarComboProveedores();

        JButton btnGuardar = new JButton("Registrar Producto");
        btnGuardar.setBackground(new Color(39, 174, 96));
        btnGuardar.setForeground(Color.WHITE);

        panelForm.add(new JLabel("Código:")); panelForm.add(txtCodigo);
        panelForm.add(new JLabel("Nombre:")); panelForm.add(txtNombre);
        panelForm.add(new JLabel("Precio Compra:")); panelForm.add(txtPCompra);
        panelForm.add(new JLabel("Precio Venta:")); panelForm.add(txtPVenta);
        panelForm.add(new JLabel("Stock:")); panelForm.add(txtStock);
        panelForm.add(new JLabel("Proveedor:")); panelForm.add(cbProveedor);
        panelForm.add(new JLabel("")); panelForm.add(btnGuardar);

        // Tabla
        modelo = new DefaultTableModel(new Object[]{"Código", "Nombre", "P. Venta", "Stock"}, 0);
        tabla = new JTable(modelo);

        add(panelForm, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
       
        JButton btnEliminar = new JButton("Eliminar Producto Seleccionado");
        btnEliminar.setBackground(new Color(231, 76, 60)); // Color Rojo
        btnEliminar.setForeground(Color.WHITE);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.add(btnEliminar);
        add(panelSur, BorderLayout.SOUTH);

        // --- Evento del botón Eliminar ---
        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un producto de la tabla");
                return;
            }

            // El código de barras está en la columna 0
            String codigo = tabla.getValueAt(fila, 0).toString();
            String nombre = tabla.getValueAt(fila, 1).toString();

            int respuesta = JOptionPane.showConfirmDialog(this, 
                    "¿Seguro que desea eliminar el producto: " + nombre + "?", 
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

            if (respuesta == JOptionPane.YES_OPTION) {
                if (proDao.eliminar(codigo)) {
                    JOptionPane.showMessageDialog(this, "Producto eliminado correctamente");
                 cargarDatos(); // Refrescar la tabla
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el producto");
                }
            }
        });
        
        // Evento Guardar
        btnGuardar.addActionListener(e -> {
            try {
                Proveedor seleccionado = (Proveedor) cbProveedor.getSelectedItem();
                Producto p = new Producto(
                    txtCodigo.getText(),
                    txtNombre.getText(),
                    Double.parseDouble(txtPCompra.getText()),
                    Double.parseDouble(txtPVenta.getText()),
                    Double.parseDouble(txtStock.getText()),
                    seleccionado.getIdProveedor()
                );
                if (proDao.registrar(p)) {
                    JOptionPane.showMessageDialog(this, "Producto registrado");
                    cargarDatos();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: Verifica los datos numéricos");
            }
        });

        cargarDatos();
    }

    private void cargarComboProveedores() {
        List<Proveedor> lista = provDao.listar();
        for (Proveedor p : lista) {
            cbProveedor.addItem(p);
        }
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        for (Producto p : proDao.listar()) {
            modelo.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecioVenta(), p.getStock()});
        }
    }
}