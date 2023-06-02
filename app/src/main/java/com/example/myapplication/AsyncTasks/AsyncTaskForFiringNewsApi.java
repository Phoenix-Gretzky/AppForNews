package com.example.myapplication.AsyncTasks;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.example.myapplication.Model.Article;
import com.example.myapplication.ViewModels.ViewModelsForMainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AsyncTaskForFiringNewsApi extends AsyncTask<String, Void, String> {

    Context mContext;
    ViewModelsForMainActivity viewModel;
    String requestUrl;
    Context context;

    public AsyncTaskForFiringNewsApi(String requestUrl, ViewModelsForMainActivity viewModel,Context context) {
        super();
        this.viewModel = viewModel;
        this.requestUrl = requestUrl;
        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        StrictMode.enableDefaults();
    }

    @Override
    public String doInBackground(String... strings) {
        String response = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream in = conn.getInputStream();
            response = MakeCall(in);
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;

    }

    private String MakeCall(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }


    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if (response != null && !response.trim().isEmpty()) {
            try {
                //convert response into json object
                JSONObject result = new JSONObject(response);
                if (result.get("status").equals("ok")) {
                    if (ReturnIfNullOrEmpty(result, "articles") ) {
//                    GlobalClassForFunctions.hideProgresDialogueWithNoDelay();

                         ArrayList<Article> newsModel= new Gson().fromJson(result.getJSONArray("articles").toString(), new TypeToken<ArrayList<Article>>() {
                        }.getType());
                        viewModel.updateLiveDataForObservableLiveDataForNewsApi(newsModel);
                    }
                }
            } catch (Exception e) {
                // hide progress bar
//                GlobalClassForFunctions.hideProgresDialogue();
//                GlobalClassForFunctions.getInstance().PrintMessageOnTheConsole("Some error occurred " + e);
            }
        } else {
//            GlobalClassForFunctions.showAlertdialog(mContext.getResources().getString(R.string.error), mContext.getResources().getString(R.string.admin_error_occurred));
        }
    }


    // show custom dialog


    public boolean ReturnIfNullOrEmpty(JSONObject object, String stringToCheckForNull) {
        try {
            return !object.isNull(stringToCheckForNull);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

