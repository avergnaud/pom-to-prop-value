# pom-to-prop-value

Besoin :
* Un service spring-boot `service-project` hérite (au sens maven) d'un projet parent `parent-project`.
* Une maven property est définie dans le `parent-project` : `<my.value>toto</my.value>`
* On veut utiliser cette property dans une Spring @Value du `service-project`

Le build + run utilise podman

```
podman machine start
```

# build & run (mode développement)

Par facilité on exécute toutes les commandes depuis un conteneur.

Depuis le répertoire racine de ce repo git (attention syntaxe powershell `${PWD}`...) :
```
podman run -it -v ${PWD}:/pom-to-prop-value -p 8080:8080 eclipse-temurin:17-jdk-alpine sh
```

## build

Dans le conteneur :

```
cd /pom-to-prop-value/service-project
./mvnw clean install
```

## run

Dans le conteneur :

```
./mvnw spring-boot:run
```

Attendu : la page `http://localhost:8080/` doit afficher `Greetings from Spring Boot! toto`

# troubleshooting

Dans le conteneur, exécuter :

## 1 vérifier la présence de la property maven dans le effective-pom du service

Dans le conteneur, exécuter :

```
/pom-to-prop-value/service-project # ./mvnw help:effective-pom > effective-pom.txt
```

On doit obtenir :
```
<properties>
  <java.version>17</java.version>
  <my.value>toto</my.value>
</properties>
```

## 2 vérifier que le maven-resources-plugin filtre correctement

Test avec un fichier quelconque `src\main\resources\test.txt`, qui contient la ligne `Hello ${name}`. (`name` est une property standard maven)

Le effective-pom du `service-project` doit contenir :
```
<resource>
	<directory>src/main/resources</directory>
	<filtering>true</filtering>	
</resource>
```

Dans le conteneur, exécuter :

```
./mvnw resources:resources
```

Le fichier `service-project\target\classes\test.txt` doit contenir la ligne `Hello service-project`.

## 3 vérifier que le resources-plugin filtre la property maven

Le fichier `service-project\target\classes\application.properties` doit contenir la ligne `also.my.value=toto`

# refs

https://spring.io/guides/gs/spring-boot

https://spring.io/guides/topicals/spring-boot-docker

https://maven.apache.org/plugins/maven-resources-plugin/examples/filter.html
