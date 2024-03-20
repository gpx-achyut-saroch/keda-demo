# Setting up minikube
1. Install minikube : https://minikube.sigs.k8s.io/docs/start/
2. Type command : **minikube start** , to start minkube env 
Note : Docker Engine should be running for minikube to run

# Creating docker Image and Pulling image in minikube
1. set the docker context to default using -> **docker context use default**
2. run following command to create docker image for consumer and producer 
   1.  **docker build -t keda-kafka-producer.jar .\keda-kafka-producer**
   2.  **docker build -t keda-kafka-consumer.jar .\keda-kafka-consumer**
Note : please build jar using **mvn clean install** if not already build 
3. Use the following command to import image in minikube, Note : minikube should be started for this process
   1. **minikube image load keda-kafka-producer.jar**
   2. **minikube image load keda-kafka-consumer.jar**

# Deploying KEDA in minikube
1. You can install KEDA using HELM chart or Kubectl
2. set minikube context => **kubectl config set-context --namespace=default**
3. using Kubectl => **kubectl apply --server-side -f https://github.com/kedacore/keda/releases/download/v2.11.2/keda-2.11.2-core.yaml**
4. using HelmChart => 
   1. helm repo add kedacore https://kedacore.github.io/charts
   2. helm repo update
   3. helm install keda kedacore/keda --namespace default

# Deploying Kafka Service, Consumer and Producer
1. use the below command to deploy minikube
   1. kubectl apply -f .\kube\
   2. kubectl apply -f .\keda-kafka-consumer\kube\
   3. kubectl apply -f .\keda-kafka-producer\kube\

# Checking HPA and ScaledObject deployed
1. kubectl get hpa
2. kubectl get so
