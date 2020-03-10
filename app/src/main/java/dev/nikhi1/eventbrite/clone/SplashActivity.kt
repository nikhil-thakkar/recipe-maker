package dev.nikhi1.eventbrite.clone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        loadKoinModules(module)

        viewModel.navigate.observe(this, Observer {

        })
    }

    override fun onDestroy() {
        unloadKoinModules(module)
        super.onDestroy()
    }
}
