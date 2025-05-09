package app.trailblazercombi.haventide.resources

import androidx.compose.ui.graphics.Color
import app.trailblazercombi.haventide.game2.data.tilemap.Position
import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.TileViewInfo
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.MechanismSummonPattern
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.aoe.Barrier
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.aoe.DispelStation
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.aoe.Life
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.aoe.SpikePrimary
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.aoe.Thorns
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.immediate.*
import app.trailblazercombi.haventide.game2.data.turntable.Team
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
        MechanismTemplate.Phoenix("AYUNA",
            fullName = Res.string.phoenix_ayuna_long_name,
            shortName = Res.string.phoenix_ayuna_short_name,
            accentColor = Color(0xFFD91410),
            profilePhoto = Res.drawable.Ayuna,
            phoenixType = DieType.VANGUARD,
            abilityInnate = AbilityTemplates.SHATTER.template,
            abilityUltimate = AbilityTemplates.BURDEN.template
        )
    ),
    AYUMI(
        MechanismTemplate.Phoenix("AYUMI",
            fullName = Res.string.phoenix_ayumi_long_name,
            shortName = Res.string.phoenix_ayumi_short_name,
            accentColor = Color(0xFF098432),
            profilePhoto = Res.drawable.Ayumi,
            phoenixType = DieType.MEDIC,
            abilityInnate = AbilityTemplates.BLESSING.template,
            abilityUltimate = AbilityTemplates.DISPEL_STATION.template
        )
    ),
    SYLVIA(
        MechanismTemplate.Phoenix("SYLVIA",
            fullName = Res.string.phoenix_sylvia_long_name,
            shortName = Res.string.phoenix_sylvia_short_name,
            accentColor = Color(0xFF67406D),
            profilePhoto = Res.drawable.Sylvia,
            phoenixType = DieType.SENTINEL,
            abilityInnate = AbilityTemplates.THORNY_PETALS.template,
            abilityUltimate = AbilityTemplates.SPIKE.template,
        )
    ),
    MALACHAI(
        MechanismTemplate.Phoenix("MALACHAI",
            fullName = Res.string.phoenix_malachai_long_name,
            shortName = Res.string.phoenix_malachai_short_name,
            accentColor = Color(0xFF3B3B93),
            profilePhoto = Res.drawable.Malachai,
            phoenixType = DieType.MEDIC,
            abilityInnate = AbilityTemplates.DISPEL.template,
            abilityUltimate = AbilityTemplates.LIFE.template
        )
    ),
    FINNIAN(
        MechanismTemplate.Phoenix("FINNIAN",
            fullName = Res.string.phoenix_finnian_long_name,
            shortName = Res.string.phoenix_finnian_short_name,
            accentColor = Color(0xFF25B97F),
            profilePhoto = Res.drawable.Finnian,
            phoenixType = DieType.SENTINEL,
            abilityInnate = AbilityTemplates.TITAN_SHIELD.template,
            abilityUltimate = AbilityTemplates.BARRIER.template,
        )
    ),
    YUMIO(
        MechanismTemplate.Phoenix("YUMIO",
            fullName = Res.string.phoenix_yumio_long_name,
            shortName = Res.string.phoenix_yumio_short_name,
            accentColor = Color(0xFF2169AD),
            profilePhoto = Res.drawable.Yumio,
            phoenixType = DieType.VANGUARD,
            abilityInnate = AbilityTemplates.FROSTBITE.template,
            abilityUltimate = AbilityTemplates.ICEBERGS.template,
        )
    ),
//    TORRENT(
//        MechanismTemplate.Phoenix("TORRENT",
//            fullName = Res.string.phoenix_torrent_long_name,
//            shortName = Res.string.phoenix_torrent_short_name,
//            accentColor = Color(0xFF465376),
//            profilePhoto = Res.drawable.Torrent,
//            phoenixType = DieType.VANGUARD
//        )
//    )

    ;

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
object ImmediateEffecterTemplates {
    enum class DamageInvokerTemplates(val template: MechanismTemplate.ImmediateEffecter.DamageInvoker) {
        BASIC_STRIKE(MechanismTemplate.ImmediateEffecter.DamageInvoker(20)),
        SHATTER(MechanismTemplate.ImmediateEffecter.DamageInvoker(40)),
    }

