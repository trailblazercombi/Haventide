package app.trailblazercombi.haventide

import android.content.Context

object ContextHolder {
    lateinit var applicationContext: Context
        private set

    fun initContext(context: Context) {
        applicationContext = context.applicationContext
    }
}
