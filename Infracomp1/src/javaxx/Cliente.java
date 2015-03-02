package javaxx;

public class Cliente extends Thread{

	private int numeroMensajes;
	private Buffer buffer;
	
	public Cliente(int num , Buffer b){
		super();
		
		buffer = b;
		numeroMensajes = num;
	}
	
	public synchronized void avisar()
	{
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (int i = 0; i < numeroMensajes; i++){
			
			Mensaje m = new Mensaje(this);
			
			boolean ya = false;
			
			while(!ya){
			ya = buffer.agregar(m);

			if(!ya){
				yield();
			}
			}
		}
	}
	
}
