import java.io.*;
import java.net.*;

import testsPackage.TestMaster;
import desireInstrumentPackage.DesireInstrument;
import desireThreadPackage.DesireThread;


public class DesireMapProgram {
	int y = 0;
	private static final int portNumber = 9252;
	private static ServerSocket listeningSocket;
	//--
	public static void main(String[] args)
	{
		TestMaster.jExec();
		System.out.println("# Program is on.\n");			
		try {
			listeningSocket = new ServerSocket(portNumber);
			while(true){
				System.out.println("# Waiting for a new client...\n");
				Socket interactiveSocket = listeningSocket.accept();
				System.out.println("# A new user has come!...\n");	
				DesireThread threadForUser = new DesireThread(interactiveSocket);
				System.out.println("# Thread is made\n");
				threadForUser.start(); // it will interrupt when user leaves or wants
			}
		} catch (IOException error){
			System.out.println(error.getMessage());
		}
		finally{
			try {
				DesireInstrument.turnOffTheBase();
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
