plugins {
    java
    id("org.jetbrains.kotlin.jvm") version "1.5.10"
    `maven-publish`
}

repositories {
    mavenCentral()
    maven("https://repo.xbaimiao.com/nexus/content/repositories/releases/")
}
java {
    withSourcesJar()
}

dependencies {
    compileOnly("io.netty:netty-all:4.1.66.Final")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

publishing {
    repositories {
        maven("https://repo.xbaimiao.com/nexus/content/repositories/releases/") {
            credentials {
                username = project.findProperty("user").toString()
                password = project.findProperty("password").toString()
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            groupId = "com.xbaimiao"
        }
    }
}
