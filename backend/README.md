# Backend

### How to run

To run the backend project, you need
[JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-0-13-later-archive-downloads.html)
or higher. You also need a running [MySQL](https://www.mysql.com/) database called `webapp`. Once
all this is set up, run the following command:

```shell
mvn spring-boot:run
```

To build the project, run the following command:

```shell
mvn -B package --file pom.xml
```

**Note:** Make sure you have your environment set up properly. You should create a `.env` directory
in the backend project directory. See `local.example.env` for environment variables.
