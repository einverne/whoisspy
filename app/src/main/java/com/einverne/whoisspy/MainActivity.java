package com.einverne.whoisspy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.einverne.whoisspy.database.WordsDataSource;
import com.einverne.whoisspy.httpclient.WordsClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {

    private ProgressDialog progressDialog;
    private WordsDataSource dataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new WordsDataSource(this);

        if (!isNetworkAvailable()){
            Toast.makeText(this,"Check your network",Toast.LENGTH_SHORT).show();
        }

        final String PREFS_NAME = "PrefsFile";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
        if (settings.getBoolean("first_time",true)){
            //first time to run app



            //change values in preferences
            settings.edit().putBoolean("first_time",false).commit();
        }
    }

    public void get(){
        progressDialog = ProgressDialog.show(MainActivity.this,"Loading","update database please wait...",true);
        WordsClient.get(new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(JSONArray response) {
                try {
                    dataSource.open();
                    dataSource.clearDatabase();
                    for (int i = 0 ; i< response.length(); i++){
                        JSONObject wordspair = response.getJSONObject(i);
                        String first = wordspair.getString("first");
                        String second = wordspair.getString("second");
                        Log.d("EV_TAG ",first + " "+ second);
                        dataSource.addWordsPair(first,second);
                    }
                    dataSource.close();
                }
                catch (JSONException e){

                }
            }
        });
        progressDialog.dismiss();
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.update_database:
                get();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
