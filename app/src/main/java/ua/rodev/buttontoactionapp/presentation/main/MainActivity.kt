package ua.rodev.buttontoactionapp.presentation.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import dagger.hilt.android.AndroidEntryPoint
import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.presentation.action.CallAction
import ua.rodev.buttontoactionapp.presentation.action.NotificationAction
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    lateinit var observer: MyLifecycleObserver

    @Inject
    lateinit var log: ua.rodev.buttontoactionapp.core.Log

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.collect(this) {
            it.navigate(supportFragmentManager, R.id.container)
        }

        observer = MyLifecycleObserver(this)
        lifecycle.addObserver(observer)

        viewModel.init()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.compose -> {
                log.print("compose item selected")
                true
            }
            R.id.contacts_intent -> {
                log.print("contacts_intent item selected")
                true
            }
            R.id.contacts_screen -> {
                log.print("contacts_screen item selected")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action == NotificationAction.SHOW_CONTACTS_ACTION) {
            CallAction(observer.resultLauncher).perform()
        }
    }
}

class MyLifecycleObserver(
    private val activity: FragmentActivity,
) : DefaultLifecycleObserver {
    lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(owner: LifecycleOwner) {
        resultLauncher = activity.activityResultRegistry.register(
            "key",
            owner,
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val contactUri = result.data?.data ?: return@register
                val contactId = arrayOf(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                val cursor = activity.contentResolver.query(
                    contactUri, contactId, null, null, null, null
                )
                if (cursor?.moveToFirst()!!) {
                    val id = cursor.getString(0)
                    val intent1 = Intent(Intent.ACTION_VIEW)
                    val uri: Uri = Uri.withAppendedPath(
                        ContactsContract.Contacts.CONTENT_URI,
                        id.toString()
                    )
                    intent1.data = uri
                    activity.startActivity(intent1)
                }
                cursor.close()
            }
        }
    }
}