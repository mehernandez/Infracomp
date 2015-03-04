package javaxx;

public class Cliente extends Thread{

	private int numeroMensajes;
	private Buffer buffer;
	
	public Cliente(int num , Buffer b){
		super();
		
		buffer = b;
		numeroMensajes = num;
	}
	
	public synchronized void enviar(Mensaje m){

        boolean ya = false;
		
		while(!ya){
		ya = m.enviarse();
		System.out.println(ya);

		if(!ya){
			yield();
		}
	
		
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (int i = 0; i < numeroMensajes; i++){
			
			Mensaje m = new Mensaje(this, buffer);
			
		   enviar(m);
			
		}
		buffer.notificarSalida();
	}
	
}
