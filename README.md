# RuleEngine

##Steps to deploy changes:

1. run "mvn -s config/settings.xml clean package"

2. copy rule-engine-0.1.0.jar to the server

3. Stop the running instance on the server

4. run "java -jar rule-engine-0.1.0.jar" to start the server for the Rule Engine


##How to change rules:

1. Rules Files are at: src/main/resources/rules

2. Change the rules and follow the above to deloy changes to the server


##The 1st Draft

1. Due to time contraint, User/Merchant records are simulated in a Stub class: src/main/java/com/ps/dao/TestDBManagerStub.java

2. The Domain Objects are at src/main/java/com/ps/entity

3. The instruction of how to test the rules are at test_cases
