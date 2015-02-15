$ ->
    
   $(document).on('click', '#friend-page', ( ->
      $.get "/showFriends",(friends) ->
         $('#friend_div').show()
         $('#friend_div').html $('<h2>').text "My Friends"
         $('#task_div').hide()
         $('#mytask_div').hide()
         $('#task_recurring').hide()
         $('#userPoints').hide()
         $('#myincompletetask_div').hide();
         $('#alltask_overdue').hide();
         $('#mytask_overdue').hide();
         $('#dashboard_div').hide()
         $('#task_incomplete').hide()  
         $.each friends,(index,friend) ->
            $('#friend_div').append $('<li class="list-group-item">').text friend.fname+" "+friend.lname
            ))
