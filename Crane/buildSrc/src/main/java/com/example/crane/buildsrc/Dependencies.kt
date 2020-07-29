/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.crane.buildsrc

object Versions {
    const val ktLint = "0.37.2"
}

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:4.2.0-alpha05"
    const val ktLint = "com.pinterest:ktlint:${Versions.ktLint}"
    const val googleMaps = "com.google.android.gms:play-services-maps:17.0.0"

    object Accompanist {
        private const val version = "0.1.8.ui-${AndroidX.Compose.snapshot}-SNAPSHOT"
//        private const val version = "0.1.7"
        const val coil = "dev.chrisbanes.accompanist:accompanist-coil:$version"
    }

    object Kotlin {
        private const val version = "1.4.0-rc"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.3.0-alpha01"

        object Compose {
            const val snapshot = "6717962"
            const val version = "0.1.0-SNAPSHOT"

            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val runtimeLivedata = "androidx.compose.runtime:runtime-livedata:$version"
            const val material = "androidx.compose.material:material:$version"
            const val foundation = "androidx.compose.foundation:foundation:$version"
            const val layout = "androidx.compose.foundation:foundation-layout:$version"
            const val animation = "androidx.compose.animation:animation:$version"
        }

        object UI {
            const val tooling = "androidx.ui:ui-tooling:${Compose.version}"
        }
    }
}

object Urls {
    const val mavenCentralSnapshotRepo = "https://oss.sonatype.org/content/repositories/snapshots/"
    const val composeSnapshotRepo = "https://androidx-dev-prod.appspot.com/snapshots/builds/" +
        // early versions Dev16
        "${Libs.AndroidX.Compose.snapshot}/artifacts/ui/repository/"
}
