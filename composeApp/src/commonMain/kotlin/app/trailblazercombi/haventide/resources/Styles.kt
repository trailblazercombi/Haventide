package app.trailblazercombi.haventide.resources

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Defines the style of [tiles][app.trailblazercombi.haventide.game.ComposableTile]
 * and [the tile map][app.trailblazercombi.haventide.game.ComposableTileMap].
 */
object TileStyle {
    val TileSize = 128.dp
    val CornerRounding = 0.dp
    val OutlineThickness = 2.dp
    val Padding = 16.dp
}

/**
 * Defines the style of
 * [ComposablePhoenixMechanismBall][app.trailblazercombi.haventide.game.mechanisms.ComposablePhoenixMechanismBall].
 */
object PhoenixBallStyle {
    val OutlineThickness = 4.dp
    val Padding = 32.dp
    val AffiliationIconSize = 28.dp
    val AffiliationIconOuterOffsetFromBottomRight = 12.dp
    val AffiliationIconInnerPadding = 4.dp
    val AllyHpIndicatorWidth = 12.dp
    val AllyHpIndicatorPadding = 12.dp
}

object GameScreenDialogBoxStyle {
    val Elevation = 12.dp
    val LargeTextSize = 36.sp
    val LargeTextLineSeparation = 50.sp
    val TitleTextSize = 32.sp
    val ButtonTextSize = 14.sp
    val InnerPadding = 8.dp
    val GameOverInnerPadding = 32.dp
    val OuterCornerRounding = 8.dp
    val ButtonCornerRounding = 4.dp
    val OutlineThickness = 0.dp
    val StretchedDialogOffsetFromEdge = 64.dp
    val TapAnywhereLabelOffset = 128.dp
    val YesNoDialogButtonMaxWidth = 360.dp
}

object GameScreenTopBubbleStyle {
    val CornerRounding = 16.dp
    val OutlineThickness = 0.dp
    val Elevation = 12.dp
    val OffsetFromEdge = 16.dp
    val InnerOffset = 8.dp
    val BubbleHeight = 56.dp
    val DoubleBubbleHeight = 80.dp
    val MiniatureWidth = 64.dp
    val MiniatureHeight = 32.dp
    val MiniatureCornerRounding = 8.dp
    val RoundCounterWidth = 128.dp
    val StandardButtonWidth = 256.dp
    val RoundCounterTextSize = 12.sp
    val UnifiedRoundTeamTextSize = 14.sp
    val TeamTurnTextSize = 16.sp
    val FillColorModifier = Palette.Abyss90
    val OutlineColorModifier = Palette.Glass00
}

object DieStyle {
    val DieSize = 40.dp
    val InnerPadding = 2.dp
    val CornerRounding = 6.dp
    val OutlineThickness = 0.dp
    val Separation = 4.dp
    val EndRoundButtonSeverity = ButtonSeverity.PREFERRED
    val CounterButtonSeverity = ButtonSeverity.DIMMED
    val ConfirmButtonSeverity = ButtonSeverity.NEUTRAL
}

object DiceCounterStyle {
    val HorizontalTextSize = 18.sp
    val VerticalTextSize = 18.sp
    val VerticalSeparation = 6.dp
}

object CiStyle {
    val MaxAbilityCardWidth = 330.dp
    val AbilityCardHeight = 64.dp
    val AbilityCardPadding = 8.dp
    val TitleSize = 18.sp
    val DescriptionSize = 14.sp
    val AbilityCardShrinkImage = 4.dp
    val PhoenixCardShrinkImage = 12.dp
    val PhoenixCornerRounding = 8.dp
    val BubbleModeCornerRounding = GameScreenTopBubbleStyle.CornerRounding
    val OffsetFromEdge = GameScreenTopBubbleStyle.OffsetFromEdge
    val CiTextIconSize = 24.dp
    val InnerPadding = 4.dp
}

object ScreenSizeThresholds {
    // NOTE: The description always matches the behaviour of value > field.
    val SpreadDiceStackOnSingleLine = 549.dp
    val FloatTopStatusBarAsBubble = 576.dp
    val StopStretchingGameOverDialogToScreenEdge = 780.dp
    val StopStackingYesNoDialogVertically = 604.dp
    val StopStretchingYesNoDialogToScreenEdge = 380.dp
    val FloatBottomBarAsBubble = 720.dp
    val UncompactCiPanel = 500.dp
    val MaxBottomBarWidth = 990.dp
}

/**
 * Keep track of click states and the associated tile colors.
 * This enum exists for the purposes of highlighting tiles.
 *
 * Please refer to the game rules to find out what each of these highlights mean.
 * @property NO_INTERACTIONS_WITH_OUTLINE The [tile][TileData] should not be highlighted unless hovered over by mouse.
 * @property CLICKED_PRIMARY The [tile][TileData] should be highlighed with yellow fill and outline.
 * @property CLICKED_SECONDARY The [tile][TileData] should be highlighted with white fill and outline.
 * @property HIGHLIGHT_PRIMARY The [tile][TileData] should be highlighted with yellow outline.
 * @property HIGHLIGHT_SECONDARY The [tile][TileData] should be highlighted with white outline.
 */
enum class UniversalColorizer(
    val fillColor: Color = Palette.Glass00,
    val outlineColor: Color = Palette.Glass00,
) {
    NO_INTERACTIONS,
    NO_INTERACTIONS_WITH_OUTLINE(outlineColor = Palette.Glass20),
    CLICKED_PRIMARY(Palette.FillYellow, Palette.FillYellow),
    CLICKED_SECONDARY(Palette.FillLightPrimary, Palette.FillLightPrimary),
    CLICKED_TERTIARY(Palette.ShadeLightAlternative, Palette.ShadeLightAlternative),
    HIGHLIGHT_PRIMARY(outlineColor = Palette.FillYellow),
    HIGHLIGHT_SECONDARY(outlineColor = Palette.FillLightPrimary),
    HOVER_GLASS(Palette.Glass10, Palette.Glass20)
}
