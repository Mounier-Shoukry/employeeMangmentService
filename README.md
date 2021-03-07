

PeopleFlow (www.pplflw.com) is a global HR platform enabling companies to hire & onboard their employees internationally, at the push of a button. It is our mission to create opportunities for anyone to work from anywhere. As work is becoming even more global and remote, there has never been a bigger chance to build a truly global HR-tech company.

<h3>To start Application</h3>
1- Open terminal <b>sudo cd Kafka-image-compose </b><br/>
2- Then run <b>sudo docker-compose up -d </b><br/>
3- Go back to project directory by <b>sudo cd .. </b><br/>
4- build your java application <b>mvn clean install</b> <br/>
5- run application by sudo <b>cd target</b> <br/> and then
<b>java -jar employeeMangmentService-0.0.1-SNAPSHOT.jar</b> or you can run from main class<br/>
6- Open http://localhost:8080/ you will find swagger window displayed in which you can test app


<h3>Application Architecture</h3>
- Application is asimple spring boot app with inegration with apache kafk
and mysql database<br>
  
- Mysql database is for demonstartion purpose in order to be to retreive emplloyee information


<h3>Main flows</h3>
    - Add employee in which we call Post /api/employee to add employee <br/>
    - Employee will be added if it's not exist on database with status ADDED by default and being sent to Message borker (Kafka) <br/>
    - Automated consumer is working at backend list at same topic on kafaka and consume anknowldge and add employee to database <br/>

<br/>
    - Edit employee status by Email in which we call Put /api/employee providing email and new state we need to add to employee <br/>
    - Here we check if employee email exist on database then we fetch it and set it new state and send it back with new state to broker so that consumer can inset it again with new state into database<br/>
<br/>
- Another endpoint added for demonstartion purpose Gem/api/employee by email

<h3>second part</h3>
Suggest what will be your silver bullet, concerns while you're reviewing this part of the software that you need to make sure is being there? </br>
- First we will agree as team for criteria of commit (I mean common code standards document)
- my silver bullet is to follow solid princple:(single responsiblity, open for extension closed for modification,liskof princple, interface segrgation,depnandcy inverstion )<br/>

What the production-readiness criteria that you consider for this solution
- these are application nonfunctional requirments (avaliblity,continuos ingeration, security, extendaplity)

<br/>
<h3> Third part</h3>

- I suggest if we there are multiple services that have to work together
to go for microservice archtictrue in which each service can discover other services through gateway
  ![Suggested architecture](microservices.png?raw=true "suggested architecture")
 





