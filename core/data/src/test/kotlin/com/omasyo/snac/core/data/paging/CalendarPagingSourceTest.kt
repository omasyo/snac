package com.omasyo.snac.core.data.paging

import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.media.MovieRelease
import com.omasyo.snac.core.network.calendar.models.CalendarMediaResponse
import com.omasyo.snac.core.network.calendar.models.CalendarMovieResponse
import com.omasyo.snac.core.network.models.Ids
import com.omasyo.snac.core.network.models.MovieResponse
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CalendarPagingSourceTest {

    private val start = LocalDate.of(2026, 11, 14)

    private fun movie(slug: String, released: LocalDate?) = CalendarMovieResponse(
        movie = MovieResponse(ids = Ids(slug = slug, trakt = 1), year = 2026, title = slug),
        released = released,
    )

    private fun source(
        response: SnacResult<List<CalendarMediaResponse>> = SnacResult.Success(emptyList()),
        onCall: (LocalDate, Int) -> Unit = { _, _ -> },
    ) = CalendarPagingSource(windowDays = 7) { date, days ->
        onCall(date, days)
        response
    }

    @Test
    fun `refresh fetches window at key and sets keys either side`() = runBlocking {
        var called: Pair<LocalDate, Int>? = null
        val result = source(onCall = { d, n -> called = d to n })
            .load(LoadParams.Refresh(start, 20, false))

        assertEquals(start to 7, called)
        result as LoadResult.Page
        assertEquals(start.minusDays(7), result.prevKey)
        assertEquals(start.plusDays(7), result.nextKey)
    }

    @Test
    fun `empty window still pages on`() = runBlocking {
        val result = source().load(LoadParams.Append(start, 20, false)) as LoadResult.Page

        assertTrue(result.data.isEmpty())
        assertEquals(start.plusDays(7), result.nextKey)
        assertEquals(start.minusDays(7), result.prevKey)
    }

    @Test
    fun `movies without release date are dropped and items are sorted by date`() = runBlocking {
        val response = SnacResult.Success(
            listOf(
                movie("later", start.plusDays(2)),
                movie("undated", null),
                movie("sooner", start),
            )
        )
        val result = source(response).load(LoadParams.Refresh(start, 20, false)) as LoadResult.Page

        assertEquals(
            listOf("sooner", "later"),
            result.data.map { (it as MovieRelease).media.slug },
        )
    }

    @Test
    fun `failure becomes error`() = runBlocking {
        val failure = SnacResult.Failure(SnacResult.Error.NoConnection)
        val result = source(failure).load(LoadParams.Refresh(start, 20, false))

        assertTrue(result is LoadResult.Error)
    }
}
