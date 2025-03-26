import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

version = "2023.05"

project {
    buildType(Build)
    vcsRoot(GitVcsRoot)
}

object Build : BuildType({
    name = "Build and Test"
    description = "Builds the Maven project and runs tests"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        maven {
            name = "Build and Test"
            goals = "clean test"
            mavenVersion = bundled_3_9()
            jdkHome = "%env.JDK_11%"
        }
    }

    triggers {
        vcs {
            branchFilter = "+:*"
        }
    }

    requirements {
        contains("env.JDK_11", "true", "RQ_1")
    }
})

object GitVcsRoot : GitVcsRoot({
    name = "Git VCS Root"
    url = "https://github.com/aakonyakin/Maven-Configuration-TeamCity-Samples.git"
    branch = "refs/heads/main"
    authMethod = password {
        userName = "git"
        password = "credentialsJSON:your-git-credentials-id"
    }
}) 