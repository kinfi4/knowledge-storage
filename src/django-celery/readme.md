## Django celery app

It's important to remember, that this app is made just for example.  

The main idea of using Celery here, is to make it deal with email listing. So we have a form, where we can add user-email and his name to the database. As soon as we add him/her, celery tast will be executed, and user is going to get email.  

Then, every 5 minutes, our celery is going to get all the users saved into database, and is going to send them spam emails. That's it.


**How to start CELERY:** 

		celery -A config worker -l info  # - to start scheduler and find all the tasts
		celery -A config beat -l info  # - to scheduled tasts


------------------------------------------
@kinfi4
