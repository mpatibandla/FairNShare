$(document).ready(

		/* displays the points earned by user and points needed by him to do fair share of work,
		 * when the user is redirected to the dashboard after log in
		 */

		function() {       
			$.get("/getPointsToComplete",function(data,status){
				$("#toComplete").text(data.PointsToComplete);
				$("#earned").text(data.EarnedPoints);
				$("#target").text(data.target);
			});    
			
			
			$("#task-page").click(function(){
				$.get("/showTasks",function(tasks,status){
					$('#friend_div').hide();
					$('#task_incomplete').hide();
					$('#userPoints').hide();
					$("#notification_div").hide();
					$('#dashboard_div').hide();
					$('#mytask_div').hide();
					$('#myincompletetask_div').hide();
					$('#alltask_overdue').hide();
					$('#mytask_overdue').hide();
					$('#task_div').hide();
					$('#task_div').html("<h2>All Tasks</h2><br><b>Task Name"+" ----- "+"Assigned to"+" ------ "+"Assigned by"+" -------- "+"Start Date"+" ---"+"End Date"+" ---"+"Score");
					jQuery.each(tasks, function(i,task) {
						$('#task_div').append('<tr><td><li class="list-group-item">'+task.title+' ---- '+task.emailAssignedTo+' ---- '+task.createdBy+'---- '+task.startDate+' ---- '+task.endDate+' ---- '+task.newPoints.toFixed(2)+'</li></td></tr>');
						$('#task_div').show();
				});
			});
			});


			$("#myRecurringtask-page").click(function(){
				$.get("/showMyRecurringTasks",function(myRecurringTasks,status){
					$('#friend_div').hide();
					$('#task_incomplete').hide();
					$('#userPoints').hide();
					$("#notification_div").hide();
					$('#dashboard_div').hide();
					$('#task_div').hide();
					$('#mytask_div').hide();
					$('#myincompletetask_div').hide();
					$('#alltask_overdue').hide();
					$('#mytask_overdue').hide();
					$('#task_recurring').html("<h2>My Recurring Tasks</h2><br><b>Task Name"+" ----- "+"Assigned to"+" ------ "+"Assigned by"+" -------- "+"Start Date"+" ---"+"End Date"+" ---"+"Score");
					jQuery.each(myRecurringTasks, function(i,task) {
						$('#task_recurring').append('<tr><td><li class="list-group-item">'+task.title+' ---- '+task.emailAssignedTo+' ---- '+task.createdBy+'---- '+task.startDate+' ---- '+task.endDate+' ---- '+task.newPoints.toFixed(2)+'</li></td></tr>');
						$('#task_recurring').show();
				});
			});
			});



			$("#mytask-page").click(function(){
				$.get("/showMyTasks",function(mytasks,status){
					$('#friend_div').hide();
					$('#task_recurring').hide();
					$("#notification_div").hide();
					$('#task_incomplete').hide();
					$('#userPoints').hide();
					$('#dashboard_div').hide();
					$('#task_div').hide();
					$('#myincompletetask_div').hide();
					$('#alltask_overdue').hide();
					$('#mytask_overdue').hide();
					$('#mytask_div').html("<h2>My Tasks</h2><br><b>Task Name"+" ----- "+"Assigned to"+" ------ "+"Assigned by"+" -------- "+"Start Date"+" ---"+"End Date"+" ---"+"Score");
					jQuery.each(mytasks, function(i,task) {
						$('#mytask_div').append('<tr><td><li class="list-group-item">'+task.title+' ---- '+task.emailAssignedTo+' ---- '+task.createdBy+'---- '+task.startDate+' ---- '+task.endDate+' ---- '+task.oldPoints.toFixed(2)+'</li></td></tr>');
						$('#mytask_div').show();
				});
			});
			});
			
			$("#myincompletetask-page").click(function(){
				$.get("/showMyIncompleteTasks",function(myincompletetasks,status){
					$('#friend_div').hide();
					$('#task_recurring').hide();
					$("#notification_div").hide();
					$('#task_incomplete').hide();
					$('#userPoints').hide();
					$('#dashboard_div').hide();
					$('#task_div').hide();
					$('#alltask_overdue').hide();
					$('#mytask_overdue').hide();
					$('#mytask_div').hide();
					$('#myincompletetask_div').html("<h2>My Incomplete Tasks</h2><br><b>Task Name"+" ----- "+"Assigned to"+" ------ "+"Assigned by"+" -------- "+"Start Date"+" ---"+"End Date"+" ---"+"Score");
					jQuery.each(myincompletetasks, function(i,task) {
						$('#myincompletetask_div').append('<tr><td><li class="list-group-item">'+task.title+' ---- '+task.emailAssignedTo+' ---- '+task.createdBy+'---- '+task.startDate+' ---- '+task.endDate+' ---- '+task.oldPoints.toFixed(2)+' <form role="form" action="/personUpdate/'+task.taskID+'" method="POST"> <input type="submit" class="btn btn-success" id="mybutton" value="Done!"></input></form></li></td></tr>');
						$('#myincompletetask_div').show();
				});
			});
			});




			$("#incomplete-page").click(function(){
				$.get("/showIncompleteTasks",function(incompleteTasks,status){
					$('#friend_div').hide();
					$('#mytask_div').hide();
					$('#task_recurring').hide();
					$("#notification_div").hide();
					$('#userPoints').hide();
					$('#dashboard_div').hide();
					$('#task_div').hide();
					$('#myincompletetask_div').hide();
					$('#alltask_overdue').hide();
					$('#mytask_overdue').hide();
					$('#task_incomplete').html("<h2>Incomplete Tasks</h2><br><b>Task Name"+" ----- "+"Assigned to"+" ------ "+"Assigned by"+" -------- "+"Start Date"+" ---"+"End Date"+" ---"+"Score");
					jQuery.each(incompleteTasks, function(i,task) {
						$('#task_incomplete').append('<tr><td><li class="list-group-item">'+task.title+' ---- '+task.emailAssignedTo+' ---- '+task.createdBy+'---- '+task.startDate+' ---- '+task.endDate+' ---- '+task.newPoints.toFixed(2)+' <form id="TaskIDForm" role="form" action="/taskUpdate/'+task.taskID+'" method="POST"> <input type="submit" class="btn btn-success" id="mybutton" value="Assign it to Me!"></input></form></li></td></tr>');
						$('#task_incomplete').show();
				});
			});
			});
			
			
			/* displays the overdue tasks out of all incomplete tasks*/
			$("#alloverdue-page").click(function(){
				$.get("/showAllOverdueTasks",function(alloverdueTasks,status){
					$('#friend_div').hide();
					$('#mytask_div').hide();
					$('#task_recurring').hide();
					$("#notification_div").hide();
					$('#userPoints').hide();
					$('#myincompletetask_div').hide();
					$('#dashboard_div').hide();
					$('#task_incomplete').hide();
					$('#task_div').hide();
					$('#mytask_overdue').hide();
					$('#alltask_overdue').html("<h2>All Overdue Tasks</h2><br><b>Task Name"+" ----- "+"Assigned to"+" ------ "+"Assigned by"+" -------- "+"Start Date"+" ---"+"End Date"+" ---"+"Score");
					jQuery.each(alloverdueTasks, function(i,task) {
						$('#alltask_overdue').append('<tr><td><li class="list-group-item">'+task.title+' ---- '+task.emailAssignedTo+' ---- '+task.createdBy+'---- '+task.startDate+' ---- '+task.endDate+' ---- '+task.newPoints.toFixed(2)+'</li></td></tr>');
						$('#alltask_overdue').show();
				});
			});
			});

			/* displays the overdue tasks of the user  */
			$("#myoverdue-page").click(function(){
				$.get("/showMyOverdueTasks",function(myoverdueTasks,status){
					$('#friend_div').hide();
					$('#mytask_div').hide();
					$('#task_recurring').hide();
					$("#notification_div").hide();
					$('#userPoints').hide();
					$('#myincompletetask_div').hide();
					$('#dashboard_div').hide();
					$('#task_incomplete').hide();
					$('#task_div').hide();
					$('#alltask_overdue').hide();
					$('#mytask_overdue').html("<h2>My Overdue Tasks</h2><br><b>Task Name"+" ----- "+"Assigned to"+" ------ "+"Assigned by"+" -------- "+"Start Date"+" ---"+"End Date"+" ---"+"Score");
					jQuery.each(myoverdueTasks, function(i,task) {
						$('#mytask_overdue').append('<tr><td><li class="list-group-item">'+task.title+' ---- '+task.emailAssignedTo+' ---- '+task.createdBy+'---- '+task.startDate+' ---- '+task.endDate+' ---- '+task.newPoints.toFixed(2)+'</li></td></tr>');
						$('#mytask_overdue').show();
				});
			});
			});

			/* displays the points earned by user and points needed by him to do fair share of work,
			 * when the user clicks the notifications tab on the left menu bar
			 */
			$("#notifications").click(function(){
				$("#task_div").hide();
				$('#friend_div').hide();
				$("#dashboard_div").hide();
				$('#task_recurring').hide();
				$('#task_incomplete').hide();
				$('#mytask_div').hide();
				$('#task_incomplete').hide();
				$('#task_div').hide();
				$('#mytask_overdue').hide();
				$.get("/getPointsToComplete",function(data,status){
					$("#toComplete").text(data.PointsToComplete);
					$("#earned").text(data.EarnedPoints);
					$("#target").text(data.target);
					$("#userPoints").prepend("<h2 id='notification_div' style='top:70px;'>Notifications</h2>");
					$("#userPoints").show();

				});
			});   

			
			/*JS for User Story 3.2
			 
			 */	
			$("#resuabletask-page").click(function(){
				$.get("/showReUsableTasks",function(reUsableTasks,status){
					$('#friend_div').hide();
					$('#mytask_div').hide();
					$('#task_recurring').hide();
					$("#notification_div").hide();
					$('#userPoints').hide();
					$('#dashboard_div').hide();
					$('#task_incomplete').hide();
					$('#task_div').hide();
					$('#task_incomplete').hide();
					$('#task_div').hide();
					$('#mytask_overdue').hide();
					$('#task_reusable').prepend("<h2>Reusable Tasks</h2>");
					$('#task_reusable').append("<b>Task Name"+" ----- "+"Assigned to"+" ------ "+"Assigned by"+" -------- "+"Start Date"+" ---"+"End Date"+" ---"+"Score");
					jQuery.each(reUsableTasks, function(i,task) {
						$('#task_reusable').append('<tr><td><li class="list-group-item">'+task.title+' ---- '+task.emailAssignedTo+' ---- '+task.createdBy+'---- '+task.startDate+' ---- '+task.endDate+' ---- '+task.newPoints.toFixed(2)+' <form id="ReuseTaskIDForm" role="form" action="/reusetaskUpdate/'+task.taskID+'" method="POST"> <input type="submit" class="btn btn-success" id="mybutton" value="Reuse"></input></form></li></td></tr>');
						$('#task_reusable').show();
				});
			});
			});
			

			/* displays the points earned by user and points needed by him to do fair share of work,
			 * when the user clicks the dashboard tab on the left menu bar
			 */

			$("#dashboard_nav").click(function(){
				$("#task_div").hide();
				$('#mytask_div').hide();
				$("#friend_div").hide();
				$('#task_incomplete').hide();
				$("#notification_div").hide();
				$('#task_recurring').hide();
				$('#task_reusable').hide();
				$('#task_incomplete').hide();
				$('#task_div').hide();
				$('#mytask_overdue').hide();
				$("#dashboard_div").show();
				$.get("/getPointsToComplete",function(data,status){
					$("#toComplete").text(data.PointsToComplete);
					$("#earned").text(data.EarnedPoints);
					$("#target").text(data.target);
					$("#userPoints").show();
				});
			}); 
		});