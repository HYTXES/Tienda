package com.abarrotes.modelos;

public class Proveedor {
    private int idProveedor;
    private String empresa;
    private String contacto;
    private String telefono;
    private String diaVisita;

    public Proveedor() {}

    public Proveedor(int idProveedor, String empresa, String contacto, String telefono, String diaVisita) {
        this.idProveedor = idProveedor;
        this.empresa = empresa;
        this.contacto = contacto;
        this.telefono = telefono;
        this.diaVisita = diaVisita;
    }

    // Getters y Setters
    public int getIdProveedor() { return idProveedor; }
    public void setIdProveedor(int idProveedor) { this.idProveedor = idProveedor; }

    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }

    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDiaVisita() { return diaVisita; }
    public void setDiaVisita(String diaVisita) { this.diaVisita = diaVisita; }
    
    // Sobreescribimos toString para que se vea bien en los ComboBox (menús desplegables)
    @Override
    public String toString() {
        return empresa;
    }
}