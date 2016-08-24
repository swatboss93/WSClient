package com.swatboss93.wsclient.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.swatboss93.wsclient.objects.User;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by swatboss93 on 14/11/15.
 */
public class WebServiceConnection {
    private String url_base_crud = "http://localhost:8084/WSRest/wsrest/users";
    private String url_base_authentication = "http://localhost:8084/WSRest/wsrest/users/authenticate";

    private List<User> getUsersWS() throws IOException {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<User>>() {
        }.getType();
        StringBuilder result = new StringBuilder();
        URL urlws = new URL(url_base_crud);
        HttpURLConnection conn = (HttpURLConnection) urlws.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        try {
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } finally {
            rd.close();
        }
        return gson.fromJson(result.toString(), listType);
    }

    public List<User> getUsers() throws IOException {
        return getUsersWS();
    }

    private User getUserWS(int id) throws IOException {
        Gson gson = new Gson();
        StringBuilder result = new StringBuilder();
        URL urlws = new URL(url_base_crud + "/" + id);
        HttpURLConnection conn = (HttpURLConnection) urlws.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        try {
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } finally {
            rd.close();
        }
        return gson.fromJson(result.toString(), User.class);
    }

    public User getUser(int id) throws IOException {
        return getUserWS(id);
    }

    private void deleteUserWS(int id) throws IOException, URISyntaxException {
        Gson gson = new Gson();
        StringBuilder result = new StringBuilder();
        URL urlws = new URL(url_base_crud + "/" + id);
        HttpURLConnection conn = (HttpURLConnection) urlws.openConnection();
        conn.setRequestMethod("DELETE");

        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        try {
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } finally {
            rd.close();
        }
    }

    public void deleteUser(int id) throws IOException, URISyntaxException {
        deleteUserWS(id);
    }

    private void insertUserWS(User user) throws IOException {
        URL urlws = new URL(url_base_crud);
        Gson gson = new Gson();
        HttpURLConnection conn = (HttpURLConnection) urlws.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        try {
            wr.writeBytes(gson.toJson(user));
            wr.flush();
        } finally {
            wr.close();
        }
        int responseCode = conn.getResponseCode();
        StringBuffer response;
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        try {
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } finally {
            in.close();
        }
    }

    public void insertUser(User user) throws IOException {
        insertUserWS(user);
    }

    private void updateUserWS(User user) throws IOException {
        StringBuilder result = new StringBuilder();
        URL urlws = new URL(url_base_crud);
        Gson gson = new Gson();
        HttpURLConnection conn = (HttpURLConnection) urlws.openConnection();
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        try {
            wr.writeBytes(gson.toJson(user));
            wr.flush();
        } finally {
            wr.close();
        }
        int responseCode = conn.getResponseCode();
        StringBuffer response;
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        try {
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } finally {
            in.close();
        }
    }

    public void updateUser(User user) throws IOException {
        updateUserWS(user);
    }

    private boolean autheticateUserWS(User user) throws IOException {
        URL urlws = new URL(url_base_crud);
        Gson gson = new Gson();
        HttpURLConnection conn = (HttpURLConnection) urlws.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        try {
            wr.writeBytes(gson.toJson(user));
            wr.flush();
        } finally {
            wr.close();
        }
        int responseCode = conn.getResponseCode();
        StringBuffer response;
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        try {
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } finally {
            in.close();
        }
        Log.d("Teste", response.toString());
        User user1 = gson.fromJson(response.toString(), User.class);
        if(user1.getId() == 0){
            return false;
        } else {
            return true;
        }
    }

    public boolean autheticateUser(User user) throws IOException {
        return autheticateUserWS(user);
    }
}