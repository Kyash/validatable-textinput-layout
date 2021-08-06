package dependencies

@Suppress("unused")
object Depends {
    object GradlePlugin {
        const val android = "com.android.tools.build:gradle:7.0.0"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
    }

    object Test {
        const val junit = "junit:junit:4.13.2"
        const val testRunner = "androidx.test:runner:1.4.0"
        const val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0"
        const val robolectric = "org.robolectric:robolectric:4.6.1"

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
        const val version = "1.5.21"
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
        private const val version = "2.9.0"
        const val core = "com.squareup.retrofit2:retrofit:$version"
        const val converterMoshi = "com.squareup.retrofit2:converter-moshi:$version"
        const val adapterRxJava3 = "com.squareup.retrofit2:adapter-rxjava3:$version"
    }

    object Kotshi {
        private const val version = "1.0.6"
        const val api = "se.ansman.kotshi:api:$version"
        const val compiler = "se.ansman.kotshi:compiler:$version"
    }

    object Rx {
        const val RxJava = "io.reactivex.rxjava3:rxjava:3.0.13"
        const val RxAndroid = "io.reactivex.rxjava3:rxandroid:3.0.0"
        const val RxKotlin = "io.reactivex.rxjava3:rxkotlin:3.0.1"
    }
}
