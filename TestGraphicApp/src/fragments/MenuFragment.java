package fragments;

import overscroll.ScrollUtility;

import com.example.testgraphicapp.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MenuFragment extends Fragment {

	private String TAG = "MenuFragment";
	private ListView menuList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		View menuView = inflater.inflate(R.layout.menu_list, null);
		menuList = (ListView) menuView.findViewById(R.id.lvMain);
		
		menuList.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
				Toast.makeText(getActivity(), "position = " + position, Toast.LENGTH_SHORT).show();
			}
		});
		
		return menuView;
			
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		String[] menuItems = getResources().getStringArray(R.array.menu_items);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.itemTextView, menuItems);
		menuList.setAdapter(adapter);
		ScrollUtility.setListViewHeightBasedOnChildren(menuList);
	}

}
