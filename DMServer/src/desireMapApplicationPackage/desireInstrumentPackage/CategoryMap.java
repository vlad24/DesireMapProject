package desireMapApplicationPackage.desireInstrumentPackage;

import java.sql.SQLException;
import java.util.HashMap;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;


public class CategoryMap {
		private CategoryManagerMain mainManager;
		private HashMap<Integer, CategoryManager> map;
		////
		public CategoryMap(DesireInstrument owner){
			map = new HashMap<Integer, CategoryManager>();
			mainManager = new CategoryManagerMain(owner);
			map.put(CodesMaster.Categories.SportCode, new CategoryManagerSport(owner));
			map.put(CodesMaster.Categories.DatingCode, new CategoryManagerDating(owner));
		}
		
		public void add(String thisDesireID, AddPack addPack) throws SQLException{
			System.out.println("Pushing to category map");
			if (map.get(addPack.desireContent.category) != null){
				mainManager.addDesire(thisDesireID, addPack);
				map.get(addPack.desireContent.category).addDesire(thisDesireID, addPack);
			}
		}
}