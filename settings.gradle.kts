rootProject.name = "Stellarica-Core"

pluginManagement {
	repositories {
		maven("https://maven.quiltmc.org/repository/release") {
			name = "Quilt"
		}
		// Currently needed for Intermediary and other temporary dependencies
		maven("https://maven.fabricmc.net/") {
			name = "Fabric"
		}
		gradlePluginPortal()
	}
}