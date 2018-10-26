modbus4j
========

A high-performance and ease-of-use implementation of the Modbus protocol written in Java by Infinite Automation Systems and Serotonin Software. Supports ASCII, RTU, TCP, and UDP transports as slave or master, automatic request partitioning and response data type parsing.

For support and general help please see our [Forum](https://forum.infiniteautomation.com/category/11/modbus4j-general-discussion)

Commercial licenses are available from https://infiniteautomation.com/modbus4j-open-source-modbus-library/

A public Maven Repository is now available with the latest builds add this to your pom.xml

```xml
<repositories>
    <repository>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
        <id>ias-snapshots</id>
        <name>Infinite Automation Snapshot Repository</name>
        <url>https://maven.mangoautomation.net/repository/ias-snapshot/</url>
    </repository>
    <repository>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
        <id>ias-releases</id>
        <name>Infinite Automation Release Repository</name>
        <url>https://maven.mangoautomation.net/repository/ias-release/</url>
    </repository>
</repositories>
```

The dependency information is:

```xml
<dependency>
    <groupId>com.infiniteautomation</groupId>
    <artifactId>modbus4j</artifactId>
    <version>3.0.3</version>
</dependency>
```
