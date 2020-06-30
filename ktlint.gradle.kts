val ktlint by configurations.creating

dependencies {
    ktlint("com.pinterest:ktlint:0.37.2")
}

val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

val ktlintMainSourceCheck by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)

    description = "Check Kotlin code style"
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args = listOf("src/main/*.kt")
}

val ktlintMainSourceFormat by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)

    description = "Fix Kotlin code style"
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args = listOf("-F", "src/main/*.kt")
}