package javaxx;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Buffer {

	//Variable que representa la cola de mensajes que faltan por responder
	private List<Mensaje> mensajes ;

	//Variable que representa la capacidad de mensajes que posee el buffer
	private int capacidad;

	// Variable que representa si se han acabado todas las operaciones o no
	public AtomicBoolean acabe;

	// Varibale que indica el número de clientes que van a emitir mensajes
	private int numClientes;


	// Método constructor de la clase Buffer
	public Buffer(int pCapacidad, int pNumClientes){


		capacidad = pCapacidad;

		mensajes = new ArrayList<Mensaje>();

		acabe=new AtomicBoolean(false);

		numClientes = pNumClientes;
	}

	// Método que agrega un nuevo mensaje a la cola de mensajes , en caso de que exista espacio
	public synchronized boolean agregar(Mensaje m){

		boolean resp = false;


		if(mensajes.size() <capacidad){
			mensajes.add(m);
			resp = true;
			System.out.println("se añadió un mensaje a la cola");

			this.notify();
		}

		return resp;
	}

	// Método que pide un mensaje de la cola para ser respondido , duerme al thread en caso de que la cola esté vacia 
	public synchronized Mensaje pedir(){
		if(!mensajes.isEmpty())
		{
			Mensaje mensajee=mensajes.remove(0);
			return mensajee;
		}
		else{
			try {
				this.wait();  

			} catch (Exception e) {
				System.out.println("Ocurrió un error en el wait : "+ e.getMessage());
			}
		}
		return null;
	}

	// Método que pregunta si el buffer ya acabó todas sus operaciones
	public synchronized boolean acabe(){
		return acabe.get();
	}

	// Método que usan los clientes para notificar que acabaron con sus mensajes y que se retiran , disminuyendo en 1 el numero de clientes en el buffer 
	// y verificando si el buffer acabó operaciones , en ese caso se cambia la variable acabe a true y se despiertan todos los threads dormidos para que 
	// finalizen su ejecución
	public synchronized void notificarSalida(){

		numClientes --;
		if(numClientes == 0){
			acabe.set(true);
			notifyAll();
			System.out.println("Se acabaron operaciones del Buffer");
			
			try {
				this.wait(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			notifyAll();
		}
		System.out.println("Salió un cliente y quedaron : "+ numClientes);

	}

}
