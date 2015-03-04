package javaxx;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Buffer {

	private List<Mensaje> mensajes ;
	private int capacidad;
	public AtomicBoolean acabe;
	private int numClientes;
	

	public Buffer(int num, int numC){

		
		capacidad = num;
		
		mensajes = new ArrayList<Mensaje>();
		
		acabe=new AtomicBoolean(false);
		
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
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public synchronized boolean acabe(){
		return acabe.get();
	}
	
	public synchronized void notificarSalida(){
		
		numClientes --;
		if(numClientes == 0){
			acabe.set(true);
			notifyAll();
		}
		System.out.println(numClientes);
		System.out.println(acabe);
	}
	
}
