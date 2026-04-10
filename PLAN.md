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

# 당신이 해야 할 작업

현재 브랜치는 feature/learning임

즉 본격적으로 학습 관련 기능을 개발할 차례

stage, problem 및 그와 관련된 도메인/DB에 대해 구현되었음

이제 reward 도메인을 구현할 차이임

# 순서

- 매 단계에서 나에게 결과물을 보여준 다음 피드백을 받고, 내가 다음 단계로 넘어가라고 지시하면 그때 다음 단계로 넘어가라.

## 가장 먼저 해야 할 것

1. 지금까지 내가 구현한 프로젝트를 전부 읽고 구조와 나의 코딩 스타일을 파악하라. 현재 아키텍처는 클린 아키텍처를 따르고 있다. 또한 신기술 도입에는 보수적이되 확장 가능성을 열어두고 있고, 계층 간 의존성 방향이 위배될 것 같으면 이벤트 드리븐을 적극적으로 사용하고 있다.

2. ERD를 보고 reward 및 그와 관련된 도메인/DB에 대해 이해하라.

## 1단계 : 계획

앞에서 본 3가지를 바탕으로 어떻게 learning 도메인을 구현할지, 패키지 구조와 파일의 예상도는 어떻게 될지 설명하라. 그런 다음 내가 그걸 읽고 평가하고 지시할 것이다.

참고로 learning 도메인에서 stage. problem은 이미 대충 구현되었으니 reward 및 그와 연계된 부분을 추가하는 작업이 필요하다.

## 2단계 : API 구현

먼저 presentation 계층에서 controller와 DTO 그리고 presentation 계층에서 필요한 예외 클래스를 구현하라.

request, response DTO는 API 명세서를 참고하여 별개의 record class로 구현하라.

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