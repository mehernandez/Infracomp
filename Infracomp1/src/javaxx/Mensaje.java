package javaxx;

public class Mensaje {

	private int numero ;
	private Cliente id;
	private Buffer buffer;

	
	public Mensaje(Cliente dueno, Buffer b){
		
		numero = 0;
		id=dueno;
		buffer = b;
		
	}

	
	public synchronized void aumentarNumero(){
		numero ++;
		this.notify();
//		System.out.println("Mensaje de: "+id);
	}
	
	public synchronized boolean enviarse(){
		
		boolean ya =  buffer.agregar(this);
		
		if(ya){
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ya;
		
	}
	
}
