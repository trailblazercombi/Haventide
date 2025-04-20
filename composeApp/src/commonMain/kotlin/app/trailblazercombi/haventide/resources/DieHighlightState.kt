@file:Suppress("PackageDirectoryMismatch")

package app.trailblazercombi.haventide.resources.functions

import app.trailblazercombi.haventide.resources.DieHighlightState

fun DieHighlightState(potential: Boolean, selected: Boolean): DieHighlightState =
    if (potential) { if (selected) DieHighlightState.POTENTIAL_SELECTED else DieHighlightState.POTENTIAL_IDLE }
    else { if (selected) DieHighlightState.SELECTED else DieHighlightState.IDLE }
