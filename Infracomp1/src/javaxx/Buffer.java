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
		
		if(mensajes.size() <capacidad){
			mensajes.add(m);
			return true;
		}
		return false;
	}
	
	public Mensaje pedir(){
		if(!mensajes.isEmpty()){
		   return mensajes.get(0);
		}
		return null;
	}
}
