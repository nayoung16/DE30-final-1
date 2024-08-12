# 🏕️ Campers: 캠핑 스타일 및 캠핑장 추천 서비스 
## 프로젝트 기간
|날짜|내용|
|---|---|
|2024.06.21 ~ 2024.06.28|프로젝트 계획 및 준비|
|2024.06.24 ~ 2024.07.03|백엔드 설계 및 구현|
|2024.06.24 ~ 2024.07.22|데이터 크롤링, 데이터 처리, 모델 설계 및 구현|
|2024.07.10 ~ 2024.07.31|Airflow ETL 프로세스 설계 및 구현, MWAA 환경 구성|
|2024.08.01 ~ 2024.08.08|Lambda 함수 작성, 모델 배포 및 API 연결|
|2024.07.22 ~ 2024.08.12|Springboot 배포 및 운영, React 개발 및 배포|

## 프로젝트 구성원
|구성원|깃허브 주소|역할|
|------|---|---|
|이수형|https://github.com/kisoo4362|데이터 엔지니어링, 데이터 수집, 프론트|
|김나영|https://github.com/nayoung16|백엔드, 프론트, 데이터 엔지니어링|
|백승민|https://github.com/wkflzn|ML, 프론트|
|이석희|https://github.com/seokkies|데이터 수집, 백엔드, 프론트|

