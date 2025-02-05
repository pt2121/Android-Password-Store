/*
 * Copyright © 2014-2021 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */

plugins {
  id("com.android.library")
  kotlin("android")
  `aps-plugin`
}

android {
  defaultConfig {
    testApplicationId = "dev.msfjarvis.aps.cryptopgp.test"
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
}

dependencies {
  api(projects.cryptoCommon)
  implementation(libs.androidx.annotation)
  implementation(libs.aps.gopenpgp)
  implementation(libs.dagger.hilt.core)
  implementation(libs.kotlin.coroutines.core)
  implementation(libs.thirdparty.kotlinResult)
  androidTestImplementation(libs.bundles.testDependencies)
  androidTestImplementation(libs.kotlin.coroutines.test)
  androidTestImplementation(libs.bundles.androidTestDependencies)
}
