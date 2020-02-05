plugins {
    id("kotlin-multiplatform")
    id("kotlinx-serialization")
    `maven-publish`
}

repositories {
    mavenCentral()
    jcenter()
}

kotlin {
    targets {
        if (isDevMode)
            currentTarget {
                compilations["main"].apply {
                    defaultSourceSet {
                        kotlin.srcDir("./src/nativeCommonMain/kotlin")
                        applyDependencies()
                    }
                }
            }
        else {
            setOf(linuxX64(), macosX64(), mingwX64())
        }
        jvm()

    }

    sourceSets {
        jvm {
            compilations["main"].defaultSourceSet {
                dependencies {
                    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationRuntimeVersion")
                    implementation("io.github.microutils:kotlin-logging:$loggingVersion")
                }
                compilations["test"].defaultSourceSet {
                    dependencies {
                        implementation(kotlin("test-junit"))
                    }
                }
            }
        }
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

        if (!isDevMode) {
            val commonNativeMain = maybeCreate("nativeCommonMain")
            targets.filterIsInstance<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().forEach {
                it.compilations.forEach { knCompilation ->
                    if (knCompilation.name == "main")
                        knCompilation.defaultSourceSet {
                            dependsOn(commonNativeMain)
                            applyDependencies()
                        }
                }
            }
        }
    }
}

tasks.withType<AbstractTestTask> {
    testLogging.showStandardStreams = true
}
tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<org.jetbrains.kotlin.gradle.dsl.KotlinCommonOptions>> {
    kotlinOptions.freeCompilerArgs += "-Xuse-experimental=kotlinx.serialization.ImplicitReflectionSerializer"
}
tasks.build {
    dependsOn("publishToMavenLocal")
}

fun org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet.applyDependencies() {
    dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:$serializationRuntimeVersion")
        implementation("io.ktor:ktor-utils-native:$ktorUtilVersion")
    }
}

publishing {
    repositories {
        maven {
            url = uri("https://oss.jfrog.org/oss-release-local")
            credentials {
                username =
                        if (project.hasProperty("bintrayUser"))
                            project.property("bintrayUser").toString()
                        else System.getenv("BINTRAY_USER")
                password =
                        if (project.hasProperty("bintrayApiKey"))
                            project.property("bintrayApiKey").toString()
                        else System.getenv("BINTRAY_API_KEY")
            }
        }
    }
}
