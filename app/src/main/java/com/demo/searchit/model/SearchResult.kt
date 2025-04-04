package com.demo.searchit.model

import kotlinx.serialization.Serializable

data class SearchResponse (val success: Boolean, val count: Int, val results: List<SearchResult>)
data class  SearchResult(val key:String, val size:Int,val uploaded:String, val url:String)