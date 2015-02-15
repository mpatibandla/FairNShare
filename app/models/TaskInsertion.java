package models;
 import java.util.Timer;  
 import java.util.TimerTask;
 
 
 
 public class TaskInsertion extends TimerTask	 {
 
 	@Override
 	public void run() {
 		System.out.println("This is being printed every 1 sec."); 
 				
 	}
 
 	
 	 public static void main(String[] args) {  
 	        // Create an instance of our task/job for execution  
 	        TaskInsertion task = new TaskInsertion();  
 	          
 	        // We use a class java.util.Timer to   
 	        // schedule our task/job for execution  
 	        Timer timer = new Timer();  
 	          
 	        // Let's schedule our task/job to be executed every 1 second  
 	        timer.scheduleAtFixedRate(task, 0, 10000);  
 	        // First parameter: task - the job logic we   
 	        // created in run() method above.  
 	        // Second parameter: 0 - means that the task is   
 	        // executed in 0 millisecond after the program runs.  
 	        // Third parameter: 1000 - means that the task is   
 	        // repeated every 1000 milliseconds  
           
 	    }  
 	  
 	}  
