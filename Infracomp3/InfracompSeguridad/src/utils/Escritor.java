package utils;

import java.io.File; 
import java.io.IOException;
import java.util.Date; 

import jxl.*; 
import jxl.format.Colour;
import jxl.write.*;
import jxl.write.Number;

public class Escritor 
{
	public static final String FILEPATH="/Users/henryfvargas/downloads/prueba.xls";
	
	private double dato;
	private int fila;
	private int columna;
	
	/**
	 * Crea un escritor, llamar a escribir() para crear la entrada
	 * @param mensaje el numero a escrbir
	 * @param fila
	 * @param columna
	 * (0,0) corresponde a A1, (1,0) B1 y (0,1) A2
	 */
	public Escritor(double mensaje, int fila, int columna)
	{
		this.dato=mensaje;
		this.fila=fila;
		this.columna=columna;
	}
	
	public void escribir()
	{
		try 
		{
			WritableWorkbook ww= Workbook.createWorkbook(new File(FILEPATH));
			WritableSheet sheet=ww.createSheet("Hoja 1", 0);
			Number number=new Number(columna, fila, dato);
			sheet.addCell(number);
			ww.write();
			ww.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] arg0)
	{
		Escritor e=new Escritor(2.3, 0, 0);
		e.escribir();
	}
}
