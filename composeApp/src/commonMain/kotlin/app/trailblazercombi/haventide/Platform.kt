package app.trailblazercombi.haventide

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform