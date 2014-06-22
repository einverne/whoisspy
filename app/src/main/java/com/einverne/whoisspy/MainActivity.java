package com.einverne.whoisspy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.einverne.whoisspy.adapter.PersonAdapter;
import com.einverne.whoisspy.database.WordsDataSource;
import com.einverne.whoisspy.httpclient.WordsClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private ProgressDialog progressDialog;
    private WordsDataSource dataSource;
    private ArrayList<String> data;
    private GridView gridView;
    private Spinner spinner_spy_num;
    private Spinner spinner_whiteborad_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new WordsDataSource(this);
        data = new ArrayList<String>();

//        for (int i = 0; i< 10; i++){
//            String s = new String(" "+i);
//            data.add(s);
//        }

        gridView = (GridView)findViewById(R.id.gridView);
        gridView.setAdapter(new PersonAdapter(this,data));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            }
        });


        spinner_spy_num = (Spinner)findViewById(R.id.spinner_spy_num);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
        for (int i = 0; i < 8 ; i++){
            spinnerAdapter.add(Integer.toString(i));
        }
        spinner_spy_num.setAdapter(spinnerAdapter);

        spinner_whiteborad_num = (Spinner)findViewById(R.id.spinner_whiteborad_num);
        spinner_whiteborad_num.setAdapter(spinnerAdapter);

//        if (!isNetworkAvailable()){
//            Toast.makeText(this,"Check your network",Toast.LENGTH_SHORT).show();
//        }

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
            case R.id.add_new:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Add player");
                LayoutInflater inflater = getLayoutInflater();
                final View v = inflater.inflate(R.layout.dialog_add,null);
                builder.setView(v);
                builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TextView tv = (TextView)v.findViewById(R.id.player_name_add);
                        if (tv.getText().length() <= 0){
                            Toast.makeText(MainActivity.this,"please enter name",Toast.LENGTH_SHORT).show();
                        }else{
                            data.add(tv.getText().toString());
                        }
                    }
                });
                builder.setNegativeButton("Cancer",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            case R.id.play:
                //start to play
                String spynum = spinner_spy_num.getSelectedItem().toString();
                int n_spynum = Integer.parseInt(spynum);
                String whitenum = spinner_whiteborad_num.getSelectedItem().toString();
                int n_whitenum = Integer.parseInt(whitenum);
                int all_plater = gridView.getChildCount();

                if (all_plater < n_spynum + n_whitenum){
                    Toast.makeText(this,"卧底和白板不能超过玩家人数",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(this,GameActivity.class);
                    startActivity(intent);
                }

                return true;
            case R.id.update_database:
                if (!isNetworkAvailable()){
                    Toast.makeText(this,"Check your network",Toast.LENGTH_SHORT).show();
                }else{
                    get();
                }
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
