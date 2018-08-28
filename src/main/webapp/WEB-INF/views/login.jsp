<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html  class="lottemarthappy">
<head>
  <title>KAKAO</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="resources/css/Font.css">
<link rel="stylesheet" href="resources/css/Login.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		
		$("#checkBox").hide();
		$("#checkBox2").hide();
		
		$("#join").on("click",function(){
			location.href = "/web/join";
		});
		$("#home").on("click",function(){
			location.href = "/web/";
		});
		/*************************************/
		
		$("#login").on("click",function(){
			
			var id = $("#id").val();
			var passwd = $("#passwd").val();
			
			var regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
			
			if (id.match(regExp) != null) {
				
				if(id == false || passwd == false){
					$("#checkBox2").show();
					$("#checkBox").hide();
				}else{
					$.ajax({
						type:"post",
						url : "/web/userLogin",
						data : {
							"id" : id,
							"passwd" : passwd
						}
					}).done(function(data){
						console.log(data);
						var d = JSON.parse(data);
						if(d.status == 0){
							$("#checkBox").show();
							$("#checkBox2").hide();
						}else{
							alert(id + "님 환영합니다.");
							location.href = "/web/";
						}
					});
				}
			}
			else {
			  alert('올바른 형식의 이메일 주소가 아닙니다.');
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
                        <h1>로그인</h1>
                    </div>
                    <div id="img">
                        <img src="https://png.icons8.com/dusk/50/000000/google-alerts.png">
                    </div>
                    
                    <input id="home" type="button" class="btn btn-default" value="HOME">
                </div>
                
                <div id="formBox">
                    <form>
                        <div id="divId">
                            <input id="id" type="text" class="form-control" placeholder="email 형식의 아이디를 입력해 주세요.">
                        </div>
                        
                        <div id="divPasswd">
                            <input id="passwd" type="password" class="form-control" placeholder="비밀번호를 입력해 주세요.">
                        </div>
                        
                        <input id="login" type="button" class="btn btn-default" value="로그인">
                        <input id="join" type="button" class="btn btn-default" value="회원가입">
                        
                    </form>
                </div>
            </div>
        </div>
        
        <div id="checkBox">
           <input id="check" type="button" class="btn btn-danger" value="아이디 또는 비밀번호가 일치하지 않습니다.">
        </div>
        <div id="checkBox2">
           <input id="checkpass" type="button" class="btn btn-danger" value="아이디와 비밀번호 모두 입력해 주세요.">
        </div>
        
    </div>
</body>
</html>