/*
 * Copyright © 2014-2021 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */

package dev.msfjarvis.aps.data.crypto

import android.content.ClipData
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.msfjarvis.aps.data.passfile.PasswordEntry
import dev.msfjarvis.aps.injection.crypto.CryptoSet
import dev.msfjarvis.aps.injection.password.PasswordEntryFactory
import dev.msfjarvis.aps.ui.crypto.DecryptActivityV2
import dev.msfjarvis.aps.util.extensions.clipboard
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltViewModel
class CryptoViewModel
@Inject
constructor(
  private val cryptoSet: CryptoSet,
  private val passwordEntryFactory: PasswordEntryFactory,
  private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

  fun setFullPath(path: String) {
    savedStateHandle["FULL_PATH"] = path
  }

  fun setRelativeParentPath(path: String) {
    savedStateHandle["RELATIVE_PARENT_PATH"] = path
  }

  fun setName(name: String) {
    savedStateHandle["NAME"] = name
  }

  fun getFullPath() = requireNotNull(savedStateHandle.get<String>("FULL_PATH"))

  fun getRelativeParentPath() = requireNotNull(savedStateHandle.get<String>("RELATIVE_PARENT_PATH"))

  fun getName() = requireNotNull(savedStateHandle.get<String>("NAME"))

  fun copyToClipboard(
    context: Context,
    text: String,
  ) {
    val clipboard = context.clipboard ?: return
    val clip = ClipData.newPlainText("pgp_handler_result_pm", text)
    clipboard.setPrimaryClip(clip)
  }

  /**
   * Given a [filePath], decrypts it with the first [CryptoHandler] instance that claims to support
   * the file and creates a [PasswordEntry] instance from the decrypted data. The [coroutineScope]
   * is provided to [PasswordEntryFactory.create] to define the lifecycle of how long the TOTP
   * values are generated.
   */
  suspend fun decryptPassword(
    filePath: String,
    coroutineScope: CoroutineScope,
  ): PasswordEntry {
    val message = withContext(Dispatchers.IO) { File(filePath).readBytes() }
    val result =
      withContext(Dispatchers.IO) {
        val crypto = cryptoSet.first { it.canHandle(filePath) }
        crypto.decrypt(
          DecryptActivityV2.PRIV_KEY,
          DecryptActivityV2.PASS.toByteArray(charset = Charsets.UTF_8),
          message,
        )
      }
    return passwordEntryFactory.create(coroutineScope, result)
  }
}
