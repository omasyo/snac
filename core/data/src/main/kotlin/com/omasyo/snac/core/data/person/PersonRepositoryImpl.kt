package com.omasyo.snac.core.data.person

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.common.map
import com.omasyo.snac.core.data.mappers.toPersonCredits
import com.omasyo.snac.core.data.mappers.toPersonDetails
import com.omasyo.snac.core.data.models.PersonCredit
import com.omasyo.snac.core.data.models.PersonDetails
import com.omasyo.snac.core.network.person.PersonNetworkSource
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@ContributesBinding(AppScope::class)
@Inject
class PersonRepositoryImpl(
    private val personNetworkSource: PersonNetworkSource,
) : PersonRepository {
    override suspend fun getDetails(slug: String): SnacResult<PersonDetails> =
        personNetworkSource.getDetails(slug).map { it.toPersonDetails() }

    override suspend fun getCredits(slug: String): SnacResult<List<PersonCredit>> = coroutineScope {
        val movies = async { personNetworkSource.getMovieCredits(slug).map { it.toPersonCredits() } }
        val shows = async { personNetworkSource.getShowCredits(slug).map { it.toPersonCredits() } }
        val movieCredits = when (val result = movies.await()) {
            is SnacResult.Failure -> return@coroutineScope result
            is SnacResult.Success -> result.value
        }
        val showCredits = when (val result = shows.await()) {
            is SnacResult.Failure -> return@coroutineScope result
            is SnacResult.Success -> result.value
        }
        SnacResult.Success(
            (movieCredits + showCredits)
                // The list is keyed by media, role and description, so drop exact repeats. // TODO
                .distinctBy { Triple(it.media, it.role, it.description) }
                .sortedWith(compareByDescending(nullsFirst()) { it.media.year })
        )
    }
}
