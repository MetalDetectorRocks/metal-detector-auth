val javaVersion: JavaVersion = JavaVersion.VERSION_11
val dependencyVersions: List<String> = listOf(
    "com.nimbusds:nimbus-jose-jwt:9.16-preview.1"
)
val dependencyGroupVersions: Map<String, String> = mapOf()

plugins {
  val kotlinVersion = "1.5.31"
  kotlin("jvm") version kotlinVersion apply false
  kotlin("plugin.spring") version kotlinVersion apply false
  kotlin("plugin.allopen") version kotlinVersion apply false

  id("org.springframework.boot") version "2.6.0" apply false
  id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
}

subprojects {
  project.apply(plugin = "org.jetbrains.kotlin.jvm")
  project.apply(plugin = "org.jetbrains.kotlin.plugin.spring")
  project.apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
  project.apply(plugin = "io.spring.dependency-management")
  project.apply(plugin = "jacoco")

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
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
      kotlinOptions {
        jvmTarget = javaVersion.toString()
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=compatibility")
      }
    }
    withType<Test> {
      useJUnitPlatform()
      testLogging.showStandardStreams = true
    }
    withType<JacocoReport> {
      reports {
        xml.required.set(true)
        html.required.set(false)
      }
    }
    withType<GroovyCompile> {
      options.encoding = "UTF-8"
    }
  }
}

tasks {
  wrapper {
    gradleVersion = "7.3"
    distributionType = Wrapper.DistributionType.ALL
  }
}
