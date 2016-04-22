package com.crossover.myconference;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crossover.myconference.fragment.ConferenceFragment;
import com.crossover.myconference.helper.PrefUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by oluwafemi.bamisaye on 3/8/2016.
 */
public class DashBoardActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,ConferenceFragment.Listener {
    private static final String TAG = DashBoardActivity.class.getName();
    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private GoogleApiClient mGoogleApiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

//        mGoogleApiClient = new GoogleApiClient(this);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View headerView = navigationView.inflateHeaderView(R.layout.drawer_header);


        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                Toast.makeText(DashBoardActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        //initialize elements of the drawer
        ImageView profilePhoto = (ImageView) headerView.findViewById(R.id.profile_image);
        TextView username = (TextView) headerView.findViewById(R.id.username);
        TextView email = (TextView) headerView.findViewById(R.id.email);
        TextView roleString = (TextView) headerView.findViewById(R.id.roleString);

        //set drawer Item
        String photoString = PrefUtils.getPhoto(this);

        Log.d(TAG, " Handle signIn email of user" + photoString);
        Bitmap decodedImg = PrefUtils.decodeBase64(photoString);
        profilePhoto.setImageBitmap(decodedImg);

        //set UserName
        String user = PrefUtils.getPersonKey(this);
        username.setText(user);

        //set User Email
        String mailTxt = PrefUtils.getEmail(this);
        email.setText(mailTxt);

        //set User Role
        String roleTxt = PrefUtils.getRole(this);
        roleString.setText(roleTxt);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if(item.isChecked()) item.setChecked(false);
                else item.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (item.getItemId()){


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.conference:
                        Toast.makeText(getApplicationContext(), "Conference Selected", Toast.LENGTH_SHORT).show();
                        ConferenceFragment fragment = new ConferenceFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame,fragment);
                        fragmentTransaction.commit();
                        return true;

                    // For rest of the options we just show a toast on click

                    case R.id.invite:
                        Toast.makeText(getApplicationContext(),"Stared Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.topic:
                        Toast.makeText(getApplicationContext(),"Send Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.signOut:
                        Toast.makeText(getApplicationContext(),"Send Selected",Toast.LENGTH_SHORT).show();
                        signOutUser();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });
    }

    private void signOutUser() {
/*        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
//                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });*/
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
//            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onFragmentAttached(ListFragment listFragment) {

    }

    @Override
    public void onFragmentDetached(ListFragment listFragment) {

    }
}
