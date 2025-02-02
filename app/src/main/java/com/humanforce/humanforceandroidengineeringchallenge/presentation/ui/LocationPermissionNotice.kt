package com.humanforce.humanforceandroidengineeringchallenge.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.humanforce.humanforceandroidengineeringchallenge.R

@Composable
fun LocationPermissionNotice(
    modifier: Modifier,
    btnLabel: String,
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onBtnClick: () -> Unit) {

    if (showDialog) {
        Dialog(onDismissRequest = onDismissRequest) {
            Column(
                modifier = modifier
                    .padding(5.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerLow),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(5.dp),
                    text = stringResource(R.string.location_permission_request),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Button(onClick = {
                    onBtnClick()
                    onDismissRequest()
                }) {
                    Text(btnLabel)
                }
            }
        }

    }
}
