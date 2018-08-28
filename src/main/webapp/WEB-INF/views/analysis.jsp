<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html  class="lottemarthappy">
<head>
  <title>KAKAO</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="resources/css/Font.css">
<link rel="stylesheet" href="resources/css/Analysis.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

<!--===================================== 차트 스크립트 ========================================-->
<script type="text/javascript" src="https://www.amcharts.com/lib/3/amcharts.js"></script>
<script type="text/javascript" src="https://www.amcharts.com/lib/3/pie.js"></script>
<script type="text/javascript" src="https://www.amcharts.com/lib/3/themes/chalk.js"></script>
<script type="text/javascript" src="https://www.amcharts.com/lib/3/serial.js"></script>
<script type="text/javascript" src="https://www.amcharts.com/lib/3/radar.js"></script>
<!--===================================== 차트 스크립트 ========================================-->

<script type="text/javascript">
	$(document).ready(function(){
		
		$("#home").on("click",function(){
			location.href = "/web/";
		});
		
<%
	  request.setCharacterEncoding("UTF-8");
	  String userNo = request.getParameter("userNo");
%>
		var userNo = '<%=userNo%>';
		console.log("userNo : ",userNo);
		
		
		var start = 0;
		fileList(start);
		/*========================FileTable 총 row 계산 page갯수 산출====================*/
		$.ajax({
			type:"post",
			url:"/web/fileRowCount",
			data : {
				"userNo" : userNo
			}
		}).done(function(data){
			
			var d = JSON.parse(data);
			var row = d.cnt.cnt;
			console.log("fileRow : ",d.cnt.cnt);
			
			var html = "";
			$(".pagination").empty();
			for(var i=0; i< Math.ceil(row/20); i++){
				
				html += '<li><a class="page">'+ (i + 1) +'</a></li>';
			}
			$(".pagination").append(html);
			
			/* ===================Page Click Event ======================= */
			$(".page").on("click", function(){
				
				var pageIndex = $(".page").index(this);
				console.log(pageIndex);
				start = pageIndex;
			
				/*==================파일 리스트가져오기====================== */
				fileList(start);
			});
		})


		
		function fileList(f){
			
			$.ajax({
				type:"post",
				url:"/web/fileList",
				data:{
					"userNo":userNo,
					"start" : f
				}
			}).done(function(data){
				
				var d = JSON.parse(data);
				
				var html = "";
				$("#olList").empty();
				for(var i=0; i<d.map.length; i++){
					
					var filename = d.map[i].fileName;
					var fileTitle = d.map[i].fileTitle;
					var fileNo = d.map[i].fileNo;
					
					html += '<li class="FileLi">';
						html += '<div class="fileNo">' + fileNo +'</div>' + '<div class="fileTitle"> '+ fileTitle +'  </div>' + '<div class="fileName"> '+ filename +'  </div>'; 
						html += '<div class="removeDiv">';
							html += '<input type="button" class="remove btn btn-default" value="x">';
						html += '</div>';
					html += '</li>';
				}
				$("#olList").append(html);
				
				$(".remove").on("click",function(){
					
					var fileNo = $(this).closest(".FileLi").find(".fileNo").text();
					console.log(fileNo);
					
					$.ajax({
						type:"post",
						url:"fileDelete",
						data:{
							"fileNo":fileNo
						}
					}).done(function(data){
						
						var d = JSON.parse(data);
						if(d.status == 1){
							alert("삭제완료");
							location.reload();
						}else{
							alert("오류");
						}
					});		
				});
				
				$(".fileName").on("click",function(){
					
					var fileNo = $(this).closest(".FileLi").find(".fileNo").text();
					console.log(fileNo);
					
					$.ajax({
						type:"post",
						url:"/web/fileOutHadoop",
						data: {
							"fileNo" : fileNo
						}
					}).done(function(data){
						$("#btnDiv").show();
						
						var d = JSON.parse(data);
						
						var dataTalk = JSON.parse(d.dataTalk);
						var dataSlang = JSON.parse(d.dataSlang);
						var dataTerm = JSON.parse(d.dataTerm);
						var dataTimeSlot = JSON.parse(d.dataTimeSlot);
						
						parseData(dataTalk);
						parseData(dataSlang);
						parseData(dataTerm);
						parseData(dataTimeSlot);
						
						talkChart(dataTalk);
						slangChart(dataSlang);
						termChart(dataTerm);
						timeSlotChart(dataTimeSlot);
						

						$("#talk").on("click",function(){
							$("#chartView1").show();
							$("#chartView2").hide();
							$("#chartView3").hide();
							$("#chartView4").hide();
						});
						$("#slang").on("click",function(){
							$("#chartView1").hide();
							$("#chartView2").show();
							$("#chartView3").hide();
							$("#chartView4").hide();
						});
						$("#term").on("click",function(){
							$("#chartView1").hide();
							$("#chartView2").hide();
							$("#chartView3").show();
							$("#chartView4").hide();
						});
						$("#timeSlot").on("click",function(){
							$("#chartView1").hide();
							$("#chartView2").hide();
							$("#chartView3").hide();
							$("#chartView4").show();
						});
						
					});
				});
				
			});
		}
		
		function parseData(data){
			for(var i =0; i < data.length; i++){
				
				data[i]["column-1"] = parseInt(data[i]["column-1"]);
			}
		}
		
		function talkChart(data){
			var data = data;
			AmCharts.makeChart("chartView1",
			{
				"type": "pie",
				"balloonText": "[[title]]<br><span style='font-size:16px; color:black'><b>[[value]]</b> ([[percents]]%)</span>",
				"titleField": "category",
				"valueField": "column-1",
				"fontSize": 16,
				"color": "#000000",
				"theme": "chalk",
				"titles": [{"size":35, "text" : "대화 빈도 수"}],
				"dataProvider" : data
			});
		}
		
		function slangChart(data){
			var data = data;
			console.log(data);
			AmCharts.makeChart("chartView2",
				{
					"type": "serial",
					"categoryField": "category",
					"startDuration": 1,
					"fontSize": 16,
					"categoryAxis": {"gridPosition": "start"},
					"graphs": [{
							"colorField": "color",
							"fillAlphas": 1,
							"id": "AmGraph-1",
							"lineColorField": "color",
							"title": "graph 1",
							"type": "column",
							"valueField": "column-1"
						}],
					"titles": [{
							"id": "Title-1",
							"size": 35,
							"text": "비속어 사용 횟수"
						}],
					"dataProvider": data
				}
			);
		}
		function termChart(data){
			var data = data;
			AmCharts.makeChart("chartView3",
				{
					"type": "radar",
					"categoryField": "category",
					"startDuration": 2,
					"theme": "patterns",
					"fontSize": 16,
					"graphs": [{
							"balloonText": "[[value]]",
							"bullet": "round",
							"id": "AmGraph-1",
							"valueField": "column-1"
						}],
					"valueAxes": [{
							"axisTitleOffset": 20,
							"gridType": "circles",
							"id": "ValueAxis-1",
							"minimum": 0,
							"axisAlpha": 0.15,
							"dashLength": 3
						}],
					"titles": [{
							"size":35,
							"text":"월별 대화 빈도"
						}],
					"dataProvider": data
				}
			);
		}
		
		function timeSlotChart(data){
			var data = data;
			
			AmCharts.makeChart("chartView4",
				{
					"type": "serial",
					"categoryField": "category",
					"startDuration": 1,
					"categoryAxis": {
						"gridPosition": "start"
					},
					"trendLines": [],
					"graphs": [
						{
							"balloonText": "[[title]] of [[category]]:[[value]]",
							"bullet": "round",
							"id": "AmGraph-1",
							"title": "graph 1",
							"type": "smoothedLine",
							"valueField": "column-1"
						}
					],
					"guides": [],
					"valueAxes": [
						{
							"id": "ValueAxis-1",
							"title": "Axis title"
						}
					],
					"allLabels": [],
					"balloon": {},
					"legend": {
						"enabled": true,
						"useGraphSettings": true
					},
					"titles": [
						{
							"id": "Title-1",
							"size": 35,
							"text": "시간대별 대화 빈도"
						}
					],
					"dataProvider": data
				}
			);
		}
	});
