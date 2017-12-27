
Execution instructions

Prerequisists: Java 8 and maven (both on the Path).

1. build the project with: ./mvn clean package
2. run with: java -jar target/gs-rest-service-0.1.0.jar

The service can be reached at localhost:8080/thumbnail. You must supply the following parameters: url, width, height.

Example:

localhost:8080/thumbnail?url=https://cdn.shopify.com/s/files/1/1061/1924/files/Hugging_Face_Emoji_2028ce8b-c213-4d45-94aa-21e1a0842b4d_large.png?15202324258887420558&height=100&width=100

have fun!




