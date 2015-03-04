package javaxx;

import java.util.ArrayList;
import java.util.List;

public class Buffer {

	private List<Mensaje> mensajes ;
	private int capacidad;
	private boolean acabe;
	private int numClientes;
	

	public Buffer(int num, int numC){

		
		capacidad = num;
		
		mensajes = new ArrayList<Mensaje>();
		
		acabe=false;
		
		numClientes = numC;
	}
	
	public synchronized boolean agregar(Mensaje m){
		
		boolean resp = false;
		
		
		if(mensajes.size() <capacidad){
			mensajes.add(m);
			resp = true;
//			System.out.println("se añadió");

			this.notify();
		}
			
		
		
		return resp;
	}
	
	public synchronized Mensaje pedir(){
		if(!mensajes.isEmpty())
		{
			Mensaje ac=mensajes.remove(0);
		   return ac;
		}
		else{
			try {
				this.wait();
//				acabe=true;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public synchronized boolean acabe(){
		return acabe;
	}
	
	public synchronized void notificarSalida(){
		
		numClientes --;
		if(numClientes == 0){
			acabe = true;
		}
		else{
			acabe=false;
		}
		
	}
	
}
