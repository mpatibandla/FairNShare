package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.format.Formats.DateTime;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class Person extends Model {

	@Id
	@Email
	private String email;
	private double defaultScore;
	
	@Required(message = "validation.required.emphasis")
	private String fname;

	@Required(message = "validation.required.emphasis")
	private String lname;


	@Required
	private double score;

	@DateTime(pattern = "mm/dd/yyyy")
	private String dob;


	private String ph_no;
	private String gender;

	@Required(message = "validation.required.emphasis")
	private String password;

	public Person() {
		this.score=0.0;
		this.dob=null;
		this.ph_no=null;
		this.setDefaultScore(15);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		if(dob.length()>0)
			this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getPh_no() {
		return ph_no;
	}

	public void setPh_no(String ph_no) {
		if(ph_no.length()>0)
			this.ph_no = ph_no;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getDefaultScore() {
		return defaultScore;
	}

	public void setDefaultScore(double defaultScore) {
		this.defaultScore = defaultScore;
	}
}