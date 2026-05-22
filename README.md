# README

## 프로젝트 소개

### 개요

EduQuest는 초등학생을 대상으로 코딩 교육을 제공하는 유니티 기반의 교육용 게임입니다. 

EduQuest 게임은 WebGL로 개발되어 웹 브라우저에서 실행됩니다.

사전에 회원가입을 한 사용자만이 게임에 접속하여 플레이 할 수 있어야 한다는 요구사항을 충족해야 합니다.

게임 자체의 데이터가 아닌 스테이지 및 문제에 대한 정보를 어딘가에 저장하고 호출해야 합니다.

게임을 위한 문제 답변 제출과 채점은 사용자가 변조할 수 없는 방식으로 이루어져야 합니다.

사용자가 자신이 틀린 문제를 확인하고 복습할 수 있는 피드백 기능이 필요합니다.

다음과 같은 요구사항을 구현하기 위해, 게임의 백엔드 시스템이 필요합니다.

백엔드는 사용자 관리, 스테이지 및 문제 관리, 채점 시스템, 오답노트, 질의응답 커뮤니티 및 AI 피드백 연동
시스템을 포함하여 게임의 백엔드 기능을 담당합니다.

### 팀원 및 역할

- **김진숙**: UI 설계, 프론트엔드 개발, 유니티 연동 로직 작성
- **김원진**: 유니티 게임 개발, 게임 로직 구현
- **박현진**: 데이터베이스 설계, 프론트엔드 개발, 백엔드 연동 로직 작성
- **김재원**: 데이터베이스 설계, API 명세서 작성, 백엔드 개발

### 개발 기간

- **개발 기간**: 2025년 9월 1일 ~ 2026년 6월 2일 (9개월)

## 주요 기능

- **Identity**: 회원 가입, 로그인, 로그아웃, 회원 정보 수정
- **Learning**: 스테이지 및 문제 관리
- **Submission**: 코드 제출 및 채점 시스템
- **Progress**: 회원별 학습 진행 상황 조회
- **WrongNote**: 오답노트 기능, 외부 AI API 연동 및 오답 피드백 제공
- **Community**: 질의응답 커뮤니티 기능
- **Reward**: 포인트 시스템 및 보상 지급 이력 관리
- **Note**: 회원별 필기 노트 기능
- **Bookmark**: 문제 즐겨찾기 기능
- **File**: 회원 썸네일 파일 업로드 및 관리 기능, S3 object storage 연동

## 기술 스택

- **프로그래밍 언어**: Java 25
- **프레임워크**: Spring Boot 4, Spring Data JPA, Spring Security
- **데이터베이스**: MariaDB
- **인증**: JWT (JSON Web Tokens)
- **컨테이너화**: Docker
- **오브젝트 스토리지**: Garage (Self-hosted S3 compatible object)
- **코드 실행 엔진**: Piston (Self-hosted code execution engine)
- **API 문서화**: Postman

## DB 설계

ERD 다이어그램은 다음과 같습니다.

## API 명세

Postman으로 작성한 API 명세는 다음 이미지와 같습니다.

## 트러블 슈팅

1. **문제** : Piston API 서버의 Bad Request 오류

| 목차 | 내용                                                                                                                                                                                                                                                                                                                               |
|----|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 상황 | RestClient로 Piston API를 호출하는 채점 메소드를 통합 테스트에서 동작시키면 400 Bad Request 또는 연결 오류를 발생시켰다. 스프링 애플리케이션 로그만으로는 원인을 알 수 없었고, Piston 컨테이너 로그에도 관련 항목이 없었다. 동일한 로직을 RestTemplate으로 작성하여 요청을 보낼 때는 정상 동작하였다.                                                                                                                                 |
| 과제 | 1) 400 Bad Request를 일으킨 원인을 네트워크/프로토콜 수준에서 규명하고 해결한다. 2) Piston API를 호출하는 통합 테스트를 통과한다.                                                                                                                                                                                                                                          |
| 행동 | 1) 와이어샤크로 테스트 시 전송된 HTTP 요청 패킷을 캡처하여 정상 동작(예: curl/RestTemplate)과 비교했다. 2) 캡처 결과 요청에 HTTP/2 cleartext(h2c) 업그레이드 헤더가 포함되어 있음을 확인했고, Piston이 h2c 헤더를 이해하지 못해 400을 반환한다고 판단했다. 3) RestClient 로직을 수정하여 plain-http(scheme == "http")일 때 JDK HTTP Client의 버전을 HTTP/1.1로 강제하도록 하였다. 4) 수정 후 와이어샤크로 패킷의 헤더를 재검증하고 통합 테스트를 실행하여 결과를 확인했다. |
| 결과 | 1) HTTP/2(h2c) 업그레이드 헤더 전송이 중단되어 Piston이 400을 반환하던 문제가 해결되었다. 2) 올바른 설정을 사용하여 Piston(도커)과 정상 통신이 가능해졌고 Application Context가 정상 로드되며 통합 테스트가 통과하였다.                                                                                                                   |



