## Testing

Run from command line:

    ./gradlew run

Regular build:

    ./gradlew packageUberJarForCurrentOs && java -jar build/compose/jars/Test-linux-x64-1.0.0.jar

Minified build:

    ./gradlew minify --info && java -jar build/compose/jars/Test-linux-x64-1.0.0.min.jar
