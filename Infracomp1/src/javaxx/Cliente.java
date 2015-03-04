package javaxx;

public class Cliente extends Thread{

	// Variable que representa el numero de mensajes que debe enviar
	private int numeroMensajes;
	
	// Variable que representa al buffer
	private Buffer buffer;


	//Método constructor de la clase Cliente 
	public Cliente(int pNumeroMensajes , Buffer pBuffer){


		buffer = pBuffer;
		numeroMensajes = pNumeroMensajes;
	}

	//Método que envía un mensaje a través del buffer y hace yield en caso de que no lo logre enviar
	public synchronized void enviar(Mensaje m){

		boolean ya = false;

		while(!ya){
			ya = m.enviarse();


			if(!ya){
				yield();
				System.out.println("Hice Yield");
			}


		}
	}

	// método run del thread que crea y envia todos los mensajes, y al final le notifica al buffer que salió del sistema
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
