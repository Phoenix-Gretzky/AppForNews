package com.example.myapplication.Model

data class Article( var source: Source? = null,  var content: String? = null,
                    var publishedAt: String? = null,  var urlToImage: String? = null,
                    var url: String? = null,  var description: String? = null,
                    var title: String? = null,  var author: String? = null
)
{
}
