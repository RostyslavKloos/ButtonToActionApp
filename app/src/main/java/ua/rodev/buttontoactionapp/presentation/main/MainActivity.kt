package ua.rodev.buttontoactionapp.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import ua.rodev.buttontoactionapp.ProvideViewModel
import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.presentation.Screen
import ua.rodev.buttontoactionapp.presentation.action.actions.CallAction
import ua.rodev.buttontoactionapp.presentation.action.actions.NotificationAction

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ProvideViewModel {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var observer: MyLifecycleObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.actions -> viewModel.replace(Screen.Action)
                R.id.settings -> viewModel.goSettings()
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

    override fun <T : ViewModel> provideViewModel(clazz: Class<T>, owner: ViewModelStoreOwner): T {
        return (application as ProvideViewModel).provideViewModel(clazz, owner)
    }
}
