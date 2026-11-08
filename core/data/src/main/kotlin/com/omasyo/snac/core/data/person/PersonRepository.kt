package com.omasyo.snac.core.data.person

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.data.models.PersonCredit
import com.omasyo.snac.core.data.models.PersonDetails

interface PersonRepository {
    suspend fun getDetails(slug: String): SnacResult<PersonDetails>

    suspend fun getCredits(slug: String): SnacResult<List<PersonCredit>>
}
