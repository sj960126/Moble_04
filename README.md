# Withpet
---
## 📝 개요 

 전 세계적으로 반려동물 관련 시장이 급격히 성장하고 있습니다. 국내 반려동물 보유 가구는 2017년 기준 28%를 넘어서며 전체 가구 중 3분 의 1 수준에 달했습니다. 하지만 반려동물의 통합적인 서비스가 부족하여 반려동물을 위한 통합 서비스 어플리케이션을제작하였습니다.
 
 ## 📱 담당한 기능 ( Android / AI ) - PL

- 친구 찾기 기능
- Android Python 소켓 통신을 통하여 프로필 변경 이미지 경로를 소켓 통신
- Calendarview를 통하여 날짜별로 Firebase에 데이터 저장 수정 삭제 기능 구현
- 기록 상세보기를 통하여 식사량을 차트 표현
- CNN Deep Learning을 통하여 견종 분류
- Deep Learning을 통하여 분류된 견종별 관심 이미지를 Recyclerview 구현
- 동물 병원 데이터 Data Parsing Tmap API 마커 표현후 클릭 리스너 구현
- 데이터 웹 크롤링
- 관리자 어플리케이션 고객센터 구현

## 💡 프로젝트를 통해 느낀 점

- 친구 찾기 기능 구현 시 **ViewModel**를 이용하여 EditText 내용이 변경되면 UI 갱신이 되도록 구현해 보면 좋을 것 같다.
- Json Parsing
- 내부 저장소를 이용하여 자동 로그인 기능
- **Firebase**
    - **Cloud Firestore**: Firestore에 세팅된 최신 데이터를 앱 DB에 저장
    - **Cloud Storage**: 앱 DB에 저앙된 이미지 URL을 통해 Storage의 이미지 호출
    - **Crashlytics**: 앱 이슈가 생기면 바로 대응하기 위해 세팅
- XML(동물 병원 데이터)를 Parsing하는 과정에서 서버와 클라이언트의 통신을 **Retrofit** 라이브러리를 사용하면 좋을 것 같다.
- Bottom Navigation을 **Google Material Design** 라이브러리를 사용하면 좋을 것 같다.
