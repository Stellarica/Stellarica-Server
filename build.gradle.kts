plugins {
	kotlin("jvm") version "1.8.10"
	kotlin("plugin.serialization") version "1.8.10"
	java
	alias(libs.plugins.quilt.loom)
	alias(libs.plugins.detekt)
}
group = property("maven_group")!!
version = property("version")!!

repositories {
	maven("https://maven.nucleoid.xyz") // polymer, stimuli, fantasy
	maven("https://jitpack.io") // mixin extras
	maven("https://oss.sonatype.org/content/repositories/snapshots") // fabric-permissions (needed by terra)
	maven("https://ladysnake.jfrog.io/artifactory/mods") // cardinal components
	maven("https://repo.codemc.io/repository/maven-public/") // terra
	maven("https://repo.stellarica.net/snapshots") // stellarica-events
}

dependencies {
	minecraft(libs.minecraft)
	mappings(variantOf(libs.quilt.mappings) { classifier("intermediary-v2") })
	modImplementation(libs.quilt.loader)
	modImplementation(libs.quilted.fabric.api)
	modImplementation(libs.quilt.kotlin)

	modImplementation(libs.bundles.polymer)
	include(libs.bundles.polymer)

	modImplementation(libs.placeholder)
	include(libs.placeholder)

	modImplementation(libs.sgui)
	include(libs.sgui)

	modImplementation(libs.bundles.silk) {
		exclude(group="net.fabricmc")
	}
	include(libs.bundles.silk) {
		exclude(group="net.fabricmc")
	}


	modImplementation(libs.bundles.cardinal)
	include(libs.bundles.cardinal)

	modImplementation(libs.terra)
	include(libs.terra)

	modImplementation(libs.fantasy)
	include(libs.fantasy)

	modImplementation(libs.bundles.stellarica)
	include(libs.bundles.stellarica)

	implementation(libs.kotlin.coroutines)
}

tasks {

	processResources {
		inputs.property("version", project.version)
		filesMatching("quilt.mod.json") {
			expand(mutableMapOf("version" to project.version))
		}
	}

	jar {
		from("LICENSE")
	}

	compileKotlin {
		kotlinOptions.jvmTarget = "17"
	}


	withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
		reports {
			html.required.set(true) // observe findings in your browser with structure and code snippets
			md.required.set(true) // simple Markdown format
		}
		jvmTarget = "17"
	}

	getByName("check") {
		this.setDependsOn(this.dependsOn.filterNot {
			it is TaskProvider<*> && it.name == "detekt"
		})
	}
}

java {
	withSourcesJar()
	//languageVersion.set(JavaLanguageVersion.of(17))
}

detekt {
	buildUponDefaultConfig = true
	allRules = false
	config = files("$projectDir/config/detekt.yml")
}