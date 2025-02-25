package yokai.presentation.onboarding.steps

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import eu.kanade.tachiyomi.R
import yokai.i18n.MR
import yokai.util.lang.getString
import dev.icerock.moko.resources.compose.stringResource
import eu.kanade.tachiyomi.util.system.toast
import kotlinx.coroutines.flow.collectLatest
import uy.kohesive.injekt.injectLazy
import yokai.domain.storage.StoragePreferences
import yokai.presentation.component.preference.storageLocationText
import yokai.presentation.theme.Size

internal class StorageStep : OnboardingStep {

    private val storagePref: StoragePreferences by injectLazy()

    private var _isComplete by mutableStateOf(false)

    override val isComplete: Boolean
        get() = _isComplete

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val handler = LocalUriHandler.current

        // SAF launcher using OpenDocumentTree.
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocumentTree()
        ) { uri: Uri? ->
            if (uri != null) {
                // Persist permissions for the selected URI.
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                // Update storage preference with the new URI.
                storagePref.baseStorageDirectory().set(uri.toString())
            } else {
                context.toast(MR.strings.file_picker_error)
            }
        }

        Column(
            modifier = Modifier.padding(Size.medium).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Size.small)
        ) {
            Text(
                stringResource(
                    MR.strings.onboarding_storage_info,
                    stringResource(MR.strings.app_name),
                    storageLocationText(storagePref.baseStorageDirectory())
                )
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { launcher.launch(null) }
            ) {
                Text(stringResource(MR.strings.onboarding_storage_action_select))
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = Size.small),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(stringResource(MR.strings.onboarding_storage_help_info))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    handler.openUri(
                        "https://mihon.app/docs/faq/storage#migrating-from-tachiyomi-v0-14-x-or-earlier"
                    )
                }
            ) {
                Text(stringResource(MR.strings.onboarding_storage_help_action))
            }
        }

        LaunchedEffect(Unit) {
            storagePref.baseStorageDirectory().changes().collectLatest {
                _isComplete = true
            }
        }
    }
}
