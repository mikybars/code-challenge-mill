plugins {
	java
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.openapi.generator") version "7.12.0"
	id("jacoco")
}

group = "com.github.mikybars"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.mapstruct:mapstruct:1.5.3.Final")
	implementation("io.swagger.core.v3:swagger-core:2.2.28")
	compileOnly("org.projectlombok:lombok")
	implementation("io.github.wimdeblauwe:error-handling-spring-boot-starter:4.5.0")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("com.h2database:h2")
	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.3.Final")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("com.approvaltests:approvaltests:24.19.0")
	testImplementation("com.google.code.gson:gson:2.12.1")
	testImplementation("com.tngtech.archunit:archunit-junit5:1.4.0")
	testImplementation("net.javacrumbs.json-unit:json-unit:4.1.0")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

openApiGenerate {
	generatorName.set("spring")  // https://openapi-generator.tech/docs/generators/spring/
	inputSpec.set("$rootDir/spec/openapi.yml")
	apiPackage.set("${group}.challenge.prices.adapters.in.rest")
	modelPackage.set(apiPackage)
	modelNameSuffix.set("Dto")
	configOptions.set(mapOf(
		"useSpringBoot3" to "true",
		"interfaceOnly" to "true",
		"skipDefaultInterface" to "true",
		"dateLibrary" to "java8-localdatetime",
		"openApiNullable" to "false",
	))
}

sourceSets {
	main {
		java {
			srcDir("${openApiGenerate.outputDir.get()}/src/main/java")
		}
	}
}

tasks.withType<JavaCompile> {
	dependsOn("openApiGenerate")
	options.compilerArgs.addAll(listOf(
		"-Amapstruct.defaultComponentModel=spring"
	))
}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
}

jacoco {
	toolVersion = "0.8.11"
}

tasks.jacocoTestReport {
	reports {
		xml.required = true
		html.required = true
	}
	classDirectories.setFrom(
		files(classDirectories.files.map {
			fileTree(it) {
				exclude(
					"**/adapters/in/rest/*Dto.class",
					"**/adapters/in/rest/*Api.class",
					"**/adapters/in/rest/ApiUtil.class",
				)
			}
		})
	)
}

tasks.jacocoTestCoverageVerification {
	violationRules {
		rule {
			limit {
				minimum = 0.7.toBigDecimal()
			}
		}
	}
	classDirectories.setFrom(tasks.jacocoTestReport.get().classDirectories)
}
