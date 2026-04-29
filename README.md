## EduQuest-Backend 설치 가이드

### docker-compose.yml 실행

repository를 로컬에 clone 합니다.

```bash
git clone <repository_url>
cd <repository_directory> 
```

docker-compose.yml 파일이 있는 디렉토리에서 다음 명령어를 실행하여 컨테이너를 시작합니다.

```bash
docker-compose up -d
```

올바른 실행을 위해서는 .env 파일이 필요합니다. .env 파일이 없는 경우, .env.example 파일을 복사하여 .env 파일을 생성하고 필요한 환경 변수를 설정합니다.

env 파일의 key는 애플리케이션 프로퍼티를 참조하세요.

### Garage 설치

Garage는 Self-hosted S3 compatible object storage입니다. 프로필 이미지를 저장하는데 사용합니다.

https://garagehq.deuxfleurs.fr/

Garage의 Docker 컨테이너는 앞선 docker-compose.yml에서 `garage` 서비스로 정의되어 있습니다. 

설치법은 다음을 참조합니다.
https://garagehq.deuxfleurs.fr/documentation/quick-start/

먼저 컨테이너를 실행하고 CLI에서 Garage가 정상적으로 설치되었는지 확인합니다.

```bash
garage status
```

Bucket을 생성하여 프로필 이미지를 저장할 준비를 합니다.
그런 다음 Bucket 목록을 확인하여 Bucket이 정상적으로 생성되었는지 확인합니다.

```bash
garage bucket create eduquest-bucket
garage bucket list
```

Bucket의 API key를 생성합니다.

```bash
garage key create eduquest-bucket-key
```

명령어의 결과는 다음과 같습니다.

```bash
Key name: eduquest-bucket-key
Key ID: GK3515373e4c851ebaad366558
Secret key: 7d37d093435a41f2aab8f13c19ba067d9776c90215f56614adad6ece597dbb34
Authorized buckets:
```

API key를 Bucket에 할당합니다.

```bash
garage bucket allow \
  --read \
  --write \
  --owner \
  eduquest-bucket \
  --key eduquest-bucket-key
```

Bucket에 어떤 Key가 할당되었는지 확인합니다.

```bash
garage bucket info eduquest-bucket
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

4. API 서버에 요청을 보내 Piston이 정상적으로 설치되었는지 확인합니다.

```bash
curl -X POST http://localhost:3000/api/v2/execute \
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