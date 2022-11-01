package ua.rodev.buttontoactionapp.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ua.rodev.buttontoactionapp.ActionUi
import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.presentation.Screen

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            viewModel.navigation.collect {
                it.navigate(supportFragmentManager, R.id.container)
            }
        }

        viewModel.init()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action == ActionUi.Notification.SHOW_CONTACTS_ACTION) {
            viewModel.navigate(Screen.Contacts)
        }
    }
}

