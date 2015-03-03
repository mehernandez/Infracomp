package javaxx;

import java.util.ArrayList;
import java.util.List;

public class Buffer {

	List<Mensaje> mensajes ;
	int capacidad;
	boolean acabe;

	public Buffer(int num){

		
		capacidad = num;
		
		mensajes = new ArrayList<Mensaje>();
		
		acabe=false;
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
			Mensaje ac=mensajes.get(0);
			mensajes.remove(0);
		   return ac;
		}
		else{
			try {
				this.wait();
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
}
