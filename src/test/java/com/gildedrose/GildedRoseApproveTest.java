package com.gildedrose;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

public class GildedRoseApproveTest {


/***  기능 JUnit 5 (Java)
-----------------------------------------------------
 * 테스트 정의      @Test void testName()
 * 값 동등         assertEquals(expected, actual) 
 * 조건 참         assertTrue(condition) 
 * 예외 발생       assertThrows(Type.class, () -> ...) 
 * 사전 실행        @BeforeEach void setUp() 
 * 사후 실행        @AfterEach void tearDown() 
 * 매개변수 반복     @ParameterizedTest @ValueSource 
 * 모킹            Mockito.mock(MyClass.class) 
 * 커버리지         JaCoCo (mvn verify
-----------------------------------------------------
*/

    // 30일 시뮬레이션
    public String simulate30Days(Item[] startItems) {

        GildedRose gr = new GildedRose(startItems);
        StringBuilder sb = new StringBuilder();

        for (int d = 0; d <= 30; d++) {
            sb.append("--- day ").append(d).append(" ---\n");

            for (Item i : gr.item)
                sb.append(i.name).append(", ").append(i.sellIn).append(", ").append(i.quality).append("\n");
                
            if (d < 30) gr.updateQuality();
             sb.append("\n");
        }

        return sb.toString();
    }
        
    // 승인 테스트 (한 줄!)
    @Test
    public void thirtyDaySimulation() {
        Item[] items = {
            new Item("+5 Dex Vest", 10, 20),
            new Item("Aged Brie", 2, 0),
            new Item("Sulfuras", 0, 80),
        // ...
        };

        Approvals.verify(simulate30Days(items));
        // ← .approved.txt와 자동 비교
        }

}