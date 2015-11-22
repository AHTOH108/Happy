package com.iandp.happy.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.iandp.happy.R;
import com.iandp.happy.fragment.CalculatorFragment;
import com.iandp.happy.temp.ListProductFragment;
import com.iandp.happy.temp.NavMenuItem;
import com.iandp.happy.temp.TestFragment;


import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private static final int MAX_WIDTH_SIDE_NAV_DPI = 320;

    private static final int NAV_MENU_POSITION_CALCULATOR= 0;
    private static final int NAV_MENU_POSITION_PRODUCT = 1;
    private static final int NAV_MENU_POSITION_SHOPPING_LIST = 2;
    private static final int NAV_MENU_POSITION_SHOP = 3;
    private static final int NAV_MENU_POSITION_TEST = 4;
    private static final int NAV_MENU_POSITION_ABOUT = 5;

    private static final int START_NAV_MENU_POSITION = NAV_MENU_POSITION_CALCULATOR;

    private static final String CURRENT_MENU_POSITION = "CurrentMenuPosition";

    private DrawerLayout mDrawerLayout;
    private LinearLayout mLeftDrawerLayout;
    private ListView mMenuListView;

    private ActionBarDrawerToggle mDrawerToggle;

    private ArrayList<NavMenuItem> navMenuItems;

    private int currentItemPosition;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_MENU_POSITION, currentItemPosition);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            currentItemPosition = savedInstanceState.getInt(CURRENT_MENU_POSITION, START_NAV_MENU_POSITION);
        } else {
            currentItemPosition = START_NAV_MENU_POSITION;
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftDrawerLayout = (LinearLayout) findViewById(R.id.left_drawer);
        mMenuListView = (ListView) findViewById(R.id.listview_sidemenu);

        mLeftDrawerLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                int MAX_WIDTH_SIDE_NAV_PX = (int) (MAX_WIDTH_SIDE_NAV_DPI * (getResources().getDisplayMetrics().densityDpi / 160f));

                if (mLeftDrawerLayout.getWidth() > MAX_WIDTH_SIDE_NAV_PX) {
                    DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mLeftDrawerLayout.getLayoutParams();
                    params.width = MAX_WIDTH_SIDE_NAV_PX;
                    mLeftDrawerLayout.setLayoutParams(params);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mLeftDrawerLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mLeftDrawerLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

        mMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                currentItemPosition = position;
                displayView(currentItemPosition);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mDrawerLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        initData();

        mMenuListView.setAdapter(new NavMenuListAdapter());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        displayView(currentItemPosition);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initData() {
        navMenuItems = new ArrayList<>();

        String[] navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        TypedArray navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        for (int i = 0; i < navMenuTitles.length; i++) {

            navMenuItems.add(new NavMenuItem(getResources().getString(getResources().getIdentifier(navMenuTitles[i], "string", MainActivity.this.getPackageName())), navMenuIcons.getResourceId(i, -1)));
        }

        navMenuIcons.recycle();
    }

    private void displayView(int position) {

        FragmentManager fragmentManager = getFragmentManager();

        Fragment fragmentCurrent = fragmentManager.findFragmentById(R.id.content_frame);


        Fragment fragmentNew = null;

        switch (position) {
            case NAV_MENU_POSITION_CALCULATOR:
                fragmentNew = new CalculatorFragment();
                break;
            case NAV_MENU_POSITION_PRODUCT:
                fragmentNew = new ListProductFragment();
                break;
            case NAV_MENU_POSITION_SHOPPING_LIST:
                fragmentNew = new TestFragment();
                break;
            case NAV_MENU_POSITION_SHOP:
                fragmentNew = new TestFragment();
                break;
            case NAV_MENU_POSITION_TEST:
                fragmentNew = new TestFragment();
                break;
            case NAV_MENU_POSITION_ABOUT:
                fragmentNew = new TestFragment();
                break;
            default:
                break;
        }


        if (fragmentNew != null)

        {

            if ((fragmentCurrent == null) || (!fragmentCurrent.getClass().equals(fragmentNew.getClass()))) {

                fragmentManager.beginTransaction().replace(R.id.content_frame, fragmentNew, fragmentNew.getClass().getCanonicalName()).commit();
            }

            displayTitle();

            mMenuListView.setItemChecked(position, true);
            mMenuListView.setSelection(position);

            mDrawerLayout.closeDrawer(mLeftDrawerLayout);
        }
    }

    private void displayTitle() {

        setTitle(navMenuItems.get(currentItemPosition).getTitle());

        if ((currentItemPosition == NAV_MENU_POSITION_ABOUT)) {
            setVisibilityLogoTitle(true);
        } else {
            setVisibilityLogoTitle(false);
        }
    }

    public void updatePositionMenu() {
        displayView(currentItemPosition);
    }

    @Override
    public void setTitle(CharSequence title) {

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(((String) title).toUpperCase());
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    private class NavMenuListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public NavMenuListAdapter() {
            this.inflater = LayoutInflater.from(MainActivity.this);
        }

        @Override
        public int getCount() {
            return navMenuItems.size();
        }

        @Override
        public Object getItem(int position) {
            return navMenuItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_nav_menu, parent, false);
                viewHolder = new ViewHolder(convertView);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.icoItem.setImageResource(navMenuItems.get(position).getIcon());
            viewHolder.nameItem.setText(navMenuItems.get(position).getTitle());

            return convertView;
        }

        private class ViewHolder {

            private ImageView icoItem;
            private TextView nameItem;

            public ViewHolder(View view) {
                icoItem = (ImageView) view.findViewById(R.id.imgview_icon_nav_menu);
                nameItem = (TextView) view.findViewById(R.id.textview_title_nav_menu);
            }
        }

        private class ViewHolderAuth extends ViewHolder {

            private ImageView badgeSocialNetwork;

            public ViewHolderAuth(View view) {
                super(view);

                badgeSocialNetwork = (ImageView) view.findViewById(R.id.imgview_icon_nav_menu);
            }
        }
    }
}
