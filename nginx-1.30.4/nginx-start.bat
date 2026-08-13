rem 한글이 깨진 것은 BAT 파일의 인코딩과 CMD 코드 페이지가 맞지 않아서입니다.
rem Windows 11 기준으로는 BAT 파일을 UTF-8로 저장하고, 첫 줄에서 UTF-8 코드 페이지(65001)를 지정하는 방법을 추천합니다.
chcp 65001 > nul

rem nginx 시작, nginx prefix 경로 지정
start "" /B "C:\eGovCI-5.0.0-Windows-64bit\nginx-1.30.4\nginx.exe" -p "C:/eGovCI-5.0.0-Windows-64bit/nginx-1.30.4/"
