package app.trailblazercombi.haventide.game2.data.tilemap

import app.trailblazercombi.haventide.netcode.Handshaker
import app.trailblazercombi.haventide.resources.Res
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi
import java.io.FileNotFoundException

@OptIn(ExperimentalResourceApi::class)
fun String.unpackToMap(): String {
    val string: String = this
    var result: String? = null
    val job = CoroutineScope(Dispatchers.IO).launch {
        try {
            result = Res.readBytes(string).decodeToString()
        } catch (e: Exception) {
            println(e.printStackTrace())
            Handshaker.cancelGameRequest()
            this.cancel()
        }
    }
    runBlocking { job.join() }
    return result ?: throw FileNotFoundException("File not found: Res.$string")
}
