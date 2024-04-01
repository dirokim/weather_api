##기상청 Open API 를 통해 데이터 가져오기 

REST API 

WebClient

HttpURLConnection 

기상청에서 제공되는 지역별 엑셀표를 활용하여 위치 적용


마주친 문제 : HttpURLConnection 에서와 같이 시간 설정해놓은 변수를 대입했더니 
local variable defined in an enclosing scope must be final or effectively final 에러가 떴다

자료를 찾아보니  람다식함수에선 동일한 이름의 변수는 선언이 되지 않는다 
baseTimes 라는 다른 변수를 새로 선언해서 대입했더니 에러가 사라졌다 
람다식 함수에선 동일한 변수선언이 불가능 



