package servidor;

public class Estadistica {

	private long tiempoIntercambioLlave;
	private long numeroConexionesLlave;
	
	public Estadistica(){
		this.tiempoIntercambioLlave = 0;
	this.numeroConexionesLlave = 0;
	}
	
	public synchronized void agregrarTiempoIntercambioLlave(long t){
		numeroConexionesLlave ++;
		tiempoIntercambioLlave = (tiempoIntercambioLlave + t ) / numeroConexionesLlave;
		System.out.println("El tiempo va en " +tiempoIntercambioLlave + 
				"\n y el numero conexiones en "+ numeroConexionesLlave);
	}
	
}
