package app.trailblazercombi.haventide.game2.jetpack.gamescreen.tilemap

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.trailblazercombi.haventide.game2.data.tilemap.TileMapData
import app.trailblazercombi.haventide.game2.jetpack.extensions.scrolling
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel

/**
 * This is the UI layer of [TileMapData].
 * @param viewModel The central [GameLoopViewModel]. Contains click states and similar.
 * @param tileMapData The [TileMapData] to be rendered. Contains intrinsic data. __DO NOT MODIFY__.
 */
@Composable
fun TileMap(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {
    // Keep the states here
    val scrollStateX = rememberScrollState()
    val scrollStateY = rememberScrollState()

    val externalScrollInputX by viewModel.stateOfScrollX.collectAsState()
    val externalScrollInputY by viewModel.stateOfScrollY.collectAsState()

    // If external ScrollState updates, update this...
    LaunchedEffect(externalScrollInputX) {
        scrollStateX.animateScrollTo(externalScrollInputX)
    }
    LaunchedEffect(externalScrollInputY) {
        scrollStateY.animateScrollTo(externalScrollInputY)
    }

    // The TileMap's background.
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(viewModel.mapData.backdropColor)
            .scrolling(scrollStateX, scrollStateY)
    ) {
        Column {
            for (y in 0 until viewModel.mapData.rows()) {
                Row {
                    for (x in 0 until viewModel.mapData.columns()) {
                        // Starting from this point, we have tile coordinates.
                        Tile(viewModel, viewModel.mapData[x, y])
                    }
                }
            }
        }
    }
}
