package dev.nikhi1.eventbrite.clone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module


class SplashActivity : AppCompatActivity() {

    private companion object {
        init {
            loadKoinModules(module {
                viewModel { SplashViewModel() }
            })
        }
    }

    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel.navigate.observe(this, Observer {

        })
    }
}
