package app.trailblazercombi.haventide.netcode

fun String.header(): String = this.split(" ").first()
fun String.args(): List<String> = this.split(" ").let { it - it.first() }
