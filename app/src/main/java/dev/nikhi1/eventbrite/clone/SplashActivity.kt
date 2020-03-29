package dev.nikhi1.eventbrite.clone

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.dynamicfeatures.fragment.DynamicFragmentNavigator
import androidx.navigation.findNavController
import com.google.android.play.core.splitcompat.SplitCompat
import org.koin.android.ext.android.getKoin
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

private var module: Module = module {
    viewModel { SplashViewModel() }
}

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModel()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        loadKoinModules(module)

        viewModel.navigate.observe(this, Observer {
            findNavController(R.id.nav_host_fragment).navigate(R.id.includedGraph)
        })
    }

    override fun onDestroy() {
        unloadKoinModules(module)
        super.onDestroy()
    }
}
