package javaxx;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Servidor {


public Servidor(int num){
	
	for(int i = 0; i < num ; i ++){
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
			// implementar	
			}
			
		});
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
			System.out.println("No se puede leer el archivo X(");
		}
		
		// inicializar servidor
		
		Servidor servidor = new Servidor(Integer.parseInt(datos.getProperty("numeroThreads")));

		// inicializar buffer
		
		Buffer buffer = new Buffer();
		
		//inicializar los clientes 
		
		int clientes = Integer.parseInt(datos.getProperty("numeroClientes"));
		for (int i =0; i < clientes ; i ++){
			
			Cliente c = new Cliente(Integer.parseInt(datos.getProperty("numeroMensajes"+i)));
			
			c.start();
		}
		
		
	}



}