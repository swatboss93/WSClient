package com.swatboss93.wsclient;

import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.swatboss93.wsclient.adapters.UserAdapter;
import com.swatboss93.wsclient.enums.Operation;
import com.swatboss93.wsclient.objects.User;
import com.swatboss93.wsclient.utils.WebServiceConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent param = new Intent(HomeActivity.this, ViewUserActivity.class);
                param.putExtra("op", Operation.INSERT);
                startActivity(param);
            }
        });
    }

    public void onResume(){
        super.onResume();
        final ListView listView = (ListView)findViewById(R.id.listView);
        List<User> users = new ArrayList<>();

        try {
            users = new GetListAsync().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        listView.setAdapter(new UserAdapter(getApplicationContext(), users));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                User user = (User)listView.getAdapter().getItem(position);
                Intent param = new Intent(HomeActivity.this, ViewUserActivity.class);
                param.putExtra("id", user.getId());
                param.putExtra("op", Operation.UPDATE);
                startActivity(param);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User)listView.getAdapter().getItem(position);
                Intent param = new Intent(HomeActivity.this, ViewUserActivity.class);
                param.putExtra("id", user.getId());
                param.putExtra("op", Operation.DELETE);
                startActivity(param);
                return false;
            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_pizzas, menu);
        return true;
    }*/

    /*@Override
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
    }*/

    static class GetListAsync extends AsyncTask<String, String, List<User>> {

        @Override
        protected List<User> doInBackground(String... params) {
            WebServiceConnection wsc = new WebServiceConnection();
            List<User> pizzas = new ArrayList<>();
            try {
                pizzas = wsc.getUsers();
            } catch (IOException e) {
                Log.e("pizzas",e.getMessage());
            }
            return pizzas;
        }
    }

}
