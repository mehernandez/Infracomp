package javaxx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Estadistica {

	int numeroConexionesPerdidas;

	public Estadistica(){
		numeroConexionesPerdidas = 0;
	}

	public synchronized void aumentarConexionPerdida(){
		numeroConexionesPerdidas ++;
		this.guardar();
	}

	public void guardar(){

		try {
			Properties props = new Properties();
			props.setProperty("numeroConexionesPerdidas", ""+numeroConexionesPerdidas);
			File f = new File("./indicadores/resultados.properties");
			OutputStream out = new FileOutputStream( f );
			props.store(out, "Indicadores");
		}
		catch (Exception e ) {
			e.printStackTrace();
		}
	}
}
