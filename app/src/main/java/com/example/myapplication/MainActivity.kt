package com.example.myapplication

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Adapters.NewsRecyclerAdapter
import com.example.myapplication.AsyncTasks.AsyncTaskForFiringNewsApi
import com.example.myapplication.Model.Article
import com.example.myapplication.ViewModels.ViewModelsForMainActivity
import com.example.myapplication.databinding.MainActivityBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var viewmodel: ViewModelsForMainActivity;
    lateinit var binding: MainActivityBinding;
    // Create a Comparator that compares two RecyclerView items based on the date they were received.
    val dateComparator = DateComparator()
    lateinit var articlesArray: ArrayList<Article>;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        initializeViewModel();
        observeViewModelLiveData();
        binding.LoadingIndicator.show();
        AsyncTaskForFiringNewsApi("https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json",viewmodel,applicationContext).execute();
      }


    private fun observeViewModelLiveData() {
        viewmodel.observableLiveDataForNewsApi.observe(this, Observer {
//            Toast.makeText(applicationContext,it.toString(),Toast.LENGTH_SHORT).show();
            System.out.println(" this is the start"+it.toString()+" this is the output");
            setReyclerForNews(it);
            binding.LoadingIndicator.hide();
        })
    }

    private fun setReyclerForNews(articles: ArrayList<Article>) {
        binding.NewsRecycler.layoutManager=LinearLayoutManager(applicationContext);
        articlesArray=articles;

// Sort the RecyclerView items using the Comparator.
//        Collections.sort(articlesArray, dateComparator)
        binding.NewsRecycler.adapter=NewsRecyclerAdapter(applicationContext,articlesArray);
    }

    private fun initializeViewModel() {
        viewmodel=ViewModelProvider(this).get(ViewModelsForMainActivity::class.java);
    }

    fun clicked(view: View) {
        // Reverse the sort order of the RecyclerView items.
        Collections.reverseOrder(dateComparator);

        // Sort the RecyclerView items using the Comparator.
        Collections.sort(articlesArray, dateComparator);

        // Notify the adapter that the data has changed.
        binding.NewsRecycler.adapter=NewsRecyclerAdapter(applicationContext,articlesArray);
    }

    class DateComparator : Comparator<Article?> {
        override fun compare(item1: Article?, item2: Article?): Int {
            // Convert the dates from string UTC format to Date objects.
            val date1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'").parse(item1!!.publishedAt)
            val date2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'").parse(item2!!.publishedAt)

            return date1.compareTo(date2);
        }


    }



}