import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;



public class UserProgram{
	
	private static void printTable(ArrayList<ArrayList<String>> table){
		for (int i = 0 ; i < table.size(); i++){
			for (int j = 0 ; j < table.get(i).size() ; j++){
				System.out.print(table.get(i).get(j) + " ");
			}
			System.out.println("");
		}
	}
	
	public static void main(String[] args) throws Exception{
		InetAddress address = InetAddress.getByName("localhost");
		Socket clientSocket = new Socket(address, 9252);
		
		Scanner scanner = new Scanner(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
		ObjectInputStream ins = new ObjectInputStream(clientSocket.getInputStream());
		
		int answer = 1;
		System.out.println("# 1 - register, 2 - log in, 3 - add desire\n4 - show desires, 5 - show info, 6 - find satisfiers, 7 - delete desires from category");
		while (answer != 0){
			System.out.println("What to do : ");
			answer = scanner.nextInt();
			switch(answer){
			case(0):{
				out.println("E");
				System.out.println(ins.readBoolean());
				break;
			}
			case(1):{
				System.out.println("# REGISTERING");
				System.out.println("Enter login : ");
				String login = scanner.next();
				System.out.println("Enter password : ");
				String password = scanner.next();
				System.out.println("Enter name : ");
				String name = scanner.next();
				System.out.println("Enter sex : ");
				String sex = scanner.next();
				System.out.println("Enter birthdate : ");
				String birth = scanner.next();
				String toSend = "R" + login + "/" + password + "/" + name + "/" + sex + "/" + birth;
				System.out.println("To send : " + toSend);
				out.println(toSend);
				System.out.println("The string is sent");
				System.out.println("Online :" + ins.readBoolean());
				break;
			}
			case(2):{
				System.out.println("# LOGGING IN");
				System.out.println("Enter login : ");
				String login = scanner.next();
				System.out.println("Enter password : ");
				String password = scanner.next();
				String toSend = "L" + login + "/" + password;
				System.out.println("To send : " + toSend);
				out.println(toSend);
				System.out.println("The string is sent");
				System.out.println("Online : " + ins.readBoolean());
				break;
			}
			case(3):{
				System.out.println("# ADDING A DESIRE");
				System.out.println("Enter your category :");
				String category = scanner.next();
				System.out.println("Enter your desire :");
				String desireString = scanner.next();
				System.out.println("Enter your tag :");
				String tag = scanner.next();
				System.out.println("Enter your latitude :");
				String lat = scanner.next();
				System.out.println("Enter your longitude :");
				String lon = scanner.next();
				String toSend = "A" + category + "/" + desireString + "/" + tag + "/" + lat + "/" + lon;
				System.out.println("To send:"  + toSend);
				out.println(toSend);
				System.out.println("The string is sent");
				System.out.println("Success: " + ins.readBoolean());
				break;
			}
			case(4):{
				System.out.println("# SHOWING DESIRES OF SOME CATEGORY");
				System.out.println("Enter your category :");
				String category = scanner.next();
				String toSend = "S" + category;
				System.out.println("To send: " + toSend);
				out.println(toSend);
				System.out.println("The string is sent");
				try{
					if (ins.readBoolean()){
						System.out.println(" | Success");
						ArrayList<ArrayList<String>> set = (ArrayList<ArrayList<String>>) ins.readObject();
						printTable(set);
						System.out.println(" | That's all");
					}
					else{
						System.out.println(" | Not Success");
					}
				} 
				catch(IOException error){
					error.getMessage();
				}
				break;
			}
			case(5):{
				System.out.println("# SHOWING YOUR PERSONAL INFO");
				System.out.println("To send: I");
				out.println("I");
				System.out.println("The string is sent\n");
				if (ins.readBoolean()){
					try{
						System.out.println(" | Success");
						ArrayList<ArrayList<String>> table = (ArrayList<ArrayList<String>>) ins.readObject();
						printTable(table);
						System.out.println(" | That's all");
					}
					catch(IOException error){
						error.getMessage();
					}
				}
				else {
					System.out.println("Fail");
				}
				break;
			}
			case (6) :{
				System.out.println("# SEARCHING FOR YOUR DESIRE SATISFIERS");
				System.out.println("Enter your category :");
				String category = scanner.next();
				System.out.println("Enter your desire :");
				String line = scanner.next();
				System.out.println("Enter your tag :");
				String tag = scanner.next();
				System.out.println("Enter your latitude :");
				String lat = scanner.next();
				System.out.println("Enter your longitude :");
				String lon = scanner.next();
				System.out.println("Enter your radius :");
				String rad = scanner.next();
				String toSend = "G" + category + "/" + line + "/" + tag + "/" + lat + "/" + lon + "/" + rad;
				System.out.println("To send:" + toSend);
				out.println(toSend);
				System.out.println("The string is sent");
				System.out.print("First bool ( adding ) :");
				System.out.println(ins.readBoolean());
				try{
					if (ins.readBoolean()){
					System.out.println(" | Success");
					ArrayList<ArrayList<String>> table = (ArrayList<ArrayList<String>>) ins.readObject();
					printTable(table);
					System.out.println(" | That's all");
					}
					else{
						System.out.println(" | Not Success");
					}
				}
				catch(IOException error){
					error.getMessage();
				}
				break;
			}
			case(7):{
				System.out.println("# DELETING");
				System.out.println("Enter your category :");
				String category = scanner.next();
				String toSend = "D" + category;
				System.out.println("To send:" + toSend);
				out.println(toSend);
				System.out.println("The string is sent");
				System.out.println(ins.readBoolean());
				break;
			}
			default:{
				answer = 0;
			}
			}
		}
		ins.close();
		out.close();
		scanner.close();
		clientSocket.close();
		System.out.println("# STREAMS AND THE SOCKET HAVE BEEN CLOSED");
	}
}
