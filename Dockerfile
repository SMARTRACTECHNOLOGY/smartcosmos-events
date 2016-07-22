FROM smartcosmos/service
MAINTAINER SMART COSMOS Platform Core Team

ADD target/smartcosmos-*.jar  /opt/smartcosmos/smartcosmos-events.jar

CMD ["java", "-jar", "/opt/smartcosmos/smartcosmos-events.jar"]
