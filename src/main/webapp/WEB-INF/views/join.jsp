<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html  class="lottemarthappy">
<head>
  <title>SHS</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="resources/css/Font.css">
<link rel="stylesheet" href="resources/css/Join.css">
<link rel="shortcut icon" type="image/x-icon" href="resources/img/amCharts.png">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	
	$("#home").on("click",function(){
		location.href = "/";
	});
	
	$("#checkBox").hide();
	$("#checkBox2").hide();
	$("#checkBox3").hide();
	
	$("#join").on("click",function(){
		var id = $("#id").val(); 
		var name = $("#name").val(); 
		var passwd = $("#passwd").val(); 
		var passwdCheck = $("#passwdCheck").val();
		
		var regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
		
		if (id.match(regExp) != null) {
			$.ajax({
				type:"post",
				url:"/OverlapCheck",
				data: {
					"id" : id,
				}
			}).done(function(data){
				console.log(data);
				var d = JSON.parse(data)
				console.log(d);
				console.log(d.status);
				
				if(d.status == 1){
					if(passwd !== passwdCheck){
						$("#checkBox3").hide();
						$("#checkBox2").show();
					}else{
						$("#checkBox2").hide();
						join();
					}
				}else if(d.status == 0){
					$("#checkBox3").show();
				}
			});
		}
		else {
			alert('올바른 형식의 이메일 주소가 아닙니다.');
			console.log("2");
		}
		
		
		function join(){
			if(id == false || name == false || passwd == false){
				$("#checkBox").show();
			}else{
				$.ajax({
					type:"post",
					url:"/userInsert",
					data: {
						"id" : id,
						"name":name,
						"passwd":passwd
					}
				}).done(function(data){
					console.log(data);
					var d = JSON.parse(data)
					console.log(d);
					console.log(d.status);
					
					if(d.status == 1){
						location.href = "/";
						alert("회원가입을 축하합니다 !");
					}else if(d.status == 0){
						
						alert("회원가입에 필요한 정보를 입력해 주세요");
						location.href="/join";
					}
				});
			}
		}
	});
});

</script>
</head>
<body>
    <div id="outer">
        <div id="main-top">
        </div>
        
        <div id="main-center">
            <div id="LoginBox" class="animation">
                <div id="h1">
                    <div>
                        <h1>회원가입</h1>
                    </div>

                    <div id="img">
                        <img src="https://png.icons8.com/dusk/50/000000/google-alerts.png">
                    </div>
                    <input id="home" type="button" class="btn btn-default" value="HOME">
                </div>
                
                <div id="formBox">
                    <form>
                        <div id="joinInfo">
                            
                            <input id="id" type="text" class="form-control" maxlength="25" placeholder="email 형식의 아이디를 입력해 주세요.">
                            <input id="name" type="text" class="form-control" maxlength="10" placeholder="이름을 입력해 주세요." >
                        
                            <input id="passwd" type="password" class="form-control" maxlength="20" placeholder="비밀번호를 입력해 주세요. (20글자까지 작성가능)">
                            
                            <input id="passwdCheck" type="password" class="form-control" maxlength="20" placeholder="비밀번호 확인">
                        </div>
                        
                        
                        <input id="join" type="button" class="btn btn-default" value="회원가입">
                        
                    </form>
                </div>
            </div>
            
        </div>
        
        <div id="checkBox">
           <input id="check" type="button" class="btn btn-danger" value="회원가입에 필요한 정보를 입력해 주세요!">
        </div>
        <div id="checkBox2">
           <input id="checkpass" type="button" class="btn btn-danger" value="비밀번호가 일치하지 않습니다.">
        </div>
        
        <div id="checkBox3">
           <input id="checkId" type="button" class="btn btn-danger" value="이미 가입되어있는 아이디 입니다.">
        </div>
    </div>
</body>
</html>