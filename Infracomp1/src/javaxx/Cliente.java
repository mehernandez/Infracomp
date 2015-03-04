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
		ya = buffer.agregar(m);

		if(!ya){
			yield();
		}
		else{
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (int i = 0; i < numeroMensajes; i++){
			
			Mensaje m = new Mensaje(this);
			
		   enviar(m);
			
		}
		buffer.notificarSalida();
	}
	
}
