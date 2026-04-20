## Piston 설치

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