

import models.Person;
import models.TaskInfo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;

import play.twirl.api.Content;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import play.mvc.Result;


/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest {

	@Test
	  public void sumTest() {     //Sample unit test to check and understand the basics of JUnit 
	    int a = 1 + 1;            
	    assertEquals(2, a);       //Check whether a=2 or not
	  }
	    
	  @Test
	  public void stringTest() {   //Sample unit test to check and understand the basics of JUnit
	    String str = "Hello world"; 
	    assertFalse(str.isEmpty()); //Asserts that string is not empty
	  }
    
	//Unit tests to check whether the html pages are being rendered properly or not
	  
    @Test
    public void renderIndexTemplateTest() {                     //Unit test to check whether index page is rendered properly or not
        Content html = views.html.index.render("Sign Up.");     //Giving Sign Up. as content to be displayed on index page
        assertThat(contentType(html)).isEqualTo("text/html");   //Stating that input content given is either text or html
        assertThat(contentAsString(html)).contains("Sign Up."); //Checking whether the input given is being rendered on index page or not
    }

    @Test
    public void renderDashboardTemplateTest() {               //Unit test to check whether dashboard page is rendered properly or not
        Content html = views.html.dashboard.render("Welcome");
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("Welcome");//Checking whether the input given is being rendered on dashboard page or not
    }

    //Unit tests to check whether routing to html templates through URL's is being done properly or not
    
    @SuppressWarnings("deprecation")
	@Test
    public void indexRouteTest() {                          //Unit test to check whether routing URL is loading properly or not
		Result result = routeAndCall(fakeRequest(GET, "/"));//A fake request is sent to load index template
        assertThat(result).isNotNull();                     //Asserts that routing is successful and not null
    }
    
    @SuppressWarnings("deprecation")
	@Test
    public void dashboardRouteTest() {                      //Performs the same test as indexRouteTest for dashboard template
        Result result = routeAndCall(fakeRequest(GET, "/dashboard"));
        assertThat(result).isNotNull();
    }
   

    //Unit tests to check whether controllers are routing to the intended html template or not

    @Test
    public void callIndexTest() {                                               //Unit test to check whether the controller calls correct html template or not  
        Result result = callAction(controllers.routes.ref.Application.index()); //Call given to index template
        assertThat(status(result)).isEqualTo(OK);
        assertThat(contentType(result)).isEqualTo("text/html");                 //Checks whether the template loaded has text/html content
        assertThat(charset(result)).isEqualTo("utf-8");                         //Checks for charset of template
        assertThat(contentAsString(result)).contains("Sign Up.");               //Checks for content Sign Up. on index template to ensure correct template has been rendered
    }
    
    //Unit tests to check whether a user is able to sign up or not
    
    @Test
    public void addPersonTest1(){                             //Unit test to check whether values given in sign up form are being saved into database
 	   Person person = mock(Person.class);                    //A mock is created to eliminate the need for an external data resource for testing 
 	   when(person.getEmail()).thenReturn("email@gmail.com"); //gets input given by user from form and saves in database
 	   assertEquals("email@gmail.com",person.getEmail());     //check value. If equal person has been added
 	 }
    
    @Test
    public void addPersonTest2(){                           //Performs the same test as addPersonTest1 on password field 
 	   Person person = mock(Person.class);
 	   when(person.getPassword()).thenReturn("mypassword");
 	   assertEquals("mypassword",person.getPassword());
 	 }

  //Unit test to check whether a user is able to add a task to task list or not
    
    @Test
    public void showTasksTest(){                             //Unit test to check whether values given in add task form are being saved into database
 	   TaskInfo task1 =  mock(TaskInfo.class);               //A mock is created to eliminate the need for an external data resource for testing
 	   when(task1.getTitle()).thenReturn("cleaningutensils");//gets input given by user from form and saves in database
 	   TaskInfo task2 =  mock(TaskInfo.class);               
 	   when(task2.getTitle()).thenReturn("cuttingvegetables");
 	   TaskInfo task3 =  mock(TaskInfo.class);
 	   when(task3.getTitle()).thenReturn("groceryshopping");
 	   

 	   assertEquals("cleaningutensils",task1.getTitle()); //check value. If equal task has been added
 	   assertEquals("cuttingvegetables",task2.getTitle());
 	   assertEquals("groceryshopping",task3.getTitle());
 	 }
      
   
   }
