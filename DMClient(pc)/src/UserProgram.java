import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.actionQueryObjectPackage.DeletePack;
import desireMapApplicationPackage.actionQueryObjectPackage.ExitPack;
import desireMapApplicationPackage.actionQueryObjectPackage.LikePack;
import desireMapApplicationPackage.actionQueryObjectPackage.LoginPack;
import desireMapApplicationPackage.actionQueryObjectPackage.MessageSendPack;
import desireMapApplicationPackage.actionQueryObjectPackage.RegistrationPack;
import desireMapApplicationPackage.actionQueryObjectPackage.SatisfyPack;
import desireMapApplicationPackage.actionQueryObjectPackage.ShowInfoPack;
import desireMapApplicationPackage.actionQueryObjectPackage.ShowPersonalDesiresPack;
import desireMapApplicationPackage.actionQueryObjectPackage.TilesPack;
import desireMapApplicationPackage.actionQueryObjectPackage.UserChatHistoryPack;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireContentPackage.Coordinates;
import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.desireContentPackage.DesireContentDating;
import desireMapApplicationPackage.desireContentPackage.DesireContentSport;
import desireMapApplicationPackage.messageSystemPackage.ClientMessage;
import desireMapApplicationPackage.outputSetPackage.DesireSet;
import desireMapApplicationPackage.outputSetPackage.SatisfySet;
import desireMapApplicationPackage.outputSetPackage.UserSet;
import desireMapApplicationPackage.userDataPackage.AndroidData;
import desireMapApplicationPackage.userDataPackage.LoginData;
import desireMapApplicationPackage.userDataPackage.MainData;
import desireMapApplicationPackage.userDataPackage.RegistrationData;



