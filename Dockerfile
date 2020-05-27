FROM maven:3.6-openjdk-11 AS build
COPY border-wars-domain /usr/src/border-wars/border-wars-domain
COPY border-wars-infrastructure /usr/src/border-wars/border-wars-infrastructure
COPY pom.xml /usr/src/border-wars
RUN mvn -f /usr/src/border-wars/pom.xml clean package

FROM jboss/wildfly

RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent

ADD customization /opt/jboss/wildfly/customization/
ADD modules /opt/jboss/wildfly/modules/
RUN /opt/jboss/wildfly/customization/execute.sh
# Fixes the environment variable based database configuration
RUN sed -i 's|\$\$|\$|' /opt/jboss/wildfly/standalone/configuration/standalone.xml
# Fixes an error on boot https://stackoverflow.com/questions/34494022/permissions-error-when-using-cli-in-jboss-wildfly-and-docker
RUN rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history/current

COPY --from=build /usr/src/border-wars/border-wars-infrastructure/target/border-wars-infrastructure-1.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/border-wars-infrastructure-1.0-SNAPSHOT.war

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
