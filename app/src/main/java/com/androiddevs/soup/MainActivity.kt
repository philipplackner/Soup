package com.androiddevs.soup

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.recycler_view_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Thread {

        }
        Thread {
            val url = "https://www.nytimes.com/section/technology"
            val document = Jsoup.connect(url).get()

            val articles = mutableListOf<ArticlePreview>()

            // this is the path to the list of the newest tech articles
            val articleHTML = document.getElementById("stream-panel")
                .select("div").first().select("ol")
                .select("div").select("div").select("a")

            for(item in articleHTML) {
                // for each article preview we need to find the path to
                // the imgUrl, its title and its description
                val imgURL = item.select("div").select("figure")
                    .select("div").select("img").attr("src")
                val title = item.select("h2").text()
                val description = item.select("p").text()

                if(!imgURL.isNullOrEmpty() || !title.isNullOrEmpty() || !description.isNullOrEmpty()) {
                    val article = ArticlePreview(imgURL, title, description)
                    articles.add(article)
                }
            }

            val adapter = RecyclerViewAdapter(articles)
            rvArticles.rootView.post {
                rvArticles.adapter = adapter
                rvArticles.layoutManager = LinearLayoutManager(this)
                progressBar.visibility = View.GONE
            }

        }.start()

    }


}
