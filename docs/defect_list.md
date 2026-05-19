# Gilded Rose Defect List

작성 관점: QA 리드  
대상 프로젝트: Gilded Rose Java 프로젝트  
기준 실행 명령: `mvn test`

## Summary

현재까지 재현된 테스트 실패 또는 확정 결함은 없다.

최신 테스트 실행 결과:

```text
Tests run: 28, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Defects

| ID | Severity | ItemType | Steps | Expected | Actual | Root Cause | Fix Summary |
| --- | --- | --- | --- | --- | --- | --- | --- |
| DEF-000 | Info | All | 1. 프로젝트 루트에서 `mvn test` 실행<br>2. `GildedRoseTest.java`의 전체 아이템 타입 테스트 결과 확인 | 전체 테스트가 실패 없이 통과해야 한다. | 총 28개 테스트가 모두 통과했다. | 실패 로그가 제공되지 않았고 현재 코드 기준 결함이 재현되지 않았다. | 코드 수정 없음. 현재 상태 유지. |

## Notes

- 현재 결함 수: 0건
- `Item` 클래스 수정 없음
- 실패 로그가 추가로 제공되면 위 형식에 맞춰 신규 결함 ID를 부여하고 Root Cause와 Fix Summary를 업데이트한다.
