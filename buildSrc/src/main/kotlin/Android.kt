import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        if (project.hasProperty("android")) {
            project.configureAndroidBlock()
            project.configureTestDependencies()
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

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

internal fun Project.configureTestDependencies() = extensions.getByType<BaseExtension>().run {

    dependencies {
        add("testImplementation", Libs.junit)
        add("testImplementation", Libs.mockk)
        add("testImplementation", Libs.corountinesTest)
    }
}