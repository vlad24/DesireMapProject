import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class ResultSetMaster {
	protected ArrayList<ArrayList<String>> getArrayListFromResultSet(ResultSet resultRows) throws SQLException{
		System.out.println("$ Convertion from ResultSet to ArrayList");
		ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
		int row = -1;
		while(resultRows.next())
		{
			row++;
			table.add(new ArrayList<String>());
			int column = 1;
			while(true){
				try{
					String tmpString = resultRows.getString(column);
					table.get(row).add(tmpString);
					column++;
				}
				catch(SQLException outOfBound){
					break;
				}
			}
		}
		System.out.println("$ Convertion completed");
		return table;
	}
}
