FROM quay.io/wildfly/wildfly

ARG WAR_PATH

ADD $WAR_PATH /opt/jboss/wildfly/standalone/deployments/