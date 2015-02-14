package travel.iknow.android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import travel.iknow.android.data.DataSource;
import travel.iknow.android.rest.Local;
import travel.iknow.android.rest.Token;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends ActionBarActivity implements LoaderCallbacks<Cursor>
{
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mNameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private String name;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(isRegistered()) startMainActivity();

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // Set up the login form.
        mNameView = (EditText) findViewById((R.id.name));

        mEmailView = (EditText) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
            {
                if (id == R.id.login || id == EditorInfo.IME_NULL)
                {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    void startMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void populateAutoComplete()
    {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin()
    {
        // Reset errors.
        mNameView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        name = mNameView.getText().toString();
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        if (TextUtils.isEmpty(name) || !isNameValid(name))
        {
            mNameView.setError(getString(R.string.error_invalid_name));
            mNameView.requestFocus();
            return;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email) || !isEmailValid(email))
        {
            mEmailView.setError(getString(R.string.error_invalid_email));
            mEmailView.requestFocus();
            return;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password))
        {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            mPasswordView.requestFocus();
            return;
        }

        saveProfile(name, email, password);

        showProgress(true);

        performAuth();
    }

    private void performAuth()
    {
        final IKnowTravelApplication application = ((IKnowTravelApplication) getApplication());

        Callback<Token> cb = new Callback<Token>()
        {
            /**
             * Successful HTTP response.
             *
             * @param newToken
             * @param response
             */
            @Override
            public void success(Token newToken, Response response)
            {
                Toast.makeText(LoginActivity.this, "token: " + newToken, Toast.LENGTH_SHORT)
                        .show();
                DataSource.currentToken = newToken.getToken();

                application.updateHeaders(newToken.getToken());
                registration();
            }

            /**
             * Unsuccessful HTTP response due to network failure, non-2XX status code, or unexpected
             * exception.
             *
             * @param error
             */
            @Override
            public void failure(RetrofitError error)
            {
                Toast.makeText(LoginActivity.this, "cannot get token", Toast.LENGTH_SHORT).show();
            }
        };

        application.apiHelper.requestToken(cb);
    }

    private void registration()
    {
        Callback<Object> cb = new Callback<Object>()
        {
            /**
             * Successful HTTP response.
             *
             * @param o
             * @param response
             */
            @Override
            public void success(Object o, Response response)
            {
                showProgress(false);
                Toast.makeText(LoginActivity.this, "Successfully registered!", Toast.LENGTH_SHORT)
                        .show();

                startMainActivity();
            }

            /**
             * Unsuccessful HTTP response due to network failure, non-2XX status code, or unexpected
             * exception.
             *
             * @param error
             */
            @Override
            public void failure(RetrofitError error)
            {
                showProgress(false);
                Toast.makeText(LoginActivity.this, "cannot load", Toast.LENGTH_SHORT).show();
            }
        };

        ((IKnowTravelApplication)getApplication()).apiHelper.registration(new Local(name, email, password, true), cb);
    }

    private void saveProfile(String name, String email, String pass)
    {
        SharedPreferences.Editor editor = getSharedPreferences(DataSource.PREFERENCES_NAME, 0).edit();
        editor.putString(DataSource.NAME_PREFERENCES_NAME, name);
        editor.putString(DataSource.EMAIL_PREFERENCES_NAME, email);
        editor.putString(DataSource.PASSWORD_PREFERENCES_NAME, pass);
        editor.commit();
    }

    private boolean isRegistered()
    {
        SharedPreferences prefs = getSharedPreferences(DataSource.PREFERENCES_NAME, 0);
        String name = prefs.getString(DataSource.NAME_PREFERENCES_NAME, "");
        String email = prefs.getString(DataSource.EMAIL_PREFERENCES_NAME, "");
        String password = prefs.getString(DataSource.PASSWORD_PREFERENCES_NAME, "");

        return (!name.isEmpty() && !email.isEmpty() && !password.isEmpty());
    }

    private boolean isNameValid(String name)
    {
        //TODO: Replace this with your own logic
        return !TextUtils.isEmpty(name);
    }

    private boolean isEmailValid(String email)
    {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password)
    {
        //TODO: Replace this with your own logic
        return password.length() > 2;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    public void showProgress(final boolean show)
    {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else
        {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI, ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE + " = ?",
                    new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor)
    {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        //addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader)
    {

    }

    private interface ProfileQuery
    {
        String[] PROJECTION = {ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,};

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    /*private void addEmailsToAutoComplete(List<String> emailAddressCollection)
    {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }*/

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
//    public class UserLoginTask extends AsyncTask<Void, Void, Boolean>
//    {
//        private final Local local;
//
//        UserLoginTask(Local local)
//        {
//            this.local = local;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params)
//        {
//            RestAdapter restAdapter = new RestAdapter.Builder()
//                        .setEndpoint("http://api.iknow.travel")
//                        .setRequestInterceptor(ApiHelper.requestInterceptor)
//                        .build();
//
//
//            ApiHelper apiHelper = restAdapter.create(ApiHelper.class);
//            //Response response = apiHelper.registration(local);
//
//            return true;//response.getStatus() == HttpStatus.SC_OK;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success)
//        {
//            mAuthTask = null;
//            showProgress(false);
//
//            if (success)
//            {
//                //finish();
//            }
//            else
//            {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
//            }
//        }
//
//        @Override
//        protected void onCancelled()
//        {
//            mAuthTask = null;
//            showProgress(false);
//        }
//    }
}



