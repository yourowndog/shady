enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "shady"

pluginManagement {
    repositories {
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include(":app")
include(":shaders")
include(":sketch")
include(":style")
