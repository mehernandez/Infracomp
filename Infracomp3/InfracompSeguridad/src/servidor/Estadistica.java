package servidor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Estadistica {

	private long tiempoIntercambioLlave;
	private int numeroConexionesLlave;
	private long tiempoInfoPosicion;
	private int numeroConexionesPosicion;
	private int numeroConexionesPerdidas;
	private Properties props;
	
	
	public void guardar(){

		    try {
		        Properties props = new Properties();
		        props.setProperty("tiempoIntercambioLlave", ""+tiempoIntercambioLlave);
		        props.setProperty("tiempoInfoPosicion", ""+tiempoInfoPosicion);
		        props.setProperty("numeroConexionesPerdidas", ""+numeroConexionesPerdidas);
		        File f = new File("./indicadores/resultados.properties");
		        OutputStream out = new FileOutputStream( f );
		        props.store(out, "Indicadores");
		    }
		    catch (Exception e ) {
		        e.printStackTrace();
		    }
	}

	public Estadistica(){
		this.tiempoIntercambioLlave = 0;
		this.numeroConexionesLlave = 0;
		this.tiempoInfoPosicion =0;
		numeroConexionesPosicion = 0;
		numeroConexionesPerdidas = 0;	
	}

	public synchronized void agregrarTiempoIntercambioLlave(long t){
		numeroConexionesLlave ++;
		tiempoIntercambioLlave = (tiempoIntercambioLlave + t ) / numeroConexionesLlave;
		this.guardar();
	}
	public synchronized void agregrarTiempoInfoPosicion(long t){
		numeroConexionesPosicion ++;
		tiempoInfoPosicion = (tiempoInfoPosicion + t ) / numeroConexionesPosicion;
		this.guardar();
	}
	public synchronized void agregarConexionPerdida(){
		numeroConexionesPerdidas ++;
		this.guardar();
	}

	public long getTiempoIntercambioLlave() {
		return tiempoIntercambioLlave;
	}

	public int getNumeroConexionesLlave() {
		return numeroConexionesLlave;
	}

	public long getTiempoInfoPosicion() {
		return tiempoInfoPosicion;
	}

	public int getNumeroConexionesPosicion() {
		return numeroConexionesPosicion;
	}

	public int getNumeroConexionesPerdidas() {
		return numeroConexionesPerdidas;
	}

	public Properties getProps() {
		return props;
	}
	
	
	
	


}
