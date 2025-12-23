package com.abarrotes.vistas;

import com.abarrotes.controladores.ProductoDAO;
import com.abarrotes.controladores.ProveedorDAO;
import com.abarrotes.modelos.Producto;
import com.abarrotes.modelos.Proveedor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.event.TableModelEvent;

public class PanelProductos extends JPanel {

    private JTextField txtCodigo, txtNombre, txtPCompra, txtPVenta, txtStock, txtBuscar;
    private JComboBox<Proveedor> cbProveedor;
    private JTable tabla;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter; // Para el buscador
    private ProductoDAO proDao = new ProductoDAO();
    private ProveedorDAO provDao = new ProveedorDAO();

    public PanelProductos() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- PANEL NORTE: FORMULARIO Y BUSCADOR ---
        JPanel panelNorte = new JPanel(new BorderLayout(10, 10));
        panelNorte.setBackground(Color.WHITE);

        // Sub-panel: Formulario de registro
        JPanel panelForm = new JPanel(new GridLayout(4, 4, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Registro de Mercancía"));
        panelForm.setBackground(Color.WHITE);

        txtCodigo = new JTextField();
        txtNombre = new JTextField();
        txtPCompra = new JTextField();
        txtPVenta = new JTextField();
        txtStock = new JTextField();
        cbProveedor = new JComboBox<>();
        cargarComboProveedores();

        JButton btnGuardar = new JButton("REGISTRAR");
        btnGuardar.setBackground(new Color(39, 174, 96));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 12));

        panelForm.add(new JLabel("Código:")); panelForm.add(txtCodigo);
        panelForm.add(new JLabel("Nombre:")); panelForm.add(txtNombre);
        panelForm.add(new JLabel("P. Compra:")); panelForm.add(txtPCompra);
        panelForm.add(new JLabel("P. Venta:")); panelForm.add(txtPVenta);
        panelForm.add(new JLabel("Stock Inicial:")); panelForm.add(txtStock);
        panelForm.add(new JLabel("Proveedor:")); panelForm.add(cbProveedor);
        panelForm.add(new JLabel("")); panelForm.add(btnGuardar);

        // Sub-panel: Buscador
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setBackground(Color.WHITE);
        txtBuscar = new JTextField(25);
        txtBuscar.setBorder(BorderFactory.createTitledBorder("Buscar producto (Nombre o Código):"));
        panelBusqueda.add(txtBuscar);

        panelNorte.add(panelForm, BorderLayout.CENTER);
        panelNorte.add(panelBusqueda, BorderLayout.SOUTH);

        // --- PANEL CENTRO: TABLA ---
        modelo = new DefaultTableModel(new Object[]{"Código", "Nombre", "P. Compra", "P. Venta", "Stock"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 3; // Editar precios directamente
            }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(25);
        
        // Sorter para el buscador
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);

        // Evento de edición automática
        modelo.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int fila = e.getFirstRow();
                int col = e.getColumn();
                if (fila >= 0 && (col == 2 || col == 3)) {
                    actualizarPreciosTabla(fila);
                }
            }
        });

        // --- PANEL SUR: ACCIONES ---
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelSur.setBackground(Color.WHITE);

        JButton btnSurtir = new JButton("SURTIR STOCK (+)");
        btnSurtir.setBackground(new Color(52, 152, 219));
        btnSurtir.setForeground(Color.WHITE);

        JButton btnEliminar = new JButton("ELIMINAR");
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);

        panelSur.add(btnSurtir);
        panelSur.add(btnEliminar);

        add(panelNorte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);

        // --- EVENTOS ---
        
        // Buscador dinámico
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + txtBuscar.getText()));
            }
        });

        // Botón Guardar
        btnGuardar.addActionListener(e -> registrarProducto());

        // Botón Surtir
        btnSurtir.addActionListener(e -> surtirStock());

        // Botón Eliminar
        btnEliminar.addActionListener(e -> eliminarProducto());

        cargarDatos();
    }

    private void registrarProducto() {
        try {
            if(txtCodigo.getText().isEmpty() || txtNombre.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Código y Nombre son obligatorios");
                return;
            }
            Proveedor sel = (Proveedor) cbProveedor.getSelectedItem();
            Producto p = new Producto(txtCodigo.getText(), txtNombre.getText(), 
                Double.parseDouble(txtPCompra.getText()), Double.parseDouble(txtPVenta.getText()), 
                Double.parseDouble(txtStock.getText()), sel.getIdProveedor());

            if (proDao.registrar(p)) {
                JOptionPane.showMessageDialog(this, "¡Producto Registrado!");
                cargarDatos();
                limpiar();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en los datos numéricos");
        }
    }

    private void surtirStock() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            String cod = tabla.getValueAt(fila, 0).toString();
            String nom = tabla.getValueAt(fila, 1).toString();
            String cantStr = JOptionPane.showInputDialog(this, "¿Cuántas piezas llegaron de: " + nom + "?");
            
            if (cantStr != null && !cantStr.isEmpty()) {
                try {
                    double cantidad = Double.parseDouble(cantStr);
                    if (proDao.agregarStock(cod, cantidad)) { // Requiere método en DAO
                        cargarDatos();
                        JOptionPane.showMessageDialog(this, "Stock actualizado");
                    }
                } catch (Exception e) { JOptionPane.showMessageDialog(this, "Valor no válido"); }
            }
        } else { JOptionPane.showMessageDialog(this, "Seleccione un producto"); }
    }

    private void actualizarPreciosTabla(int fila) {
        try {
            String cod = modelo.getValueAt(fila, 0).toString();
            double pc = Double.parseDouble(modelo.getValueAt(fila, 2).toString());
            double pv = Double.parseDouble(modelo.getValueAt(fila, 3).toString());
            proDao.actualizarPrecios(cod, pc, pv);
        } catch (Exception e) { cargarDatos(); }
    }

    private void eliminarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            String cod = tabla.getValueAt(fila, 0).toString();
            if (JOptionPane.showConfirmDialog(this, "¿Eliminar?") == JOptionPane.YES_OPTION) {
                if (proDao.eliminar(cod)) cargarDatos();
            }
        }
    }

    private void cargarComboProveedores() {
        cbProveedor.removeAllItems();
        List<Proveedor> lista = provDao.listar();
        for (Proveedor p : lista) cbProveedor.addItem(p);
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        List<Producto> lista = proDao.listar();
        for (Producto p : lista) {
            modelo.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecioCompra(), p.getPrecioVenta(), p.getStock()});
        }
    }

    private void limpiar() {
        txtCodigo.setText(""); txtNombre.setText(""); txtPCompra.setText("");
        txtPVenta.setText(""); txtStock.setText(""); txtCodigo.requestFocus();
    }
}