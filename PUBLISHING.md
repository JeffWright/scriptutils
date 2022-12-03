
# Publish to jitpack:
1. Push to GitHub
2. Tag commit with version (e.g. `git tag 0.6.1 && git push origin 0.6.1`)
  1. NOTE: ignores version specified in build.gradle.kts
3. To consume: `repositories { maven { setUrl("https://jitpack.io") } }` 

# Publish to Maven Local:
1. Set version in build.gradle.kts
2. `./gradlew clean publishToMavenLocal`