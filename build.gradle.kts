plugins {
    kotlin("multiplatform") version "1.3.70"
    id("com.epam.drill.cross-compilation") version "0.15.1"
    `maven-publish`
}

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://dl.bintray.com/kotlin/kotlinx/")
}

val serializationRuntimeVersion: String by extra
val ktorUtilVersion: String by extra
val loggingVersion: String by extra

kotlin {

    crossCompilation {
        common {
            defaultSourceSet.dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:$serializationRuntimeVersion")
                implementation("io.ktor:ktor-utils-native:$ktorUtilVersion")
            }
        }
    }
    macosX64()
    mingwX64()
    linuxX64()

    jvm {
        compilations["main"].defaultSourceSet {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationRuntimeVersion")
                implementation("io.github.microutils:kotlin-logging:$loggingVersion")
            }
        }
        compilations["test"].defaultSourceSet {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serializationRuntimeVersion")
                implementation("io.github.microutils:kotlin-logging-common:$loggingVersion")
            }
        }

        commonTest {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-common")
                implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
            }
        }
    }
}
