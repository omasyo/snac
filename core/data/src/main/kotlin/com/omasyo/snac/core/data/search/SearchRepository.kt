package com.omasyo.snac.core.data.search

import androidx.paging.PagingData
import com.omasyo.snac.core.data.models.Person
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.model.media.TrendingSearchItem
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getTrendingSearchResults(): Flow<PagingData<TrendingSearchItem>>

    fun searchMovies(query: String): Flow<PagingData<Movie>>

    fun searchShows(query: String): Flow<PagingData<Show>>

    fun searchPeople(query: String): Flow<PagingData<Person>>
}
