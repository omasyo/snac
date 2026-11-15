package com.omasyo.snac.core.network.search

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.NetworkSource
import com.omasyo.snac.core.network.search.models.MovieSearchResponse
import com.omasyo.snac.core.network.search.models.PersonSearchResponse
import com.omasyo.snac.core.network.search.models.ShowSearchResponse
import com.omasyo.snac.core.network.search.models.TrendingSearchResponse

interface SearchNetworkSource : NetworkSource {
    suspend fun getTrendingSearchResults(page: Int, limit: Int): SnacResult<List<TrendingSearchResponse>>

    suspend fun searchMovies(query: String, page: Int, limit: Int): SnacResult<List<MovieSearchResponse>>

    suspend fun searchShows(query: String, page: Int, limit: Int): SnacResult<List<ShowSearchResponse>>

    suspend fun searchPeople(query: String, page: Int, limit: Int): SnacResult<List<PersonSearchResponse>>
}
