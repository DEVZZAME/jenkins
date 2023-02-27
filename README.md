1. Jenkins 설치 및 설정
Jenkins를 설치하고 설정합니다. Jenkins 설치 가이드는 공식 홈페이지를 참고하세요.

2. Jenkins Plugin 설치
Jenkins Plugin Manager에서 다음과 같은 Plugin을 설치합니다.

Maven Plugin
Git Plugin
3. Jenkins Job 생성
Jenkins Dashboard에서 새로운 Job을 생성합니다.

3.1. 소스 코드 관리
소스 코드 관리 항목에서 Git을 선택하고, 본인의 프로젝트 Git Repository URL을 입력합니다.

3.2. 빌드
빌드 항목에서 "Invoke top-level Maven targets"를 선택하고, "Goals"에 다음과 같은 내용을 입력합니다.

clean package
3.3. 빌드 트리거 설정
빌드 트리거 항목에서 다음과 같은 설정을 합니다.

Poll SCM: 체크박스를 체크하고, Schedule에 다음과 같이 입력합니다.
* * * * *
GitHub hook trigger for GITScm polling: 체크박스를 체크합니다.
3.4. 빌드 후 조치
빌드 후 조치 항목에서 다음과 같은 설정을 합니다.

Send build artifacts over SSH: 체크박스를 체크하고, SSH Site에 본인의 서버 정보를 입력합니다.
SSH 실행 스크립트: 다음과 같은 스크립트를 입력합니다.
#!/bin/bash

APP_NAME={Spring Boot Application 이름}
APP_PORT={Spring Boot Application 실행 포트}

APP_PID=$(ps -ef | grep $APP_NAME | grep -v grep | awk '{print $2}')

if [ -z "$APP_PID" ]
then
    echo "No running $APP_NAME"
else
    kill -9 $APP_PID
    echo "Killed $APP_NAME (PID: $APP_PID)"
fi

nohup java -jar -Dserver.port=$APP_PORT {Spring Boot Application 실행 파일 경로} > /dev/null 2>&1 &
echo "Started $APP_NAME"
Jenkins Job 설정이 완료되었습니다.

4. GitHub Webhook 설정
GitHub Repository에서 다음과 같은 Webhook을 설정합니다.

Payload URL: http://{Jenkins 서버 주소}/github-webhook/
Content type: application/json
Which events would you like to trigger this webhook?: Just the push event
이제 코드를 Push하면 Jenkins에서 자동으로 빌드하고, 빌드 결과물을 서버에 배포하게 됩니다.

5. 문제점
이 방법으로 CI/CD를 진행하다보면 몇 가지 문제점이 발생할 수 있습니다.

5.1. 무분별한 빌드
빌드 트리거 항목에서 "Poll SCM"을 체크하고, Schedule을 설정하면 주기적으로 Git Repository를 Polling합니다. 이 때, Git Repository에 변경 사항이 없어도 무분별하게 빌드를 진행하게 됩니다. 따라서, "GitHub hook trigger for GITScm polling"을 이용해 Push 이벤트가 발생할 때만 빌드하도록 설정하는 것이 좋습니다.

5.2. 보안
Jenkins에서 빌드 후 조치 항목에서 SSH Site 정보를 입력해야 합니다. 이 때, 보안상 취약점이 발생할 수 있습니다. 따라서, SSH Site 정보를 Jenkins Credential에 등록하고, Jenkins Job 설정에서 Credential을 이용하도록 변경하는 것이 좋습니다.

6. 결론
이번 포스팅에서는 Spring Boot Maven 프로젝트를 Jenkins를 이용해 CI/CD하는 방법을 안내했습니다. 하지만, 이 방법이 유일한 방법은 아니며, 더 나은 방법이나 다른 도구를 이용해 CI/CD를 진행할 수도 있습니다.

해당 포스팅은 아래 블로그를 참고하여 실습하며 요약 작성한 것입니다.
https://velog.io/@korea3611/Spring-Boot-Jenkins-%EC%9D%B4%EC%9A%A9%ED%95%98%EC%97%AC-CICD-%EA%B5%AC%EC%B6%95%ED%95%98%EA%B8%B0