    enum class HealingInvokerTemplates(val template: MechanismTemplate.ImmediateEffecter.HealingInvoker) {
        LIFE(MechanismTemplate.ImmediateEffecter.HealingInvoker(80)),
    }

    enum class ModificatorInvokerTemplates(val template: MechanismTemplate.ImmediateEffecter.ModificatorInvoker) {
        TITAN_SHIELD(MechanismTemplate.ImmediateEffecter.ModificatorInvoker(Modificators.TITAN_SHIELD)),
        BURDEN(MechanismTemplate.ImmediateEffecter.ModificatorInvoker(Modificators.BURDEN)),
        BLESSING( MechanismTemplate.ImmediateEffecter.ModificatorInvoker(Modificators.BLESSING)),
        THORNY_PETALS(MechanismTemplate.ImmediateEffecter.ModificatorInvoker(Modificators.THORNY_PETALS)),
        FROSTBITE(MechanismTemplate.ImmediateEffecter.ModificatorInvoker(Modificators.FROSTBITE)),
    }

    enum class ModificatorRemoverTemplates(val template: MechanismTemplate.ImmediateEffecter.DispelDebuffsInvoker) {
        REMOVE_ALL_DEBUFFS(MechanismTemplate.ImmediateEffecter.DispelDebuffsInvoker())
    }

    enum class MechanismSummonerTemplates(val template: MechanismTemplate.ImmediateEffecter.MechanismSummoner) {
        MANIFEST_BARRIER(
            MechanismTemplate.ImmediateEffecter.MechanismSummoner(
                mechanism = AoEEffecterTemplates.BARRIER_SINGLE.template,
                pattern = MechanismSummonPattern::Filled3x3
            )
        ),
        DISPEL_STATION(
            MechanismTemplate.ImmediateEffecter.MechanismSummoner(
                mechanism = AoEEffecterTemplates.DISPEL_STATION.template,
                pattern = MechanismSummonPattern::Itself
            )
        ),
        SPIKE(
            MechanismTemplate.ImmediateEffecter.MechanismSummoner(
                mechanism = AoEEffecterTemplates.SPIKE_PRIMARY.template,
                pattern = MechanismSummonPattern::Itself
            )
        ),
        MANIFEST_THORNS(
            MechanismTemplate.ImmediateEffecter.MechanismSummoner(
                mechanism = AoEEffecterTemplates.THORNS.template,
                pattern = MechanismSummonPattern::Hollow3x3
            )
        ),
        ICEBERGS(
            MechanismTemplate.ImmediateEffecter.MechanismSummoner(
                mechanism = ModificatorInvokerTemplates.FROSTBITE.template,
                pattern = MechanismSummonPattern::Filled5x5
            )
        ),
        DISPEL(
            MechanismTemplate.ImmediateEffecter.MechanismSummoner(
                mechanism = ModificatorRemoverTemplates.REMOVE_ALL_DEBUFFS.template,
                pattern = MechanismSummonPattern::Filled5x5
            )
        ),
        LIFE_MASTER(
            MechanismTemplate.ImmediateEffecter.MechanismSummoner(
                mechanism = HealingInvokerTemplates.LIFE.template,
                pattern = MechanismSummonPattern::Filled7x7
            )
        )
    }
}

/**
 * A Factory for AoEEffecters.
 * Builds Effecters in the same way and similar fashion to
 * [app.trailblazercombi.haventide.game.modificators.Modificators] builds Modificators.
 */
enum class AoEEffecterTemplates(val template: MechanismTemplate) {
    BARRIER_SINGLE(MechanismTemplate.Custom { parentTile, _ -> Barrier(parentTile) }),
    DISPEL_STATION(MechanismTemplate.Custom { parentTile, _ -> DispelStation(parentTile) }),
    SPIKE_PRIMARY(MechanismTemplate.Custom { parentTile, team -> SpikePrimary(parentTile, team!!) }),
    THORNS(MechanismTemplate.Custom {parentTile, _ -> Thorns(parentTile) }),
    LIFE(MechanismTemplate.Custom {parentTile, team -> Life(parentTile, team!!) }),

