package com.abarrotes.vistas;

import com.abarrotes.controladores.ProductoDAO;
import com.abarrotes.controladores.VentaDAO;
import com.abarrotes.modelos.Producto;
import com.abarrotes.modelos.SesionUsuario; // Importante
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PanelVentas extends JPanel {
    private JTextField txtCodigo, txtCantidad, txtPago, txtCambio;
    private JLabel lblTotal;
    private JTable tablaVenta;
    private DefaultTableModel modelo;
    private double totalPagar = 0.00;
    
    private ProductoDAO proDao = new ProductoDAO();
    private VentaDAO venDao = new VentaDAO();

    public PanelVentas() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNorte.setBackground(Color.WHITE);

        txtCantidad = new JTextField("1", 5);
        txtCantidad.setBorder(BorderFactory.createTitledBorder("Cant."));
        txtCantidad.setHorizontalAlignment(JTextField.CENTER);

        txtCodigo = new JTextField(20);
        txtCodigo.setBorder(BorderFactory.createTitledBorder("Código de Barras"));
        
        JButton btnQuitar = new JButton("Quitar Producto");
        btnQuitar.setBackground(new Color(231, 76, 60));
        btnQuitar.setForeground(Color.WHITE);

        panelNorte.add(txtCantidad);
        panelNorte.add(txtCodigo);
        panelNorte.add(btnQuitar);
        
        modelo = new DefaultTableModel(new Object[]{"Código", "Descripción", "Cant.", "Precio Unit.", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return column == 2; }
        };
        
        tablaVenta = new JTable(modelo);
        tablaVenta.setRowHeight(30);
        
        JPanel panelSur = new JPanel(new GridLayout(1, 4, 20, 0));
        panelSur.setBackground(Color.WHITE);
        panelSur.setBorder(BorderFactory.createTitledBorder("Finalizar Venta"));

        lblTotal = new JLabel("TOTAL: $0.00");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 24));
        
        txtPago = new JTextField();
        txtPago.setBorder(BorderFactory.createTitledBorder("Paga con:"));
        
        txtCambio = new JTextField();
        txtCambio.setEditable(false);
        txtCambio.setBorder(BorderFactory.createTitledBorder("Cambio:"));
        
        JButton btnVender = new JButton("GENERAR VENTA");
        btnVender.setBackground(new Color(46, 204, 113));
        btnVender.setForeground(Color.WHITE);

        panelSur.add(lblTotal); panelSur.add(txtPago);
        panelSur.add(txtCambio); panelSur.add(btnVender);

        add(panelNorte, BorderLayout.NORTH);
        add(new JScrollPane(tablaVenta), BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);

        // Eventos
        txtCodigo.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) { if (e.getKeyCode() == KeyEvent.VK_ENTER) buscarYAgregar(); }
        });

        modelo.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 2) actualizarFila(e.getFirstRow());
        });

        txtPago.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { calcularCambio(); }
        });

        btnQuitar.addActionListener(e -> {
            int f = tablaVenta.getSelectedRow();
            if (f != -1) { modelo.removeRow(f); recalcularTotal(); }
        });

        btnVender.addActionListener(e -> finalizarVenta());
    }

    private void buscarYAgregar() {
        String cod = txtCodigo.getText().trim();
        if (cod.isEmpty()) return;
        try {
            double cant = Double.parseDouble(txtCantidad.getText());
            Producto p = proDao.buscarPorCodigo(cod);
            if (p != null) {
                for (int i = 0; i < tablaVenta.getRowCount(); i++) {
                    if (tablaVenta.getValueAt(i, 0).equals(cod)) {
                        double cAct = Double.parseDouble(tablaVenta.getValueAt(i, 2).toString());
                        modelo.setValueAt(cAct + cant, i, 2);
                        txtCodigo.setText(""); txtCantidad.setText("1");
                        return;
                    }
                }
                double sub = cant * p.getPrecioVenta();
                modelo.addRow(new Object[]{p.getCodigo(), p.getNombre(), cant, p.getPrecioVenta(), sub});
                txtCodigo.setText(""); txtCantidad.setText("1");
                recalcularTotal();
            } else { JOptionPane.showMessageDialog(this, "No encontrado"); }
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error en cantidad"); }
    }

    private void actualizarFila(int fila) {
        try {
            double cant = Double.parseDouble(modelo.getValueAt(fila, 2).toString());
            double pre = Double.parseDouble(modelo.getValueAt(fila, 3).toString());
            modelo.setValueAt(cant * pre, fila, 4);
            recalcularTotal();
        } catch (Exception e) { modelo.setValueAt(1.0, fila, 2); }
    }

    private void recalcularTotal() {
        totalPagar = 0;
        for (int i = 0; i < tablaVenta.getRowCount(); i++) 
            totalPagar += Double.parseDouble(tablaVenta.getValueAt(i, 4).toString());
        lblTotal.setText("TOTAL: $" + String.format("%.2f", totalPagar));
        calcularCambio();
    }

    private void calcularCambio() {
        try {
            double pago = Double.parseDouble(txtPago.getText());
            txtCambio.setText(String.format("%.2f", (pago - totalPagar)));
        } catch (Exception e) { txtCambio.setText("0.00"); }
    }

    private void finalizarVenta() {
        if (tablaVenta.getRowCount() == 0) return;
        try {
            double pago = Double.parseDouble(txtPago.getText());
            if (pago < totalPagar) { JOptionPane.showMessageDialog(this, "Pago insuficiente"); return; }

            // USAMOS EL ID DE LA SESIÓN AQUÍ
            int idUser = SesionUsuario.getUsuario().getIdUsuario();
            int idVenta = venDao.registrarVenta(totalPagar, pago, Double.parseDouble(txtCambio.getText()), idUser);
            
            if (idVenta != -1) {
                for (int i = 0; i < tablaVenta.getRowCount(); i++) {
                    String cod = tablaVenta.getValueAt(i, 0).toString();
                    double cant = Double.parseDouble(tablaVenta.getValueAt(i, 2).toString());
                    double pre = Double.parseDouble(tablaVenta.getValueAt(i, 3).toString());
                    double sub = Double.parseDouble(tablaVenta.getValueAt(i, 4).toString());
                    venDao.registrarDetalle(idVenta, cod, cant, pre, sub);
                }
                JOptionPane.showMessageDialog(this, "Venta Exitosa");
                limpiarTodo();
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
    }

    private void limpiarTodo() {
        modelo.setRowCount(0);
        txtPago.setText(""); txtCambio.setText("");
        txtCodigo.setText(""); txtCantidad.setText("1");
        recalcularTotal();
        txtCodigo.requestFocus();
    }
}