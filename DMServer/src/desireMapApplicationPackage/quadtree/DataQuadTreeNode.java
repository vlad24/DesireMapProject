package desireMapApplicationPackage.quadtree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import desireMapApplicationPackage.desireContentPackage.DesireContent;

public class DataQuadTreeNode implements Serializable{
	
	private DataQuadTreeNode northWestNode;
	private DataQuadTreeNode northEastNode;
	private DataQuadTreeNode southWestNode;
	private DataQuadTreeNode southEastNode;
	private QuadTreeNodeBox nodeBox;

	public static final int boxCapacity = 25;
	private ArrayList<DesireContent> points;


	public DataQuadTreeNode(QuadTreeNodeBox nodeBox){
		this.nodeBox = nodeBox;
		points = new ArrayList<DesireContent>();
	}
	public DataQuadTreeNode(){
		this.nodeBox = new QuadTreeNodeBox(-180,-90,180,90);
		points = new ArrayList<DesireContent>();
	}
	

	private DataQuadTreeNode subdivide(double x, double y){
		double xMid = (nodeBox.x0 + nodeBox.x1) / 2;
		double yMid = (nodeBox.y0 + nodeBox.y1) / 2;

		if(y > yMid){
			if(x < xMid){
				if(northWestNode == null){
					QuadTreeNodeBox northWestBox = new QuadTreeNodeBox(nodeBox.x0, yMid, xMid, nodeBox.y1);
					northWestNode = new DataQuadTreeNode(northWestBox);
				}
				return northWestNode;
			}
			if(northEastNode == null){
				QuadTreeNodeBox northEastBox = new QuadTreeNodeBox(xMid, yMid, nodeBox.x1, nodeBox.y1);
				northEastNode = new DataQuadTreeNode(northEastBox);
			}
			return northEastNode;
		}

		if(x < xMid){
			if(southWestNode == null){
				QuadTreeNodeBox southWestBox = new QuadTreeNodeBox(nodeBox.x0, nodeBox.y0, xMid, yMid);
				southWestNode = new DataQuadTreeNode(southWestBox);
			}
			return southWestNode;
		}
		if(southEastNode == null){
			QuadTreeNodeBox southEastBox = new QuadTreeNodeBox(xMid, nodeBox.y0, nodeBox.x1, yMid);
			southEastNode = new DataQuadTreeNode(southEastBox);
		}
		return southEastNode;

	}


	public boolean insertData(DataQuadTreeNode node, DesireContent desire){
		if(!node.nodeBox.contains(desire.coordinates.latitude, desire.coordinates.longitude))
			return false;

		if(node.points.size() < boxCapacity){
			node.points.add(desire);
			return true;
		}

		if(northWestNode == null)
			subdivide(desire.coordinates.latitude, desire.coordinates.longitude);

		if(insertData(northEastNode, desire)) return true;
		if(insertData(northWestNode, desire)) return true;
		if(insertData(southWestNode, desire)) return true;
		if(insertData(southEastNode, desire)) return true;

		return false;			

	}
	
	public boolean insertData(DesireContent desire){
		return insertData(this, desire); 
	}

	public void getData(DataQuadTreeNode node, QuadTreeNodeBox screenBox, HashSet<DesireContent> resultSet){
		if(node.nodeBox.notIntersect(screenBox))
				return;
		
		for(DesireContent desire: node.points)
			if(screenBox.contains(desire.coordinates.latitude, desire.coordinates.longitude))
				resultSet.add(desire);
		
		if(node.northWestNode != null)
			getData(node.northWestNode, screenBox, resultSet);
		if(node.northEastNode != null)
			getData(node.northEastNode, screenBox, resultSet);
		if(node.southWestNode != null)
			getData(node.southWestNode, screenBox, resultSet);
		if(node.southEastNode != null)
			getData(node.southEastNode, screenBox, resultSet);
	}

	public void print(){
		printHelper(this);
	}
	
	private void printHelper(DataQuadTreeNode node) {
		if (node == null){
			return;
		}
		else{
			for (DesireContent cont : this.points){
				System.out.println(cont.desireID + " " + cont.login + " " + cont.description + " " + cont.time.toString());
			}
			printHelper(node.northEastNode);
			printHelper(node.northWestNode);
			printHelper(node.southEastNode);
			printHelper(node.southWestNode);
		}
	}

}
