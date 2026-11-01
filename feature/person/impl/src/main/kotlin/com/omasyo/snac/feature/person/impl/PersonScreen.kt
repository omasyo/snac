package com.omasyo.snac.feature.person.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.data.models.CreditRole
import com.omasyo.snac.core.data.models.PersonCredit
import com.omasyo.snac.core.data.models.PersonDetails
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.IconButton
import com.omasyo.snac.core.ui.components.TabRow
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.bottomBarPadding
import com.omasyo.snac.core.ui.format.formatCount
import com.omasyo.snac.core.ui.format.formatFullDate
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.map
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.models.transpose
import com.omasyo.snac.core.ui.modifiers.Side
import com.omasyo.snac.core.ui.modifiers.film
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import dev.zacsweers.metrox.viewmodel.metroViewModel
import java.time.LocalDate
import java.time.Period
import com.omasyo.snac.core.ui.R as UiR

@Composable
fun PersonScreen(
    onBackClick: () -> Unit,
    onMediaClick: (Media) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PersonViewModel = metroViewModel(),
) {
    PersonScreen(
        onBackClick = onBackClick,
        onMediaClick = onMediaClick,
        person = viewModel.person.collectAsStateWithLifecycle().value,
        selectedRole = viewModel.selectedRole.collectAsStateWithLifecycle().value,
        onSelectedRoleChange = viewModel::changeRole,
        selectedMediaFilter = viewModel.selectedMediaFilter.collectAsStateWithLifecycle().value,
        onSelectedMediaFilterChange = viewModel::changeMediaFilter,
        credits = viewModel.credits.collectAsStateWithLifecycle().value,
        modifier = modifier,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PersonScreen(
    onBackClick: () -> Unit,
    onMediaClick: (Media) -> Unit,
    person: UiState<PersonDetails>,
    selectedRole: CreditRole,
    onSelectedRoleChange: (CreditRole) -> Unit,
    selectedMediaFilter: MediaFilter,
    onSelectedMediaFilterChange: (MediaFilter) -> Unit,
    credits: UiState<List<PersonCredit>>,
    modifier: Modifier = Modifier,
) {
    val cardModifier = creditCardModifier()

    Box(modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(SnacColors.page),
        ) {
            item("details") {
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.padding(bottom = 24.dp),
                ) {
                    PersonHero(state = person)
                    person.map { it.summary }.transpose()?.let {
                        PersonSummary(
                            state = it,
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )
                    }
                    when (person) {
                        is UiState.Success -> PersonInfoClapper(
                            info = person.content.info(),
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                        )

                        else -> PersonInfoClapperPlaceholder(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                        )
                    }
                    person.map { it.biography }.transpose()?.let {
                        BiographyCard(
                            state = it,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                        )
                    }
                    FlowRow(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                    ) {
                        TabRow(
                            selectedTab = selectedRole,
                            onSelectedTabChange = onSelectedRoleChange,
                        )
                        TabRow(
                            selectedTab = selectedMediaFilter,
                            onSelectedTabChange = onSelectedMediaFilterChange,
                        )
                    }
                }
            }

            when (credits) {
                is UiState.Error -> {
                    item("credits-error") {
                        ErrorBox(
                            description = credits.error.message,
                            onRetry = credits.refresh,
                            alignment = Alignment.TopCenter,
                            errorPadding = PaddingValues(top = 120.dp),
                        ) { CreditsPlaceholder(cardModifier) }
                    }
                }

                UiState.Loading -> {
                    item("credits-loading") { CreditsPlaceholder(cardModifier) }
                }

                is UiState.Success -> {
                    if (credits.content.isEmpty()) {
                        item("credits-empty") {
                            Text(
                                text = stringResource(R.string.credits_empty),
                                style = SnacTypography.hand,
                                color = SnacColors.muted,
                                modifier = Modifier.padding(horizontal = 16.dp),
                            )
                        }
                    }
                    items(
                        items = credits.content,
                        key = { "${it.media::class.simpleName}-${it.media.slug}-${it.role}-${it.description}" },
                    ) { credit ->
                        CreditCard(
                            credit = credit,
                            onClick = { onMediaClick(credit.media) },
                            modifier = Modifier
                                .animateItem(
                                    fadeInSpec = null,
                                    fadeOutSpec = null,
                                )
                                .then(cardModifier),
                        )
                    }
                }
            }

            item("bottom-padding") {
                Spacer(
                    Modifier
                        .bottomBarPadding()
                        .navigationBarsPadding()
                )
            }
        }
        IconButton(
            icon = UiR.drawable.caret_left_bold,
            contentDescription = stringResource(R.string.back),
            onClick = onBackClick,
            backgroundColor = SnacColors.chip,
            contentColor = SnacColors.ink,
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 16.dp, top = 16.dp)
                .size(48.dp),
        )
    }
}

