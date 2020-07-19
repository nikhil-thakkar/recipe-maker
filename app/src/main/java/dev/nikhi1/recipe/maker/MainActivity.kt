package dev.nikhi1.recipe.maker

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.google.android.play.core.splitcompat.SplitCompat
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

private var module: Module = module {
    viewModel { MainViewModel() }
}

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        loadKoinModules(module)

        if (savedInstanceState == null) {
            viewModel.navigate.observe(this, Observer {
                findNavController(R.id.nav_host_fragment).navigate(
                    R.id.action_fragment_to_second_graph, null,
                    NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
                )
            })
        }
    }

    override fun onDestroy() {
        unloadKoinModules(module)
        super.onDestroy()
    }
}
