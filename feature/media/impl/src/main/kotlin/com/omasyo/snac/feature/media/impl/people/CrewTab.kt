package com.omasyo.snac.feature.media.impl.people


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.data.models.Crew
import com.omasyo.snac.core.data.models.Person
import com.omasyo.snac.core.ui.R
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.Icon
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

@Composable
fun ColumnScope.CrewTab(
    onPersonClick: (Person) -> Unit,
    crewGroup: UiState<Map<String, List<Crew>>>,
    listState: LazyListState,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val modifier = modifier.weight(1f)
    Box(modifier) {
        val placeholder =
            remember {
                movableContentOf<Boolean> { scrollable ->
                    Placeholder(
                        listState,
                        scrollable,
                        contentPadding,
                        modifier,
                    )
                }
            }

        when (crewGroup) {
            is UiState.Error -> {
                ErrorBox(
                    description = crewGroup.error.message,
                    onRetry = crewGroup.refresh,
                ) {
                    placeholder(false)
                }
            }


            UiState.Loading -> placeholder(true)

            is UiState.Success -> {
                LazyColumn(
                    state = listState,
                    contentPadding = contentPadding,
                    modifier = modifier,
                ) {
                    for ((group, people) in crewGroup.content) {
                        item(
                            key = group,
                            contentType = "group-header",
                        ) {
                            Text(
                                text = group,
                                style = SnacTypography.overline,
                                modifier = Modifier.padding(bottom = 12.dp),
                            )
                        }
                        itemsIndexed(
                            items = people,
                            key = { _: Int, crew: Crew -> "${crew.person.slug}-${crew.job}" },
                            contentType = { _: Int, _: Crew -> "crew" },
                        ) { index: Int, crew: Crew ->
                            CrewCard(
                                crew = crew,
                                onClick = { onPersonClick(crew.person) },
                                index = index,
                                lastIndex = people.lastIndex,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CrewCard(
    crew: Crew,
    onClick: () -> Unit,
    index: Int,
    lastIndex: Int,
    modifier: Modifier = Modifier,
) {
    val borderColor = SnacColors.outline
    val dashColor = SnacColors.rule

    val indicationColor = SnacColors.muted

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(bottom = if (index != lastIndex) 0.dp else 24.dp)
            .silhouette()
            .clickable(
                onClick = onClick,
                indication = remember { TintIndication { indicationColor } },
                interactionSource = null,
            )
            .crewCardFrame(index, lastIndex, borderColor, dashColor)
            .background(SnacColors.card)
            .padding(12.dp),
    ) {
        Image(
            imageUrl = crew.person.imageUrl,
            modifier = Modifier
                .size(40.dp)
                .border(2.dp, SnacColors.outline, CircleShape)
                .clip(CircleShape)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = crew.person.name,
                style = SnacTypography.subtitle,
            )
            Text(
                text = crew.job,
                style = SnacTypography.caption,
                color = SnacColors.muted,
            )
        }
        Icon(
            icon = R.drawable.caret_right_bold,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
        )
    }
}


@Composable
private fun Placeholder(
    listState: LazyListState,
    scrollable: Boolean,
    contentPadding: PaddingValues,
    modifier: Modifier,
) {
    val borderColor = SnacColors.outline
    val dashColor = SnacColors.rule
    LazyColumn(
        state = listState,
        contentPadding = contentPadding,
        userScrollEnabled = scrollable,
        modifier = modifier,
    ) {
        PlaceholderGroupSizes.forEachIndexed { groupIndex, groupSize ->
            item(contentType = "group-header") {
                Box(
                    Modifier
                        .padding(bottom = 12.dp)
                        .width(remember(groupIndex) { Random(groupIndex + 40).nextInt(64, 112).dp })
                        .height(10.dp)
                        .background(SnacColors.placeholder)
                )
            }
            items(groupSize, contentType = { "crew" }) { index ->
                val lastIndex = groupSize - 1
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(bottom = if (index != lastIndex) 0.dp else 24.dp)
                        .crewCardFrame(index, lastIndex, borderColor, dashColor)
                        .background(SnacColors.card)
                        .fillMaxWidth()
                        .padding(12.dp),
                ) {
                    Box(
                        Modifier
                            .size(40.dp)
                            .border(2.dp, SnacColors.outline, CircleShape)
                            .clip(CircleShape)
                            .background(imageGradient())
                    )
                    Box(
                        Modifier
                            .width(
                                remember(groupIndex, index) {
                                    Random(groupIndex * 10 + index).nextInt(96, 196).dp
                                }
                            )
                            .height(16.dp)
                            .background(SnacColors.placeholder)
                    )
                }
            }
        }
    }
}

private val PlaceholderGroupSizes = listOf(1, 2, 3, 1, 2)

private fun Modifier.crewCardFrame(
    index: Int,
    lastIndex: Int,
    borderColor: Color,
    dashColor: Color,
): Modifier = drawWithContent {
    drawContent()
    val strokeWidth = 2.dp.toPx()
    if (index == 0) {
        drawRect(
            borderColor,
            Offset(0f, 0f),
            size.copy(height = strokeWidth)
        )
    } else {
        drawLine(
            color = dashColor,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = strokeWidth,
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(
                    4.dp.toPx(),
                    2.dp.toPx()
                )
            )
        )
    }
    if (index == lastIndex) {
        drawRect(
            borderColor,
            Offset(0f, size.height),
            size.copy(height = strokeWidth)
        )
    }
    drawRect(
        borderColor,
        Offset(0f, 0f),
        size.copy(width = strokeWidth)
    )
    drawRect(
        borderColor,
        Offset(size.width, 0f),
        size.copy(width = strokeWidth)
    )
}

internal val PreviewCrewGroup = mapOf(
    "Directing" to listOf(
        Crew(
            jobs = listOf("Director"),
            person = Person(
                name = "Guillermo del Toro",
                slug = "guillermo-del-toro",
                imageUrl = null,
            ),
        ),
    ),
    "Writing" to listOf(
        Crew(
            jobs = listOf("Screenplay"),
            person = Person(
                name = "Guillermo del Toro",
                slug = "guillermo-del-toro",
                imageUrl = null,
            ),
        ),
        Crew(
            jobs = listOf("Novel"),
            person = Person(
                name = "Mary Shelley",
                slug = "mary-shelley",
                imageUrl = null,
            ),
        ),
    ),
    "Production" to listOf(
        Crew(
            jobs = listOf("Producer"),
            person = Person(
                name = "J. Miles Dale",
                slug = "j-miles-dale",
                imageUrl = null,
            ),
        ),
        Crew(
            jobs = listOf("Executive Producer"),
            person = Person(
                name = "Scott Stuber",
                slug = "scott-stuber",
                imageUrl = null,
            ),
        ),
    ),
    "Camera" to listOf(
        Crew(
            jobs = listOf("Director of Photography"),
            person = Person(
                name = "Dan Laustsen",
                slug = "dan-laustsen",
                imageUrl = null,
            ),
        ),
    ),
    "Sound" to listOf(
        Crew(
            jobs = listOf("Original Music Composer"),
            person = Person(
                name = "Alexandre Desplat",
                slug = "alexandre-desplat",
                imageUrl = null,
            ),
        ),
    ),
)

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        Column(Modifier.background(SnacColors.page)) {
            CrewTab(
                onPersonClick = { },
                crewGroup = UiState.Success(PreviewCrewGroup),
                listState = rememberLazyListState(),
                contentPadding = PaddingValues(16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun LoadingToErrorPreview() {
    var state by remember { mutableStateOf<UiState<Map<String, List<Crew>>>>(UiState.Loading) }

    LaunchedEffect(Unit) {
        delay(3.seconds)
        state = UiState.Error(SnacResult.Error.Unexpected) {}
    }

    SnacTheme {
        Column(Modifier.background(SnacColors.page)) {
            CrewTab(
                onPersonClick = { },
                crewGroup = state,
                listState = rememberLazyListState(),
                contentPadding = PaddingValues(16.dp),
            )
        }
    }
}