##기상청 Open API 를 통해 데이터 가져오기 

REST API 

WebClient

HttpURLConnection 

기상청에서 제공되는 지역별 엑셀표를 활용하여 위치 적용


마주친 문제 

1

HttpURLConnection 에서와 같이 시간 설정해놓은 변수를 대입했더니 
local variable defined in an enclosing scope must be final or effectively final 에러가 떴다
자료를 찾아보니  람다식 함수에선 동일한 이름의 변수는 선언이 되지 않는다 
baseTimes 라는 다른 변수를 새로 선언해서 대입했더니 에러가 사라졌다 
람다식 함수에선 동일한 변수선언이 불가능 

2 

HttpUrlConnection 방식에서는 API 잘 되었지만 WebClient로  요청 보낸  이후에는 계속 서버 오류가 발생했다.
SERVICE KEY IS NOT REGISTERED ERROR 문서를 보니 서비스 키가 오지 않았을떄 일어나는 에러
구글링을 했더니 WebClient를 이용해서 HTTP 요청을 할 때 WebClient가 queryParam을 UriComponentsBuilder#encode() 방식을 이용해서 
인코딩하기 때문에 service key의 값이 달라져서 생기는 문제 
DefaultUriBuilderFactory() 객체를 생성 setEncodingMode  VALUES_ONLY로 변경 
uriBuilderFactory 에 값을 넣고
요청을 보내면 제대로 잘 동작한다.


