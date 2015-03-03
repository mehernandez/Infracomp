package javaxx;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Servidor {

Buffer buffer;
	
public Servidor(int num, Buffer b){
	
	buffer = b;
	
	for(int i = 0; i < num ; i ++){
		System.out.println("thread: "+i);
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
			// implementar
				
				while(buffer.acabe())
				{
					Mensaje temp=buffer.pedir();
					if(temp!=null)
					{
						temp.aumentarNumero();
//						System.out.println("se aumento");
					}
				}
			}
			
		});
		t.start();
	}
	
}



	public static void main(String[] args) {
		//leer archivo
		File arch = new File("./properties/config.properties");
		Properties datos = new Properties( );

		try
		{
			FileInputStream in = new FileInputStream( arch );
			datos.load( in );
			
			System.out.println(datos.getProperty("numeroClientes"));
			
			in.close( );
		}
		catch( Exception e )
		{
			System.out.println("No se puede leer el archivo :"+arch.getAbsolutePath());
		}
		
		// inicializar buffer
		
		Buffer buffer = new Buffer(Integer.parseInt(datos.getProperty("capacidadBuffer")));
		
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