package com.calenda.coffeeadmin.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "Productos")
public class Producto {
	
	@DatabaseField(generatedId = true)
	int id;
	
	@DatabaseField (canBeNull=false)
	String producto;
	
	@DatabaseField (canBeNull=false)
	Float precio;
	
	Producto(){
		
	}
	
	public Producto(String producto, Float precio){
		this.producto = producto;
		this.precio = precio;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(producto).append(" - $").append(precio);
		return sb.toString();
	}

}