## 서비스 개요
서비스 url : http://43.203.225.206:8000/
<img width="1854" alt="스크린샷 2024-08-09 오후 2 14 22" src="https://github.com/user-attachments/assets/9f37f96a-4c66-47b3-adcd-1982c36e33ec"><br/>
- 사용자들이 자신의 캠핑 스타일에 맞는 캠핑장을 쉽게 찾고, 캠핑 계획을 보다 체계적으로 관리할 수 있도록 돕는 것을 목적으로 합니다.
- 사용자 개개인의 취향과 라이프스타일에 맞춘 캠핑장 추천을 통해 만족도 높은 캠핑 경험을 제공하며, 계획을 캘린더에 추가함으로써 더욱 효율적이고 즐거운 캠핑을 지원합니다.
## 서비스 기능
![초록색 회색 심플한 마케팅 프로젝트 프레젠테이션 (1)](https://github.com/user-attachments/assets/00e3d6d2-2619-4024-9e26-e9ee8fe42f15)

## 사용 기술
![Campers](https://github.com/user-attachments/assets/67cae742-ff6f-4dd7-8966-361fec894b28)

- Language : Python, Java, Javascript, SQL <br/>
- Frontend : React.js, Node.js <br/>
- Backend : SpringBoot, JPA, Spring Security <br/>
- Data Engineering : Apahce Airflow, AWS Lambda <br/>
- Infra : Docker, MWAA <br/>
- CI/CD : Github Actions, AWS Codedeploy <br/>
- Storage : MySQL, RDS, S3 <br/>
- Data : Pandas, Numpy, Selenium, Beautifulsoup <br/>
- ML : Scikit-learn, Tensorflow, KoNLPy
## 사용 데이터
![초록색 회색 심플한 마케팅 프로젝트 프레젠테이션](https://github.com/user-attachments/assets/9be4ca76-17bf-42a7-87b5-181e462c9580)

## ERD
![스크린샷 2024-08-09 오후 1 08 49](https://github.com/user-attachments/assets/22c3d5ff-49ab-4203-bef2-fbf71a15e170)

## 전체 아키텍처
<img width="891" alt="스크린샷 2024-08-09 오후 1 01 48" src="https://github.com/user-attachments/assets/e5168416-5602-41b9-9d2a-0770b0fe876a"><br/>
- 에어플로우를 통해 데이터를 수집하고, S3에 데이터를 저장하고 이후 MySQL에 적재
- Lambda를 사용하여 모델 결과를 Springboot에서 받아와 DB, React로 전송하도록 설계
- React와 Springboot는 같은 EC2 서버 내에 배포
## 데이터 수집
### 리뷰 데이터 크롤링
고캠핑 데이터의 캠핑장 주소를 기준으로 네이버 플레이스 리뷰 크롤링 진행<br/>
Beautifulsoup, Selenium 사용
### Airflow ETL 파이프라인
<img width="704" alt="스크린샷 2024-08-09 오후 1 04 17" src="https://github.com/user-attachments/assets/f289d7b7-5081-4f11-8b99-81a320262ef1"><br/>
#### 고캠핑 데이터
- 일주일 간격으로 고캠핑 데이터(캠핑장 정보)를 갱신
- 받아온 데이터를 캠핑 기본정보, 기타정보 테이블로 나누어 적재
#### 단기예보 데이터
- 단기예보와 중기예보의 행정구역의 범위가 일치하지않아, 구역을 똑같이 맞춰줘야하는 과정 진행
- 행정구역 범위를 확대시켜, 각 도의 주요 도시를 기준으로 하여 행정구역을 지정
- 데이터 갱신은 하루마다 진행하며, 갱신 방법은 기존 데이터를 전부 제거하고, 새로 갱신된 데이터만 저장
#### 중기예보 데이터
- 중기육상예보조회에서 3~10일 데이터 중 7일까지의 날씨 예보 데이터를 선정
- 중기기온조회 3~10일 데이터 중 7일까지의 예상 최고기온, 최저기온 데이터를 선정하여 사용
- 데이터 형식을 일별로 row 형태로 저장하였으며, 갱신은 오늘 기준 3일~7일 이후의 데이터만을 받아 데이터의 무분별한 증가를 방지
## Airflow on Docker vs MWAA
![Campers (1)](https://github.com/user-attachments/assets/cdc3b7f9-2113-4b96-930b-bdcf87dfac04)
MWAA에서의 크레딧 문제로 이후에 로컬에서 진행
## ML 모델링
### 캠핑 스타일 추천 모델
- 사용 모델 : Logistic Regression
- 캠핑 스타일별로 정답지를 생성해두고, 질문에 대한 사용자 답변을 받고 그 값을 숫자로 변환
- 스타일 별 가중치와 비교하여 지정해 둔 스타일별 값과 가장 근접한 캠핑 스타일을 추천
### 캠핑장 추천 모델
- 캠핑 스타일 + 환경, 활동, 지역 선택을 통해 캠핑장 추천
- 토큰화하여 형태소 분석을 진행하여 네가지 컴포넌트와 캠핑장 정보와의 유사도로 가중치를 부여
- 추천받을때마다 다양한 캠핑장이 나올수 있게 하기 위해 상위 10개 캠핑장 중 랜덤으로 5개 추출

### 자체 평점 모델
![Campers (3)](https://github.com/user-attachments/assets/f126129b-3ee0-4c1e-abd1-c2ab2319d7cc)
- 크롤링한 네이버 리뷰 데이터를 불용어 처리 및 토큰화를 통해 리뷰에 대한 텍스트 분석을 진행한 후 분석한 내용을 기반으로 긍부정도를 나누어 자체 별점을 수치화
## 캠핑 스타일, 캠핑장 추천 모델 학습 파이프라인
<img width="818" alt="스크린샷 2024-08-09 오후 1 05 20" src="https://github.com/user-attachments/assets/b6470343-2b71-4652-a010-03032fda7b70"><br/>
### 설명
1) 사용자가 키워드를 선택하면, 리액트에서 스프링부트로 키워드들이 전달되고 rds에 저장
2) 동시에 람다함수가 트리거되어 갱신된 고캠핑 데이터를 rds에서 읽어오고, 미리 학습된 스타일 추천 모델 pkl 파일을 s3에서 읽어옴
3) 스타일 추천 모델을 통해 캠핑 스타일이 먼저 예측되고, 이 스타일과 사용자 선택 키워드들을 통해 캠핑장들이 추천되어 다시 스프링부트를 통해 리액트로 전달되는 구조

- 초기에 fastapi로 ML 서버를 구축하고 새로운 EC2에 올리려고 계획하였음
- 서버리스 환경으로 구현하므로써 RDS, S3 등의 AWS 서비스들과 더욱 좋은 접근성을 가지고, 서버 비용을 절약할 수 있었음
- 람다함수를 트리거하기 위해 API Gateway를 사용하여 백엔드와 연결함
### ML 학습 환경 구성
![Campers (2)](https://github.com/user-attachments/assets/7df79af9-3b9d-4f48-93bb-f4dda957bb28)
- ML 학습 환경을 구성하기 위해 람다 레이어를 사용
- 람다에서 레이어에 사이킷런, 텐서플로 등의 모듈을 올리기 위한 레지스트리 서비스가 필요해 ECR을 사용
## 백엔드 CI/CD 배포 파이프라인
![cicd](https://github.com/user-attachments/assets/f83c84a0-4366-40d8-9d9b-0bded116fe64)
- main 브랜치에 push가 생기면, github actions에서 작성한 배포 스크립트에 따라 프로젝트를 빌드하고, 빌드된 프로젝트를 s3에 저장합니다.
- Codedeploy에 빌드된 프로젝트 배포 명령을 내리고, Codedeploy는 도커 이미지를 빌드하여 EC2 서버에 배포합니다.
- Github secrets에서 aws iam 사용자 키를 관리하고, application.properties 파일은 base64로 인코딩

## 시연 영상

