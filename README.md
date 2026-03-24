# 💎 fisher
![로고](docs/Facet_Logo.png)
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

## 📖 2. 프로젝트 개요

- **프로젝트 개요**: 기존 이커머스의 직관적인 UX에 '공동구매' 방식을 결합하여, 특정 목표 인원이 모이면 자동으로 할인된 가격에 구매할 수 있는 플랫폼입니다.
- **기대 효과**: 소비자는 복잡한 절차 없이 가격 혜택을 누리고, 판매자는 대량 판매를 통한 재고 소진과 안정적인 수익 구조를 확보하는 상생 모델을 지향합니다.
- **핵심 프로세스**: 사용자는 별도의 리스크 없이 구매 예약을 진행하며, 목표 인원 달성 시에만 자동 결제가 이루어지고 미달 시에는 자동으로 취소되는 시스템입니다.

---

🔗 **상세 내용 확인하기 (클릭하여 이미지 펼치기)**
<details>
<summary>📑 요구사항 명세서 이미지 보기</summary>
<br>
<img src="./docs/요구사항명세서.png" alt="요구사항 명세서" width="100%">
<br>
</details>

---

## 🛠️ 3. 기술 스택 (Tech Stack)

### 💻 Backend
<img src="https://img.shields.io/badge/Java 17-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/Spring Boot 3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"> <img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white"> <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white"> <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">

### ☁️ Infrastructure & External API
<img src="https://img.shields.io/badge/AWS EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white"> <img src="https://img.shields.io/badge/AWS S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white"> <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"> <img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white">
<img src="https://img.shields.io/badge/PortOne V2-1A1A1A?style=for-the-badge&logo=portone&logoColor=white"> <img src="https://img.shields.io/badge/Google OAuth 2.0-4285F4?style=for-the-badge&logo=google&logoColor=white">

> **💡 기술 선택 핵심 포인트**
> - **결제 무결성 확보 (PortOne V2)**: 클라이언트 결제 후 서버 간 통신(S2S)을 통한 3단계 교차 검증으로 결제 금액 위변조를 원천 차단했습니다.
> - **안정적인 동시성 제어 (Pessimistic Lock)**: 한정 수량 펀딩 및 경매 입찰 시 발생하는 데이터 정합성 문제를 비관적 락으로 해결했습니다.
> - **유연한 소셜 로그인 확장 (OAuth 2.0)**: Google, Kakao 등 상이한 규격의 유저 데이터를 단일 로직으로 통합 정규화하는 아키텍처를 구축했습니다.
> - **보안 강화**: API Secret Key 등 민감 데이터는 환경 변수로 철저히 분리하여 소스 코드 노출 위험을 제거했습니다.

---

## 🏛️️ 4. 시스템 아키텍처 (System Architecture)

> 트래픽을 안정적으로 처리하고, 실시간 경매의 동시성 문제를 해결하기 위해 아래와 같은 아키텍처로 설계되었습니다.

<div style="text-align: center;">
  <img src="./img/시스템 아키텍처.png" width="85%">
</div>


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
