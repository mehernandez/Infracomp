package javaxx;

public class Mensaje {

	// Variable que representa si el mensaje ha sido contestado o no  (0-no, 1-si)
	private int numero ;

	// Variable que representa al cliente que emitió el mensaje
	private Cliente id;

	// Variable que representa al buffer
	private Buffer buffer;

	//Método constructor de la clase Mensaje
	public Mensaje(Cliente dueno, Buffer pBuffer){

		numero = 0;
		id=dueno;
		buffer = pBuffer;

	}

	//Método que contesta el mensaje, consiste en aumentar en 1 la variable numero y despertar al cliente que se encuentra dormido
	public synchronized void aumentarNumero(){
		numero ++;
		this.notify();
		System.out.println("contestado Mensaje de: "+id);
	}

	//Método que envia el mensaje al buffer y se duerme para que se duerma el cliente, hasta que lo contesten y lo despierten
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
