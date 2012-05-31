package com.calenda.coffeeadmin.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "Productos")
public class Producto {
	
	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField (canBeNull=false)
	private String producto;
	
	@DatabaseField (canBeNull=false)
	private Float precio;
	
	Producto(){
		
	}
	
	public Producto(String producto, Float precio){
		this.producto = producto;
		this.precio = precio;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(producto).append(" -  $ ").append(precio);
		return sb.toString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public Float getPrecio() {
		return precio;
	}

	public void setPrecio(Float precio) {
		this.precio = precio;
	}

}
