package pro1a.ics.upjs.sk.the_heck.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import pro1a.ics.upjs.sk.the_heck.R;
import pro1a.ics.upjs.sk.the_heck.fragment.AppointmentFragment;
import pro1a.ics.upjs.sk.the_heck.fragment.FavouriteDoctorFragment;
import pro1a.ics.upjs.sk.the_heck.fragment.FilterDoctorFragment;
import pro1a.ics.upjs.sk.the_heck.fragment.UserProfileFragment;
import pro1a.ics.upjs.sk.the_heck.retrofit.SessionManager;

import static pro1a.ics.upjs.sk.the_heck.help.Constant.PREFERENCIES_ACCESS_TOKEN;
import static pro1a.ics.upjs.sk.the_heck.help.Constant.PREFERENCIES_FILTERS;

public class Main extends AppCompatActivity {
    private SessionManager sessionManager;
    public SharedPreferences sharedPreferencesToken;
    public SharedPreferences sharedPreferencesDate;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        sharedPreferencesToken = getSharedPreferences(PREFERENCIES_ACCESS_TOKEN, Context.MODE_PRIVATE);
        sharedPreferencesDate = getSharedPreferences(PREFERENCIES_FILTERS, Context.MODE_PRIVATE);
        sessionManager = new SessionManager(getApplicationContext());
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        adapter.addFragment(new AppointmentFragment(), "HOME");
        adapter.addFragment(new FilterDoctorFragment(), "ADD NEW");
        adapter.addFragment(new FavouriteDoctorFragment(), "DOCTORS");
        adapter.addFragment(new UserProfileFragment(), "PROFILE");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_document);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_heart);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_user);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private Context context;
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager, Context context) {
            super(manager);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                return true;

            case R.id.action_logout:
                SharedPreferences.Editor editor = sharedPreferencesToken.edit();
                editor.clear();
                editor.commit();
                SharedPreferences.Editor editorDate = sharedPreferencesDate.edit();
                editorDate.clear();
                editorDate.commit();
                sessionManager.setLogin(false);
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
