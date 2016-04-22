package com.crossover.myconference;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.crossover.myconference.fragment.RoleDialogFragment;
import com.crossover.myconference.helper.PrefUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.InputStream;

public class SignUpActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static int RC_SIGN_IN = 9001;
    private static final int PROFILE_PIC_SIZE = 400;
    private Boolean isSignedIn = false;
    static Bitmap bResult;

    private GoogleApiClient mGoogleApiClient;
    private TextView statusTextView;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if (!PrefUtils.isFirstTime(this)){

            Intent homeIntent = new Intent(this, DashBoardActivity.class);
            startActivity(homeIntent);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        statusTextView = (TextView) findViewById(R.id.title_text);

        SignInButton signInButton = (SignInButton) findViewById(R.id.btn_sign_in);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());

        signInButton.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            Log.d(TAG, "Got cached sign-in");

        } else {

        }
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String emailPref = acct.getEmail();

            Log.d(TAG, " Handle signIn email of user" + emailPref);
            PrefUtils.setEmail(this, emailPref);
            //save Acct Name
            String namePref = acct.getDisplayName();
            PrefUtils.setPersonKey(this, namePref);

            Log.d(TAG, " Handle signIn email of user" + namePref);


            Uri mPhoto = acct.getPhotoUrl();
            if (mPhoto != null) {
                String personPhotoUrl = mPhoto.toString();
                Log.e(TAG, "Profile Image" + personPhotoUrl);
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

                new LoadProfileImage().execute(personPhotoUrl);
            }else

            {
                Bitmap def = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

                PrefUtils.setPhoto(this, def);

                showRoleDialog();
            }

        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(this, "Ensure you are connected to a Network to be able to Sign In",Toast.LENGTH_SHORT).show();
            signOutUser();
            RC_SIGN_IN++;
            updateUI(false);
        }

    }
    // [END handleSignInResult]
    public void showRoleDialog() {

        FragmentManager fm = getSupportFragmentManager();
        RoleDialogFragment newRoleFragment = new RoleDialogFragment();
        newRoleFragment.show(fm, "role dialog"); //show dialog fragment

    }

    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
//        ImageView bmImage = (ImageView) findViewById(R.id.imgProfilePicNull);
        Bitmap bitmap = SignUpActivity.bResult;

        public LoadProfileImage() {
//            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(SignUpActivity.this, "Getting Information, Please Wait.....", Toast.LENGTH_SHORT).show();

        }

        protected Bitmap doInBackground(String... urls) {
            hideProgressDialog();
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);

                Thread.sleep(1000);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }


            Log.e(TAG, "Bitmap returned" + mIcon11);
            return mIcon11;

        }

        protected void onPostExecute(Bitmap result) {
            this.bitmap=result;

            if (result != null){
                PrefUtils.setPhoto(SignUpActivity.this, result);
                String m = PrefUtils.encodeTobase64(result);
                Log.e(TAG, "Encoded Profile image " + m);


                showRoleDialog();
            }else {

            }
        }
    }


    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOutUser() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.btn_sign_in).setVisibility(View.GONE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            statusTextView.setText(R.string.signed_out);
            findViewById(R.id.btn_sign_in).setVisibility(View.VISIBLE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                signIn();/*
                break;
            case R.id.bt_sign_up:
                proceedHome();*/
//                break;
//            case R.id.disconnect_button:
//                revokeAccess();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
