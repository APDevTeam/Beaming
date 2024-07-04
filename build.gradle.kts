plugins {
    `java-library`
    `maven-publish`
    id("io.github.0ffz.github-packages") version "1.2.1"
    id("io.papermc.hangar-publish-plugin") version "0.1.2"
}

repositories {
    gradlePluginPortal()
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven { githubPackage("apdevteam/movecraft")(this) }
    maven("https://repo.essentialsx.net/releases/")
}

dependencies {
    api("org.jetbrains:annotations-java5:24.1.0")
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
    compileOnly("net.countercraft:movecraft:+")
    compileOnly("net.essentialsx:EssentialsX:2.20.1")
}

group = "net.TylerS1066.Beaming"
version = "2.0.0_beta-1_gradle"
description = "Beaming"
java.toolchain.languageVersion = JavaLanguageVersion.of(17)

tasks.jar {
    archiveBaseName.set("Beaming")
    archiveClassifier.set("")
    archiveVersion.set("")
}

tasks.processResources {
    from(rootProject.file("LICENSE.md"))
    filesMatching("*.yml") {
        expand(mapOf("projectVersion" to project.version))
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "net.TylerS1066.beaming"
            artifactId = "beaming"
            version = "${project.version}"

            artifact(tasks.jar)
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/apdevteam/beaming")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

hangarPublish {
    publications.register("plugin") {
        version.set(project.version as String)
        channel.set("Release")
        id.set("Airship-Pirates/Beaming")
        apiKey.set(System.getenv("HANGAR_API_TOKEN"))
        platforms {
            register(io.papermc.hangarpublishplugin.model.Platforms.PAPER) {
                jar.set(tasks.jar.flatMap { it.archiveFile })
                platformVersions.set(listOf("1.18.2-1.21"))
                dependencies {
                    hangar("Movecraft") {
                        required.set(true)
                    }
                    hangar("Essentials") {
                        required.set(false)
                    }
                }
            }
        }
    }
}
