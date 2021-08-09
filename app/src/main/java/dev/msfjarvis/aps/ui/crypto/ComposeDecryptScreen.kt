/*
 * Copyright © 2014-2021 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */

package dev.msfjarvis.aps.ui.crypto

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import dev.msfjarvis.aps.R
import dev.msfjarvis.aps.data.crypto.CryptoViewModel
import dev.msfjarvis.aps.data.passfile.PasswordEntry
import dev.msfjarvis.aps.util.extensions.snackbar

@OptIn(ExperimentalUnitApi::class)
@Composable
fun DecryptScreen(
  viewModel: CryptoViewModel,
) {
  var passwordEntry by remember { mutableStateOf<PasswordEntry?>(null) }
  val coroutineScope = rememberCoroutineScope()
  val context = LocalContext.current as FragmentActivity
  Column(modifier = Modifier.padding(horizontal = 16.dp)) {
    Text(
      text = viewModel.getRelativeParentPath(),
      fontSize = TextUnit(18f, TextUnitType.Sp),
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(vertical = 8.dp),
    )
    Text(
      text = viewModel.getName(),
      color = MaterialTheme.colors.primary,
      fontSize = TextUnit(24f, TextUnitType.Sp),
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(bottom = 8.dp),
    )
    Divider()
    if (passwordEntry != null) {
      DecryptedPassword(passwordEntry!!) { text ->
        viewModel.copyToClipboard(context, text)
        context.snackbar(message = context.resources.getString(R.string.clipboard_copied_text))
      }
    } else {
      Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
          modifier = Modifier.align(Alignment.Center).fillMaxSize(0.4f),
        )
      }
      LaunchedEffect(passwordEntry) {
        passwordEntry =
          viewModel.decryptPassword(
            viewModel.getFullPath(),
            coroutineScope,
          )
      }
    }
  }
}

@Composable
fun DecryptedPassword(
  entry: PasswordEntry,
  copyToClipboard: (String) -> Unit,
) {
  if (!entry.password.isNullOrBlank()) {
    val password = entry.password!!
    PasswordField(
      label = "Password",
      text = password,
    ) {
      IconButton(onClick = { copyToClipboard(password) }) {
        Icon(
          painter = painterResource(id = R.drawable.ic_content_copy),
          contentDescription = "",
        )
      }
    }
  }
  if (!entry.username.isNullOrBlank()) {
    val username = entry.username!!
    PasswordField(
      label = "Username",
      text = username,
    ) {
      IconButton(onClick = { copyToClipboard(username) }) {
        Icon(
          painter = painterResource(id = R.drawable.ic_content_copy),
          contentDescription = "",
        )
      }
    }
  }
  if (entry.hasTotp()) {
    val totp = entry.totp.collectAsState()
    PasswordField(
      label = "OTP",
      text = totp.value,
    ) {
      IconButton(onClick = { copyToClipboard(totp.value) }) {
        Icon(
          painter = painterResource(id = R.drawable.ic_content_copy),
          contentDescription = "",
        )
      }
    }
  }
  entry.extraContent.forEach { (key, value) ->
    PasswordField(
      label = key,
      text = value,
      copyToClipboard,
    )
  }
}

@Composable
fun PasswordField(
  label: String,
  text: String,
  copyToClipboard: ((String) -> Unit)? = null,
  trailingIcon: @Composable (() -> Unit)? = null,
) {
  var modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth()
  if (copyToClipboard != null) {
    modifier = Modifier.clickable { copyToClipboard(text) }.then(modifier)
  }
  OutlinedTextField(
    value = text,
    onValueChange = {},
    label = { Text(label) },
    trailingIcon = trailingIcon,
    readOnly = true,
    modifier = modifier,
  )
}