public class UserProgram{
	public static Random rand = new Random();
	public static int serverPort = 9252;
	public static String serverAddress = "localhost";
	public static Scanner scanner = new Scanner(System.in);
	public static String currentUser = "?";
	public static boolean loggedIn = false;
	public static boolean onMap = false;


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
		InetAddress address = InetAddress.getByName(serverAddress);
		Socket clientSocket = new Socket(address, serverPort);
		ObjectOutputStream outs = new ObjectOutputStream(clientSocket.getOutputStream());
		outs.flush();
		ObjectInputStream ins = new ObjectInputStream(clientSocket.getInputStream());
		int answer = 1;
		System.out.println("# 1 - register, 2 - log in, 3 - add desire\n4 - show desires, 5 - show info," +
				" 6 - find satisfiers, 7 - delete desires from category\n 8 - send message 9 - tiles pack");
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
				onMap = false;
				System.out.println("# REGISTERING");
				String login = askString("login");
				String password = askString("password");
				String name = askString("name");
				char sex = askString("sex").charAt(0);
				String birth = askString("birth");
				RegistrationPack regPack = new RegistrationPack(new RegistrationData(login, password, name, sex, birth), new AndroidData("testAndroidID"));
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
				onMap = false;
				System.out.println("# LOGGING IN");
				String login = askString("login");
				String password = askString("password");
				Integer pseudoId = rand.nextInt();
				LoginPack logPack = new LoginPack(new LoginData(login, password), new AndroidData(pseudoId.toString()));
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
					double lat = askDouble("latitude");
					double lon = askDouble("longitude");
					Coordinates coord = new Coordinates(lat, lon);
					String tile = askString("tile");
					switch (catCode){
					case (CodesMaster.Categories.SportCode):
					{
						String sport = askString("sport");
						String advantages = askString("advantages");
						outs.writeObject(new AddPack(new DesireContentSport(currentUser, null, desDes, coord, null, 0, sport, advantages), tile));
						outs.flush();
						System.out.println("Success : id = " + ins.readUTF());
						break;
					}
					case (CodesMaster.Categories.DatingCode):
					{
						char pSex = askString("partnerSex").charAt(0);
						int ageFrom = askInt("ageFrom");
						int ageTo = askInt("ageTo");
						outs.writeObject(new AddPack(new DesireContentDating(currentUser, null, desDes, coord, null,0, pSex, ageFrom, ageTo), tile));
						outs.flush();
						System.out.println("Success : " + ins.readUTF());
						break;
					}
					}
				}
				break;
			}
			case(4):{
				onMap = false;
				System.out.println("# SHOWING DESIRES OF SOME CATEGORY");
				int categoryCode = askInt("category code 0 - sport;  1 - dating");
				outs.writeObject(new ShowPersonalDesiresPack(categoryCode));
				System.out.println("! Sent");
				DesireSet dSet = (DesireSet) ins.readObject();
				System.out.println("! Received");
				if (dSet != null){
					System.out.println(" | Success");
					for (DesireContent content : dSet.dArray){
						System.out.println(content.desireID + " | " + content.description);
					}
					System.out.println(" | That's all");
				}
				else{
					System.out.println(" | Null DesireSet received");
				}
				break;
			}
			case(5):{
				onMap = false;
				System.out.println("# SHOWING YOUR PERSONAL INFO");
				outs.writeObject(new ShowInfoPack());
				System.out.println("! Sent");
				MainData data = (MainData)ins.readObject();
				System.out.println("! Received");
				if (data != null){
					System.out.println(" | Success");
					System.out.println(data.login + " " + data.name + " " + data.sex + " " + data.birth + " " + data.rating);
					System.out.println(" | That's all");
				}
				else {
					System.out.println("-Null data received");
				}
				break;
			}
			case (6) :{
				System.out.println("# Finding satisfiers");
				HashSet<String> tiles = new HashSet<String>();
				String dID = askString("desireID");
				String tile1 = askString("tile1");
				String tile2 = askString("tile2");
				String tile3 = askString("tile3");
				String tile4 = askString("tile4");
				tiles.add(tile1);
				tiles.add(tile2);
				tiles.add(tile3);
				tiles.add(tile4);
				outs.writeObject(new SatisfyPack(dID, tiles, tile1.length()));
				onMap = true;
				System.out.println("! Sent\n");
				try{
					System.out.println(" | Success");
					SatisfySet set = (SatisfySet)ins.readObject();
					if (set != null){
						set.dTree.print();
						System.out.println("___________ You liked : ");
						Iterator<String> iterator = set.likedByUser.iterator();
						while(iterator.hasNext()){
							System.out.println(iterator.next());
						}
						System.out.println(" | That's all");
					}
					else{
						System.out.println("Empty set");
					}
				}
				catch(IOException error){
					error.getMessage();
				}
				break;
			}
			case(7):{
				if (loggedIn){
					System.out.println("# DELETING DESIRES");
					String setString = askString("Set of desiresID in '': ");
					DeletePack dPack = new DeletePack(setString); 
					outs.writeObject(dPack);
					if (ins.readBoolean()){
						System.out.println("+Deleted");
					}
					else{
						System.out.println("-Fail. Not deleted");
					}
				}
				else{
					System.out.println("- Log in, please");
				}
				break;
			}
			case(8):{
				onMap = false;
				if (loggedIn){
					String mess = askString("message");
					String whoTo = askString("receiver");
					ClientMessage message = new ClientMessage(null, currentUser, whoTo, mess);
					MessageSendPack pack = new MessageSendPack(message);
					outs.writeObject(pack);
					outs.flush();
				}
				break;
			}
			case(9):{
				if (loggedIn && onMap){
					HashSet<String> hSet = new HashSet<String>();
					String t1 = askString("tile1");
					String t2 = askString("tile2");
					String t3 = askString("tile3");
					String t4 = askString("tile4");
					hSet.add(t1);
					hSet.add(t2);
					hSet.add(t3);
					hSet.add(t4);
					TilesPack tiles = new TilesPack(t1.length(), hSet);
					outs.writeObject(tiles);
					outs.flush();
					SatisfySet sSet = (SatisfySet) ins.readObject();
					if (sSet != null)
						sSet.dTree.print();
				}
				else{
					System.out.println("...No info from server");
				}
				break;
			}
			case(10):{
				onMap = false;
				if (loggedIn){
					outs.writeObject(new UserChatHistoryPack());
					outs.flush();
					System.out.println("...UserChatHistoryPack sent");
					UserSet userSet = (UserSet) ins.readObject();
					if (userSet != null){
						for (String s : userSet.uSet){
							System.out.println(s);
						}
					}
				}
				else{
					System.out.println("...No info from server");
				}
				break;
			}
			case(11):{
				if (onMap){
					String desireID = askString("desire id to like");
					int liked = askInt("liking? 0,1");
					if (liked == 1){
					outs.writeObject(new LikePack(desireID, true));
					outs.flush();
					}
					else{
						outs.writeObject(new LikePack(desireID, false));
						outs.flush();
					}
					System.out.println("...LikePack sent");
				}
				break;
			}
			case(12):{
				System.out.println("# Finding satisfiers without cryteria");
				int categoryCode  = askInt("Category code : ");
				HashSet<String> tiles = new HashSet<String>();
				String tile1 = askString("tile1");
				String tile2 = askString("tile2");
				String tile3 = askString("tile3");
				String tile4 = askString("tile4");
				tiles.add(tile1);
				tiles.add(tile2);
				tiles.add(tile3);
				tiles.add(tile4);
				outs.writeObject(new SatisfyPack(categoryCode, tiles, tile1.length()));
				onMap = true;
				System.out.println("! Sent\n");
				try{
					System.out.println(" | Success");
					SatisfySet set = (SatisfySet)ins.readObject();
					if (set != null){
						set.dTree.print();
						System.out.println("___________ You liked : ");
						Iterator<String> iterator = set.likedByUser.iterator();
						while(iterator.hasNext()){
							System.out.println(iterator.next());
						}
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

			}
		}
		ins.close();
		outs.close();
		scanner.close();
		clientSocket.close();
		System.out.println("# STREAMS AND THE SOCKET HAVE BEEN CLOSED");
	}

}