</script>
</head>
<body>
    <div id="outer">
        <div id="left">
            <div id="leftTitle" class="animation">
                <h1 id="h1">분석</h1>

                <div id="leftImg">
                    <img src="https://png.icons8.com/dusk/50/000000/google-alerts.png">
                </div>
                <input id="home" type="button" class="btn btn-default" value="HOME">
            </div>
            
            <div id="leftList" class="animation">
            
                <div id="fileList">
                    <ol id="olList"></ol>
                </div>
                
                <div id="paging">
                  <ul class="pagination">
<!--                     <li><a href="#">1</a></li> -->
<!--                     <li><a href="#">2</a></li> -->
<!--                     <li><a href="#">3</a></li> -->
<!--                     <li><a href="#">4</a></li> -->
<!--                     <li><a href="#">5</a></li> -->
                  </ul>
                    
                    
                </div>
            </div>
        </div>

        <div id="right">
            <div id="chartDiv">
				<div id="chartBtnDiv">
                    <div id="btnDiv">
                        <input type="button" id="talk" class="btn btn-default" value="사용자별 대화 빈도">
                        <input type="button" id="slang" class="btn btn-default" value="비속어 사용 빈도">
                        <input type="button" id="term" class="btn btn-default" value="월별 대화 빈도">
                        <input type="button" id="timeSlot" class="btn btn-default" value="시간대별 대화 빈도">
                    </div>
				</div>                 
				
                <div id="chartViewDiv">
					<div id="chartView1" class="chartView"></div>           
					<div id="chartView2" class="chartView"></div>           
					<div id="chartView3" class="chartView"></div>           
					<div id="chartView4" class="chartView"></div>           
                </div>
                <div id="saveDive">
                    <input id="save" type="button" class="btn btn-default" value="저장하기">
                </div>
            </div>
        </div>
    </div>
</body>
</html>