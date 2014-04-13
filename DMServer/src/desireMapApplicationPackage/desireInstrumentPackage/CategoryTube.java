package desireMapApplicationPackage.desireInstrumentPackage;

import java.sql.SQLException;
import java.util.ArrayList;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.desireContentPackage.DesireContent;


public class CategoryTube {
		private ArrayList<CategoryManager> tube;
		////
		public CategoryTube(){
			tube = new ArrayList<CategoryManager>();
			tube.add(new CategoryManagerMain());
			tube.add(new CategoryManagerSport());
			tube.add(new CategoryManagerDating());
		}
		
		public void tryToAdd(String thisDesireID, AddPack pack) throws SQLException{
			System.out.println("Pushing to category tube");
			for (CategoryManager categoryManager : tube){
				categoryManager.tryToAddDesire(thisDesireID, pack);
			}
		}
}