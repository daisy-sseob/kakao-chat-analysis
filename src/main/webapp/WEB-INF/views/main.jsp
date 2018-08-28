<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html  class="lottemarthappy">
<head>
  <title>KAKAO</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="resources/css/Font.css">
<link rel="stylesheet" type="text/css" href="resources/css/Main.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		
		$("#check").hide();
		$("#checkLogin").hide();
		
		$.ajax({
			type:"post",
			url:"/web/sessionCheck"
		}).done(function(data){

			var d = JSON.parse(data);
			var userNo;
			console.log("login Data : ", d.user);
			console.log("session status : " + d.status);
			
			if(d.status == 1){
				$("#logoutDiv").show();
				$("#myinfoDiv").show();
				$("#loginDiv").hide();
				$("#joinDiv").hide();
				
				userNo = d.user.userNo;
				$("#fileSubmit").on("click",function(){
					
					$("#checkLogin").hide();
					$("#check").hide();
					
					var fileTitle = $("#fileTitle").val();
					var file = $("#file").val();
					console.log("fileTitle: ",fileTitle,"file: ",file);
					
					if(fileTitle == false && file == false){
						$("#check").show();	
					}else{
						$("#check").hide();
						$("#userNo").val(userNo);
						$.ajax({
							type:"post",
							url:"/web/fileUpload",
							data: new FormData($("#form")[0]),
							contentType:false,
							cache:false,
							processData:false
						}).done(function(data){
							
							var dd = JSON.parse(data);
							
							console.log("fileUpload ajax : ",dd);							
							if(dd.status == 0){
								$("#check").show();
							}else{
								$("#check").hide();
								alert("파일업로드가 정상적으로 처리 되었습니다.");							
							}
						});
					}
				});
			}else{
				$("#logoutDiv").hide();
				$("#myinfoDiv").hide();
				$("#loginDiv").show();
				$("#joinDiv").show();
				
				$("#fileSubmit").on("click",function(){
					
					$("#checkLogin").show();
				});
			}
			console.log(userNo);
			$("#analysis").on("click",function(){
				
				if(d.status == 0){
					$("#checkLogin").show();
				}else{
					location.href="/web/analysis?userNo=" + userNo;
					$("#checkLogin").hide();
				}
			});
		});
		$("#logoutDiv").hide();
		
		$("#Join").on("click",function(){
			
			location.href="/web/join";
		});
		$("#Login").on("click", function(){
			location.href="/web/login";
		});
		$("#myinfo").on("click", function(){
			location.href="/web/myinfo";
		});

		$("#Logout").on("click",function(){
			$.ajax({
				type:"post",
				url:"/web/logout"
			}).done(function(data){
				var d = JSON.parse(data);
				if(d.status == 1){
					alert("로그아웃 하셨습니다.");
					location.href = "/web/";
				}
			});
		});
	});
</script>
</head>
<body>
    <div id="outer">
        <div id="main-top">
            
            <div id="top-L">
                <div id="shuttle">
                    <img class="shuttle" src="https://png.icons8.com/material-outlined/40/ffffff/space-shuttle.png">
                </div>
            </div>
            
            <div id="top-R">
                <div id="top-R-L">
                    
                    <div class="btn_div" id="joinDiv">
                        <button type="button" id="Join" class="btn btn-warning animation">회원가입</button>
                    </div>
                    
                    <div class="btn_div" id="logoutDiv">
                        <button type="button" id="Logout" class="btn btn-warning animation">로그아웃</button>
                    </div>
                    
                    <div class="btn_div" id="loginDiv">
                        <button type="button" id="Login" class="btn btn-warning animation">로그인</button>
                    </div>
                    
                    <div class="btn_div" id="myinfoDiv">
                        <button type="button" id="myinfo" class="btn btn-warning animation">나의정보</button>
                    </div>
                </div>
                
                <div id="top-R-R">

                </div>
            </div>
        </div>
        <div id="main-center">
            <div id="upload" class="animation">
                <div id="h1">
                    <div>
                        <h1>카카오톡 대화 분석기</h1>
                    </div>
                    
                    <div id="img">
                        <img src="https://png.icons8.com/dusk/50/000000/google-alerts.png">
                    </div>
                </div>
                
                <div id="h2">
                    <h2>1.회원가입/로그인 하기.</h2>
                    <h2>2.분석할 카카오톡 대화내용 파일들의 소제목 입력하기.</h2>
                    <h2>3.파일 선택하기</h2>
                    <h2>4.서버로 전송하기 버튼 클릭하기.</h2>
                </div>
                    
                <form id="form" method="post" enctype="multipart/form-data">
                
                	<input type="hidden" id="userNo" name="userNo" value="">
                	<div id="titleDiv">
                		<input type="text" name="fileTitle" id="fileTitle" style=" text-align:left; width: 100%"  class="form-control" placeholder="첨부파일들의 소제목을 입력해 주세요.">
                	</div>
                    <div id="file_input">
                        <input type="file" name="file" id="file" accept=".txt" class="form-control" multiple="multiple">
                    </div>
                    
	                <div id="submitDiv">
                        <input type="button" id="fileSubmit" class="btn btn-default" value="서버로 전송하기.">
                    </div>
                </form>
                    
                <div>
                    <input id="analysis" type="button" class="btn btn-default" value="분석하러 가기">
                </div>
                
            </div>    
            
            <div id="checkBoxDiv">
		        <div class="checkBox">
		           <input id="check" type="button" class="btn btn-danger" value="제목과 첨부파일을 모두 작성해 주세요.">
		        </div>
		        <div class="checkBox">
		           <input id="checkLogin" type="button" class="btn btn-danger" value="로그인 후 이용해 주세요.">
		        </div>
            </div>
        </div>
        
        
    </div>
</body>
</html>
       