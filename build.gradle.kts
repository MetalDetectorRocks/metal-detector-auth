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
  val kotlinVersion = "1.8.21"
  kotlin("jvm") version kotlinVersion apply false
  kotlin("plugin.spring") version kotlinVersion apply false
  kotlin("plugin.allopen") version kotlinVersion apply false

  id("org.springframework.boot") version "3.0.6" apply false
  id("io.spring.dependency-management") version "1.1.0" apply false
  id("de.europace.docker-publish") version "1.4.3" apply false
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
