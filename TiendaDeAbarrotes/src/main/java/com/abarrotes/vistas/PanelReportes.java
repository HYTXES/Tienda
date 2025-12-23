package com.abarrotes.vistas;

import com.abarrotes.controladores.VentaDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class PanelReportes extends JPanel {
    private JTable tabla;
    private DefaultTableModel modelo;
    private JLabel lblTotalVenta, lblTotalGanancia;
    private JTextField txtFiltroCajero;
    private VentaDAO vDao = new VentaDAO();

    public PanelReportes() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- NORTE: FILTRO ---
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltro.setBackground(Color.WHITE);
        txtFiltroCajero = new JTextField(20);
        txtFiltroCajero.setBorder(BorderFactory.createTitledBorder("Filtrar por Cajero:"));
        panelFiltro.add(txtFiltroCajero);
        add(panelFiltro, BorderLayout.NORTH);

        // --- CENTRO: TABLA ---
        modelo = new DefaultTableModel(new Object[]{"ID", "Producto", "Cant.", "Precio", "Ganancia", "Cajero"}, 0);
        tabla = new JTable(modelo);
        tabla.setRowHeight(25);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // --- SUR: TOTALES ---
        JPanel panelTotales = new JPanel(new GridLayout(1, 2, 20, 0));
        panelTotales.setBackground(new Color(245, 245, 245));
        panelTotales.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        lblTotalVenta = new JLabel("Venta Total: $0.00");
        lblTotalVenta.setFont(new Font("SansSerif", Font.BOLD, 18));
        
        lblTotalGanancia = new JLabel("Ganancia Real: $0.00");
        lblTotalGanancia.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTotalGanancia.setForeground(new Color(39, 174, 96));

        panelTotales.add(lblTotalVenta);
        panelTotales.add(lblTotalGanancia);
        add(panelTotales, BorderLayout.SOUTH);

        // --- EVENTO FILTRO ---
        txtFiltroCajero.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                cargarReporte(txtFiltroCajero.getText());
            }
        });

        cargarReporte(""); // Cargar todo al inicio
    }

    private void cargarReporte(String filtro) {
        modelo.setRowCount(0);
        List<Object[]> datos = vDao.obtenerReporteDiario(filtro);
        double sumVenta = 0;
        double sumGanancia = 0;

        for (Object[] fila : datos) {
            modelo.addRow(fila);
            double cant = Double.parseDouble(fila[2].toString());
            double precio = Double.parseDouble(fila[3].toString());
            double ganancia = Double.parseDouble(fila[4].toString());

            sumVenta += (cant * precio);
            sumGanancia += ganancia;
        }

        lblTotalVenta.setText("Venta Total: $" + String.format("%.2f", sumVenta));
        lblTotalGanancia.setText("Ganancia Real: $" + String.format("%.2f", sumGanancia));
    }
}