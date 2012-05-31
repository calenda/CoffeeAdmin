package com.calenda.coffeeadmin.model;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "Comanda")
public class Comanda {
	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField (canBeNull=false, dataType = DataType.DATE_STRING)
	private Date fecha;
	
	@DatabaseField (canBeNull=false)
	private Float total;
	
	Comanda(){
		
	}
	
	public Comanda(Date fecha, Float total) {
		this.fecha = fecha;
		this.total = total;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(id).append(" "+fecha).append(" $"+total);
		return sb.toString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Float getTotal() {
		return total;
	}

	public void setTotal(Float total) {
		this.total = total;
	}
}
