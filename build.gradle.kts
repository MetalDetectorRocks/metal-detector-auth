import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES

val javaVersion: JavaVersion = JavaVersion.VERSION_21

val dependencyVersions = listOf(
    "com.nimbusds:nimbus-jose-jwt:9.41.2",
    "org.jetbrains:annotations:25.0.0",
    "org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.9.0",
    "org.opentest4j:opentest4j:1.3.0"
)

val dependencyGroupVersions = mapOf(
    libs.kotestFrameworkEngineJvm.get().group to libs.kotestFrameworkEngineJvm.get().version
)

plugins {
  alias(libs.plugins.kotlinJvm) apply false
  alias(libs.plugins.kotlinSpring) apply false
  alias(libs.plugins.kotlinAllopen) apply false

  alias(libs.plugins.springBoot) apply false
  alias(libs.plugins.springDependencyManagement) apply false
  alias(libs.plugins.dockerPublish) apply false
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
