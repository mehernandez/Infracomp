package javaxx;

import java.util.ArrayList;
import java.util.List;

public class Buffer {

	List<Mensaje> mensajes ;
	int capacidad;

	public Buffer(int num){

		
		capacidad = num;
		
		mensajes = new ArrayList<Mensaje>();
		
		
	}
	
	public boolean agregar(Mensaje m){
		
		boolean resp = false;
		
		
		if(mensajes.size() <capacidad){
			mensajes.add(m);
			resp = true;
			
//			try {
//				wait();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
			
		
		
		return resp;
	}
	
	public synchronized Mensaje pedir(){
		if(!mensajes.isEmpty()){
		   return mensajes.get(0);
		}
		return null;
	}
}
