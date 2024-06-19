## Commit Convention

================= 클론 뒤 띄어쓰기 + 맨 뒤 -#[이슈번호] 필수! ================

    Feat:             새로운 기능을 추가
    Update:           기능 수정
    Fix:              버그 수정
    !BREAKING CHANGE: 커다란 API 변경의 경우
    !HOTFIX:          급하게 치명적인 버그를 고쳐야하는 경우
    Style:            CSS 및 UI, 코드 포맷 변경, 세미 콜론 누락, 코드 수정이 없는 경우
    Refactor:         코드 리팩토링 (기능 변경 X, 코드 가독성, 구조, 품질 개선의 경우)
    Comment:          필요한 주석 추가 및 변경
    Chore:            빌드 업무 수정, 패키지 매니저 수정
    Docs:             문서 수정
    Test:             빌드 업무 수정, 패키지 매니저 수정, 패키지 관리자 구성 등 업데이트, Production Code 변경 없음
    Rename:           파일 혹은 폴더명을 수정하거나 옮기는 작업만인 경우
    Remove:           파일을 삭제하는 작업만 수행한 경우
  
===========================================================================

## Error Code (4자리 숫자)

### - 첫번째 자리: 도메인

- 0: Auth / Token
- 1: User
- 2: Team
- 3: Personal Matching
- 4: Team Matching
- 5: Personal Match Join
- 6: Team Match Join
- 7: UserTeam
- 8: Recruitment
- 9: region
- 10: global

### - 두번째 자리: 에러 종류

- 0: 도메인 / Dto 객체 생성 오류 (MethodArgumentValidException, ConstraintViolationException 등)
- 1: 도메인 로직 상의 오류
- 2: 인증, 인가가 안된 오류
- 3: 존재하지 않는 리소스에 대한 접근 오류
- 4: 외부 API 관련 오류

### - 나머지 두자리: 순서 부여

- 00 ~ 99 까지 순서대로 부여
