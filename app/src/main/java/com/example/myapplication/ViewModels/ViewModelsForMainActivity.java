package com.example.myapplication.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Model.Article;

import java.util.ArrayList;

public class ViewModelsForMainActivity extends AndroidViewModel {


    private static final MutableLiveData<ArrayList<Article>> MutableLiveDataForNewsApi = new MutableLiveData<>();
    private final LiveData<ArrayList<Article>> ObservableLiveDataForNewsApi;

    public ViewModelsForMainActivity(@NonNull Application application) {
        super(application);

        ObservableLiveDataForNewsApi = getMutableLiveDataForNewsApi();


    }
    @Override
    public void onCleared() {
      MutableLiveDataForNewsApi.setValue(null);
        super.onCleared();
    }

    private LiveData<ArrayList<Article>> getMutableLiveDataForNewsApi() {
        return MutableLiveDataForNewsApi;
    }


    public void updateLiveDataForObservableLiveDataForNewsApi(ArrayList<Article> notifyRecords) {
        MutableLiveDataForNewsApi.setValue(notifyRecords);
    }

    public LiveData<ArrayList<Article>> getObservableLiveDataForNewsApi() {
        return ObservableLiveDataForNewsApi;
    }

}
