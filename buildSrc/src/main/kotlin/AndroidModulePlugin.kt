import com.android.build.gradle.BaseExtension
import com.hiya.plugins.JacocoAndroidUnitTestReportExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.sonarqube.gradle.SonarQubeExtension

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
                plugins.apply("kotlin-kapt")

                configureSonarqube()
                configureJacoco()
                configureAndroidBlock()
                if (name != "test_shared") {
                    configureCommonDependencies()
                    configureCoreModuleForOtherModules()
                }
                configureTestSharedDependencies()
                configureTestSharedModuleForOtherModules()
            }
        }
    }
}

internal fun Project.configureAndroidBlock() = extensions.getByType<BaseExtension>().run {

    buildToolsVersion(Versions.Android.buildTools)
    compileSdkVersion(Versions.Android.compileSDK)

    defaultConfig {
        minSdkVersion(Versions.Android.minSDK)
        targetSdkVersion(Versions.Android.targetSDK)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    dataBinding.isEnabled = true

    tasks.withType(KotlinCompile::class.java).configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }

    testOptions {
        unitTests.apply {
            isReturnDefaultValues = true
        }
    }

    buildTypes {
        getByName("debug") {
            //had to turn this off because of some issue with Gradle 6.x and causing jacoco agent to fail
            //this is off by default
            isTestCoverageEnabled = false
        }
    }
}

internal fun Project.configureCommonDependencies() {

    extensions.getByType<BaseExtension>().run {
        dependencies {
            add("implementation", Libs.Design.material)
            add("implementation", Libs.AndroidX.coreKtx)
            add("implementation", Libs.Networking.retrofit)
            add("implementation", Libs.koinViewModel)
            add("implementation", Libs.AndroidX.Lifecycle.ext)
            add("implementation", Libs.AndroidX.Lifecycle.viewModel)
            add("implementation", Libs.AndroidX.Lifecycle.viewModelKtx)
            add("implementation", Libs.AndroidX.Lifecycle.liveDataKtx)
            add("implementation", Libs.coroutines)
            add("implementation", Libs.AndroidX.coroutines)
            add("implementation", Libs.Networking.gson)
            add("implementation", Libs.Networking.responseConverter)

            if (name != "core") {
                add("implementation", Libs.AndroidX.Navigation.navigator)
            }
        }
    }
}

internal fun Project.configureTestSharedDependencies() {
    if (name != "test_shared") return

    extensions.getByType<BaseExtension>().run {
        dependencies {

            add("implementation", Libs.AndroidX.Lifecycle.ext)
            add("implementation", Libs.AndroidX.Lifecycle.viewModel)
            add("implementation", Libs.AndroidX.Lifecycle.viewModelKtx)
            add("implementation", Libs.AndroidX.Lifecycle.liveDataKtx)

            add("implementation", Libs.Testing.junit)
            add("implementation", Libs.Testing.mockk)
            add("implementation", Libs.Testing.archCore)
            add("implementation", Libs.Testing.corountines)
        }
    }
}

internal fun Project.configureCoreModuleForOtherModules() {
    if (name == "core") return
    val core = findProject(":core") as Project

    extensions.getByName("android").apply {
        when (this) {
            is BaseExtension -> {
                //TODO: Need to work on this
                //Idea is to share the [test] folder of [core] with other modules to inject the [DispatchProvider]
                //val test= sourceSets.getByName("test")
                //test.java.setSrcDirs(files("${core.projectDir}/src/test/java", test.java.srcDirs))

                dependencies {
                    add("implementation", core)
                }
            }
        }
    }
}

internal fun Project.configureTestSharedModuleForOtherModules() {
    if (name == "test_shared") return
    val test_shared = findProject(":test_shared") as Project
    extensions.getByName("android").apply {
        when (this) {
            is BaseExtension -> {
                dependencies {
                    add("testImplementation", test_shared)
                    add("testImplementation", Libs.Testing.junit)
                    add("testImplementation", Libs.Testing.mockk)
                    add("testImplementation", Libs.Testing.archCore)
                    add("testImplementation", Libs.Testing.corountines)
                }
            }
        }
    }
}

internal fun Project.configureSonarqube() {
    val plugin = rootProject.plugins.findPlugin("org.sonarqube")
    if (plugin == null) {
        println("Applying sonar qube")
        rootProject.plugins.apply("org.sonarqube")
        rootProject.extensions.getByType<SonarQubeExtension>().run {
            properties {
                property("sonar.projectKey", "nikhil-thakkar_recipe-maker")
                property("sonar.organization", "nikhil-thakkar")
                property("sonar.sources", "src/main/java")
                property("sonar.sources.coveragePlugin", "jacoco")
                property("sonar.host.url", "https://sonarcloud.io/")
                property("sonar.exclusions", "**/*.js,**/test/**, buildSrc/*")
                property("sonar.login", "")
            }
        }
    }

    extensions.getByType<SonarQubeExtension>().run {
        properties {
            property(
                "sonar.coverage.jacoco.xmlReportPaths",
                "${buildDir}/jacoco/jacoco.xml"
            )
        }
    }
}

internal fun Project.configureJacoco() {
    plugins.apply("com.hiya.jacoco-android")

    //apply("https://raw.githubusercontent.com/JakeWharton/SdkSearch/master/gradle/projectDependencyGraph.gradle")
    extensions.getByType<JacocoPluginExtension>().run {
        toolVersion = "0.8.5"
    }

    tasks.withType<Test>().run {
        all {
            configure<JacocoTaskExtension>() {
                isIncludeNoLocationClasses = true
            }
        }
    }

    //Exclude androidx databinding files
    extensions.getByType<JacocoAndroidUnitTestReportExtension>().run {
        excludes = excludes + listOf(
            "androidx/databinding/**/*.class",
            "**/androidx/databinding/*Binding.class",
            "**/**Bind**/**"
        )
    }

    /*
     * The [com.hiya.jacoco-android] plugin doesn't add the code coverage execution data for Instrumentation tests which are generated by running
     * [connectedDebugAndroidTest] or [connectedAndroidTest]
     * Issue open https://github.com/autonomousapps/jacoco-android-gradle-plugin/issues/1
     * TODO: Remove this block once the issue is fixed
     */
    afterEvaluate {
        tasks.withType<JacocoReport>().run {
            all {
                val tree = fileTree(buildDir)
                tree.include("**/*.ec")
                executionData(tree)
            }
        }
    }
}