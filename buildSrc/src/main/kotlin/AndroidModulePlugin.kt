import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/***
 * This plugin is instantiated every time when you apply the this plugin to build.gradle in feature module
 * <code>apply plugin: 'dev.nikhi1.plugin.android'</code>
 */
class AndroidModulePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        if (project.hasProperty("android")) {
            with(project) {
                plugins.apply("kotlin-android")
                plugins.apply("kotlin-android-extensions")
                configureAndroidBlock()
                if (name != "test_shared") {
                    configureCommonDependencies()
                }
                configureTestSharedDependencies()
                configureCoreModuleForOtherModules()
            }
        }
    }
}

internal fun Project.configureAndroidBlock() = extensions.getByType<BaseExtension>().run {

    buildToolsVersion(Versions.buildTools)
    compileSdkVersion(Versions.compileSDK)

    defaultConfig {
        minSdkVersion(Versions.minSDK)
        targetSdkVersion(Versions.targetSDK)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType(KotlinCompile::class.java).configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
}

internal fun Project.configureCommonDependencies() {

    extensions.getByType<BaseExtension>().run {
        dependencies {
            add("implementation", Libs.material)
            add("implementation", Libs.coreKtx)
            add("implementation", Libs.retrofit)
            add("implementation", Libs.koinViewModel)
            add("implementation", Libs.lifecycle)
            add("implementation", Libs.lifecycleViewModel)
            add("implementation", Libs.lifecycleViewModelKtx)
            add("implementation", Libs.lifecycleLiveDataKtx)
            add("implementation", Libs.coroutines)
            add("implementation", Libs.androidCoroutines)
            add("implementation", Libs.gson)

            if (name != "core") {
                add("implementation", Libs.navFrag)
                add("implementation", Libs.navUi)
            }
        }
    }
}

internal fun Project.configureTestSharedDependencies() {
    val core = findProject(":core")
    val testShared = findProject(":test_shared")
    val app = findProject(":app")

    extensions.getByType<BaseExtension>().run {
        dependencies {
            if (name == "test_shared") {
                if (core != null) {
                    add("implementation", core)
                }
                add("implementation", Libs.lifecycle)
                add("implementation", Libs.lifecycleViewModel)
                add("implementation", Libs.lifecycleViewModelKtx)
                add("implementation", Libs.lifecycleLiveDataKtx)
            }
            if (testShared != null) {
                add("testImplementation", testShared)
            }

            add("testImplementation", Libs.junit)
            add("testImplementation", Libs.mockk)
            add("testImplementation", Libs.coreTesting)
            add("testImplementation", Libs.corountinesTest)

            if (app != null && name != "app" && name != "core" && name != "test_shared") {
                add("androidTestImplementation", app)
            }
        }
    }
}

internal fun Project.configureCoreModuleForOtherModules() {
    if (name == "core") return
    val core = findProject(":core") as Project

    extensions.getByName("android").apply {
        when (this) {
            is BaseExtension -> {
                dependencies {
                    add("implementation", core)
                }
            }
        }
    }
}