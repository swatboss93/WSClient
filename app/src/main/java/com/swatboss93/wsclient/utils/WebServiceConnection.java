package com.swatboss93.wsclient.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.swatboss93.wsclient.objects.User;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by swatboss93 on 14/11/15.
 */
public class WebServiceConnection {
    //Ruby
    private String url_base_crud = "http://192.168.1.13:8084/users";
    private String url_base_authentication = "http://192.168.1.13:8084/authentication";
    //Java
    //private String url_base_crud = "http://192.168.1.13:8084/WSRest/wsrest/users";
    //private String url_base_authentication = "http://192.168.1.13:8084/WSRest/wsrest/users/authenticate";

    private List<User> getUsersWS() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        User[] userArray;
        List<User> userList = new ArrayList<>();
        try {
            userArray = restTemplate.getForObject(url_base_crud, User[].class);
            userList = Arrays.asList(userArray);
        } catch (Exception e) {
            Log.e("WebService", e.getMessage());
        }
        return userList;
    }

    public List<User> getUsers() throws IOException {
        return getUsersWS();
    }

    private User getUserWS(int id) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        User user1 = new User();
        try {
            user1 = restTemplate.getForObject(url_base_crud + "/" + id, User.class);
        } catch (Exception e) {
            Log.e("WebService", e.getMessage());
        }
        return user1;
    }

    public User getUser(int id) throws IOException {
        return getUserWS(id);
    }

    private boolean deleteUserWS(User user) throws IOException, URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        try {
            Log.e("WebService", user.getName() + " " + user.getId());
            restTemplate.delete(url_base_crud + "/" + user.getId(), user);
            return true;
        } catch (Exception e) {
            Log.e("WebService", e.getMessage());
            return false;
        }
        /*
        Gson gson = new Gson();
        StringBuilder result = new StringBuilder();
        URL urlws = new URL(url_base_crud + "/" + user.getId());
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
        }*/
    }

    public boolean deleteUser(User user) throws IOException, URISyntaxException {
        return deleteUserWS(user);
    }

    private boolean insertUserWS(User user) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        try {
            User user1 = restTemplate.postForObject(url_base_crud, user, User.class);
            if(user1.getId() == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            Log.e("WebService", e.getMessage());
            return false;
        }
    }

    public boolean insertUser(User user) throws IOException {
        return insertUserWS(user);
    }

    private boolean updateUserWS(User user) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        try {
            Log.e("WebService", user.getName() + " " + user.getId());
            restTemplate.put(url_base_crud + "/" + user.getId(), user);
            return true;
        } catch (Exception e) {
            Log.e("WebService", e.getMessage());
            return false;
        }
    }

    public boolean updateUser(User user) throws IOException {
        return updateUserWS(user);
    }

    private boolean autheticateUserWS(User user) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        User user1 = new User();
        try {
            user1 = restTemplate.postForObject(url_base_authentication, user, User.class);
        } catch (Exception e) {
            Log.e("WebService", e.getMessage());
        }
        if(user1.getId() == 0) {
            return false;
        }
        return true;
    }

    public boolean autheticateUser(User user) throws IOException {
        return autheticateUserWS(user);
    }
}