<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html class="lottemarthappy">
<head>
  <title>SHS</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="resources/css/Font.css">
<link rel="stylesheet" href="resources/css/Myinfo.css">
<link rel="shortcut icon" type="image/x-icon" href="resources/img/amCharts.png">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript">

$(document).ready(function(){
	
	$("#removeUserQ").hide();
	
	$("#home").on("click",function(){
		location.href = "/";
	});
	
	
	/**************** 회원탈퇴 ***************/
	$.ajax({
		type:"post",
		url : "/sessionCheck"
	}).done(function(data){
		
		var d = JSON.parse(data);
		var user = d.user;
		var name = d.name;
		var userNo = d.user.userNo;
		
		$("#id").text(user.id);
		$("#name").text(user.name);
		$("#regDate").text(user.toDate);
		
		console.log(d);
		
		$("#removeUser").on("click", function(){
			
			$("#removeUser").hide();
			$("#removeUserQ").show();
			alert("정말 탈퇴하시겠습니까?");
		});
		
		$("#removeUserQ").on("click",function(){
			$.ajax({
				type:"post",
				url : "/removeUser",
				data :  {"userNo" : userNo}
			}).done(function(data){
				var d = JSON.parse(data);
				console.log(d.status);
				if(d.status == 1){
					alert("회원 탈되가 완료되었습니다.");
					location.href = "/";
				}
			});
		});
		/**************** 비밀번호 수정 ***************/
		$("#passModify").on("click",function(){
			
	        $("#layer").show();
	        $("#pw-change-box").show();
        	$("#checkpass").hide();
        	$("#voidCheck").hide();
	        
	        $("#modify").on("click",function(){
	        	
	        	var passwd = $("#change_pw").val(); 
	        	var passwdCheck = $("#change_pw_check").val();
	        	
	        	if(passwd == false || passwdCheck == false){
	        		$("#voidCheck").show();
	        		$("#checkpass").hide();
	        	}else{
	        		
		        	if(passwd !== passwdCheck){
		        		$("#checkpass").show();
		        		$("#voidCheck").hide();
		        		
		        	}else{
						$.ajax({
							type:"post",
							url : "/passModify",
							data:{
								"userNo" : userNo,
								"passwd" : passwd
							}
						}).done(function(data){
							
							var d = JSON.parse(data);
							if(d.status == 1){
								$.ajax({
									type:"post",
									url:"/logout"
								}).done(function(data){
									
									alert("비밀번호 수정이 완료되었습니다.");
									
									var d = JSON.parse(data);
									if(d.status == 1){
										location.href = "/";
									}else{
										alert("오류발생 (myinfo.jsp /logout 2번째 if문)");
									}
								});
							}else{
								alert("오류발생 (myinfo.jsp /logout 1번째 if문)");
							}
						});
		        	}
	        	}
	        })
		});
		
	    $("#layer").on("click", function(){
			$("#layer").hide();
			$("#pw-change-box").hide();
	    });
		
	});
	
});
</script>
</head>
<body>
   	<div id="layer">
   		<img src="resources/img/fireworks.gif">
   	</div>
    <div id="outer">
        <div id="main-top">
            
        </div>
        
        <div id="main-center">
            <div id="myinfoBox" class="animation">
                <div id="h1">
                    <div>
                        <h1>나의정보</h1>
                    </div>

                    <div id="img">
                        <img src="https://png.icons8.com/dusk/50/000000/google-alerts.png">
                    </div>
                    
                    <input id="home" type="button" class="btn btn-default" value="HOME">
                </div>
                
                <div id="infoBox">
                    <div id="myinfo">
                        
                        <div id="infoTable">
                            <div id="idLDiv">
                                <p>아이디 :</p>
                            </div>
                            <div id="idRDiv">
                                <p id="id"></p>
                            </div>
                        </div>
                        
                        <div id="nameLDiv">
                            <p>이름  :</p>
                        </div>
                        
                        <div id="nameRDiv">
                            <p id="name"></p>
                        </div>
                        
                        <div id="passLDiv">
                            <p>비밀번호  :</p>
                        </div>
                        
                        <div id="passRDiv">
                            <input type="button" id="passModify" class="btn btn-default" value="비밀번호 수정">
                        </div>
                        
                        <div id="regLDiv">
                            <p>가입 날짜  :</p>
                        </div>
                        <div id="regRDiv">
                            <p id="regDate"></p>
                        </div>
                    </div>
                    <div id="bottomBox">
                        <input id="removeUser" type="button" class="btn btn-danger" value="회원탈퇴">
                        <input id="removeUserQ" type="button" class="btn btn-danger" value="탈퇴버튼을 누르면 즉시 탈퇴 됩니다.">
                        
                    </div>
                    
                </div>
            </div>
        </div>
		<div id="pw-change-box">
		     <form>
		        <div class="pw-box-text">
		            <h3>비밀번호 변경</h3><br>
		        </div>
		        
		        <div class="input-group join-box-input">
		            <input type="password" class="form-control" id="change_pw" name="pass" maxlength="20" placeholder="변경하실 비밀번호를 입력하세요.">
		            <input type="password" class="form-control" id="change_pw_check" maxlength="20" placeholder="비밀번호 확인">
		        </div>
		        
		        <div class="join-btn-box">
		        	<input type="button" id="modify" class="btn btn-success" value="변경">
		        </div>
		    </form>
	        <input type="button" id="checkpass" class="btn btn-danger" value="비밀번호가 일치하지 않습니다.">
	        <input type="button" id="voidCheck" class="btn btn-danger" value="바꿀 비밀번호를 입력해 주세요.">
		</div>
    </div>
</body>
</html>