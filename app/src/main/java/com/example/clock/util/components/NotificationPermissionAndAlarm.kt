package com.example.clock.util.components

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.example.clock.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermission() {
    val permissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    if (!permissionState.status.isGranted && permissionState.status.shouldShowRationale) {
        AlertDialog(
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false,
            ),
            icon = { Icon(Icons.Filled.NotificationsActive, contentDescription = null) },
            title = {
                Text(
                    text = stringResource(id = R.string.notification_permission),
                    style = MaterialTheme.typography.headlineSmall,
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.notification_permission_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { permissionState.launchPermissionRequest() },
                ) {
                    Text(stringResource(id = R.string.confirm))
                }
            },
            onDismissRequest = {},
        )
    } else {
        LaunchedEffect(key1 = Unit) {
            this.launch {
                permissionState.launchPermissionRequest()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun CheckExactAlarmPermission() {
    val context = LocalContext.current
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val openDialog = remember { mutableStateOf(true) }
    if (!alarmManager.canScheduleExactAlarms() && openDialog.value) {
        AlertDialog(
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false,
            ),
            icon = { Icon(Icons.Filled.Alarm, contentDescription = null) },
            title = {
                Text(
                    text = stringResource(id = R.string.exact_alarm),
                    style = MaterialTheme.typography.headlineSmall,
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.exact_alarm_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val alarmPermissionIntent = Intent(
                            Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                            Uri.parse("package:com.example.clock"),
                        )
                        context.startActivity(alarmPermissionIntent)
                        openDialog.value = false
                    },
                ) {
                    Text(stringResource(id = R.string.confirm))
                }
            },
            onDismissRequest = {},
        )
    }
}
