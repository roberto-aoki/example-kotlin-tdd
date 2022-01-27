import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.liquibase.gradle") version "2.1.0"
    id("nu.studer.jooq") version ("6.0.1")
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    liquibaseRuntime("org.yaml:snakeyaml:1.17")
    liquibaseRuntime("org.liquibase:liquibase-core:4.5.0")
    liquibaseRuntime("info.picocli:picocli:4.6.1")
    liquibaseRuntime("org.postgresql:postgresql")


    jooqGenerator("org.jooq:jooq-meta-extensions:3.16.3")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.4.3")
    testImplementation("io.mockk:mockk:1.11.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

liquibase {
    activities.register("main") {
        this.arguments = mapOf(
            "logLevel" to "info",
            "classpath" to "$projectDir/src/main/resources",
            "changeLogFile" to "liquibase-changelog.yml",
            "url" to "jdbc:postgresql://localhost:5432/foo",
            "username" to "foo",
            "password" to "foo"
        )
    }
    runList = "main"
}

jooq {
    configurations {
        create("main") {
            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc = null
                generator.apply {
                    database.apply {
                        name = "org.jooq.meta.extensions.ddl.DDLDatabase"
                        properties.add(
                            org.jooq.meta.jaxb.Property().withKey("scripts")
                                .withValue("src/main/resources/migrations/*.sql")
                        )
                        properties.add(
                            org.jooq.meta.jaxb.Property().withKey("defaultNameCase").withValue("lower")
                        )
                    }

                    generate.apply {
                        isDaos = false
                        isPojosEqualsAndHashCode = true
                    }

                    target.apply {
                        packageName = "com.example.foo.jooq"
                    }
                }
            }
        }
    }
}
