import java.util.ArrayList;

public class StringDivider{
	protected ArrayList<String> parseSlashedString (String str){
		int start = 0;
		int end = str.indexOf('/');
		ArrayList<String> stringSet = new ArrayList<String>();
		while (end != -1)
		{
			stringSet.add(str.substring(start + 1, end));
			start = end;
			end = str.indexOf('/', start + 1);
		}
		stringSet.add(str.substring(start + 1));
		for (int i = 0 ; i < stringSet.size(); i++){
			String s = stringSet.get(i);
			System.out.println(s);
		}
		return stringSet;		
	}
}
