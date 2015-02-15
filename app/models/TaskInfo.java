package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
@SuppressWarnings("serial")
@Entity
public class TaskInfo extends Model
{

	@Id
	private long taskID;

	@Required(message = "validation.required.emphasis")
	private String title;

	private String description;

	private String createdBy;

	@ManyToOne
	@Email
	@JoinColumn(name = "emailAssignedTo", referencedColumnName = "email")
	private String emailAssignedTo;

	@Required
	private boolean done;
	
	private boolean assigned;

	private boolean recurring_status;

	//@DateTime(pattern = "mm/dd/yyyy hh:mm")
	private String startDate;


	//@DateTime(pattern = "mm/dd/yyyy hh:mm")
	private String endDate;

	@Required
	private double oldPoints;

	@Required
	private double newPoints;



	public TaskInfo()
	{
		this.done=false;
		this.assigned=false;
		this.recurring_status=false;
		this.startDate=null;
		this.endDate=null;
		this.emailAssignedTo=null;
	}

	public boolean isRecurring_status() {
		return recurring_status;
	}

	public void setRecurring_status(boolean recurring_status) {
		this.recurring_status = recurring_status;
	}

	public double getOldPoints() {
		return oldPoints;
	}

	public void setOldPoints(double oldPoints) {
		this.oldPoints = oldPoints;
	}

	public double getnewPoints() {
		return newPoints;
	}

	public void setNewPoints(double newPoints) {
		this.newPoints = newPoints;
	}

	public long getTaskID() {
		return taskID;
	}

	public void setTaskID(long taskID) {
		this.taskID = taskID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmailAssignedTo() {
		return emailAssignedTo;
	}

	public void setEmailAssignedTo(String emailAssignedTo) {
		if(emailAssignedTo!=null && emailAssignedTo.length()>0)
			this.emailAssignedTo = emailAssignedTo;
	}
	
	public static Finder<Long,TaskInfo> findTask = new Finder<Long,TaskInfo>(
			 		    Long.class, TaskInfo.class
			 		  );
	
	public boolean getDone() {
		return done;
	}

	public boolean getAssigned() {
		return assigned;
	}
	public void setDone(boolean status) {
		this.done = status;
	}
	public void setAssigned(boolean status) {
		this.assigned = status;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		if(startDate.length()>0)
			this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		if(endDate.length()>0)
			this.endDate = endDate;
	}



	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}



}
