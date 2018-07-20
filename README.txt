Build Via the Ant Targets, maven will automatically download and install libraries from web and modbus4j-maven-local repository included in project.

modbus4J.jar requires: (See pom.xml)

A discussion forum for this package can be found at http://mango.serotoninsoftware.com/forum/forums/show/11.page. 

There is a public maven repository for downloading Modbus4J as a dependency in your project.  Just add this:

    <repositories>
        <repository>
            <releases>
                <enabled>true</enabled>
                <checksumPolicy>ignore</checksumPolicy>
            </releases>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
            <id>modbus-data-source</id>
            <name>Modbus Dependencies</name>
            <url>https://maven.mangoautomation.net/repository/ias-release/</url>
        </repository>
    </repositories>
    
    Use this as the dependency info:
	<dependency>
		<groupId>com.infiniteautomation</groupId>
 		<artifactId>modbus4j</artifactId>
        <version>3.0.3</version>
    </dependency>