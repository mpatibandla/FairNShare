package controllers;
import static play.libs.Json.toJson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.Person;
import models.TaskInfo;
import play.api.Logger;

import org.slf4j.LoggerFactory;










import play.data.DynamicForm;
import play.data.Form;
import play.db.ebean.Model;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.calendar;
import views.html.index;

import com.fasterxml.jackson.databind.node.ObjectNode;

@SuppressWarnings({ "unused", "unchecked" })
	public class Application extends Controller {
	
	public static Result index() {

		return ok(index.render(""));
	}

	public static Result loginFail() {
		 
		 		return ok(index.render("Login Fail"));
		 	}

	public static Result redirectDashBoardURL() {
		String user = session("connected");
		
		if(user==null)
			return redirect(routes.Application.index());
		return ok(views.html.dashboard.render("Welcome " + user));
		//logger.debug(arg0);
		
	}
	
	public static Result calendar()
	{
		//return ok();
		return ok(calendar.render("CALENDAR"));
	}
	
	public static Result changeCalendar() throws ParseException{
		DynamicForm requestData = Form.form().bindFromRequest();
        
        String sdate = session("fairdate");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
       
        /* If we want to check for system time than the earlier set time
        Date sysTime = new Date();
        String systemdate = (String) dateFormat.format(sysTime);
        sysTime = dateFormat.parse(systemdate);
        */
       
        Date sessiondate = dateFormat.parse(sdate);
        String date = requestData.get("calendar");
        Date newcalendardate = dateFormat.parse(date);
        if(sessiondate.before(newcalendardate))
        {
               session("fairdate", date);
               return ok(views.html.dashboard.render("WELCOME..."));
        }
        else
        {
               return ok(views.html.calendar.render("Please enter a valid date"));
        }
	}
	
	public static Result addPerson() {

		Person person=Form.form(Person.class).bindFromRequest().get();
		person.save();
		return ok(index.render("User Registered"));
	}
	
	private static String getStartDate(String startDate, int count, String recurringType) throws ParseException
	{
		Calendar c = Calendar.getInstance();		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	//Formatting from String to Date data type

		Date date = sdf.parse(startDate);
		c.setTime(date);

		if(recurringType.equals("weekly"))
			c.add(Calendar.DATE, 7*count);

		else if(recurringType.equals("monthly"))
			c.add(Calendar.DATE, 30*count);

		return sdf.format(c.getTime());

	}
	@SuppressWarnings("rawtypes")
	public static Result createTask() throws ParseException
	{

		TaskInfo newTask = new TaskInfo();
		DynamicForm requestData = Form.form().bindFromRequest();
		// 1- new task, 2 - reusing a task
		newTask.setCreatedBy(session("connectedmail"));
		newTask.setDescription(requestData.get("description"));

		Person existingPerson = (Person) new Model.Finder(String.class,Person.class).byId(requestData.get("emailAssignedTo"));
		if(!requestData.get("emailAssignedTo").equals("") && existingPerson!=null)
			newTask.setEmailAssignedTo(requestData.get("emailAssignedTo"));
		else if(!requestData.get("emailAssignedTo").equals("") && existingPerson==null)
			return ok(views.html.dashboard.render("Task could not be created. Email to assign does not exist in the database"));

		newTask.setStartDate(requestData.get("startDate"));
		int days=0;

		String ending_in = requestData.get("enddays");
		if(ending_in.equals("")){
			days = 0;
		}
		else{
			days = Integer.parseInt(ending_in);
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date start_date = dateFormat.parse(requestData.get("startDate"));
		Calendar c = Calendar.getInstance();
		c.setTime(start_date);
		String end=null;
		if(days>0)
		{
			c.add(Calendar.DATE, days);		//add the number of days to the start date of the task
			end = dateFormat.format(c.getTime());
			newTask.setEndDate(end);
		}
		else
			newTask.setEndDate(requestData.get("endDate"));

			newTask.setOldPoints(Double.parseDouble(requestData.get("newPoints")));
		newTask.setNewPoints(Double.parseDouble(requestData.get("newPoints")));
		newTask.setTitle(requestData.get("title"));

		if(!requestData.get("recurring_type").equals(" "))
		{
				newTask.setRecurring_status(true);
		}
		
		if(requestData.get("toggleValue").split(":")[0].equals("2"))		// reusable task	
			TaskInfo.findTask.ref(Long.parseLong(requestData.get("toggleValue").split(":")[1])).delete();

		List<TaskInfo> allTasks = new Model.Finder(String.class,TaskInfo.class).all();
		boolean taskAlreadyExists = false;
		for(TaskInfo task : allTasks)
			if(task.getTitle().equalsIgnoreCase(newTask.getTitle()))
				taskAlreadyExists=true;


		newTask.save(); 	// only if newTask could be saved, proceed with next recurring

		if(newTask.isRecurring_status())
		{
			int taskCount=0;
			if(!requestData.get("taskCount").equals(""))
				taskCount=Integer.parseInt(requestData.get("taskCount"));
			if(taskCount>0)
			{
				TaskInfo newRecurringTask;
				for(int i=0;i<taskCount-1;i++)
				{

					newRecurringTask = new TaskInfo();
					newRecurringTask.setCreatedBy(newTask.getCreatedBy());
					newRecurringTask.setDescription(newTask.getDescription());
					newRecurringTask.setEmailAssignedTo(newTask.getEmailAssignedTo());
					newRecurringTask.setNewPoints(newTask.getnewPoints());
					newRecurringTask.setOldPoints(newTask.getOldPoints());
					newRecurringTask.setTitle(newTask.getTitle());
					newTask.setRecurring_status(true);
					newRecurringTask.setRecurring_status(true);
					if(newTask.getStartDate()!=null)
					{	
						newRecurringTask.setStartDate(getStartDate(newTask.getStartDate(),i+1,requestData.get("recurring_type")));
						if(days == 0)
							newRecurringTask.setEndDate(getStartDate(newTask.getEndDate(),i+1,requestData.get("recurring_type")));
						else
							newRecurringTask.setEndDate(getEndDate(newRecurringTask.getStartDate(),days));
					}
					newRecurringTask.save();
				}
			}


			return ok(views.html.dashboard.render("Task Created !"));
		}

		return ok(views.html.dashboard.render(""));
	}
	
	
	
	private static String getEndDate(String startDate, int days) throws ParseException
	{
		Calendar c = Calendar.getInstance();		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	//Formatting from String to Date data type

		Date date = sdf.parse(startDate);
		c.setTime(date);

		c.add(Calendar.DATE, days);

		return sdf.format(c.getTime());
	}	
	
	public static Result checkIfReusable(String taskStart) throws ParseException
	{

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		taskStart = "%"+taskStart+"%";
		
		List<TaskInfo> suggestionsTemp =TaskInfo.findTask.where().ilike("title", taskStart).findList();
		List<TaskInfo> suggestions = new ArrayList<TaskInfo>();
		Date currentDate = new Date();
		System.out.println("suggestions "+suggestionsTemp);
		for(TaskInfo task : suggestionsTemp)
		{
			if(currentDate.after(dateFormat.parse(task.getEndDate())))	// deadline has not passed, hence currently assigned
			{
				System.out.println(" before task"+suggestions.size());	
				suggestions.add(task);
				System.out.println(" after task"+suggestions.size());				
			}	
		}

			return ok(toJson(suggestions));
	}
	@SuppressWarnings({ "rawtypes" })
	public static Result showTasks() {												//method to show all tasks in the system
		List<TaskInfo> tasks = new Model.Finder(String.class,TaskInfo.class).all(); //extracting all tasks from the database
		return ok(toJson(tasks));  													//passing the tasks as a json object
	}
	
	

	
	@SuppressWarnings({ "rawtypes" })
	public static Result showMyRecurringTasks(){
		List<TaskInfo> Tasks = new Model.Finder(String.class,TaskInfo.class).all();  		//list with all the tasks
		List<TaskInfo> myRecurringTasks = new ArrayList<TaskInfo>();  					 //empty list taken as array for purpose
		for(TaskInfo eachTask : Tasks)  																//for each task in the list of all tasks,
			if(eachTask.isRecurring_status() &&  eachTask.getEmailAssignedTo()!=null && eachTask.getEmailAssignedTo().equalsIgnoreCase(session("connectedmail")))			//if the status of the task is not done,
				 				myRecurringTasks.add(eachTask);											    	//that task is added to the incompleteTasks list
		return ok(toJson(myRecurringTasks));										//the incompleteTasks list with all the tasks in a list is sent as Json Object

	}
	
	@SuppressWarnings({ "rawtypes" })
	
	
	
	
	
	public static Result showMyTasks(){  													//method to show only user tasks in the system, i.e, only tasks assigned to the particular user
		List<TaskInfo> tasks = new Model.Finder(String.class,TaskInfo.class).all();			//extracting all tasks from the ddatabase into a list
		Person currentUser= (Person) new Model.Finder(String.class,Person.class).byId(session("connectedmail"));
		List<TaskInfo> myTasks = new ArrayList<TaskInfo>();									//creating an empty list of the type object TaskInfo
		String usermail = session("connectedmail");											//getting the current session email of the current user
		for(TaskInfo eachTask:tasks)														//for each task in the list of tasks
		{	
			if(eachTask.getEmailAssignedTo()!=null && eachTask.getEmailAssignedTo().equalsIgnoreCase(currentUser.getEmail()))		//the email to which the task is assigned is checked with the current session email
				myTasks.add(eachTask);														//if they are equal, the task is added to the mist myTasks
		}
		return ok(toJson(myTasks));															//list myTasks is sent as a json object
	}
	
	
	@SuppressWarnings({  "rawtypes" })
	public static Result showMyIncompleteTasks(){  													//method to show only user tasks in the system, i.e, only tasks assigned to the particular user
		List<TaskInfo> tasks = new Model.Finder(String.class,TaskInfo.class).all();			//extracting all tasks from the ddatabase into a list
		Person currentUser= (Person) new Model.Finder(String.class,Person.class).byId(session("connectedmail"));
		List<TaskInfo> myTasks = new ArrayList<TaskInfo>();									//creating an empty list of the type object TaskInfo
		String usermail = session("connectedmail");											//getting the current session email of the current user
		for(TaskInfo eachTask:tasks)														//for each task in the list of tasks
		{	
			if(eachTask.getEmailAssignedTo()!=null && eachTask.getEmailAssignedTo().equalsIgnoreCase(currentUser.getEmail()) && eachTask.getDone()==false)		//the email to which the task is assigned is checked with the current session email
				myTasks.add(eachTask);														//if they are equal, the task is added to the mist myTasks
		}
		return ok(toJson(myTasks));															//list myTasks is sent as a json object
	}
	
	@SuppressWarnings({  "rawtypes" })
	
	public static Result taskUpdate(String taskID) {										//method to update task which is incomplete, to assign it to the user himself
		TaskInfo existingTask = (TaskInfo) new Model.Finder(String.class,TaskInfo.class).byId(Integer.parseInt(taskID));
		TaskInfo allTasks = (TaskInfo) new Model.Finder(String.class,TaskInfo.class).byId(Integer.parseInt(taskID));
		String usermail = session("connectedmail"); 										//getting the current email session of the user
		double points=existingTask.getnewPoints();
		
		List<TaskInfo> tasks = new Model.Finder(String.class,TaskInfo.class).all();			//get all the tasks in the table TaskInfo
		int count=0;																		//Check if the task is not assigned to the user & check if the flag assigned to false to get the
																							//get the count of tasks that has to be assigned to someone
		
		for(TaskInfo t: tasks)																
		{  
			if(t.getAssigned()==false)
			{
				if(t.getEmailAssignedTo()!=null && usermail.contentEquals(t.getEmailAssignedTo()))
				{
					
				}
				else
				{
					count++;
				}
			}
		}
																					//if there is only 1 task as incomplete and to be taken, then don't alter the points
		if(count<=1)
		{
			existingTask.setEmailAssignedTo(usermail);								//changing the email task assigned to, to the current user
			existingTask.setOldPoints(points);
			existingTask.save();													//Save the only task to to the user who selects without altering the points as it is the last task
			return ok(views.html.dashboard.render(""));
		}
	
		existingTask.setAssigned(true);												//Set the 'assigned' flag to true to check the task and alter the points (reduce for selected task) 
		existingTask.save();
		double sumOfAllUnassignedTasks = 0;
		int noOfTasks= tasks.size();
		
		for(TaskInfo t: tasks)														//calculate the sum of all UnAssigned tasks for the formula
		{   
			if(t.getAssigned())
			{
			}
			else
			{
			sumOfAllUnassignedTasks = sumOfAllUnassignedTasks + t.getnewPoints();
			}
		}
		
		existingTask.setEmailAssignedTo(usermail);									//changing the email task assigned to, to the current user
		double chosenTaskPoints = existingTask.getnewPoints();
		existingTask.setOldPoints(points);											//copy the new points to old points table to assign the selected points for the user & not the altered
		double sumOfUnchosenTasks = sumOfAllUnassignedTasks - chosenTaskPoints;
		double totalDelta=(double) (chosenTaskPoints * 0.2);
		double individualDeltaForTaskX =totalDelta/(sumOfUnchosenTasks);
		double newPointValueofChosenTask = chosenTaskPoints-totalDelta;
		existingTask.setNewPoints(newPointValueofChosenTask);						//set the new points of the current task by reducing it by 20% (changeable)
		existingTask.save();	
		for(TaskInfo t: tasks)
		{	
			long idOfList= t.getTaskID();
			int idOfList1 = (int)idOfList;
			long idOfselectedTask=Integer.parseInt(taskID);
			if(idOfList1 != idOfselectedTask)										//to alter points for the unselected incomplete tasks
			{
				TaskInfo cTask= (TaskInfo) new Model.Finder(String.class,TaskInfo.class).byId(idOfList1);
				if(cTask.getAssigned())
				{
				}
				else
				{
					double x=cTask.getnewPoints();
					double newPointValueofUnchosenTask=(x/sumOfUnchosenTasks)*totalDelta;
					double y=newPointValueofUnchosenTask+x;							//Alter the points based on the auto adjust & increase by the percentage based on the formula 
					cTask.setNewPoints(y);
					cTask.save();	
				}
			}
		}
		return ok(views.html.dashboard.render(""));	
}
/*
	 * Gives the points needed by a user to be doing fair share of work
	 * 
	 * Implementation :
	 * Current score of the user is pulled from the database
	 * Current scores of other users are pulled from the database and its average is computed
	 * If the given user's current score is less than the average of points earned by other users,
	 * then the difference in points is shown as the points he needs to achieve in order to be contributing fairly.
	 * 
	 * If the difference is negative (i.e. he has earned more points than the average points earned by others,
	 * then the difference is rounded to 0 and displayed (indicating that he has done fair share of work)
	 */ 
	
	@SuppressWarnings({ "rawtypes" })
	public static Result getPointsToComplete() throws ParseException
	{
		Person currentUser= (Person) new Model.Finder(String.class,Person.class).byId(session("connectedmail"));
		ObjectNode initialPoints = getInitialPoints();
		Double pointsNeededForFairShare = initialPoints.get("PointsToComplete").doubleValue();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String startOfRecurrence= "2014-12-01";
		String todayDate = session("fairdate");

		Date startDateOfRecurrence = dateFormat.parse(startOfRecurrence);
		Date today = dateFormat.parse(todayDate);

		int differenceOfDays =(int) ((today.getTime()-startDateOfRecurrence.getTime()) / (24 * 60 * 60 * 1000));
		Double pointsToComplete = pointsNeededForFairShare + (differenceOfDays/7)*currentUser.getDefaultScore();

		ObjectNode userPoints=Json.newObject();
		userPoints.put("PointsToComplete",  pointsToComplete - initialPoints.get("EarnedPoints").doubleValue() );
		userPoints.put("EarnedPoints",initialPoints.get("EarnedPoints").doubleValue() );
		userPoints.put("target", pointsToComplete);
		return ok(userPoints);
	}
	
	
	@SuppressWarnings({ "rawtypes"})
	public static ObjectNode getInitialPoints()  {
		
		Person currentUser= (Person) new Model.Finder(String.class,Person.class).byId(session("connectedmail"));	// get current user from session
		double earnedPoints = currentUser.getScore();					//get points earned by him until now
		double pointsToComplete=0;										

		// get a list of other roommates by getting all the roommates and removing the currently logged in user from the list
		List<Person> otherUsers= new Model.Finder(String.class,Person.class).all(); 	
		otherUsers.remove(currentUser);				

		// get average score of other users
		double scoreOfOtherUsers=0;
		for(Person user : otherUsers)
			scoreOfOtherUsers += user.getScore();
		
		/* Here, Exception handling is done.
		* Case: if currently logged in user is the only user in the database & none of his roommates are registered,
		* then otherUsers will be 0. So, when finding average using the formula : (sum of scores)/No.Of.Users,
		* the denominator would throw a divideByZero Exception or NumberFormatException.
		* But the case has been handled by performing division only if there are other users in the database. ( as shown below)
		*/
		if(otherUsers.size()>0)    
			pointsToComplete=scoreOfOtherUsers/otherUsers.size()-earnedPoints;

		/* If current user's earned points are above the average of other's points, 
		*then the negative difference is rounded to zero. 
		*/
		if(pointsToComplete<0)
			pointsToComplete=0;

		// User points are packaged in a JSON object and sent to the UI
		ObjectNode userPoints=Json.newObject();
		userPoints.put("PointsToComplete", pointsToComplete);
		userPoints.put("EarnedPoints", earnedPoints);
		return userPoints;

	}
	
	@SuppressWarnings({  "rawtypes" })
	
	public static Result showFriends() {											//method to show friends
		List<Person> persons = new Model.Finder(String.class,Person.class).all(); 	//extracting all the persons in the database
		return ok(toJson(persons));													//passing persons as Json object for displaying
	}
	
	
	@SuppressWarnings({ "rawtypes" })
	
	
	public static Result personUpdate(String taskID){											//method to update the score of a person, takes task as the parameter
		 
		//TaskIDRetrieval currentTask = Form.form(TaskIDRetrieval.class).bindFromRequest().get();		
		TaskInfo existingTask = (TaskInfo) new Model.Finder(String.class,TaskInfo.class).byId(Integer.parseInt(taskID));
		System.out.println("title : "+existingTask.getTitle()+ existingTask.getDone());
		existingTask.setDone(true);
		System.out.println("title : "+existingTask.getTitle()+ existingTask.getDone());
		existingTask.save();
		//for that particular task, we change in the database that the task is done
		//we get the user email id who is assigned to the task
		Person currentUser= (Person) new Model.Finder(String.class,Person.class).byId(session("connectedmail"));		//we extract the user based on the email id obtained above
		double currentscore = currentUser.getScore();						//the already present score of the user the taken into currentscore by using getter
		currentUser.setScore(existingTask.getOldPoints()+currentscore);	//now, the score of user is set to current score+ points of the task which he has done
		currentUser.save();
		return ok(views.html.dashboard.render(""));
		}
	
	
	
	@SuppressWarnings({ "rawtypes" })
	
	
	public static Result reusetaskUpdate(String taskID){											

		//Method incomplete and left for further implementation as it was decided to take up by the other person earlier
		
		
		//TaskIDRetrieval currentTask = Form.form(TaskIDRetrieval.class).bindFromRequest().get();		
		TaskInfo existingTask = (TaskInfo) new Model.Finder(String.class,TaskInfo.class).byId(Integer.parseInt(taskID));
		System.out.println("title : "+existingTask.getTitle()+ existingTask.getDone());
		//existingTask.setDone(true);
		System.out.println("title : "+existingTask.getTitle()+ existingTask.getDone());
		
		return ok(toJson("existingTask"));	
		
		/*
		existingTask.save();
		//for that particular task, we change in the database that the task is done
		//we get the user email id who is assigned to the task
		Person currentUser= (Person) new Model.Finder(String.class,Person.class).byId(session("connectedmail"));		//we extract the user based on the email id obtained above
		int currentscore = currentUser.getScore();						//the already present score of the user the taken into currentscore by using getter
		currentUser.setScore(existingTask.getnewPoints()+currentscore);	//now, the score of user is set to current score+ points of the task which he has done
		currentUser.save();
		return ok(views.html.dashboard.render(""));
		*/
		}
	

	
	@SuppressWarnings({  "rawtypes" })
	
	
	public static Result showIncompleteTasks()	{    									//to show incomplete tasks out of the whole list of tasks
		List<TaskInfo> Tasks = new Model.Finder(String.class,TaskInfo.class).all();  		//list with all the tasks
		List<TaskInfo> incompleteTasks = new ArrayList<TaskInfo>();  					 //empty list taken as array for purpose
		for(TaskInfo eachTask : Tasks)  
			 		{
			 			System.out.println("inside here" + eachTask);//for each task in the list of all tasks,
			 		
			 			if(eachTask.getDone()==false &&  (eachTask.getEmailAssignedTo()==null || !eachTask.getEmailAssignedTo().equalsIgnoreCase(session("connectedmail"))))			//if the status of the task is not done,
			 				incompleteTasks.add(eachTask);
			 		}
		return ok(toJson(incompleteTasks));//that task is added to the incompleteTasks listreturn ok(toJson(incompleteTasks));										//the incompleteTasks list with all the tasks in a list is sent as Json Object

	}
	
	@SuppressWarnings({  "rawtypes" })
	
	
	public static Result showReUsableTasks()	{    									//to show incomplete tasks out of the whole list of tasks
		List<TaskInfo> Tasks = new Model.Finder(String.class,TaskInfo.class).all();  		//list with all the tasks
		List<TaskInfo> reUsableTasks = new ArrayList<TaskInfo>();  					 //empty list taken as array for purpose
		for(TaskInfo eachTask : Tasks)  																//for each task in the list of all tasks,
		//if(!eachTask.getDone() &&  !eachTask.getEmailAssignedTo().equalsIgnoreCase(session("connectedmail")))			//if the status of the task is not done,
		reUsableTasks.add(eachTask);											    	//that task is added to the incompleteTasks list
		return ok(toJson(reUsableTasks));										//the incompleteTasks list with all the tasks in a list is sent as Json Object
		//return ok(toJson("Tasks"));										//the incompleteTasks list with all the tasks in a list is sent as Json Object

	}
	
	
	@SuppressWarnings({ "rawtypes" })

	public static Result showAllOverdueTasks() throws ParseException	{
		List<TaskInfo> Tasks = new Model.Finder(String.class,TaskInfo.class).all();			//taking list of all existing tasks
		List<TaskInfo> overdueTasks = new ArrayList<TaskInfo>();		//a new list to add each overdue task to
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");		//specifying the date format to be followed
		String date1=session("fairdate");
		Date date_change = dateFormat.parse(date1);
		//a new Date object gives us the current system time
		String enddate_string;	//string to take end date in
		for(TaskInfo eachTask : Tasks){			//for each task in the list of tasks
			enddate_string = eachTask.getEndDate();	//getting the end date of the task
			Date date2 = dateFormat.parse(enddate_string);		//converting the end date string into required date format
			if(date_change.after(date2) && eachTask.getDone()==false)			//if currrent date is after the end date of tasks
				overdueTasks.add(eachTask);		//add that task to list of overdue tasks
		}
		return ok(toJson(overdueTasks));		//return overdue tasks */
	}

	@SuppressWarnings({ "rawtypes" })
	public static Result showMyOverdueTasks() throws ParseException	{
		List<TaskInfo> Tasks = new Model.Finder(String.class,TaskInfo.class).all();			//taking list of all existing tasks
		List<TaskInfo> overdueTasks = new ArrayList<TaskInfo>();		//a new list to add each overdue task to
		Person currentUser= (Person) new Model.Finder(String.class,Person.class).byId(session("connectedmail"));
		String usermail = session("connectedmail");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");		//specifying the date format to be followed
		String date1=session("fairdate");
		Date date_change = dateFormat.parse(date1);
		//a new Date object gives us the current system time
		String enddate_string;	//string to take end date in
		for(TaskInfo eachTask : Tasks){			//for each task in the list of tasks
			enddate_string = eachTask.getEndDate();		//getting the end date of the task
			Date date2 = dateFormat.parse(enddate_string);		//converting the end date string into required date format
			if(date_change.after(date2) && eachTask.getEmailAssignedTo().equalsIgnoreCase(currentUser.getEmail())  && eachTask.getDone()==false)			//if currrent date is after the end date of tasks
				overdueTasks.add(eachTask);		//add that task to list of overdue tasks
				//the email to which the task is assigned is checked with the current session email
				//if they are equal, the task is added to the list of the user's overdue tasks
		}
		return ok(toJson(overdueTasks));		//return overdue tasks
	}
	
			
	public static Result checkPerson() {																//check if the person exists in the database or not
		Login loginInfo=Form.form(Login.class).bindFromRequest().get();		
		@SuppressWarnings("rawtypes")
		Person existingPerson = (Person) new Model.Finder(String.class,Person.class).byId(loginInfo.getEmail());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dateObj = new Date();
		String date = (String) df.format(dateObj);
		if(existingPerson!=null && existingPerson.getPassword().equals(loginInfo.getPassword()))		//if there exists a person, and the password matches,
		{	

			String username=existingPerson.getFname(); 			//Get the First name by using the primary key email
			String usermail=existingPerson.getEmail(); 			//Get the email id of the user & set in the session variable to use for other activities
			session("connected", username);						//Assign it to the session variable
			session("connectedmail", usermail);
			session("fairdate", date);
			String user = session("connected");
			return ok(views.html.dashboard.render("Welcome " + user));
		}

		return ok(index.render("Invalid username or password!"));

	}

	public static Result endSession() 
	{                      
		session().clear();										//Ends user session and redirects to index page
		String user = session("connected");
		System.out.println("user "+user);
		return redirect(routes.Application.index());	
	} 



		

	public static class Login {

		private String email;
		private String password;

		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
	}
	
	public static class TaskIDRetrieval{
		private String taskID;
		public String getTaskID()
		{
			return taskID;
		}
		public void setTaskID(String taskID) {
			this.taskID = taskID;
		}
	
}}
