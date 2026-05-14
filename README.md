# GildedRose_01
Author   : 김경림  
Reviewer : 문정환,박길구,현경민,김재원 
Viewer   : 김대경 강사, 최혁성


## Test Enviroments
### OS : Windows 11
### Tech Stack : JDK 21,  Maven 3.9, Jacoco
### IDE  : VS Code (Version: 1.119.0)
### Test : mvn test / mvn verify / JUnit5

## 목적 :  테스트 코드 작성을 통한 legacy code 이해

##  개발 요구 사항
--------------------------------------------------------- 
    # Gilded Rose
    Gilded Rose 는 게임 “World of Warcraft”에 나오는 여관 이름입니다.  
    아이템은 퀄리티 속성을 가지고 있으며, 예외 사항이 있는 아이템들이 있습니다.  
    - 아이템은 [아이템 이름, 퀄리티, 유통기한]의 속성을 가집니다.  
    - 아이템의 퀄리티는 0 이상이고 아이템의 퀄리티는 하루가 지날 때마다 1씩 줄어듭니다.  
    - 유통 기한이 지난 아이템의 퀄리티는 2배의 속도로 떨어집니다.  
    - 퀄리티는 최대값이 50입니다.  

    ### Aged Brie, Backstage Pass, Sulfuras 의 예외적 규칙  

    - Aged Brie는 하루가 지날 때마다 퀄리티가 1씩 증가합니다.  
    - 유통기한이 지나면 퀄리티가 2씩 증가합니다.  
    - Backstage Pass 는 유통기한(콘서트일)이 다가올수록 퀄리티가 증가합니다.  
    유통기한이 11일 이상일 때는 1, 10일 이하일 때는 2, 5일 이하일 때는 3씩 증가하지만  
    콘서트 날이 지나면 퀄리티는 0이 됩니다.  
    - Sulfuras의 퀄리티는 변화가 없습니다. 

##  To-Do 
---------------------------------------------------------  
## 실습 목적 :  테스트 코드 작성을 통한 legacy code 이해

    ### GildedRose 의 Legacy 테스트 코드
    - GildedRoseTest : foo()      // 초기 수정하여 올림
    - TexttestFixture :  main()   // No Error but days  를 2 에서 5로 변경 test
    
    
    ### GildedRoseTest.java : failed test 수정
    - unit test 내용 : 문서상에 나타난 동작들 확인           // 단위 Test 별 연계성 Test --> 에러 발생   
    - updateQuality() 의  test case를 추가 작성해 보세요. 
    
    ### TexttestFixture.java  
    : non-automated golden-master test  
    => Automated test 로 변경(ApprovalTest 이용)          //  Automated ??????? ㅡ.ㅡa // ApprovalTest 일단 함.
    
    ### unit test VS golden-master test 비교
    
### To Do List
[] Legacy 테스트 문제점
    1. 가독성 최악    :  중첩 if 5개
    2. OCP 위반      : 새 아이템 추가시 전체 if 수정
    3. 타입 코드 남용 : 문자열 비교로 타입 분기
    4. 테스트 불가    : 로직이 뒤섞여 단위 테스트 불가


```
### Test CASE
####  일반 아이템(Test Cases)						                      
TC ID	아이템	초기 SellIn	초기 Quality	1일 후 SellIn	1일 후 Quality	설명
TC-01	Normal Item	10	20	9	19	일반 아이템은 하루에 Quality 1 감소
TC-02	Normal Item	0	20	-1	18	유통기한 지나면 2 감소
TC-03	Normal Item	5	0	4	0	Quality는 0 이하 불가
TC-04	Normal Item	-1	1	-2	0	유통기한 지난 상태에서도 0 이하 불가
                                       
#### Aged Brie(Test Cases)						               
TC ID	아이템	초기 SellIn	초기 Quality	1일 후 SellIn	1일 후 Quality	설명
TC-05	Aged Brie	10	20	9	21	하루에 1 증가
TC-06	Aged Brie	0	20	-1	22	유통기한 지나면 2 증가
TC-07	Aged Brie	5	50	4	50	최대값 50 유지
TC-08	Aged Brie	-1	49	-2	50	증가하더라도 최대 50
                                        
#### Backstage Pass(Test Cases)						                    
TC ID	아이템	초기 SellIn	초기 Quality	1일 후 SellIn	1일 후 Quality	설명
TC-09	Backstage Pass	15	20	14	21	11일 이상 남으면 +1
TC-10	Backstage Pass	10	20	9	22	10일 이하이면 +2
TC-11	Backstage Pass	5	20	4	23	5일 이하이면 +3
TC-12	Backstage Pass	1	20	0	23	콘서트 직전까지 증가
TC-13	Backstage Pass	0	20	-1	0	콘서트 지나면 0
TC-14	Backstage Pass	5	49	4	50	최대값 50 유지
TC-15	Backstage Pass	10	49	9	50	+2 증가여도 최대 50
TC-16	Backstage Pass	5	48	4	50	+3 증가여도 최대 50
                                            
#### Sulfuras(Test Cases)						                   
TC ID	아이템	초기 SellIn	초기 Quality	1일 후 SellIn	1일 후 Quality	설명
TC-17	Sulfuras	10	80	10	80	변화 없음
TC-18	Sulfuras	0	80	0	80	유통기한 지나도 변화 없음
TC-19	Sulfuras	-1	80	-1	80	항상 동일
                                        
#### 경계값(Boundary Value) 테스트						                   
TC ID	설명	입력	기대 결과			
TC-20	Quality 최소값 확인	Normal Item, Quality=0	감소하지 않음			
TC-21	Quality 최대값 확인	Aged Brie, Quality=50	증가하지 않음			
TC-22	Backstage Pass 최대값 초과 방지	Quality=49, +3 조건	결과는 50			
TC-23	유통기한 경계 10일	SellIn=10	+2 적용			
TC-24	유통기한 경계 5일	SellIn=5	+3 적용			
TC-25	유통기한 경계 0일	SellIn=0	다음날 0			
```
