import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    java
    `maven-publish`
    kotlin("jvm") version "1.3.21" apply false
}

allprojects {
    group = "com.m3.tracing"
    version = "1.0.1-SNAPSHOT"

    repositories {
        jcenter()
    }
}

dependencies {
    // Make the root project archives configuration depend on every subproject
    subprojects.forEach {
        archives(it)
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "maven-publish")

    tasks.withType<KotlinCompile> {
        kotlinOptions.javaParameters = true
        kotlinOptions.freeCompilerArgs = listOf("-Xprogressive", "-Xjvm-default=enable")
        kotlinOptions.jvmTarget = "1.8"
    }

    val opencensusVersion by extra { "0.20.0" }

    //Intentionally support Servlet API 3.0
    //Intentionally support Servlet API 3.0
    val servletApiVersion by extra { "3.0.1" }

    // Following versions are based on spring-boot
    val springBootVersion by extra { "2.1.4.RELEASE" }
    val springVersion by extra { "5.1.6.RELEASE" }
    val apacheHttpClientVersion by extra { "4.5.8" }

    dependencies {
        compile(kotlin("stdlib-jdk8"))

        implementation("com.google.code.findbugs:jsr305:3.0.2")
        implementation("com.google.guava:guava:27.1-jre")

        implementation("org.slf4j:slf4j-api:1.7.26")
        testImplementation("org.slf4j:jul-to-slf4j:1.7.26")
        testImplementation("ch.qos.logback:logback-classic:1.2.3")

        testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
        testImplementation("org.mockito:mockito-junit-jupiter:2.26.0")
        testImplementation("org.mockito:mockito-core:2.27.0")
        testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
        testImplementation("com.google.truth:truth:0.44")
        testImplementation("com.google.truth.extensions:truth-java8-extension:0.44")
    }

    publishing {
        repositories {
            maven {
                // change to point to your repo, e.g. http://my.org/repo
                if (version.toString().endsWith("SNAPSHOT")) {
                    url = uri("http://maven:8081/artifactory/libs-snapshots")
                } else {
                    url = uri("http://maven:8081/artifactory/libs-releases")
                }
            }
        }

        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])

                pom {
                    url.set("https://github.com/m3dev/m3-tracing")

                    licenses {
                        license {
                            name.set("MIT")
                            url.set("https://github.com/m3dev/m3-tracing/blob/master/LICENSE")
                        }
                    }
                }
            }
        }
    }
}
