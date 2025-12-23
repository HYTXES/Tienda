package com.abarrotes.vistas;

import com.abarrotes.controladores.ProductoDAO;
import com.abarrotes.controladores.VentaDAO;
import com.abarrotes.modelos.Producto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PanelVentas extends JPanel {
    private JTextField txtCodigo, txtPago, txtCambio;
    private JLabel lblTotal;
    private JTable tablaVenta;
    private DefaultTableModel modelo;
    private double totalPagar = 0.00;
    
    private ProductoDAO proDao = new ProductoDAO();
    private VentaDAO venDao = new VentaDAO();

    public PanelVentas() {
        setLayout(new BorderLayout());

        // --- NORTE: BUSCADOR ---
        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtCodigo = new JTextField(20);
        txtCodigo.setBorder(BorderFactory.createTitledBorder("Escanear Código de Barras"));
        panelNorte.add(txtCodigo);
        
        // --- CENTRO: CARRITO ---
        modelo = new DefaultTableModel(new Object[]{"Código", "Descripción", "Cantidad", "Precio", "Subtotal"}, 0);
        tablaVenta = new JTable(modelo);
        
        // --- SUR: TOTAL Y PAGO ---
        JPanel panelSur = new JPanel(new GridLayout(1, 4, 10, 10));
        lblTotal = new JLabel("TOTAL: $0.00");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 20));
        
        txtPago = new JTextField();
        txtPago.setBorder(BorderFactory.createTitledBorder("Paga con:"));
        
        txtCambio = new JTextField();
        txtCambio.setEditable(false);
        txtCambio.setBorder(BorderFactory.createTitledBorder("Cambio:"));
        
        JButton btnVender = new JButton("FINALIZAR VENTA");
        btnVender.setBackground(new Color(46, 204, 113));
        btnVender.setForeground(Color.WHITE);

        panelSur.add(lblTotal); panelSur.add(txtPago);
        panelSur.add(txtCambio); panelSur.add(btnVender);

        add(panelNorte, BorderLayout.NORTH);
        add(new JScrollPane(tablaVenta), BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);

        // --- EVENTO: BUSCAR PRODUCTO AL DAR ENTER ---
        txtCodigo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarProducto();
                }
            }
        });

        // --- EVENTO: CALCULAR CAMBIO ---
        txtPago.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calcularCambio();
            }
        });

        // --- EVENTO: FINALIZAR ---
        btnVender.addActionListener(e -> finalizarVenta());
    }

    private void buscarProducto() {
    String cod = txtCodigo.getText().trim(); // Usa .trim() para evitar espacios accidentales
        if (cod.isEmpty()) return;

        Producto p = proDao.buscarPorCodigo(cod); // <--- Aquí llama a la base de datos
    
        if (p != null) {
            // Lógica para añadir a la tabla...
            modelo.addRow(new Object[]{p.getCodigo(), p.getNombre(), 1, p.getPrecioVenta(), p.getPrecioVenta()});
            txtCodigo.setText(""); // Limpia para el siguiente producto
            recalcularTotal();
        } else {
            JOptionPane.showMessageDialog(this, "El producto con código " + cod + " no existe.");
        }
    }

    private void recalcularTotal() {
        totalPagar = 0;
        for (int i = 0; i < tablaVenta.getRowCount(); i++) {
            totalPagar += (double) tablaVenta.getValueAt(i, 4);
        }
        lblTotal.setText("TOTAL: $" + String.format("%.2f", totalPagar));
    }

    private void calcularCambio() {
        try {
            double pago = Double.parseDouble(txtPago.getText());
            txtCambio.setText(String.format("%.2f", (pago - totalPagar)));
        } catch (Exception e) { txtCambio.setText("0.00"); }
    }

    private void finalizarVenta() {
        if (tablaVenta.getRowCount() == 0) return;
        
        // Aquí usaríamos el ID del usuario logueado, por ahora 1 (admin)
        int idVenta = venDao.registrarVenta(totalPagar, Double.parseDouble(txtPago.getText()), Double.parseDouble(txtCambio.getText()), 1);
        
        if (idVenta != -1) {
            for (int i = 0; i < tablaVenta.getRowCount(); i++) {
                String cod = tablaVenta.getValueAt(i, 0).toString();
                double cant = (double) tablaVenta.getValueAt(i, 2);
                double precio = (double) tablaVenta.getValueAt(i, 3);
                double sub = (double) tablaVenta.getValueAt(i, 4);
                venDao.registrarDetalle(idVenta, cod, cant, precio, sub);
            }
            JOptionPane.showMessageDialog(this, "Venta Exitosa");
            limpiarVenta();
        }
    }

    private void limpiarVenta() {
        modelo.setRowCount(0);
        lblTotal.setText("TOTAL: $0.00");
        txtPago.setText("");
        txtCambio.setText("");
    }
}