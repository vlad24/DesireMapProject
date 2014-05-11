import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import desireMapApplicationPackage.desireInstrumentPackage.DesireInstrument;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;


public class DesireMapProgram {
	private static final int portNumber = 9252;
	private static ServerSocket listeningSocket;
	private static int maxUserLoad = 4;
	//--
	public static void main(String[] args)
	{
		System.out.println("# Program is on.\n");	
		try {
			DesireInstrument.prepareInstrument();
			listeningSocket = new ServerSocket(portNumber);
			Executor poolExecutor = Executors.newFixedThreadPool(maxUserLoad);
			while(true){
				System.out.println("# Waiting for a new client...\n");
				Socket interactiveSocket = listeningSocket.accept();
				System.out.println("# A new user has come!...\n");
				poolExecutor.execute(new DesireThread(interactiveSocket));
				//new Thread(new DesireThread(interactiveSocket)).start(); // it will interrupt when user leaves or wants
			}
		} catch (IOException error){
			System.out.println(error.getMessage());
		}
		finally{
			try {
				DesireInstrument.throwAwayAndCleanBase();
				listeningSocket.close();
				System.out.println("# Server socket is successfully closed\n");
			} 
			catch (IOException error) {
				System.out.println("# Listening socket can't be closed. DANGER!\n");
			}
		}
		System.out.println("# Program is successfully over.\n");
	}
	
}
