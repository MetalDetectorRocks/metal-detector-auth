import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES

val javaVersion: JavaVersion = JavaVersion.VERSION_17
val dependencyVersions = listOf(
    "org.objenesis:objenesis:3.3"
)
val dependencyGroupVersions = mapOf(
    libs.kotestFrameworkEngineJvm.get().group to libs.kotestFrameworkEngineJvm.get().version
)

plugins {
  alias(libs.plugins.kotlinJvmPlugin) apply false
  alias(libs.plugins.kotlinSpringPlugin) apply false
  alias(libs.plugins.kotlinAllopenPlugin) apply false

  alias(libs.plugins.springBootPlugin) apply false
  alias(libs.plugins.springDependencyManagementPlugin) apply false
  alias(libs.plugins.dockerPublishPlugin) apply false
}

subprojects {
  project.apply(plugin = "org.jetbrains.kotlin.jvm")
  project.apply(plugin = "org.jetbrains.kotlin.plugin.spring")
  project.apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
  project.apply(plugin = "io.spring.dependency-management")

  the<DependencyManagementExtension>().apply {
    imports {
      mavenBom(BOM_COORDINATES)
    }
  }

  repositories {
    mavenCentral()
  }

  configure<JavaPluginExtension> {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
  }

  configurations {
    all {
      resolutionStrategy {
        failOnVersionConflict()
        force(dependencyVersions)
        eachDependency {
          val forcedVersion = dependencyGroupVersions[requested.group]
          if (forcedVersion != null) {
            useVersion(forcedVersion)
          }
        }
        cacheDynamicVersionsFor(0, "seconds")
      }
    }
  }

  tasks {
    withType<KotlinCompile> {
      kotlinOptions {
        jvmTarget = javaVersion.toString()
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all-compatibility")
      }
    }
    withType<Test> {
      useJUnitPlatform()
      testLogging.showStandardStreams = true
    }
  }
}
