plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

repositories {
    mavenCentral()
}

val javafxVersion = "21.0.2"
val javafxPlatform = when (org.gradle.internal.os.OperatingSystem.current()) {
    org.gradle.internal.os.OperatingSystem.MAC_OS -> "mac-aarch64"
    org.gradle.internal.os.OperatingSystem.LINUX -> "linux"
    org.gradle.internal.os.OperatingSystem.WINDOWS -> "win"
    else -> "linux"
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation(libs.junit.jupiter.engine)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(libs.guava)

    implementation("org.openjfx:javafx-controls:$javafxVersion:$javafxPlatform")
    implementation("org.openjfx:javafx-graphics:$javafxVersion:$javafxPlatform")
    implementation("org.openjfx:javafx-base:$javafxVersion:$javafxPlatform")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "org.example.MainAppKt"
}

tasks.withType<JavaExec> {
    val jvmArgs = mutableListOf(
        "--module-path", classpath.asPath,
        "--add-modules", "javafx.controls,javafx.graphics,javafx.base"
    )
    jvmArgs(jvmArgs)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
