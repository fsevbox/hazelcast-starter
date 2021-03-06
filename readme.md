## About

Starter project which exposes a Hazelcast ReplicatedMap.

Contains all the necessary Kubernetes resources in order to be deployed on a local cluster (e.g minikube) using skaffold. 

## Endpoints

**retrieve value by key**

```curl --request GET "http://${IP}:${PORT}/map/get?key=c"```

**update value**

```curl --request POST "http://${IP}:${PORT}/map/put?key=c&value=c1"```

## Deployment

```mvn clean package```

**local**
* on port 8080

  ```java -jar target/hazelcast-starter.jar```
  
* on port of your choice

  ```java -jar target/hazelcast-starter.jar --server.port=${PORT}```

**minikube**

```skaffold run```

This will start 3 service instances. In order to grab the service ip:port use ```minikube service --url hazelcast-starter```

