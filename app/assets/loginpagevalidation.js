function validatePassword(){
if (password.value != password_confirmation.value) { 
   alert("Your password and confirmation password do not match.");
   password_confirmation.focus();
   return false; 
}
}
