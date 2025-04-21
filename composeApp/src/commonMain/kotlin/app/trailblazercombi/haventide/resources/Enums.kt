package app.trailblazercombi.haventide.resources

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.ModificatorHandler
import app.trailblazercombi.haventide.game2.data.tilemap.modificators.Modificator
import app.trailblazercombi.haventide.game2.data.tilemap.modificators.TitanShield
import app.trailblazercombi.haventide.game2.data.turntable.PlayerInGame
import app.trailblazercombi.haventide.playerdata.PlayerProfile
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * This is for formatting Buttons by various Comopsables.
 */
enum class ButtonSeverity(val fillColor: Color, val outlineColor: Color, val contentColor: Color) {
    NEUTRAL(Palette.FillLightPrimary, Palette.FillLightPrimary, Palette.FullBlack),
    DIMMED(Palette.Glass00, Palette.FillLightPrimary, Palette.FillLightPrimary),
    PREFERRED(Palette.FillYellow, Palette.FillYellow, Palette.FullBlack),
    DESTRUCTIVE_MINOR(Palette.FillRed, Palette.FillRed, Palette.FullWhite),
    DESTRUCTIVE(Palette.FillRed, Palette.FillRed, Palette.FullWhite)
}

/**
 * This represents the Result any given Game can come to.
 */
enum class GameResult(val string: StringResource, val color: Color) {
    VICTORY(Res.string.game_over_dialog_result_good, Palette.FillGreen),
    VICTORY_FORFEIT(Res.string.game_over_dialog_result_contumation, Palette.FillGreen),
    DEFEAT(Res.string.game_over_dialog_result_bad, Palette.FillRed),
    DEFEAT_FORFEIT(Res.string.game_over_dialog_result_forfeit, Palette.FillRed),
    DRAW(Res.string.game_over_dialog_result_neutral, Palette.FullGrey),
    GAME_ONGOING(Res.string.game_over_dialog_result_error, Palette.FillYellow)
}

/**
 * This is for the TurnTable to keep track of Player Turn States of the Local Player.
 */
enum class PlayerTurnStates(val string: StringResource) {
    LOCAL_PLAYER_TURN(Res.string.game_turn_state_good),
    REMOTE_PLAYER_TURN(Res.string.game_turn_state_bad),
    LOCAL_PLAYER_ROUND_DONE(Res.string.game_turn_state_over);

    private var player: String = "Unnamed"

    fun consume(player: PlayerInGame) {
        this.player = player.profile.name
    }

    fun retrieve(): String = this.player
}

/**
 * This enum representing various types of [modificators][Modificator].
 * Good for categorization, and not much else.
 */
enum class ModificatorType {
    BUFF,
    DEBUFF,
    OTHER
}

enum class Modificators(val build: (ModificatorHandler) -> Modificator) {
    TITAN_SHIELD( { parent -> TitanShield(parent) } ),
}

// [MENUS] TODO Replace with an actual player profile.
//  This will do for testing purposes though.
enum class PlaceholderPlayers(
    private val playerName: String,
    private val member1: MechanismTemplate.Phoenix,
    private val member2: MechanismTemplate.Phoenix,
    private val member3: MechanismTemplate.Phoenix
) {
    PLAYER_TWO(
        "You",
        PhoenixTemplates.FINNIAN.template,
        PhoenixTemplates.YUMIO.template,
        PhoenixTemplates.MALACHAI.template
    ),
    PLAYER_ONE(
        "Opponent",
        PhoenixTemplates.AYUNA.template,
        PhoenixTemplates.AYUMI.template,
        PhoenixTemplates.SYLVIA.template
    );

    fun toProfile() = PlayerProfile(playerName, listOf(member1, member2, member3))
}

enum class TargetType { ALLY, ENEMY, EMPTY_TILE }

enum class DieType(val icon: DrawableResource, val title: StringResource, val order: Int) {
    VANGUARD(Res.drawable.vanguard, Res.string.die_type_vanguard, 0),
    SENTINEL(Res.drawable.sentinel, Res.string.die_type_sentinel, 1),
    MEDIC(Res.drawable.medic, Res.string.die_type_medic, 2)
}

enum class DieHighlightState(val fillColor: Color, val outlineColor: Color, val contentColor: Color) {
    IDLE(Palette.Abyss50.compositeOver(Palette.FullGrey), Palette.FullGrey, Palette.FullGrey),
    POTENTIAL_IDLE(Palette.Abyss50.compositeOver(Palette.FullGrey), Palette.FullWhite, Palette.FullWhite),
    SELECTED(
        Palette.Abyss70.compositeOver(Palette.FillYellow),
        Palette.Abyss40.compositeOver(Palette.FillYellow),
        Palette.Abyss40.compositeOver(Palette.FillYellow)
    ),
    POTENTIAL_SELECTED(Palette.Abyss70.compositeOver(Palette.FillYellow), Palette.FillYellow, Palette.FillYellow);
}
