import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    id("com.ncorti.ktfmt.gradle") version "0.11.0"
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    api("com.github.ajalt.mordant:mordant:2.0.0-beta7")
    testImplementation(kotlin("test"))

    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    api("org.jline:jline-builtins:3.21.0")
    api("org.jline:jline-reader:3.21.0")
    api("org.jline:jline-terminal:3.21.0")

    api("com.squareup.moshi:moshi:1.14.0")
    api("com.squareup.moshi:moshi-kotlin:1.14.0")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-moshi:2.9.0")
    api("com.squareup.okhttp3:logging-interceptor:4.10.0")


}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "11"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "11"
}

java {
    // Publish Sources
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {

            groupId = "com.github.JeffWright"
            version = "0.6.3"
            artifactId = "scriptutils"

            from(components["java"])
        }
    }
}
