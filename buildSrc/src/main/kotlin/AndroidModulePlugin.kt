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
            project.plugins.apply("kotlin-android")
            project.plugins.apply("kotlin-android-extensions")
            project.configureAndroidBlock()
            if (project.name != "test_shared") {
                project.configureCommonDependencies()
            }
            project.configureTestSharedDependencies()
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
            jvmTarget = "1.8"
        }
    }
}

internal fun Project.configureCommonDependencies() {
    //find the app module
    //replace this with your base module if using otherwise
    val app = findProject(":app")
    val core = findProject(":core")
    extensions.getByType<BaseExtension>().run {
        dependencies {
            if (!(name == "app" || name == "core")) {
                // Since the feature modules need to depend on the :app or the base module
                //Don't add the app to itself or to core
                if (app != null) {
                    add("implementation", app)
                }

                if (core != null) {
                    add("implementation", core)
                }
            }
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