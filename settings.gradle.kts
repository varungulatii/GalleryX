pluginManagement {
    repositories {
        google {
//            content {
//                includeGroupByRegex("com\\.android.*")
//                includeGroupByRegex("com\\.google.*")
//                includeGroupByRegex("androidx.*")
//            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "GalleryX"
include(":app")
include(":core")
include(":domain")
include(":data")
include(":feature:gallery")

// Explicitly map modules to their directories
project(":core").projectDir = file("core")
project(":domain").projectDir = file("domain")
project(":data").projectDir = file("data")
project(":feature:gallery").projectDir = file("feature/gallery")
