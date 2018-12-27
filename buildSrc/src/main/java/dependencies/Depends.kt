package dependencies

@Suppress("unused")
object Depends {
    object GradlePlugin {
        const val android = "com.android.tools.build:gradle:3.2.1"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
        const val ktlint = "gradle.plugin.org.jlleitschuh.gradle:ktlint-gradle:3.0.0"
        const val fabric = "io.fabric.tools:gradle:1.25.4"
        const val androidMaven = "com.github.dcendents:android-maven-gradle-plugin:2.0"
    }

    object Test {
        const val junit = "junit:junit:4.12"
        const val testRunner = "androidx.test:runner:1.1.0"
        const val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0"
        const val robolectric = "org.robolectric:robolectric:3.5.1"

        object Espresso {
            const val core = "androidx.test.espresso:espresso-core:3.1.0-alpha4"
            const val intents = "androidx.test.espresso:espresso-intents:3.1.0-alpha4"
        }
    }

    object AndroidX {
        const val appCompat = "androidx.appcompat:appcompat:1.0.0"
        const val recyclerView = "androidx.recyclerview:recyclerview:1.0.0"
        const val cardView = "androidx.cardview:cardview:1.0.0"
        const val design = "com.google.android.material:material:1.1.0-alpha01"
    }

    object Kotlin {
        const val version = "1.3.11"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$version"
    }

    object Stetho {
        const val version = "1.5.0"
        const val core = "com.facebook.stetho:stetho:$version"
        const val okhttp = "com.facebook.stetho:stetho-okhttp3:$version"
    }

    object Crashlytics {
        const val core = "com.crashlytics.sdk.android:crashlytics:2.8.0@aar"
    }

    object Retrofit {
        private const val version = "2.5.0"
        const val core = "com.squareup.retrofit2:retrofit:$version"
        const val converterMoshi = "com.squareup.retrofit2:converter-moshi:$version"
        const val adapterRxJava2 = "com.squareup.retrofit2:adapter-rxjava2:$version"
    }

    object Kotshi {
        private const val version = "1.0.6"
        const val api = "se.ansman.kotshi:api:$version"
        const val compiler = "se.ansman.kotshi:compiler:$version"
    }

    object Rx {
        const val RxJava = "io.reactivex.rxjava2:rxjava:2.2.4"
        const val RxAndroid = "io.reactivex.rxjava2:rxandroid:2.1.0"
        const val RxKotlin = "io.reactivex.rxjava2:rxkotlin:2.3.0"
    }
}