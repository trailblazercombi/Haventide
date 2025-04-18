package app.trailblazercombi.haventide.resources

import androidx.compose.ui.graphics.Color
import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.MechanismSummonPattern
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels.components.DieType
import app.trailblazercombi.haventide.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import kotlin.math.sqrt

/**
 * This is a definition of all the Phoenixes' data blobs.
 * Use to create new instances of [PhoenixMechanism].
 * @see [MechanismTemplate.Phoenix]
 */
enum class PhoenixTemplates(val template: MechanismTemplate.Phoenix) {
    AYUNA(
        MechanismTemplate.Phoenix(
            fullName = Res.string.phoenix_ayuna_long_name,
            shortName = Res.string.phoenix_ayuna_short_name,
            accentColor = Color(0xFFD91410),
            profilePhoto = Res.drawable.Ayuna,
            phoenixType = DieType.VANGUARD
        )
    ),
    AYUMI(
        MechanismTemplate.Phoenix(
            fullName = Res.string.phoenix_ayumi_long_name,
            shortName = Res.string.phoenix_ayumi_short_name,
            accentColor = Color(0xFF098432),
            profilePhoto = Res.drawable.Ayumi,
            phoenixType = DieType.MEDIC
        )
    ),
    SYLVIA(
        MechanismTemplate.Phoenix(
            fullName = Res.string.phoenix_sylvia_long_name,
            shortName = Res.string.phoenix_sylvia_short_name,
            accentColor = Color(0xFF67406D),
            profilePhoto = Res.drawable.Sylvia,
            phoenixType = DieType.SENTINEL
        )
    ),
    MALACHAI(
        MechanismTemplate.Phoenix(
            fullName = Res.string.phoenix_malachai_long_name,
            shortName = Res.string.phoenix_malachai_short_name,
            accentColor = Color(0xFF3B3B93),
            profilePhoto = Res.drawable.Malachai,
            phoenixType = DieType.MEDIC
        )
    ),
    FINNIAN(
        MechanismTemplate.Phoenix(
            fullName = Res.string.phoenix_finnian_long_name,
            shortName = Res.string.phoenix_finnian_short_name,
            accentColor = Color(0xFF25B97F),
            profilePhoto = Res.drawable.Finnian,
            phoenixType = DieType.SENTINEL
        )
    ),
    YUMIO(
        MechanismTemplate.Phoenix(
            fullName = Res.string.phoenix_yumio_long_name,
            shortName = Res.string.phoenix_yumio_short_name,
            accentColor = Color(0xFF2169AD),
            profilePhoto = Res.drawable.Yumio,
            phoenixType = DieType.VANGUARD
        )
    ),
    TORRENT(
        MechanismTemplate.Phoenix(
            fullName = Res.string.phoenix_torrent_long_name,
            shortName = Res.string.phoenix_torrent_short_name,
            accentColor = Color(0xFF465376),
            profilePhoto = Res.drawable.Torrent,
            phoenixType = DieType.VANGUARD
        )
    );

    /**
     * @see [MechanismTemplate.build].
     */
    fun build(parentTile: TileData, team: Team): Mechanism {
        return template.build(parentTile, team)
    }
}

/**
 * Defintions for all the Immidiate Effecters ever.
 */
@Suppress("MemberVisibilityCanBePrivate")
object ImmediateEffecters {
    enum class DamageInvokers(val template: MechanismTemplate.ImmediateEffecter.DamageInvoker) {
        BASIC_STRIKE(MechanismTemplate.ImmediateEffecter.DamageInvoker(2000))
    }

    enum class HealingInvokers(val template: MechanismTemplate.ImmediateEffecter.HealingInvoker) {

    }

    enum class ModificatorInvokers(val template: MechanismTemplate.ImmediateEffecter.ModificatorInvoker) {
        TITAN_SHIELD(MechanismTemplate.ImmediateEffecter.ModificatorInvoker(Modificators.TITAN_SHIELD))
    }

    enum class MechanismSummoners(val template: MechanismTemplate.ImmediateEffecter.MechanismSummoner) {
        BARRIER_MAKER(
            MechanismTemplate.ImmediateEffecter.MechanismSummoner(
                mechanism = AoEEffecters.BARRIER_SINGLE.template,
                pattern = MechanismSummonPattern::Filled3x3
            )
        )
    }
}

/**
 * A Factory for AoEEffecters.
 * Builds Effecters in the same way and similar fashion to
 * [app.trailblazercombi.haventide.game.modificators.Modificators] builds Modificators.
 */
enum class AoEEffecters(val template: MechanismTemplate) {
    BARRIER_SINGLE(MechanismTemplate.Custom { parentTile, _ ->
        app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.aoe.Barrier(parentTile)
    });

