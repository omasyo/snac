package com.omasyo.snac.core.network.search

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.safeCall
import com.omasyo.snac.core.network.search.models.MovieSearchResponse
import com.omasyo.snac.core.network.search.models.PersonSearchResponse
import com.omasyo.snac.core.network.search.models.ShowSearchResponse
import com.omasyo.snac.core.network.search.models.TrendingSearchResponse
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

@ContributesBinding(AppScope::class)
@Inject
class SearchNetworkSourceImpl(
    private val client: HttpClient
) : SearchNetworkSource {
    override suspend fun getTrendingSearchResults(
        page: Int,
        limit: Int
    ): SnacResult<List<TrendingSearchResponse>> =
        safeCall {
            client.get("search/recent_by_id/global/movies,shows") {
                parameter("extended", "full")
                parameter("page", page)
                parameter("limit", limit)
            }
        }

    override suspend fun searchMovies(
        query: String,
        page: Int,
        limit: Int
    ): SnacResult<List<MovieSearchResponse>> =
        safeCall {
            client.get("search/movie") {
                parameter("extended", "full")
                parameter("query", query)
                parameter("page", page)
                parameter("limit", limit)
            }
        }

    override suspend fun searchShows(
        query: String,
        page: Int,
        limit: Int
    ): SnacResult<List<ShowSearchResponse>> =
        safeCall {
            client.get("search/show") {
                parameter("extended", "full")
                parameter("query", query)
                parameter("page", page)
                parameter("limit", limit)
            }
        }

    override suspend fun searchPeople(
        query: String,
        page: Int,
        limit: Int
    ): SnacResult<List<PersonSearchResponse>> =
        safeCall {
            client.get("search/person") {
                parameter("extended", "full")
                parameter("query", query)
                parameter("page", page)
                parameter("limit", limit)
            }
        }
}
