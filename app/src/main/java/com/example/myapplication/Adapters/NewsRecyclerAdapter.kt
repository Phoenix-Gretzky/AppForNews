package com.example.myapplication.Adapters

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.text.Html
import android.text.format.DateUtils
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Model.Article
import com.example.myapplication.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class NewsRecyclerAdapter(context: Context, newsList: MutableList<Article>) :
    RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder>() {
    private val mContext: Context
    private val mNewsList: MutableList<Article>
    private var sharedPrefs: SharedPreferences? = null

    /**
     * Constructs a new [NewsRecyclerAdapter]
     * @param context of the app
     * @param newsList is the list of Article, which is the data source of the adapter
     */
    init {
        mContext = context
        mNewsList = newsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.news_tile, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mNewsList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView
        val authorTextView: TextView
        val dateTextView: TextView
        val thumbnailImageView: ImageView
        val trailTextView: TextView
        val cardView: CardView

        init {
            titleTextView = itemView.findViewById(R.id.title_card)
            authorTextView = itemView.findViewById(R.id.author_card)
            dateTextView = itemView.findViewById(R.id.date_card)
            thumbnailImageView = itemView.findViewById(R.id.thumbnail_image_card)
            trailTextView = itemView.findViewById(R.id.trail_text_card)
            cardView = itemView.findViewById(R.id.card_view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        setColor(holder)

        setTextSize(holder)

        // Find the current Article that was clicked on
        val currentNews: Article = mNewsList[position]
        holder.titleTextView.text=currentNews.title;
        // If the author does not exist, hide the authorTextView
        if (currentNews.author == null) {
            holder.authorTextView.visibility = View.GONE
        } else {
            holder.authorTextView.visibility = View.VISIBLE
            holder.authorTextView.setText(currentNews.author)
        }

        // Get time difference between the current date and web publication date and
        // set the time difference on the textView
        holder.dateTextView.text = getTimeDifference(formatDate(currentNews.publishedAt))

        // Get string of the description and convert Html text to plain text
        // and set the plain text on the textView
        val description: String = currentNews.description!!;
        holder.trailTextView.text = description

        // Set an OnClickListener to open a website with more information about the selected article
        holder.cardView.setOnClickListener(object :OnClickListener  {
            override fun onClick(view: View?) {
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                val newsUri: Uri = Uri.parse(currentNews.url)

                // Create a new intent to view the Article URI
                val websiteIntent = Intent(Intent.ACTION_VIEW, newsUri)
                websiteIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Send the intent to launch a new activity
                mContext.startActivity(websiteIntent)
            }
        })
        if (currentNews.urlToImage == null) {
            holder.thumbnailImageView.setVisibility(View.GONE)
        } else {
            holder.thumbnailImageView.setVisibility(View.VISIBLE)
            // Load image with glide
            Glide.with(mContext.getApplicationContext())
                .load(currentNews.urlToImage)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.error_icon)
                .into(holder.thumbnailImageView)
        }

    }

    private fun setColor(holder: ViewHolder) {
            holder.titleTextView.setBackgroundResource(R.color.white)
            holder.titleTextView.setTextColor(Color.BLACK)
    }


    private fun setTextSize(holder: ViewHolder) {

            holder.titleTextView.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                mContext.getResources().getDimension(R.dimen.sp22)
            )

            holder.trailTextView.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                mContext.getResources().getDimension(R.dimen.sp16)
            )
            holder.authorTextView.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                mContext.getResources().getDimension(R.dimen.sp14)
            )
            holder.dateTextView.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                mContext.getResources().getDimension(R.dimen.sp14)
            )

    }


    /**
     * Clear all data (a list of [Article] objects)
     */
    fun clearAll() {
        mNewsList.clear()
        notifyDataSetChanged()
    }

    /**
     * Add  a list of [Article]
     * @param newsList is the list of Article, which is the data source of the adapter
     */
    fun addAll(newsList: List<Article>?) {
        mNewsList.clear()
        mNewsList.addAll(newsList!!)
        notifyDataSetChanged()
    }

    /**
     * Convert date and time in UTC (webPublicationDate) into a more readable representation
     * in Local time
     *
     * @param dateStringUTC is the web publication date of the article (i.e. 2014-02-04T08:00:00Z)
     * @return the formatted date string in Local time(i.e "Jan 1, 2000  2:15 AM")
     * from a date and time in UTC
     */
    private fun formatDate(dateStringUTC: String?): String {
        // Parse the dateString into a Date object
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'")
        var dateObject: Date? = null
        try {
            dateObject = simpleDateFormat.parse(dateStringUTC)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        // Initialize a SimpleDateFormat instance and configure it to provide a more readable
        // representation according to the given format, but still in UTC
        val df = SimpleDateFormat("MMM d, yyyy  h:mm a", Locale.ENGLISH)
        val formattedDateUTC: String = df.format(dateObject)
        // Convert UTC into Local time
        df.setTimeZone(TimeZone.getTimeZone("UTC"))
        var date: Date? = null
        try {
            date = df.parse(formattedDateUTC)
            df.setTimeZone(TimeZone.getDefault())
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return df.format(date)
    }

    /**
     * Get the time difference between the current date and web publication date
     * @param formattedDate the formatted web publication date string
     * @return time difference (i.e "9 hours ago")
     */
    private fun getTimeDifference(formattedDate: String): CharSequence {
        val currentTime = System.currentTimeMillis()
        val publicationTime = getDateInMillis(formattedDate)
        return DateUtils.getRelativeTimeSpanString(
            publicationTime, currentTime,
            DateUtils.SECOND_IN_MILLIS
        )
    }

    companion object {
        /**
         * Get the formatted web publication date string in milliseconds
         * @param formattedDate the formatted web publication date string
         * @return the formatted web publication date in milliseconds
         */
        private fun getDateInMillis(formattedDate: String): Long {
            val simpleDateFormat = SimpleDateFormat("MMM d, yyyy  h:mm a")
            val dateInMillis: Long
            val dateObject: Date
            try {
                dateObject = simpleDateFormat.parse(formattedDate)
                dateInMillis = dateObject.getTime()
                return dateInMillis
            } catch (e: ParseException) {
                Log.e("Problem parsing date", e.message!!)
                e.printStackTrace()
            }
            return 0
        }
    }
}
