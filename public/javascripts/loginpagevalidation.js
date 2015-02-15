$('form').on('submit',function(){
   if($('#password').val()!=$('#password_confirmation').val()){
       alert('Password not matches');
       return false;
   }
   return true;
});