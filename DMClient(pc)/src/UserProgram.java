import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;

import desireMapApplicationPackage.actionQueryObjectPackage.*;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireContentPackage.Coordinates;
import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.desireContentPackage.DesireContentDating;
import desireMapApplicationPackage.desireContentPackage.DesireContentSport;
import desireMapApplicationPackage.messageSystemPackage.Message;
import desireMapApplicationPackage.outputArchitecturePackage.DesireSet;
import desireMapApplicationPackage.outputArchitecturePackage.MessageSet;
import desireMapApplicationPackage.outputArchitecturePackage.SatisfySet;
import desireMapApplicationPackage.quadtree.DataQuadTreeNode;
import desireMapApplicationPackage.userDataPackage.LoginData;
import desireMapApplicationPackage.userDataPackage.MainData;
import desireMapApplicationPackage.userDataPackage.RegistrationData;



public class UserProgram{
	
	public static Scanner scanner = new Scanner(System.in);
	public static String currentUser = "?";
	public static boolean loggedIn = false;
	
	public static int askInt(String name){
		System.out.println("Enter " + name + ": ");
		int s = scanner.nextInt();
		return s;
	}
	
	public static String askString(String name){
		System.out.println("Enter " + name + ": ");
		String s = scanner.next();
		return s;
	}
	
	public static double askDouble(String name){
		System.out.println("Enter " + name + ": ");
		double s = scanner.nextDouble();
		return s;
	}
		
	public static void main(String[] args) throws Exception{
		InetAddress address = InetAddress.getByName("localhost");
		Socket clientSocket = new Socket(address, 9252);
		ObjectOutputStream outs = new ObjectOutputStream(clientSocket.getOutputStream());
		outs.flush();
		ObjectInputStream ins = new ObjectInputStream(clientSocket.getInputStream());
		int answer = 1;
		System.out.println("# 1 - register, 2 - log in, 3 - add desire\n4 - show desires, 5 - show info," +
				" 6 - find satisfiers, 7 - delete desires from category\n 8 - send message 9 - deliver messages");
		while (answer != 0){
			answer = askInt("action");
			switch(answer){
			case(0):{
				currentUser = "?";
				loggedIn = false;
				outs.writeObject(new ExitPack());
				outs.flush();
				System.out.println(ins.readBoolean());
				break;
			}
			case(1):{
				System.out.println("# REGISTERING");
				String login = askString("login");
				String password = askString("password");
				String name = askString("name");
				char sex = askString("sex").charAt(0);
				String birth = askString("birth");
				RegistrationPack regPack = new RegistrationPack(new RegistrationData(login, password, name, sex, birth));
				outs.writeObject(regPack);
				outs.flush();
				System.out.println("! Sent");
				boolean serverAnswer =  ins.readBoolean();
				loggedIn = serverAnswer;
				System.out.println("Online :" + serverAnswer);
				if (serverAnswer){
					currentUser = login;
				}
				break;
			}
			case(2):{
				System.out.println("# LOGGING IN");
				String login = askString("login");
				String password = askString("password");
				LoginPack logPack = new LoginPack(new LoginData(login, password));
				outs.writeObject(logPack);
				outs.flush();
				System.out.println("! Sent");
				Boolean serverAnswer =  ins.readBoolean();
				loggedIn = serverAnswer;
				System.out.println("Online :" + serverAnswer);
				if (serverAnswer){
					currentUser = login;
				}
				break;
			}
			case(3):{
				if (loggedIn){
					System.out.println("# ADDING A DESIRE");
					int catCode= askInt("category code 0 1");
					String desDes = askString("desire description");
					//String tag = askString("tag");
					double lat = askDouble("latitude");
					double lon = askDouble("longitude");
					Coordinates coord = new Coordinates(lat, lon);
					switch (catCode){
					case (CodesMaster.Categories.SportCode):
					{
						String sport = askString("sport");
						int advantages = askInt("advantages");
						outs.writeObject(new AddPack(new DesireContentSport(currentUser, 0, desDes, coord, sport, advantages), "10"));
						System.out.println("Success : id = " + ins.readInt());
						break;
					}
					case (CodesMaster.Categories.DatingCode):
					{
						char pSex = askString("partnerSex").charAt(0);
						int age = askInt("age");
						outs.writeObject(new AddPack(new DesireContentDating(currentUser, 0, desDes, coord, pSex, age), "11"));
						System.out.println("Success : " + ins.readInt());
						break;
					}
					}
				}
				break;
			}
			case(4):{
				System.out.println("# SHOWING DESIRES OF SOME CATEGORY");
				int catCode = askInt("category code 0 1");
				outs.writeObject(new ShowPersonalDesiresPack(catCode));
				System.out.println("! Sent");
				try{
					if (ins.readBoolean()){
						System.out.println(" | Success");
						DesireSet set = (DesireSet)ins.readObject();
						set.dTree.print();
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
				outs.writeObject(new ShowInfoPack());
				System.out.println("! Sent\n");
				if (ins.readBoolean()){
					try{
						System.out.println(" | Success");
						MainData data = (MainData)ins.readObject();
						System.out.println(data.login + " " + data.name + " " + data.sex + " " + data.birth + " " + data.rating);
						System.out.println(" | That's all");
					}
					catch(IOException error){
						error.getMessage();
					}
				}
				else {
					System.out.println("-Fail");
				}
				break;
			}
			case (6) :{
				System.out.println("# Finding satisfiers");
				HashSet<String> tiles = new HashSet<String>();
				int dID = askInt("desireID");
				tiles.add("10");
				tiles.add("11");
				tiles.add("12");
				outs.writeObject(new SatisfyPack(dID, tiles));
				System.out.println("! Sent\n");
				if (ins.readBoolean()){
					try{
						System.out.println(" | Success");
						SatisfySet set = (SatisfySet)ins.readObject();
						if (set != null){
							set.dTree.print();
							System.out.println(" | That's all");
						}
						else{
							System.out.println("Empty set");
						}
					}
					catch(IOException error){
						error.getMessage();
					}
				}
				else {
					System.out.println("-Fail");
				}
				break;
			}
			case(7):{
				if (loggedIn){
					System.out.println("# DELETING DESIRES");
					String setString = askString("Set of desiresID : ");
					DeletePack dPack = new DeletePack(setString); 
					outs.writeObject(dPack);
					if (ins.readBoolean()){
						System.out.println("+Deleted");
					}
					else{
						System.out.println("-Fail");
					}
				}
				else{
					System.out.println("- Log in, please");
				}
				break;
			}
			case(8):{
				if (loggedIn){
					String mess = askString("message");
					String whoTo = askString("receiver");
					Message message = new Message(whoTo, currentUser, mess);
					MessageSendPack pack = new MessageSendPack(message);
					outs.writeObject(pack);
					outs.flush();
				}
				break;
			}
			case(9):{
				MessageDeliverPack pack = new MessageDeliverPack();
				outs.writeObject(pack);
				outs.flush();
				System.out.println("+Deliver query has been posted");
				MessageSet set = (MessageSet) ins.readObject();
				System.out.println("+MessageSet has been got");
				for(Message m : set.messages){
					System.out.println("| From: " + m.sender + " | To: " + m.receiver + "\nText :" + m.text);
				}
				break;
			}
			}
		}
		ins.close();
		outs.close();
		scanner.close();
		clientSocket.close();
		System.out.println("# STREAMS AND THE SOCKET HAVE BEEN CLOSED");
	}

}