    ;

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
        val gameId: String,
        val fullName: StringResource,
        val shortName: StringResource,
        val accentColor: Color,
        val profilePhoto: DrawableResource,
        val phoenixType: DieType,
        val maxHitPoints: Int = 120,
//        val maxEnergyPoints: Int = 50,
//        val energyForUltimate: Int = 50,
        val abilityBasic1: AbilityTemplate = AbilityTemplates.BASIC_MOVE.template,
        val abilityBasic2: AbilityTemplate = AbilityTemplates.BASIC_STRIKE.template,
        val abilityInnate: AbilityTemplate,
        val abilityUltimate: AbilityTemplate,
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

        class DispelDebuffsInvoker(): MechanismTemplate() {
            override fun build(parentTile: TileData, teamAffiliation: Team?): Mechanism
            = ImmediateModificatorRemover(parentTile)
        }

        /**
         * This data blob represents an [app.trailblazercombi.haventide.game.mechanisms.ImmediateMechanismSummoner].
         */
        data class MechanismSummoner(val mechanism: MechanismTemplate, val pattern: (Position) -> Set<Position>): MechanismTemplate() {
            override fun build(parentTile: TileData, teamAffiliation: Team?): Mechanism
            = ImmediateMechanismSummoner(this.mechanism,
                this.pattern, parentTile, teamAffiliation)
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

enum class AbilityTemplates(val template: AbilityTemplate) {
    // Basic universal abilities
    BASIC_MOVE(
        AbilityTemplate(
            systemName = "BASIC_MOVE",
            friendlyName = Res.string.generic_move_ability_name,
            friendlyDescription = Res.string.generic_move_ability_description,
            friendlyIcon = Res.drawable.basic_move,
            alignedCost = 0,
            scatteredCost = 1,
            range = sqrt(2.toDouble()),
            targetType = TargetType.EMPTY_TILE,
            abilityVerb = AbilityVerb.MOVE,
            executionCheck = { doer, target -> doer.canMove(target) },
            execution = { doer, target -> doer.move(target) }
        )
    ),
    BASIC_STRIKE(
        AbilityTemplate(
            systemName = "BASIC_STRIKE",
            friendlyName = Res.string.generic_strike_ability_name,
            friendlyDescription = Res.string.generic_strike_ability_description,
            friendlyIcon = Res.drawable.basic_strike,
            alignedCost = 0,
            scatteredCost = 2,
            range = 2.65,
            targetType = TargetType.ENEMY_OR_NEUTRAL,
            abilityVerb = AbilityVerb.ATTACK,
            execution = { doer, target ->
                ImmediateEffecterTemplates.DamageInvokerTemplates.BASIC_STRIKE.template.build(target, doer.teamAffiliation)
            },
        )
    ),
    // FINNIAN
    TITAN_SHIELD(
        AbilityTemplate(
            systemName = "TITAN_SHIELD",
            friendlyName = Res.string.phoenix_finnian_ability_innate_name,
            friendlyDescription = Res.string.phoenix_finnian_ability_innate_description,
            friendlyIcon = Res.drawable.ability_finnian,
            alignedCost = 1,
            scatteredCost = 2,
            range = 3.80,
            targetType = TargetType.ALLY,
            abilityVerb = AbilityVerb.BUFF,
            execution = { doer, target ->
                ImmediateEffecterTemplates.ModificatorInvokerTemplates.TITAN_SHIELD.template.build(target, doer.teamAffiliation)
            }
        )
    ),
    BARRIER(
        AbilityTemplate(
            systemName = "BARRIER",
            friendlyName = Res.string.phoenix_finnian_ability_ultimate_name,
            friendlyDescription = Res.string.phoenix_finnian_ability_ultimate_description,
            friendlyIcon = Res.drawable.ultimate_finnian,
            alignedCost = 2,
            scatteredCost = 2,
            range = 2.50,
            targetType = TargetType.EMPTY_TILE,
            abilityVerb = AbilityVerb.BUILD,
            execution = { doer, target ->
                ImmediateEffecterTemplates.MechanismSummonerTemplates.MANIFEST_BARRIER.template.build(target, doer.teamAffiliation)
            },
            executionCheck = { _, target ->
                target.canAddMechanism(DummyImmediateEffecter(target))
            }
        )
    ),
    // AYUNA
    SHATTER(
        AbilityTemplate(
            systemName = "SHATTER",
            friendlyName = Res.string.phoenix_ayuna_ability_innate_name,
            friendlyDescription = Res.string.phoenix_ayuna_ability_innate_description,
            friendlyIcon = Res.drawable.ability_ayuna,
            alignedCost = 2,
            scatteredCost = 1,
            range = 2.65,
            targetType = TargetType.ENEMY_OR_NEUTRAL,
            abilityVerb = AbilityVerb.ATTACK,
            execution = { doer, target ->
                ImmediateEffecterTemplates.DamageInvokerTemplates.SHATTER.template.build(target, doer.teamAffiliation)
            }
        )
    ),
    BURDEN(
        AbilityTemplate(
            systemName = "BURDEN",
            friendlyName = Res.string.phoenix_ayuna_ability_ultimate_name,
            friendlyDescription = Res.string.phoenix_ayuna_ability_ultimate_description,
            friendlyIcon = Res.drawable.ultimate_ayuna,
            alignedCost = 3,
            scatteredCost = 1,
            range = 3.80,
            targetType = TargetType.ENEMY,
            abilityVerb = AbilityVerb.IMPAIR,
            execution = { doer, target ->
                ImmediateEffecterTemplates.ModificatorInvokerTemplates.BURDEN.template.build(target, doer.teamAffiliation)
            }
        )
    ),
    // AYUMI
    BLESSING(
        AbilityTemplate(
            systemName = "BLESSING",
            friendlyName = Res.string.phoenix_ayumi_ability_innate_name,
            friendlyDescription = Res.string.phoenix_ayumi_ability_innate_description,
            friendlyIcon = Res.drawable.ability_ayumi,
            alignedCost = 1,
            scatteredCost = 2,
            range = 3.80,
            targetType = TargetType.ALLY,
            abilityVerb = AbilityVerb.HEAL,
            execution = { doer, target ->
                ImmediateEffecterTemplates.ModificatorInvokerTemplates.BLESSING.template.build(target, doer.teamAffiliation)
            }
        )
    ),
    DISPEL_STATION(
        AbilityTemplate(
            systemName = "DISPEL_STATION",
            friendlyName = Res.string.phoenix_ayumi_ability_ultimate_name,
            friendlyDescription = Res.string.phoenix_ayumi_ability_ultimate_description,
            friendlyIcon = Res.drawable.ultimate_ayumi,
            alignedCost = 2,
            scatteredCost = 2,
            range = 2.50,
            targetType = TargetType.EMPTY_TILE,
            abilityVerb = AbilityVerb.BUILD,
            execution = { doer, target ->
                ImmediateEffecterTemplates.MechanismSummonerTemplates.DISPEL_STATION.template.build(target, doer.teamAffiliation)
            },
            executionCheck = { _, target -> target.isEmpty() }
        )
    ),
    // SYLVIA
    THORNY_PETALS(
        AbilityTemplate(
            systemName = "THORNY_PETALS",
            friendlyName = Res.string.phoenix_sylvia_ability_innate_name,
            friendlyDescription = Res.string.phoenix_sylvia_ability_innate_description,
            friendlyIcon = Res.drawable.ability_sylvia,
            alignedCost = 2,
            scatteredCost = 1,
            range = 3.87,
            targetType = TargetType.ENEMY,
            abilityVerb = AbilityVerb.IMPAIR,
            execution = { doer, target ->
                ImmediateEffecterTemplates.ModificatorInvokerTemplates.THORNY_PETALS.template.build(target, doer.teamAffiliation)
            }
        )
    ),
    SPIKE(
        AbilityTemplate(
            systemName = "SPIKE",
            friendlyName = Res.string.phoenix_sylvia_ability_ultimate_name,
            friendlyDescription = Res.string.phoenix_sylvia_ability_ultimate_description,
            friendlyIcon = Res.drawable.ultimate_sylvia,
            alignedCost = 3,
            scatteredCost = 1,
            range = 2.50,
            targetType = TargetType.EMPTY_TILE,
            abilityVerb = AbilityVerb.BUILD,
            execution = { doer, target ->
                ImmediateEffecterTemplates.MechanismSummonerTemplates.SPIKE.template.build(target, doer.teamAffiliation)
            },
            executionCheck = { _, target -> target.isEmpty() }
        )
    ),
    // YUMIO
    FROSTBITE(
        AbilityTemplate(
            systemName = "FROSTBITE",
            friendlyName = Res.string.phoenix_yumio_ability_innate_name,
            friendlyDescription = Res.string.phoenix_yumio_ability_innate_description,
            friendlyIcon = Res.drawable.ability_yumio,
            alignedCost = 1,
            scatteredCost = 2,
            range = 3.80,
            targetType = TargetType.ENEMY,
            abilityVerb = AbilityVerb.ATTACK,
            execution = { doer, target ->
                ImmediateEffecterTemplates.ModificatorInvokerTemplates.FROSTBITE.template.build(target, doer.teamAffiliation)
            }
        )
    ),
    ICEBERGS(
        AbilityTemplate(
            systemName = "ICEBERGS",
            friendlyName = Res.string.phoenix_yumio_ability_ultimate_name,
            friendlyDescription = Res.string.phoenix_yumio_ability_ultimate_description,
            friendlyIcon = Res.drawable.ultimate_yumio,
            alignedCost = 2,
            scatteredCost = 2,
            range = 3.80,
            targetType = TargetType.ENEMY,
            abilityVerb = AbilityVerb.ATTACK,
            execution = { doer, target ->
                ImmediateEffecterTemplates.MechanismSummonerTemplates.ICEBERGS.template.build(target, doer.teamAffiliation)
            },
            executionCheck = { _, _ -> true }
        )
    ),
    // MALACHAI
    DISPEL(
        AbilityTemplate(
            systemName = "DISPEL_5x5",
            friendlyName = Res.string.phoenix_malachai_ability_innate_name,
            friendlyDescription = Res.string.phoenix_malachai_ability_innate_description,
            friendlyIcon = Res.drawable.ability_malachai,
            alignedCost = 2,
            scatteredCost = 1,
            range = 2.50,
            targetType = TargetType.EMPTY_TILE,
            abilityVerb = AbilityVerb.HEAL,
            execution = { doer, target ->
                ImmediateEffecterTemplates.MechanismSummonerTemplates.DISPEL.template.build(target, doer.teamAffiliation)
            },
            executionCheck = { _, _ -> true }
        )
    ),
    LIFE(
        AbilityTemplate(
            systemName = "LIFE",
            friendlyName = Res.string.phoenix_malachai_ability_ultimate_name,
            friendlyDescription = Res.string.phoenix_malachai_ability_ultimate_description,
            friendlyIcon = Res.drawable.ultimate_malachai,
            alignedCost = 2,
            scatteredCost = 2,
            range = 2.50,
            targetType = TargetType.EMPTY_TILE,
            abilityVerb = AbilityVerb.HEAL,
            execution = { doer, target ->
                ImmediateEffecterTemplates.MechanismSummonerTemplates.LIFE_MASTER.template.build(target, doer.teamAffiliation)
            }
        )
    )
}

data class AbilityTemplate(
    val systemName: String,
    val friendlyName: StringResource,
    val friendlyDescription: StringResource,
    val friendlyIcon: DrawableResource,
    val alignedCost: Int,
    val scatteredCost: Int,
    val range: Double,
    val targetType: TargetType,
    val abilityVerb: AbilityVerb,
    val execution: (Mechanism, TileData) -> Unit,
    val executionCheck: (Mechanism, TileData) -> Boolean = { doer, target -> targetType.typeCheck(doer, target) }
)

enum class AbilityVerb(val string: StringResource) {
    MOVE(Res.string.ability_move),
    BUILD(Res.string.ability_construct),
    ATTACK(Res.string.ability_attack),
    HEAL(Res.string.ability_restore),
    IMPAIR(Res.string.ability_impair),
    BUFF(Res.string.ability_support),
}
