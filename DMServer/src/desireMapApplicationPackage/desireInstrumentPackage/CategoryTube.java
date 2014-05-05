package desireMapApplicationPackage.desireInstrumentPackage;

import java.sql.SQLException;
import java.util.ArrayList;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;


public class CategoryTube {

		private ArrayList<CategoryManager> tube;
		////
		public CategoryTube(DesireInstrument owner){
			tube = new ArrayList<CategoryManager>();
			tube.add(new CategoryManagerMain(owner));
			tube.add(new CategoryManagerSport(owner));
			tube.add(new CategoryManagerDating(owner));
		}
		
		public void tryToAdd(String thisDesireID, AddPack pack) throws SQLException{
			System.out.println("Pushing to category tube");
			for (CategoryManager categoryManager : tube){
				categoryManager.tryToAddDesire(thisDesireID, pack);
			}
		}
}