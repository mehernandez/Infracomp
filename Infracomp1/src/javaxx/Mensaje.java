package javaxx;

public class Mensaje {

	private int numero ;
	private Cliente id;
	
	public Mensaje(Cliente dueño){
		
		numero = 0;
		id=dueño;
	}
	
	public void aumentarNumero(){
		numero ++;
//		System.out.println("Incrementó: "+numero);
	}
	
}
