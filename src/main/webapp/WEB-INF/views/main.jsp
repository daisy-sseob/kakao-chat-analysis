<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html  class="lottemarthappy">
<head>
  <title>SHS</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="resources/css/Font.css">
<link rel="stylesheet" type="text/css" href="resources/css/Main.css">
<link rel="shortcut icon" type="image/x-icon" href="resources/img/amCharts.png">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="//developers.kakao.com/sdk/js/kakao.min.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		Kakao.init('421cbce990b3e8f628f6feb5c5d70c70');
		
		$("#check").hide();
		$("#checkLogin").hide();
		$("#checkFile").hide();
		
		$.ajax({
			type:"post",
			url:"/sessionCheck"
		}).done(function(data){

			var d = JSON.parse(data);
			var userNo;
			
			console.log("login Data : ", d.user);
			console.log("kakaoLogin Data : ", d.kakaoUser);
			console.log("session status : " + d.status);
			
			if(d.kakaoUser != null){
				var profileImg = d.kakaoUser.profileImg;
				if(profileImg != null){
					var html = '<img id="kakaoProfile" src = ' + d.kakaoUser.image + '>';
					$("#kakaoDiv").append(html);
				}else{
					var html = '<img id="kakaoProfile" src = "http://mblogthumb1.phinf.naver.net/20150417_264/ninevincent_14291992723052lDb3_JPEG/kakao_11.jpg?type=w2">';
					$("#kakaoDiv").append(html);
				}
			}
			
			$("#kakaoProfile").on("click",function(){
				if(profileImg != null){
					window.open(profileImg);
				}else{
					window.open("http://mblogthumb1.phinf.naver.net/20150417_264/ninevincent_14291992723052lDb3_JPEG/kakao_11.jpg?type=w2");
				}
			});
			if(d.kakaoUser != null){
				$("#myinfoDiv").hide();
			}else if(d.user != null){
				$("#myinfoDiv").show();
			}
			if(d.status == 1){
				$("#logoutDiv").show();
				$("#loginDiv").hide();
				$("#joinDiv").hide();
				
				if(d.user != null){
					userNo = d.user.userNo;
				}else if(d.kakaoUser != null){
					userNo = d.kakaoUser.kakaoId;
				}
				
				$("#fileSubmit").on("click",function(){
					
					$("#checkLogin").hide();
					$("#check").hide();
					
					var fileTitle = $("#fileTitle").val();
					var file = $("#file").val();
					console.log("fileTitle: ",fileTitle,"file: ",file);
					
					if(fileTitle == false || file == false){
						$("#check").show();	
					}else{
						$("#check").hide();
						$("#userNo").val(userNo);
						$.ajax({
							type:"post",
							url:"/fileUpload",
							data: new FormData($("#form")[0]),
							contentType:false,
							cache:false,
							processData:false
						}).done(function(data){
							
							var dd = JSON.parse(data);
							
							console.log("fileUpload ajax : ",dd);							
							if(dd.status == 0){
								$("#check").show();
								$("#checkFile").hide();
							}else if(dd.status == 2){
								$("#checkFile").show();
								$("#check").hide();
							}else{
								$("#check").hide();
								$("#checkFile").hide();
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
			console.log("userNo : ",userNo);
			$("#analysis").on("click",function(){
				
				if(d.status == 0){
					$("#checkLogin").show();
				}else{
					location.href="/analysis?userNo=" + userNo;
					$("#checkLogin").hide();
				}
			});

			$("#Logout").on("click",function(){
				
				if(d.user !== null){
		 			$.ajax({
		 				type:"post",
		 				url:"/logout"
		 			}).done(function(data){
		 				var d = JSON.parse(data);
		 				if(d.status == 1){
	 						alert("로그아웃 하셨습니다.");
	 						location.reload();
		 				}
		 			});
				}else if(d.kakaoUser !== null){
					$.ajax({
		 				type:"post",
		 				url:"/logout"
		 			}).done(function(data){
		 				var d = JSON.parse(data);
		 				if(d.status == 1){
		 					kakaoLogOut();
		 					setTimeout(function(){
		 						alert("로그아웃 하셨습니다.");
		 						location.reload();
		 					}, 400);
		 				}
		 			});
				}
			});
		});
		$("#logoutDiv").hide();
		$("#Join").on("click",function(){
			
			location.href="/join";
		});
		$("#Login").on("click", function(){
			location.href="/login";
		});
		$("#myinfo").on("click", function(){
			location.href="/myinfo";
		});
		function kakaoLogOut(){
			// 카카오톡 로그아웃 할때 토큰 삭제 요청
			Kakao.Auth.logout();
			// 카카오톡 사이트로 로그아웃 요청
			var kakaoWindow = window.open("http://developers.kakao.com/logout");
			setTimeout(function(){
				kakaoWindow.close(); // 로그아웃 요청 후 화면 닫기 위해서 딜레이 처리
			}, 500);
		}
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
                    <h1>카카오톡 대화 분석기</h1>
                    
                    <div id="img">
                        <img src="https://png.icons8.com/dusk/50/000000/google-alerts.png">
                    </div>
                    <div id="kakaoDiv"></div>
                </div>
                
                <div id="h2">
                    <h2>1.회원가입/로그인 하기.</h2>
                    <h2>2.분석할 카카오톡 대화내용 파일들의 소제목 입력하기.</h2>
                    <h2>3.파일 선택하기</h2>
                    <h2>4.서버로 전송하기 버튼 클릭하기.</h2>
                </div>
                    
                <form id="form" method="post" enctype="multipart/form-data" onsubmit="return false;">
                
                	<input type="hidden" id="userNo" name="userNo" value="">
                	<div id="titleDiv">
                		<input type="text" name="fileTitle" maxlength="10" id="fileTitle" style=" text-align:left; width: 100%"  class="form-control" placeholder="첨부파일들의 소제목을 입력해 주세요.">
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
		           <input id="check" type="button"  class="btn btn-danger" value="제목과 첨부파일을 모두 작성해 주세요.">
		        </div>
		        <div class="checkBox">
		           <input id="checkLogin" type="button" class="btn btn-danger" value="로그인 후 이용해 주세요.">
		        </div>
   		        <div class="checkBox">
		           <input id="checkFile" type="button" class="btn btn-danger" value="txt파일만 업로드 할 수 있습니다.">
		        </div>
            </div>
            <div id="footerDiv" class="animation">
            	<div id="footer">
            		<p>Develope by 심현섭</p>
            		<p>Developer email : orolsyeo@gmail.com</p>
            		<p>Developer phone number : 010-2586-2863</p>
            	</div>
            </div>
        </div>
        
        
    </div>
</body>
</html>
       