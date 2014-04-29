package desireMapApplicationPackage.outputSetPackage;

import java.io.Serializable;
import java.util.HashMap;

import desireMapApplicationPackage.quadtree.DataQuadTreeNode;
import desireMapApplicationPackage.userDataPackage.MainData;


public class SatisfySet implements Serializable{
	public SatisfySet(){
		dTree = new DataQuadTreeNode();
		desireAuthors = new HashMap<String, MainData>();
	}
	public DataQuadTreeNode dTree;
	public HashMap<String, MainData> desireAuthors;
}
