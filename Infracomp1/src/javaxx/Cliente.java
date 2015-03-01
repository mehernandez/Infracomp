package javaxx;

public class Cliente extends Thread{

	private int numeroMensajes;
	
	public Cliente(int num ){
		super();
		
		numeroMensajes = num;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (int i = 0; i < numeroMensajes; i++){
			
			Mensaje m = new Mensaje();
			
			// falta contactar al buffer
			
		}
	}
	
}
