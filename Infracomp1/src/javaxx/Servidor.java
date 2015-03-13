package javaxx;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Servidor {

	// Variable que representa al Buffer
Buffer buffer;
	
//Método constructor del servidor 
public Servidor(int numeroThreads, Buffer pBuffer){
	
	buffer = pBuffer;
	
	// Se crean todos los threads y se corren para que comiencen a atender mensajes
	for(int i = 0; i < numeroThreads ; i ++){
		System.out.println("Se creó el Thread: "+i);
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {

				
				while(!buffer.acabe.get())
				{
					Mensaje temp=buffer.pedir();
					if(temp!=null)
					{
						temp.aumentarNumero();
					}
				}
				System.out.println("Acabé mi labor como Thread");
			}
			
		});
		t.start();
	}
	
	
}



	public static void main(String[] args) {
		//leer archivo
		File arch = new File("./properties/config1.properties");
		Properties datos = new Properties( );

		try
		{
			FileInputStream in = new FileInputStream( arch );
			datos.load( in );
			
			//System.out.println("clientes: "+datos.getProperty("numeroClientes"));
			
			in.close( );
		}
		catch( Exception e )
		{
			System.out.println("No se puede leer el archivo :"+arch.getAbsolutePath());
		}
		
		// inicializar buffer
		
		Buffer buffer = new Buffer(Integer.parseInt(datos.getProperty("capacidadBuffer")),Integer.parseInt(datos.getProperty("numeroClientes")));
		
		// inicializar servidor
		
		Servidor servidor = new Servidor(Integer.parseInt(datos.getProperty("numeroThreads")),buffer);


		
		//inicializar los clientes 
		
		int clientes = Integer.parseInt(datos.getProperty("numeroClientes"));
		for (int i =0; i < clientes ; i ++){
			
			Cliente c = new Cliente(Integer.parseInt(datos.getProperty("numeroMensajes"+i)),buffer);
			
			c.start();
		}
		
		
	}



}