    /**
     * @see [MechanismTemplate.build]
     */
    fun build(parentTile: TileData, team: Team): Mechanism {
        return template.build(parentTile, team)
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// MECHANISM TEMPLATES
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * This class represents various templates to build [Mechanisms][Mechanism].
 *
 * Its purpose is universal, however, mainly to be able to summon [Mechanisms][Mechanism].
 */
sealed class MechanismTemplate {
    /**
     * This data blob represents a [PhoenixMechanism].
     * @see [app.trailblazercombi.haventide.game.mechanisms.Phoenixes]
     */
    data class Phoenix(
        val fullName: StringResource,
        val shortName: StringResource,
        val accentColor: Color,
        val profilePhoto: DrawableResource,
        val phoenixType: DieType,
        val maxHitPoints: Int = 120,
        val maxEnergyPoints: Int = 50,
        val energyForUltimate: Int = 50,
        val abilityBasic1: AbilityTemplate = Abilities.BASIC_MOVE.template,
        val abilityBasic2: AbilityTemplate = Abilities.BASIC_STRIKE.template,
        // val abilityInnate1: AbilityTemplate,
        // val abilityInnate2: AbilityTemplate,
        // val abilityUltimate: AbilityTemplate,
        // [ABILITY STACK] TODO Abilities
        //  Lore pages
        //  Et cetera
    ) : MechanismTemplate() {
        override fun build(parentTile: TileData, teamAffiliation: Team?): Mechanism {
            return PhoenixMechanism(parentTile, this,
                teamAffiliation ?: throw NullPointerException("Cannot summon a Neutral Faction Phoenix!"))
        }
    }

    /**
     * A category of all [Immediate Effecters][app.trailblazercombi.haventide.game.mechanisms.ImmediateEffecter].
     */
    sealed class ImmediateEffecter {
        /**
         * This data blob represents an [app.trailblazercombi.haventide.game.mechanisms.ImmediateDamageInvoker].
         */
        data class DamageInvoker(val damage: Int): MechanismTemplate() {
            override fun build(parentTile: TileData, teamAffiliation: Team?)
            = ImmediateDamageInvoker(this.damage, parentTile)
        }

        /**
         * This data blob represents an [app.trailblazercombi.haventide.game.mechanisms.ImmediateHealingInvoker].
         */
        data class HealingInvoker(val healing: Int): MechanismTemplate() {
            override fun build(parentTile: TileData, teamAffiliation: Team?)
            = ImmediateHealingInvoker(this.healing, parentTile)
        }

        /**
         * This data blob represents an [app.trailblazercombi.haventide.game.mechanisms.ImmediateModificatorInvoker].
         */
        data class ModificatorInvoker(val modificator: Modificators): MechanismTemplate() {
            override fun build(parentTile: TileData, teamAffiliation: Team?): Mechanism
            = ImmediateModificatorInvoker(this.modificator, parentTile)
        }

        /**
         * This data blob represents an [app.trailblazercombi.haventide.game.mechanisms.ImmediateMechanismSummoner].
         */
        data class MechanismSummoner(val mechanism: MechanismTemplate, val pattern: (Position) -> Set<Position>): MechanismTemplate() {
            override fun build(parentTile: TileData, teamAffiliation: Team?): Mechanism
            = ImmediateMechanismSummoner(this.mechanism, this.pattern, parentTile)
        }
    }

    /**
     * This is for fancy custom Mechanisms, such as [AoEEffecters][AoEEffecter]
     */
    data class Custom(val customBuildInstructions: (TileData, Team?) -> Mechanism) : MechanismTemplate() {
        override fun build(parentTile: TileData, teamAffiliation: Team?): Mechanism
        = customBuildInstructions.invoke(parentTile, teamAffiliation)
    }

    /**
     * Builds the desired [Mechanism].
     * @param parentTile The [tile][TileData] this [Mechanism] shall be placed upon.
     * @param teamAffiliation The [Team] this [Mechanism] shall belong to.
     * @return The new [Mechanism] according to the [MechanismTemplate].
     */
    abstract fun build(parentTile: TileData, teamAffiliation: Team?): Mechanism
}

enum class Abilities(val template: AbilityTemplate) {
    // Basic universal abilities
    BASIC_MOVE(
        AbilityTemplate(
            alignedCost = 0,
            scatteredCost = 1,
            range = sqrt(2.toDouble()),
            executionCheck = { doer, target -> doer.canMove(target) },
            execution = { doer, target -> doer.move(target) }
        )
    ),
    BASIC_STRIKE(
        AbilityTemplate(
            alignedCost = 0,
            scatteredCost = 2,
            range = 2.65,
            executionCheck = { doer, target -> (target.findTeams() - doer.teamAffiliation).isNotEmpty() },
            execution = { doer, target ->
                ImmediateEffecters.DamageInvokers.BASIC_STRIKE.template.build(target, doer.teamAffiliation) },
        )
    ),
    BARRIER(
        AbilityTemplate(
            alignedCost = 2,
            scatteredCost = 2,
            range = 3.27,
            executionCheck = { _, target -> target.canAddMechanism(DummyImmediateEffecter(target)) },
            execution = { doer, target ->
                ImmediateEffecters.MechanismSummoners.BARRIER_MAKER.template.build(target, doer.teamAffiliation) },
        )
    )
}

data class AbilityTemplate(
    val alignedCost: Int,
    val scatteredCost: Int,
    val energyCost: Int = 0,
    val range: Double,
    val executionCheck: (Mechanism, TileData) -> Boolean,
    val execution: (Mechanism, TileData) -> Unit,
)