## EduQuest-Backend 설치 가이드

### docker-compose.yml 실행

repository를 로컬에 clone 합니다.

```bash
git clone https://github.com/Capstone-EduQuest/backend.git
cd backend
```

.env 파일과 prod.env 파일을 해당 디렉토리에 넣어줍니다.

docker-compose.yml 파일이 있는 디렉토리에서 다음 명령어를 실행하여 컨테이너를 시작합니다.

```bash
docker-compose up -d
```

올바른 실행을 위해서는 .env 파일이 필요합니다.

env 파일의 key는 애플리케이션 프로퍼티를 참조하세요.

### Garage 설치

Garage는 Self-hosted S3 compatible object storage입니다. 프로필 이미지를 저장하는데 사용합니다.

https://garagehq.deuxfleurs.fr/

Garage의 Docker 컨테이너는 앞선 docker-compose.yml에서 `garage` 서비스로 정의되어 있습니다. 

설치법은 다음을 참조합니다.
https://garagehq.deuxfleurs.fr/documentation/quick-start/

먼저 컨테이너를 실행하고 CLI에서 Garage가 정상적으로 설치되었는지 확인합니다.

```bash
docker exec -it garage /garage status
```

클러스터 레이아웃을 생성해야 합니다.
```bash
docker exec -it garage /garage layout assign -z dc1 -c 1G <node_id>
docker exec -it garage /garage layout apply --version 1
```
<node_id>에는 garage status 명령어로 확인한 node ID를 입력해야 합니다.

Bucket을 생성하여 프로필 이미지를 저장할 준비를 합니다.
그런 다음 Bucket 목록을 확인하여 Bucket이 정상적으로 생성되었는지 확인합니다.

```bash
docker exec -it garage /garage bucket create eduquest-bucket
docker exec -it garage /garage bucket list
```

Bucket의 API key를 생성합니다.

```bash
docker exec -it garage /garage key create eduquest-bucket-key
```

명령어의 결과는 다음과 같습니다.

```bash
Key name: eduquest-bucket-key
Key ID: GK3515373e4c851ebaad366558
Secret key: 7d37d093435a41f2aab8f13c19ba067d9776c90215f56614adad6ece597dbb34
Authorized buckets:
```
(key ID와 Secret key는 예시입니다. 실제로는 명령어 실행 시 생성된 값을 사용해야 합니다.)

.env와 prod.env 파일의 AWS_S3_ACCESS_KEY와 AWS_S3_SECRET_KEY에 각각 Key ID와 Secret key로 변경합니다.

API key를 Bucket에 할당합니다.

```bash
docker exec -it garage /garage bucket allow --read --write --owner eduquest-bucket --key eduquest-bucket-key
```

Bucket에 어떤 Key가 할당되었는지 확인합니다.

```bash
docker exec -it garage /garage bucket info eduquest-bucket
```

### Piston 설치

Piston은 자바스크립트로 작성되 경량 코드 실행 엔진입니다. 

https://github.com/engineer-man/piston

백엔드에서는 코드 채점을 위해 Piston API를 사용합니다. Piston API는 다양한 프로그래밍 언어를 지원하며, 코드 실행과 채점에 필요한 기능을 제공합니다.

Piston을 설치하려면 다음 단계를 따르세요.

1. Piston repository를 클론합니다.

```bash
git clone https://github.com/engineer-man/piston
```

2. cli에 필요한 의존성을 설치합니다.

```bash
cd piston
cd cli
npm install
cd ..
```

3. cli를 사용해 파이썬 패키지를 설치합니다.

```bash
cli/index.js ppman list
cli/index.js ppman install python
```

4. docker-compose로 컨테이너를 실행합니다.

```bash
docker-compose up -d api
```

5. API 서버에 curl 요청을 보내 Piston이 정상적으로 설치되었는지 확인합니다.

```bash
curl -X POST http://localhost:2000/api/v2/execute \
-H "Content-Type: application/json" \
-d '{
  "language": "python",
  "version": "3.12.0",
  "files": [
    {
      "name": "main.py",
      "content": "print(\"Hello, World!\")"
    }
  ],
  "stdin": "",
  "compile_timeout": 10000,
  "run_timeout": 3000,
  "compile_memory_limit": -1,
  "run_memory_limit": -1
}'
```