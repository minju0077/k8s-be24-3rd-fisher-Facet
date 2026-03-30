# 💎 fisher
![로고](docs/logo.png)
> **"소유의 한계를 넘어, 가치를 나누는 경험"**
---
>**FACET**은 고가의 하이엔드 쥬얼리 자산을 **실시간 경매**로 획득하거나,
> 독창적인 1인 메이커들의 성장을 지원하는 **프리오더 펀딩**이 결합된 주얼리 플랫폼입니다.

### | **Figma Link** | [📑 FACET 프로젝트 기획안 바로가기](https://www.figma.com/board/g2avPc8dqkUD6l9QFFDGlL/%EA%B2%BD%EB%A7%A4-%ED%8E%80%EB%94%A9-%EC%A1%B0?node-id=0-1&p=f&t=SHtzMwX4IUWIm3jS-0) |
---
### | **서비스 배포 링크** | [🔗 FACET 바로가기](https://www.facet7.kro.kr) |

<br/>

---

## 👥 1. 팀원 소개

| 이름 | GitHub |
| :---: | :---: |
| **전민주** | [@minju0077](https://github.com/minju0077) |
| **이후경** | [@sarapoba](https://github.com/sarapoba) |
| **이지희** | [@dwg0245](https://github.com/dwg0245) |

---

## 💎  2. 서비스 소개



### 프로젝트 배경
기존 이커머스의 단순 구매 방식을 넘어, 가치 있는 하이엔드 주얼리를 획득하는 '실시간 경매'와 독창적인 1인 메이커의 성장을 응원하는 '프리오더 펀딩'을 결합했습니다. 소비자는 리스크 없는 예약 결제로 새로운 쇼핑 경험과 가격 혜택을 누리고, 판매자(메이커)는 재고 부담 없이 대량 판매와 안정적인 수익을 확보하는 상생 모델을 지향하는 플랫폼입니다.


✨ 3. 주요 기능
- **실시간 경매 시스템**: 고가의 하이엔드 주얼리 자산에 대한 실시간 입찰 및 자동 낙찰 처리
- **목표 달성형 프리오더 펀딩**: 선결제를 통해 펀딩에 참여한 후, 목표 인원 및 금액이 달성된 경우에만 지정된 날짜에 최종 결제가 승인되는 안전한 조건부 결제 시스템
- **통합 인증 및 소셜 로그인**: 회원 가입시 이메일 인증과, Google, Kakao OAuth 2.0을 활용한 간편 로그인
- **결제 및 포인트 시스템**: Portone을 연동한 결제 시스템 제공, 결제로 포인트로 충전에 경매 포인트 충전 시스템
---

🔗 **상세 내용 확인하기 (클릭하여 이미지 펼치기)**
<details>
<summary>📑 요구사항 명세서 이미지 보기</summary><br>

* [요구사항 명세서 PDF](./API정의서.pdf)

[Swagger 요구사항 명세서](https://api.facet7.kro.kr:442/swagger-ui/index.html)

<br>
</details>

---

## 🛠️ 4. 기술 스택

### 💻 Backend
<img src="https://img.shields.io/badge/Java 17-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/Spring Boot 3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"> <img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white"> <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white"> <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">

### ☁️ Infrastructure & External API
<img src="https://img.shields.io/badge/AWS EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white"> <img src="https://img.shields.io/badge/AWS S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white"> 
<img src="https://img.shields.io/badge/PortOne V2-1A1A1A?style=for-the-badge&logo=portone&logoColor=white"> <img src="https://img.shields.io/badge/Google OAuth 2.0-4285F4?style=for-the-badge&logo=google&logoColor=white"> <img src="https://img.shields.io/badge/Kakao OAuth 2.0-FFCD00?style=for-the-badge&logo=kakaotalk&logoColor=black">

> **💡 기술 선택 핵심 포인트**
> - **결제 무결성 확보 (PortOne V2)**: 클라이언트 결제 후 서버 간 통신(S2S)을 통한 3단계 교차 검증으로 결제 금액 위변조를 원천 차단했습니다.
> - **안정적인 동시성 제어 (Pessimistic Lock)**: 한정 수량 펀딩 및 경매 입찰 시 발생하는 데이터 정합성 문제를 비관적 락으로 해결했습니다.
> - **유연한 소셜 로그인 확장 (OAuth 2.0)**: Google, Kakao 등 상이한 규격의 유저 데이터를 단일 로직으로 통합 정규화하는 아키텍처를 구축했습니다.
> - **보안 강화**: API Secret Key 등 민감 데이터는 환경 변수로 철저히 분리하여 소스 코드 노출 위험을 제거했습니다.

---

## 🏛️️ 5. 시스템 아키텍처 (System Architecture)

> 트래픽을 안정적으로 처리하고, 실시간 경매의 동시성 문제를 해결하기 위해 아래와 같은 아키텍처로 설계되었습니다.

<div style="text-align: center;">
  <img src="./img/시스템 아키텍처.png" width="85%">
</div>

## 📋 6. 상세 기능 (Detailed Features)

FACET 서비스의 전체 기능 요약입니다. 핵심 비즈니스 로직인 **'조건부 예약 펀딩'**의 상세한 결제 프로세스와 기술적 고민(동시성 제어, 결제 무결성)은 아래 별도 페이지에서 자세히 확인하실 수 있습니다.


<details>
<summary><b> 📑 전체 상세 기능 목록 펼쳐보기 </b></summary>
<div markdown="1">

- **👤 회원 및 권한 (User)**
  - Google, Kakao OAuth 2.0 기반 소셜 로그인 및 JWT 발급
  - 마이페이지: 회원 정보 수정, 예약된 펀딩/경매 내역 조회, 포인트 적립 내역, 비밀번호 수정

- **🎁 프리오더 펀딩 (Funding)**
  - 카테고리별 펀딩 프로젝트 조회 및 검색
  - 리워드 선택 및 배송지, 요청사항 입력
  - 목표 달성 시 지정일에만 자동 결제되는 예약 시스템 적용

- **💎 실시간 경매 (Auction)**
  - 하이엔드 주얼리 실시간 입찰 및 남은 시간 카운트다운
  - 최고 입찰가 갱신 및 낙찰 자동 처리

- **💳 결제 및 정산 (Payment)**
  - PortOne V2 API를 연동한 간편결제 및 카드 등록
  - 펀딩 성공/실패, 경매 낙찰 여부에 따른 결제 승인 및 자동 취소 스케줄링

</div>
</details>








<details>
<summary><b>🔐 인증 및 보안</b></summary>
<div markdown="1">

  ### 회원가입 및 로그인
  - **회원가입**
  - **로그인**

  | 회원가입 | 로그인 |
  | :---: | :---: |
  | ![회원가입](./docs/회원가입.gif) | ![로그인](./docs/로그인.gif) |

</div>
</details>

<details>
<summary><b>💎 핵심 서비스</b></summary>
<div markdown="1">

  ### 실시간 경매 및 주얼리 펀딩
  - **경매**
  - **펀딩**

  | 경매 (Auction) | 펀딩 (Funding) |
  | :---: | :---: |
  | ![경매](./docs/경매.gif) | ![펀딩](./docs/펀딩.gif) |

</div>
</details>

<details>
<summary><b>🔍 검색 및 탐색 </b></summary>
<div markdown="1">

  ###  검색 시스템


  ![검색기능](./docs/검색기능.gif)

</div>
</details>

<details>
<summary><b>💰 포인트 시스템 </b></summary>
<div markdown="1">

  ### 활동 포인트 적립 및 관리

  ![포인트적립](./docs/포인트적립.gif)

</div>
</details>

---
