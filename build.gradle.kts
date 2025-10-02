import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.9.21"
  id("com.ncorti.ktfmt.gradle") version "0.16.0"
  `maven-publish`
}

repositories {
  mavenLocal()
  mavenCentral()
  repositories { maven { setUrl("https://jitpack.io") } }
}

dependencies {
  api("com.github.ajalt.mordant:mordant:2.0.0-beta7")
  testImplementation(kotlin("test"))

  api("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

  api("org.jline:jline-builtins:3.21.0")
  api("org.jline:jline-reader:3.21.0")
  api("org.jline:jline-terminal:3.21.0")

  api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

  api("com.github.JeffWright:jtbw-retrofit:0.9.0")
}

tasks.test { useJUnitPlatform() }

ktfmt {
  googleStyle() // 2-space indentation
}

tasks.withType<KotlinCompile> { kotlinOptions.jvmTarget = "11" }

kotlin { jvmToolchain(17) }

java {
  // Publish Sources
  withSourcesJar()
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      groupId = "com.github.JeffWright"
      version = "0.8.0"
      artifactId = "scriptutils"

      from(components["java"])
    }
  }
}
