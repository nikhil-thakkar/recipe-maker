package dev.nikhi1.recipe.maker

import com.google.android.play.core.splitcompat.SplitCompatApplication
import dev.nikhi1.recipe.core.Core
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class RecipeMaker : SplitCompatApplication() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            androidLogger(Level.DEBUG)
            androidFileProperties()
        }
        Core(BuildConfig.DEBUG)
    }
}