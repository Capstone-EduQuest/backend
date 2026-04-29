# 현재 프로젝트

초등학생 ~ 중학생을 대상으로 한 코딩 교육 게임의 백엔드 시스템 개발.

# 기술 스택

자바 + 스프링 부트 사용 중

자세한 백엔드 의존성은 build.gradle 파일 참조

# API 명세서

EduQuest.postman_collection.json

POSTMAN으로 작성함

# ERD 및 DDL

ERD는 erd2.erd 파일 참조

DDL은 resources/db/migration의 sql들 참조 

# 현재 완료된 작업

1. 초기 세팅
2. identity 도메인 구현 (로그인, 회원가입, 유저 프로필 기능)
3. learning, reward 도메인 구현

# 당신이 해야 할 작업

현재 브랜치는 feature/submission-wrongnote임

즉 본격적으로 사용자의 문제 답변 제출 및 피드백을 개발할 차례

현재 submission, evaluation, wrongnote을 구현됨

이제 progress 도메인과 그와 관련된 API를 구현할 차례

# 개발시 주의할 점

- var 키워드 쓰지 않기
- 람다식에서 최대한 축약어(예 : e, t, s 등) 자제하기
- 코딩 스타일은 identity 도메인을 참조하여 일관성 있게 유지하기
- 같은 계층의 다른 도메인의 Service를 참조하기 보다는 어느 정도 로직의 중복을 허용하더라도 하위 계층을 참조하여 의존성 방향을 지키도록 노력하기
- 계층 간 의존성 방향이 위배될 것 같으면 이벤트 드리븐을 적극적으로 사용하기
- 배열은 xxxs, 리스트는 xxxList, 맵은 xxxMap 등으로 명명하기
- CRUD 로직 시 service에서는 findXXXByxxx, repository에서는 findByXXX 등으로 일관성 있게 명명하기
- CommandService에서는 xxxJpaRepository만을, QueryService에서는 xxxQueryRepository만을 참조하기
- querydsl 사용 시 xxxQueryRepository -> xxxQRepositoryImpl -> xxxRepositoryImpl 순으로 구현하기
- application 계층에서는 정말 구현체 확장이 필요한 경우가 아니면 Service 인터페이스를 만들지 말고 바로 구현체 클래스를 만들어서 사용하기
- 계층 간 의존 방향은 presentation -> application -> domain <- infra 순으로 유지하기

# 순서

- 매 단계에서 나에게 결과물을 보여준 다음 피드백을 받고, 내가 다음 단계로 넘어가라고 지시하면 그때 다음 단계로 넘어가라.

## 가장 먼저 해야 할 것

1. 이미 구현된 identity 도메인의 코드를 읽고 나의 코딩 스타일을 파악하라. 현재 아키텍처는 클린 아키텍처를 따르고 있다. 또한 신기술 도입에는 보수적이되 확장 가능성을 열어두고 있고, 계층 간 의존성 방향이 위배될 것 같으면 이벤트 드리븐을 적극적으로 사용하고 있다.

2. ERD를 보고 reward 및 그와 관련된 도메인/DB에 대해 이해하라.

3. EduQuest.postman_collection.json 파일을 보고 API 명세서를 이해하라.

## 1단계 : 계획

앞에서 본 3가지를 바탕으로 어떻게 progress 도메인을 구현할지, 패키지 구조와 파일의 예상도는 어떻게 될지 설명하라. 그런 다음 내가 그걸 읽고 평가하고 지시할 것이다.

## 2단계 : API 구현

먼저 presentation 계층에서 controller와 DTO 그리고 presentation 계층에서 필요한 예외 클래스를 구현하라.

request, response DTO는 API 명세서(EduQuest.postman_collection.json)를 참고하여 별개의 record class로 구현하라.

identity 도메인을 참조하여 코딩 스타일을 유지하라.

## 3단계 : 엔티티 클래스

db/migration 폴더 내 sql 파일을 보고 infra 계층의 persistence에서 엔티티 클래스를 구현하라.

identity 도메인을 참조하여 코딩 스타일을 유지하라.

## 4단계 : 도메인 구현

domain 계층에서 필요한 도메인 패키지 및 component, dto, event, model class, service interface를 구현하라.

identity 도메인을 참조하여 코딩 스타일을 유지하라.

## 5단계 : 비즈니스 로직 구현

application 계층에서 필요한 Service, DTO, listener, exception 등을 구현하라

identity 도메인을 참조하여 코딩 스타일을 유지하라.

## 6단계 : 기술 계층

infra 계층에서 필요한 repository, repository 인터페이스 및 impl, 스프링 시큐리티 설정 등을 구현하라.

identity 도메인을 참조하여 코딩 스타일을 유지하라.

## 7단계 : 테스트

identity 도메인을 참조하여 코딩 스타일을 유지하라.

각 계층별로 필요한 테스트 코드를 작성하라. 단위 테스트, 통합 테스트, 인수 테스트 등 다양한 유형의 테스트를 작성하라.