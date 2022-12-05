package ua.rodev.buttontoactionapp.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ua.rodev.buttontoactionapp.R

@AndroidEntryPoint
class SettingsComposeFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {

            var useComposeChecked by remember { mutableStateOf(viewModel.isComposeUsed()) }
            var useContactsScreen by remember { mutableStateOf(viewModel.isContactsScreenUsed()) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    verticalAlignment = CenterVertically
                ) {
                    Text(text = stringResource(R.string.use_compose))
                    Spacer(modifier = Modifier.width(4.dp))
                    Switch(
                        checked = useComposeChecked,
                        onCheckedChange = {
                            useComposeChecked = !useComposeChecked
                            viewModel.useCompose(it)
                            viewModel.recreateFragment()
                        }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    verticalAlignment = CenterVertically
                ) {
                    Text(text = stringResource(R.string.use_contacts_screen))
                    Spacer(modifier = Modifier.width(4.dp))
                    Switch(
                        checked = useContactsScreen,
                        onCheckedChange = {
                            useContactsScreen = !useContactsScreen
                            viewModel.useCompose(it)
                        }
                    )
                }
            }
        }
    }
}
