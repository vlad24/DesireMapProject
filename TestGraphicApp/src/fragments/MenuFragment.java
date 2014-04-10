package fragments;

import overscroll.ScrollUtility;

import com.example.testgraphicapp.MainActivity;
import com.example.testgraphicapp.MenuCustomArrayAdapter;
import com.example.testgraphicapp.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.view.ViewGroup.LayoutParams;

public class MenuFragment extends Fragment {

	private ListView menuList;
	private LinearLayout menuLayout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		View menuView = inflater.inflate(R.layout.menu_list, container, false);
		menuList = (ListView) menuView.findViewById(R.id.lvMain);
		menuLayout = (LinearLayout) menuView.findViewById(R.id.menuLinearLayout);
		
		menuList.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
				((MainActivity) getActivity()).switchFragment(position);
			}
		});
		
		return menuView;
			
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		String[] menuItems = getResources().getStringArray(R.array.menu_items);
		MenuCustomArrayAdapter adapter = new MenuCustomArrayAdapter(getActivity(), menuItems);
		menuList.setAdapter(adapter); 

		DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
		LayoutParams layoutParams = menuLayout.getLayoutParams();
		int listHeight = ScrollUtility.setListViewHeightBasedOnChildren(menuList);
		layoutParams.height = (int)((displayMetrics.heightPixels - listHeight)/(2.3d));
	}

}
