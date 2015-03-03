package javaxx;

public class Mensaje {

	private int numero ;
	private Cliente id;

	
	public Mensaje(Cliente dueno){
		
		numero = 0;
		id=dueno;
		
		
	}

	
	public synchronized void aumentarNumero(){
		numero ++;
		this.notify();
		System.out.println("Incrementó: "+numero);
	}
	
}
