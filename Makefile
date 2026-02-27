.PHONY: build run clean test

default:setup

setup: 
	gradle wrapper --gradle-version 8.5

run-ne: build
	./gradlew :app:run --args='-ne'

# Compila el proyecto con Gradle
build: clean
	./gradlew :app:build

# Ejecuta el proyecto
run: build
	./gradlew :app:run

# Limpia el proyecto
clean:
	./gradlew :app:clean
