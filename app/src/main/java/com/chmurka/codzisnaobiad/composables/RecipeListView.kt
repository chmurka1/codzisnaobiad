package com.chmurka.codzisnaobiad.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.NoPhotography
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.chmurka.codzisnaobiad.database.Recipe
import com.chmurka.codzisnaobiad.database.RecipeEvent
import com.chmurka.codzisnaobiad.database.RecipeState
import java.lang.Integer.max

data class MeasuredRow(
    val items: MutableList<Placeable>,
    var width: Int,
    var height: Int
)

/* Taken from https://stackoverflow.com/questions/61022452/any-ideas-on-the-list-with-fit-wrap-width-on-item-in-jetpack-compose */
//TODO: rewrite this
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
    verticalGap: Dp = 0.dp,
    horizontalGap: Dp = 0.dp,
    content: @Composable () -> Unit
) = Layout(content, modifier) { measurables, constraints ->
    val hGapPx = horizontalGap.roundToPx()
    val vGapPx = verticalGap.roundToPx()

    val rows = mutableListOf<MeasuredRow>()
    val itemConstraints = constraints.copy(minWidth = 0)

    for (measurable in measurables) {
        val lastRow = rows.lastOrNull()
        val placeable = measurable.measure(itemConstraints)

        if (lastRow != null && lastRow.width + hGapPx + placeable.width <= constraints.maxWidth) {
            lastRow.items.add(placeable)
            lastRow.width += hGapPx + placeable.width
            lastRow.height = max(lastRow.height, placeable.height)
        } else {
            val nextRow = MeasuredRow(
                items = mutableListOf(placeable),
                width = placeable.width,
                height = placeable.height
            )

            rows.add(nextRow)
        }
    }

    val width = rows.maxOfOrNull { row -> row.width } ?: 0
    val height = rows.sumBy { row -> row.height } + max(vGapPx.times(rows.size - 1), 0)

    val coercedWidth = width.coerceIn(constraints.minWidth, constraints.maxWidth)
    val coercedHeight = height.coerceIn(constraints.minHeight, constraints.maxHeight)

    layout(coercedWidth, coercedHeight) {
        var y = 0

        for (row in rows) {
            var x = when(alignment) {
                Alignment.Start -> 0
                Alignment.CenterHorizontally -> (coercedWidth - row.width) / 2
                Alignment.End -> coercedWidth - row.width

                else -> throw Exception("unsupported alignment")
            }

            for (item in row.items) {
                item.place(x, y)
                x += item.width + hGapPx
            }

            y += row.height + vGapPx
        }
    }
}


@Composable
fun RecipeCard(
    recipe : Recipe,
    tags : List<String>,
    onClick : () -> Unit,
    onDatabaseEvent : (RecipeEvent) -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
                text = recipe.name,
                style = MaterialTheme.typography.titleLarge
            )
        },
        supportingContent = {
            Column {
                FlowRow(verticalGap = 0.dp, horizontalGap = 5.dp) {
                    for (tag in tags) {
                        AssistChip(
                            modifier = Modifier.defaultMinSize(minHeight = 20.dp),
                            onClick = {},
                            label = {
                                Text(text = tag)
                            })
                    }
                }
                Text("Czas przygotowania: - min")
                Text("Kalorie: - kcal")
            }
        },
        leadingContent = {
            Icon(Icons.Filled.NoPhotography, contentDescription = null, Modifier.size(50.dp))
                         },
        trailingContent = {
            Row() {
                IconButton(onClick = { onDatabaseEvent(RecipeEvent.DeleteRecipe(recipe)) }) {
                    Icon(Icons.Filled.Delete, contentDescription = null)
                }
            }
        },
        modifier = Modifier.clickable {
                onDatabaseEvent(RecipeEvent.SetCurrentRecipe(recipe))
                onClick()
            }
    )
}

@Composable
fun RecipeList(
    innerPadding: PaddingValues = PaddingValues(),
    recipeState : RecipeState,
    onRecipeClick: () -> Unit,
    onEvent : (RecipeEvent) -> Unit,
) {
    LazyColumn(
        contentPadding = innerPadding,
    ) {
        /*val recipes : List<RecipeHeader> = arrayListOf(
            RecipeHeader("Currywurst", arrayListOf("tanie", "comfort food")),
            RecipeHeader("Zupa krem z soczewicy", arrayListOf("tanie", "obiadowe", "zdrowe")),
            RecipeHeader("Pierogi ruskie", arrayListOf("tanie", "obiadowe", "comfort food")),
            RecipeHeader("Pad thai", arrayListOf("we dwoje", "obiadowe")),
            RecipeHeader("Muesli z owocami", arrayListOf("tanie", "szybkie", "śniadania", "zdrowe")),
            RecipeHeader("Ciasto biszkoptowe z brzoskwiniami", arrayListOf("desery")),
            RecipeHeader("Owsianka kokosowo-czekoladowa z malinami", arrayListOf("tanie", "szybkie", "śniadania", "desery", "zdrowe", "comfort food")),
            RecipeHeader("Spaghetti z sosem bolońskim", arrayListOf("obiadowe", "comfort food", "we dwoje")),
        )*/
        items(recipeState.recipes) {
            RecipeCard(recipe = it, tags = arrayListOf("obiadowe"/*, "comfort food", "we dwoje"*/), onClick = { onRecipeClick() }, onDatabaseEvent = { e -> onEvent(e)})
            Divider()
        }
    }
}