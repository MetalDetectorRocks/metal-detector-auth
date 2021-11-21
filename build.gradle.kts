val javaVersion: JavaVersion = JavaVersion.VERSION_17
val dependencyVersions: List<String> = listOf(
    "com.nimbusds:nimbus-jose-jwt:9.15.2"
)
val dependencyGroupVersions: Map<String, String> = mapOf()

plugins {
  kotlin("jvm") version "1.6.0" apply false
  kotlin("plugin.spring") version "1.6.0" apply false
  kotlin("plugin.allopen") version "1.6.0" apply false

  id("org.springframework.boot") version "2.6.0" apply false
  id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
}

subprojects {
  project.apply(plugin = "org.jetbrains.kotlin.jvm")
  project.apply(plugin = "org.jetbrains.kotlin.plugin.spring")
  project.apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
  project.apply(plugin = "io.spring.dependency-management")

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
