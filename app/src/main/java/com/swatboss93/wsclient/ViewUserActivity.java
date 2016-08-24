package com.swatboss93.wsclient;

import android.os.AsyncTask;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;

import com.swatboss93.wsclient.enums.Operation;
import com.swatboss93.wsclient.objects.User;
import com.swatboss93.wsclient.utils.WebServiceConnection;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

public class ViewUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            Object op = extras.get("op");
            if(op.equals(Operation.INSERT)) {
                configureInsert();
            } else if (op.equals(Operation.UPDATE)){
                User user = getUser(extras.getInt("id"));
                configureUpdate(user);
            } else if (op.equals(Operation.DELETE)){
                User user = getUser(extras.getInt("id"));
                configureDelete(user);
            }
        }
    }

    private User getUser(int id) {
        User user = new User();
        try {
            user = new GetUserAsync().execute(id).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return user;
    }


    private void configureInsert(){
        Button btn = (Button) findViewById(R.id.btn_insert);
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateFields()){
                    User user = new User();
                    EditText name = (EditText) findViewById(R.id.edit_name);
                    EditText email = (EditText) findViewById(R.id.edit_email);
                    EditText password = (EditText) findViewById(R.id.edit_password);
                    user.setName(name.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setPassword(password.getText().toString());
                    try {
                        if(new InsertUserAsync().execute(user).get()){
                            finish();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void configureUpdate(final User user){
        fillField(user, Operation.UPDATE);
        Button btn = (Button) findViewById(R.id.btn_update);
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateFields()){
                    User userUpdate = new User();
                    EditText name = (EditText) findViewById(R.id.edit_name);
                    EditText email = (EditText) findViewById(R.id.edit_email);
                    EditText password = (EditText) findViewById(R.id.edit_password);
                    userUpdate.setId(user.getId());
                    userUpdate.setName(name.getText().toString());
                    userUpdate.setEmail(email.getText().toString());
                    userUpdate.setPassword(password.getText().toString());
                    try {
                        if(new UpdateUserAsync().execute(userUpdate).get()){
                            finish();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void configureDelete(final User user){
        fillField(user, Operation.DELETE);
        Button btn = (Button) findViewById(R.id.btn_delete);
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(new DeleteUserAsync().execute(user).get()){
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void fillField(User user, Operation operation) {
        EditText name = (EditText) findViewById(R.id.edit_name);
        EditText email = (EditText) findViewById(R.id.edit_email);
        EditText password = (EditText) findViewById(R.id.edit_password);
        name.setText(user.getName());
        email.setText(user.getEmail());
        password.setText(user.getPassword());
        if(operation == Operation.DELETE){
            name.setClickable(false);
            name.setFocusable(false);
            email.setClickable(false);
            email.setFocusable(false);
            password.setClickable(false);
            password.setFocusable(false);
        }
    }

    private boolean validateFields() {
        View focusView = null;
        EditText name = (EditText)findViewById(R.id.edit_name);
        EditText email = (EditText)findViewById(R.id.edit_email);
        EditText password = (EditText)findViewById(R.id.edit_password);
        name.setError(null);
        email.setError(null);
        password.setError(null);
        boolean validated;
        if(!validName(name)){
            name.setError(getString(R.string.invalid_name));
            focusView = name;
            validated = false;
        } else if (!validEmail(email)){
            email.setError(getString(R.string.invalid_email));
            focusView = email;
            validated = false;
        } else if(!validPassword(password)){
            email.setError(getString(R.string.invalid_pasword));
            focusView = password;
            validated = false;
        } else {
            validated = true;
        }

        if(!validated){
            focusView.requestFocus();
        }
        return validated;
    }

    private boolean validName(EditText name) {
        return ((!name.getText().toString().equals("")) || (!(name.getText().toString().length() < 2)));
    }

    private boolean validEmail(EditText email) {
        return (email.getText().toString().contains("@"));
    }

    private boolean validPassword(EditText password) {
        return !(password.getText().toString().length() < 4);
    }

    static class GetUserAsync extends AsyncTask<Integer, String, User> {

        @Override
        protected User doInBackground(Integer... params) {
            WebServiceConnection wsc = new WebServiceConnection();
            User user = new User();
            try {
                user = wsc.getUser(params[0].intValue());
            } catch (IOException e) {
                Log.e("pizzas", e.getMessage());
            }
            return user;
        }
    }

    static class DeleteUserAsync extends AsyncTask<User, String, Boolean> {

        @Override
        protected Boolean doInBackground(User... params) {
            WebServiceConnection wsc = new WebServiceConnection();
            boolean status;
            try {
                if(wsc.deleteUser(params[0])){
                    status = true;
                } else {
                    status = false;
                }
            } catch (IOException e) {
                Log.e("user",e.getMessage());
                status = false;
            } catch (URISyntaxException e) {
                Log.e("user", e.getMessage());
                status = false;
            }
            return status;
        }
    }

    static class InsertUserAsync extends AsyncTask<User, String, Boolean> {

        @Override
        protected Boolean doInBackground(User... params) {
            WebServiceConnection wsc = new WebServiceConnection();
            Boolean status;
            try {
                status = wsc.insertUser(params[0]);
            } catch (IOException e) {
                Log.e("user",e.getMessage());
                status = false;
            }
            return status;
        }
    }

    static class UpdateUserAsync extends AsyncTask<User, String, Boolean> {

        @Override
        protected Boolean doInBackground(User... params) {
            WebServiceConnection wsc = new WebServiceConnection();
            Boolean status;
            try {
                status = wsc.updateUser(params[0]);
            } catch (IOException e) {
                Log.e("user",e.getMessage());
                status = false;
            }
            return status;
        }
    }
}