@Composable
private fun creditCardModifier(): Modifier {
    val stripeColor = SnacColors.accent
    return Modifier
        .padding(horizontal = 16.dp)
        .drawBehind {
            val stripeWidth = CreditStripeWidth.toPx()
            drawRect(
                color = stripeColor,
                topLeft = Offset(size.width - stripeWidth, 0f),
                size = Size(stripeWidth, size.height),
            )
        }
        .padding(end = CreditStripeWidth)
        .film(
            side = Side.Start,
            crossEdgeInset = 12.dp,
        )
        .background(SnacColors.film)
}

private val CreditStripeWidth = 6.dp

@Composable
private fun CreditsPlaceholder(cardModifier: Modifier) {
    Column {
        repeat(10) {
            CreditCardPlaceholder(cardModifier)
        }
    }
}

@Composable
private fun PersonSummary(
    state: UiState<String>,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is UiState.Success -> {
            Text(
                text = state.content,
                style = SnacTypography.hand,
                modifier = modifier,
            )
        }

        else -> {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier
                        .height(16.dp)
                        .fillMaxWidth()
                        .background(SnacColors.faint)
                )
                Box(
                    modifier
                        .size(200.dp, 16.dp)
                        .background(SnacColors.faint)
                )
            }
        }
    }
}

@Composable
private fun PersonDetails.info(): List<PersonInfo> = buildList {
    birthday?.let {
        add(
            PersonInfo(
                label = stringResource(R.string.info_born),
                value = it.formatFullDate(),
                detail = stringResource(
                    R.string.info_age,
                    Period.between(it, death ?: LocalDate.now()).years,
                ),
            )
        )
    }
    birthCity?.let {
        add(
            PersonInfo(
                label = stringResource(R.string.info_from),
                value = it,
                detail = birthCountry,
            )
        )
    }
    add(
        PersonInfo(
            label = stringResource(R.string.info_in_lists),
            value = listCount.formatCount(),
            detail = stringResource(R.string.info_on_trakt),
        )
    )
}

@Preview(device = "spec:width=1080px,height=5000px,dpi=440")
@Composable
private fun Preview() {
    var role by remember { mutableStateOf(CreditRole.Acting) }
    var filter by remember { mutableStateOf(MediaFilter.All) }
    SnacTheme {
        PersonScreen(
            onBackClick = {},
            onMediaClick = {},
            person = UiState.Success(PersonDetails.PreviewSample),
            selectedRole = role,
            onSelectedRoleChange = { role = it },
            selectedMediaFilter = filter,
            onSelectedMediaFilterChange = { filter = it },
            credits = UiState.Success(PersonCredit.PreviewSample.filter { it.role == role }),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(device = "spec:width=1080px,height=5000px,dpi=440")
@Composable
private fun LoadingPreview() {
    SnacTheme {
        PersonScreen(
            onBackClick = {},
            onMediaClick = {},
            person = UiState.Loading,
            selectedRole = CreditRole.Acting,
            onSelectedRoleChange = {},
            selectedMediaFilter = MediaFilter.All,
            onSelectedMediaFilterChange = {},
            credits = UiState.Loading,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(device = "spec:width=1080px,height=5000px,dpi=440")
@Composable
private fun ErrorPreview() {
    val error = UiState.Error(SnacResult.Error.Unexpected) {}
    SnacTheme {
        PersonScreen(
            onBackClick = {},
            onMediaClick = {},
            person = error,
            selectedRole = CreditRole.Acting,
            onSelectedRoleChange = {},
            selectedMediaFilter = MediaFilter.All,
            onSelectedMediaFilterChange = {},
            credits = error,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
