function onSignIn(googleUser) {
      var profile = googleUser.getBasicProfile();
      $(".g-signin2").css("display","none");
      $(".googledata").removeClass("hide");
      $(".googleform").removeClass("hide");
      $(".logoutbtn").removeClass("hide");
      $("#name").text(profile.getName());
      $('#emailid').attr('value',profile.getEmail());
      $('#googleusername').attr('value',profile.getName());
}    
function googleLogout() {
	var auth2 = gapi.auth2.getAuthInstance();
	auth2.signOut().then(function() {
		alert("signout success");
		$(".g-signin2").css("display","block");
	    $(".googledata").addClass("hide");
	      $(".googleform").addClass("hide");
	      $(".logoutbtn").addClass("hide");
	});
}
function ValidateSize(file) {
    var FileSize = file.files[0].size / 1024 / 1024; // in MB
    if (FileSize > 10) {
        alert('File size exceeds 10 MB');        
    } 
}
