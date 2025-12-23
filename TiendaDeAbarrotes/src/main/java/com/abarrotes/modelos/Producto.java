package com.abarrotes.modelos;

public class Producto {
    private String codigo;
    private String nombre;
    private double precioCompra;
    private double precioVenta;
    private double stock;
    private int idProveedor;

    public Producto() {}

    public Producto(String codigo, String nombre, double precioCompra, double precioVenta, double stock, int idProveedor) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.stock = stock;
        this.idProveedor = idProveedor;
    }

    // Getters y Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(double precioCompra) { this.precioCompra = precioCompra; }
    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }
    public double getStock() { return stock; }
    public void setStock(double stock) { this.stock = stock; }
    public int getIdProveedor() { return idProveedor; }
    public void setIdProveedor(int idProveedor) { this.idProveedor = idProveedor; }
}