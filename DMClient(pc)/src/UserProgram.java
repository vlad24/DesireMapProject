import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.sql.ResultSet;



public class UserProgram{
	public static void main(String[] args) throws Exception{
		InetAddress address = InetAddress.getByName("localhost");
		Socket clientSocket = new Socket(address, 9252);
		
		Scanner scanner = new Scanner(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
		ObjectInputStream ins = new ObjectInputStream(clientSocket.getInputStream());
		
		int answer = 1;
		System.out.println("# 1 - register, 2 - log in, 3 - add desire, 4 - show desires, 5 - show info");
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
				System.out.println("Register\n Enter login : ");
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
				System.out.println("Enter your desire :");
				String line = scanner.next();
				System.out.println("Enter your tag :");
				String tag = scanner.next();
				System.out.println("Enter your latitude :");
				String lat = scanner.next();
				System.out.println("Enter your longitude :");
				String lon = scanner.next();
				String toSend = "A" + line + "/" + tag + "/" + lat + "/" + lon;
				System.out.println("To send:"  + toSend);
				out.println(toSend);
				System.out.println("The string is sent");
				System.out.println("Success: " + ins.readBoolean());
				break;
			}
			case(4):{
				System.out.println("To send: S");
				out.println("S");
				System.out.println("The string is sent");
				try{
					if (ins.readBoolean()){
					System.out.println(" | Success");
					ArrayList<String> set = (ArrayList<String>) ins.readObject();
					for (int i = 0; i < set.size(); i++){
						String currentDesireString = set.get(i);
						System.out.println(currentDesireString);
					}
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
				System.out.println("To send: I");
				out.println("I");
				System.out.println("The string is sent\n");
				if (ins.readBoolean()){
					try{
						System.out.println(" | Success");
						ResultSet set = (ResultSet) ins.readObject();
						while (set.next()){
									String name = set.getString("name");
									String sex  = set.getString("sex");
									String birth = set.getString("birth");
									System.out.println(name + " "  + sex + " " + birth);
							}
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
			default:{
				answer = 0;
			}
			}
		}
		ins.close();
		out.close();
		scanner.close();
		clientSocket.close();
	}
}
