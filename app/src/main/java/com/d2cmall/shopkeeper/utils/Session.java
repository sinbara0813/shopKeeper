package com.d2cmall.shopkeeper.utils;


import android.content.Context;

import com.d2cmall.shopkeeper.common.Action;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.common.SharePrefConstant;
import com.d2cmall.shopkeeper.model.UserBean;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

public class Session {

    private UserBean user;

    private Session() {
    }

    public static Session getInstance() {
        return SessionHolder.INSTANCE;
    }


    private static class SessionHolder {
        private static final Session INSTANCE = new Session();
    }

    public UserBean getUserFromFile(Context context) {
        if (user == null) {
            try {
                InputStream in = context.openFileInput(Constants.USER_FILE);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;

                while ((length = in.read(buffer)) != -1) {
                    stream.write(buffer, 0, length);
                }

                Gson gson = new Gson();
                user = gson.fromJson(stream.toString(), UserBean.class);
                in.close();
                stream.close();
            } catch (IOException e) {
                return null;
            }
        }
        return user;
    }

    public UserBean getUser() {
        return user;
    }

    public void saveUserToFile(Context context, UserBean user) {
        if (user == null) {
            return;
        }
        this.user = user;
        Gson gson = new Gson();
        String personJson = gson.toJson(user, UserBean.class);
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = context.openFileOutput(Constants.USER_FILE, Context.MODE_PRIVATE);
            if (fileOutputStream != null) {
                OutputStreamWriter out = new OutputStreamWriter(fileOutputStream);
                out.write(personJson);
                out.flush();
                out.close();
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout(Context context) {
        context.deleteFile(Constants.USER_FILE);
        SharedPreUtil.setString(SharePrefConstant.TOKEN,"");
        SharedPreUtil.setString(SharePrefConstant.SHOP_BACK_ADDRESS,"");
        SharedPreUtil.setString(SharePrefConstant.SEARCH_HISTORY,"");
        this.user = null;
        EventBus.getDefault().post(new Action(Constants.ActionType.LOGOUT));
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
}
