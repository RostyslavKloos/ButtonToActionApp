package ua.rodev.buttontoactionapp.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.presentation.Screen
import ua.rodev.buttontoactionapp.presentation.action.actions.CallAction
import ua.rodev.buttontoactionapp.presentation.action.actions.NotificationAction
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var observer: MyLifecycleObserver

    @Inject
    lateinit var log: ua.rodev.buttontoactionapp.core.Log

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.actions -> viewModel.replace(Screen.Action)
                R.id.settings -> {
                    if (viewModel.isComposeUsed()) {
                        viewModel.replace(Screen.SettingsCompose)
                    } else {
                        viewModel.replace(Screen.Settings)
                    }
                }
            }
            true
        }

        viewModel.collect(this) {
            it.navigate(supportFragmentManager, R.id.container)
        }

        observer = MyLifecycleObserver(this)
        lifecycle.addObserver(observer)

        viewModel.init()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action == NotificationAction.SHOW_CONTACTS_ACTION) {
            CallAction(observer.resultLauncher).perform()
        }
    }
}
