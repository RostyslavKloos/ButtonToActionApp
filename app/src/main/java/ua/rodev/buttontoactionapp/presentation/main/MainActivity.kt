package ua.rodev.buttontoactionapp.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.presentation.Screen
import ua.rodev.buttontoactionapp.presentation.action.CallAction
import ua.rodev.buttontoactionapp.presentation.action.NotificationAction
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
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.actions -> viewModel.navigate(Screen.Action)
                R.id.settings -> viewModel.navigate(Screen.Contacts)
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
