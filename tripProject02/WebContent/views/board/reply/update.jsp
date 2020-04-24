<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="http://code.jquery.com/jquery.js"></script>
<style type="text/css">
.row{
	margin: 0 auto;
}
h2 {
	text-align: center;
}
#update{
	margin: 0px auto;
	display: block;
	width: 800px;
	
}
</style>
<script type="text/javascript">
$(function(){
	$('#pwd2').keyup(function(){ // 유저가 뭔가 입력후 바로 체크 ==> keyup으로 체크 
		var k=$(this).val();
		console.log(k);
		var no=$('#no').val();	
		$.ajax({
			type:'POST',
			url:'../reply/password_check.do', 
			data:{"pwd":k,"no":no}, // 요청과 함께 서버에 보내는 string 또는 json
			success:function(res) // success:  요청이 성공일때 실행되는 callback 함수
			{ 
				var no=res.trim();
				if(no==1){
					$('#pwd_check_result').html("<font color=blue>비밀번호가 맞습니다. 수정할 수 있습니다.</font>");
					$('#updateBtn').attr('disabled', false);
				}
				else
				{
					$('#pwd_check_result').html("<font color=red>비밀번호가 틀립니다.</font>");
					$('#updateBtn').attr('disabled', true);
				}
			},
			error:function(e){
				alert(e);
			}
		})
	});
});
</script>
</head>
<body>
<div class="breadcumb-area bg-img bg-overlay" style="background-image: url(../../template/css/img/bg-img/jeju.png)"></div>
<div class="wrapper row2">
  <div id="services" class="clear">
  <div class="text-center">
  		<img src="reply.png">
  	</div> 
	<div class="container">
		<h2>수정하기</h2>
		<div id="update">
			<form method=post action="update_ok.do"> 
				<table class="table table-hover" style="width:80%; margin:0px auto;">
					<tr>
						<th width=20% class="text-right success">이름</th>
						<td width=80%>
							<input type="text" name="name" size=15 value="${vo.name }" required />
							<input type="hidden" name="no" id="no" value="${vo.no }">
						</td>
					</tr>
					<tr>
						<th width=20% class="text-right success">제목</th>
						<td width=80%>
							<input type="text" name="subject" size=50 value="${vo.subject }" required />
						</td>
					</tr>
					<tr>
						<th width=20% class="text-right success">내용</th>
						<td width=80%>
							<textarea rows="8" cols="50" name="content" required>${vo.content }</textarea>
							<!-- input은 value로 주고, textarea는 태그 사이에 값 준다. -->
						</td>
					</tr>
					<tr>
						<th width=20% class="text-right success">비밀번호</th>
						<td width=80%>
							<input type="password" name="pwd" size=10 required id="pwd2"> <!-- main.jsp의 비번창이 id가 pwd라서 얘는 id를 pwd2로 줬음 -->
							<div id="pwd_check_result"></div>
						</td>
					</tr>
					<tr>
						<td class="text-center" colspan="2">
							<input type="submit" value="수정" class="btn btn-sm btn-primary"
							id="updateBtn" disabled>
							<input type="button" value="취소" class="btn btn-sm btn-danger"
							onclick="javascript:history.back()">
						</td>
					</tr>
				</table>
			</form>
		</div>
		</div>
		</div>
		</div>
	</div>
</body>
</html>