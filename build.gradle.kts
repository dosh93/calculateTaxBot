import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.1"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
	kotlin("plugin.jpa") version "1.7.22"
	kotlin("plugin.serialization") version "1.7.22"
}

group = "ru.rmatyuk"
version = "1.0.1"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.glassfish.expressly:expressly:5.0.0")
	implementation("org.hibernate.validator:hibernate-validator:8.0.0.Final")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
	implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.7.0")
	implementation("mysql:mysql-connector-java:8.0.31")
	implementation("org.telegram:telegrambots:6.4.0")
	implementation("com.vdurmont:emoji-java:5.1.1")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.named("jar") {
	enabled = false
}