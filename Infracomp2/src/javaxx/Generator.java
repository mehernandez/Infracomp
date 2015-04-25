package javaxx;

import uniandes.gload.core.LoadGenerator;
import uniandes.gload.core.Task;
import uniandes.gload.examples.clientserver.generator.ClientServerTask;

public class Generator {

	private LoadGenerator generator;

	public Generator(){
		Task work = createTask();
		int numberOfTasks = 100;
		int gapBetweenTasks = 1000;
		generator = new LoadGenerator("Client-Server load test", numberOfTasks ,work , gapBetweenTasks);
		generator.generate();
	}
	
	public Task createTask(){
		return new ClientServerTask(){
			
			@Override
			public void execute() {
				Cliente c = new Cliente();
//				System.out.println("hola");
			}
		};
	}
	
	public static void main(String[] args) {
		
		@SuppressWarnings("unused")
		Generator gen = new Generator();
	}

}
