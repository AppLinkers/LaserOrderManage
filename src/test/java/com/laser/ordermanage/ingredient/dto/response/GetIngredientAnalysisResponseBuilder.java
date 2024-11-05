package com.laser.ordermanage.ingredient.dto.response;

import com.laser.ordermanage.common.paging.ListResponse;

import java.time.LocalDate;
import java.util.List;

public class GetIngredientAnalysisResponseBuilder {
    // 전체 합계, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 보기, 수량
    // 전체 합계, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 선택, 수량
    public static GetIngredientAnalysisResponse build1() {
        GetIngredientAnalysisItemResponse analysisItem1 = new GetIngredientAnalysisItemResponse("incoming", List.of(2998,588));
        GetIngredientAnalysisItemResponse analysisItem2 = new GetIngredientAnalysisItemResponse("optimal", List.of(170,222));
        GetIngredientAnalysisItemResponse analysisItem3 = new GetIngredientAnalysisItemResponse("production", List.of(2399,780));
        GetIngredientAnalysisItemResponse analysisItem4 = new GetIngredientAnalysisItemResponse("stock", List.of(599,402));

        return GetIngredientAnalysisResponse.builder()
                .timeUnit("year")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2024, 1, 1))
                .itemList(new ListResponse<>(List.of(analysisItem1,analysisItem2, analysisItem3, analysisItem4)))
                .build();
    }

    // 전체 합계, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 보기, 무게
    // 전체 합계, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 선택, 무게
    public static GetIngredientAnalysisResponse build2() {
        GetIngredientAnalysisItemResponse analysisItem1 = new GetIngredientAnalysisItemResponse("incoming", List.of(216079.00,33242.00));
        GetIngredientAnalysisItemResponse analysisItem2 = new GetIngredientAnalysisItemResponse("optimal", List.of(12147.00,16228.50));
        GetIngredientAnalysisItemResponse analysisItem3 = new GetIngredientAnalysisItemResponse("production", List.of(158876.00,60509.50));
        GetIngredientAnalysisItemResponse analysisItem4 = new GetIngredientAnalysisItemResponse("stock", List.of(57203.00,29000.50));

        return GetIngredientAnalysisResponse.builder()
                .timeUnit("year")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2024, 1, 1))
                .itemList(new ListResponse<>(List.of(analysisItem1,analysisItem2, analysisItem3, analysisItem4)))
                .build();
    }

    // 전체 합계, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 단가 - 전체 보기
    // 전체 합계, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 단가 - 전체 선택
    public static GetIngredientAnalysisResponse build3() {
        GetIngredientAnalysisItemResponse analysisItem1 = new GetIngredientAnalysisItemResponse("sell", List.of(35000, 28800));
        GetIngredientAnalysisItemResponse analysisItem2 = new GetIngredientAnalysisItemResponse("purchase", List.of(33000, 27000));

        return GetIngredientAnalysisResponse.builder()
                .timeUnit("year")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2024, 1, 1))
                .itemList(new ListResponse<>(List.of(analysisItem1,analysisItem2)))
                .build();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    // 전체 합계, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 보기, 수량
    // 전체 합계, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 선택, 수량
    public static GetIngredientAnalysisResponse build4() {
        GetIngredientAnalysisItemResponse analysisItem1 = new GetIngredientAnalysisItemResponse("incoming", List.of(170,100,200,200,190,460,130,400,150,328,370,300));
        GetIngredientAnalysisItemResponse analysisItem2 = new GetIngredientAnalysisItemResponse("optimal", List.of(20,20,40,70,75,100,117,147,142,142,150,170));
        GetIngredientAnalysisItemResponse analysisItem3 = new GetIngredientAnalysisItemResponse("production", List.of(90,80,120,120,145,145,234,244,284,251,338,348));
        GetIngredientAnalysisItemResponse analysisItem4 = new GetIngredientAnalysisItemResponse("stock", List.of(80,100,180,260,305,620,516,672,538,615,647,599));

        return GetIngredientAnalysisResponse.builder()
                .timeUnit("month")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 1))
                .itemList(new ListResponse<>(List.of(analysisItem1,analysisItem2, analysisItem3, analysisItem4)))
                .build();
    }

    // 전체 합계, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 보기, 무게
    // 전체 합계, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 선택, 무게
    public static GetIngredientAnalysisResponse build5() {
        GetIngredientAnalysisItemResponse analysisItem1 = new GetIngredientAnalysisItemResponse("incoming", List.of(6999.00,3730.00,11670.00,7460.00,11203.00,26784.00,19410.00,38970.00,10645.00,19878.00,25030.00,34300.00));
        GetIngredientAnalysisItemResponse analysisItem2 = new GetIngredientAnalysisItemResponse("optimal", List.of(840.00,840.00,2007.00,3126.00,3592.50,5441.00,8130.00,11560.00,10625.00,10625.00,10980.00,12147.00));
        GetIngredientAnalysisItemResponse analysisItem3 = new GetIngredientAnalysisItemResponse("production", List.of(3639.00,3266.00,5506.00,5506.00,6718.50,8241.50,16260.00,20538.00,21250.00,19926.00,23732.00,24293.00));
        GetIngredientAnalysisItemResponse analysisItem4 = new GetIngredientAnalysisItemResponse("stock", List.of(3360.00,3824.00,9988.00,11942.00,16426.50,34969.00,38119.00,56551.00,45946.00,45898.00,47196.00,57203.00));

        return GetIngredientAnalysisResponse.builder()
                .timeUnit("month")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 1))
                .itemList(new ListResponse<>(List.of(analysisItem1,analysisItem2, analysisItem3, analysisItem4)))
                .build();
    }


    // 전체 합계, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 단가 - 전체 보기
    // 전체 합계, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 단가 - 전체 선택
    public static GetIngredientAnalysisResponse build6() {
        GetIngredientAnalysisItemResponse analysisItem1 = new GetIngredientAnalysisItemResponse("sell", List.of(3000,2700,5100,5400,8800,14500,25300,33500,33700,33800,34200,35000));
        GetIngredientAnalysisItemResponse analysisItem2 = new GetIngredientAnalysisItemResponse("purchase", List.of(2600,2400,4400,4600,7600,12800,22800,30500,31000,31100,31600,33000));

        return GetIngredientAnalysisResponse.builder()
                .timeUnit("month")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 1))
                .itemList(new ListResponse<>(List.of(analysisItem1,analysisItem2)))
                .build();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    // 평균, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 보기, 수량
    // 평균, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 선택, 수량
    public static GetIngredientAnalysisResponse build7() {
        GetIngredientAnalysisItemResponse analysisItem1 = new GetIngredientAnalysisItemResponse("incoming", List.of(299.80,65.33));
        GetIngredientAnalysisItemResponse analysisItem2 = new GetIngredientAnalysisItemResponse("optimal", List.of(17.0,24.67));
        GetIngredientAnalysisItemResponse analysisItem3 = new GetIngredientAnalysisItemResponse("production", List.of(239.90,86.67));
        GetIngredientAnalysisItemResponse analysisItem4 = new GetIngredientAnalysisItemResponse("stock", List.of(59.9,44.67));

        return GetIngredientAnalysisResponse.builder()
                .timeUnit("year")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2024, 1, 1))
                .itemList(new ListResponse<>(List.of(analysisItem1,analysisItem2, analysisItem3, analysisItem4)))
                .build();
    }

    // 평균, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 보기, 무게
    // 평균, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 선택, 무게
    public static GetIngredientAnalysisResponse build8() {
        GetIngredientAnalysisItemResponse analysisItem1 = new GetIngredientAnalysisItemResponse("incoming", List.of(21607.90,3693.56));
        GetIngredientAnalysisItemResponse analysisItem2 = new GetIngredientAnalysisItemResponse("optimal", List.of(1214.70,1803.17));
        GetIngredientAnalysisItemResponse analysisItem3 = new GetIngredientAnalysisItemResponse("production", List.of(15887.60,6723.28));
        GetIngredientAnalysisItemResponse analysisItem4 = new GetIngredientAnalysisItemResponse("stock", List.of(5720.30,3222.28));

        return GetIngredientAnalysisResponse.builder()
                .timeUnit("year")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2024, 1, 1))
                .itemList(new ListResponse<>(List.of(analysisItem1,analysisItem2, analysisItem3, analysisItem4)))
                .build();
    }

    // 평균, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 단가 - 전체 보기
    // 평균, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 단가 - 전체 선택
    public static GetIngredientAnalysisResponse build9() {
        GetIngredientAnalysisItemResponse analysisItem1 = new GetIngredientAnalysisItemResponse("sell", List.of(3500.0,3200.0));
        GetIngredientAnalysisItemResponse analysisItem2 = new GetIngredientAnalysisItemResponse("purchase", List.of(3300.0,3000.0));

        return GetIngredientAnalysisResponse.builder()
                .timeUnit("year")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2024, 1, 1))
                .itemList(new ListResponse<>(List.of(analysisItem1,analysisItem2)))
                .build();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    // 평균, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 보기, 수량
    // 평균, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 선택, 수량
    public static GetIngredientAnalysisResponse build10() {
        GetIngredientAnalysisItemResponse analysisItem1 = new GetIngredientAnalysisItemResponse("incoming", List.of(85.00,50.00,66.67,66.67,47.50,76.67,16.25,40.00,15.00,32.80,37.00,30.00));
        GetIngredientAnalysisItemResponse analysisItem2 = new GetIngredientAnalysisItemResponse("optimal", List.of(10.0,10.0,13.33,23.33,18.75,16.67,14.63,14.7,14.2,14.2,15.0,17.0));
        GetIngredientAnalysisItemResponse analysisItem3 = new GetIngredientAnalysisItemResponse("production", List.of(45.00,40.00,40.00,40.00,36.25,24.17,29.25,24.40,28.40,25.10,33.80,34.80));
        GetIngredientAnalysisItemResponse analysisItem4 = new GetIngredientAnalysisItemResponse("stock", List.of(40.0,50.0,60.0,86.67,76.25,103.33,64.5,67.2,53.8,61.5,64.7,59.9));

        return GetIngredientAnalysisResponse.builder()
                .timeUnit("month")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 1))
                .itemList(new ListResponse<>(List.of(analysisItem1,analysisItem2, analysisItem3, analysisItem4)))
                .build();
    }

    // 평균, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 보기, 무게
    // 평균, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 선택, 무게
    public static GetIngredientAnalysisResponse build11() {
        GetIngredientAnalysisItemResponse analysisItem1 = new GetIngredientAnalysisItemResponse("incoming", List.of(3499.50,1865.00,3890.00,2486.67,2800.75,4464.00,2426.25,3897.00,1064.50,1987.80,2503.00,3430.00));
        GetIngredientAnalysisItemResponse analysisItem2 = new GetIngredientAnalysisItemResponse("optimal", List.of(420.00,420.00,669.00,1042.00,898.13,906.83,1016.25,1156.00,1062.50,1062.50,1098.00,1214.70));
        GetIngredientAnalysisItemResponse analysisItem3 = new GetIngredientAnalysisItemResponse("production", List.of(1819.50,1633.00,1835.33,1835.33,1679.63,1373.58,2032.50,2053.80,2125.00,1992.60,2373.20,2429.30));
        GetIngredientAnalysisItemResponse analysisItem4 = new GetIngredientAnalysisItemResponse("stock", List.of(1680.00,1912.00,3329.33,3980.67,4106.63,5828.17,4764.88,5655.10,4594.60,4589.80,4719.60,5720.30));

        return GetIngredientAnalysisResponse.builder()
                .timeUnit("month")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 1))
                .itemList(new ListResponse<>(List.of(analysisItem1,analysisItem2, analysisItem3, analysisItem4)))
                .build();
    }

    // 평균, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 단가 - 전체 보기
    // 평균, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 단가 - 전체 선택
    public static GetIngredientAnalysisResponse build12() {
        GetIngredientAnalysisItemResponse analysisItem1 = new GetIngredientAnalysisItemResponse("sell", List.of(1500.0,1350.0,1700.0,1800.0,2200.0,2416.67,3162.5,3350.0,3370.0,3380.0,3420.0,3500.0));
        GetIngredientAnalysisItemResponse analysisItem2 = new GetIngredientAnalysisItemResponse("purchase", List.of(1300.0,1200.0,1466.67,1533.33,1900.0,2133.33,2850.0,3050.0,3100.0,3110.0,3160.0,3300.0));

        return GetIngredientAnalysisResponse.builder()
                .timeUnit("month")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 1))
                .itemList(new ListResponse<>(List.of(analysisItem1,analysisItem2)))
                .build();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    // 자재 선택 (id = 1), 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 보기, 수량
    // 자재 선택 (id = 1), 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 선택, 수량
    public static GetIngredientAnalysisResponse build13() {
        GetIngredientAnalysisItemResponse analysisItem1 = new GetIngredientAnalysisItemResponse("incoming", List.of(800,200));
        GetIngredientAnalysisItemResponse analysisItem2 = new GetIngredientAnalysisItemResponse("optimal", List.of(40,60));
        GetIngredientAnalysisItemResponse analysisItem3 = new GetIngredientAnalysisItemResponse("production", List.of(780,150));
        GetIngredientAnalysisItemResponse analysisItem4 = new GetIngredientAnalysisItemResponse("stock", List.of(20,70));

        return GetIngredientAnalysisResponse.builder()
                .timeUnit("year")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2024, 1, 1))
                .itemList(new ListResponse<>(List.of(analysisItem1,analysisItem2, analysisItem3, analysisItem4)))
                .build();
    }

    // 자재 선택 (id = 1), 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 보기, 무게
    // 자재 선택 (id = 1), 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 선택, 무게
    public static GetIngredientAnalysisResponse build14() {
        GetIngredientAnalysisItemResponse analysisItem1 = new GetIngredientAnalysisItemResponse("incoming", List.of(29840.00,7460.00));
        GetIngredientAnalysisItemResponse analysisItem2 = new GetIngredientAnalysisItemResponse("optimal", List.of(1492.00,2238.00));
        GetIngredientAnalysisItemResponse analysisItem3 = new GetIngredientAnalysisItemResponse("production", List.of(29094.00,5595.00));
        GetIngredientAnalysisItemResponse analysisItem4 = new GetIngredientAnalysisItemResponse("stock", List.of(746.00,2611.00));

        return GetIngredientAnalysisResponse.builder()
                .timeUnit("year")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2024, 1, 1))
                .itemList(new ListResponse<>(List.of(analysisItem1,analysisItem2, analysisItem3, analysisItem4)))
                .build();
    }

    // 자재 선택 (id = 1), 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 단가 - 전체 보기
    // 자재 선택 (id = 1), 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 단가 - 전체 선택
    public static GetIngredientAnalysisResponse build15() {
        GetIngredientAnalysisItemResponse analysisItem1 = new GetIngredientAnalysisItemResponse("sell", List.of(1000,1000));
        GetIngredientAnalysisItemResponse analysisItem2 = new GetIngredientAnalysisItemResponse("purchase", List.of(900,900));

        return GetIngredientAnalysisResponse.builder()
                .timeUnit("year")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2024, 1, 1))
                .itemList(new ListResponse<>(List.of(analysisItem1,analysisItem2)))
                .build();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    // 자재 선택 (id = 1), 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 보기, 수량
    // 자재 선택 (id = 1), 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 선택, 수량
    public static GetIngredientAnalysisResponse build16() {
        GetIngredientAnalysisItemResponse analysisItem1 = new GetIngredientAnalysisItemResponse("incoming", List.of(100,100,0,200,0,200,0,0,0,200,0,0));
        GetIngredientAnalysisItemResponse analysisItem2 = new GetIngredientAnalysisItemResponse("optimal", List.of(10,10,10,40,40,40,40,40,40,40,40,40));
        GetIngredientAnalysisItemResponse analysisItem3 = new GetIngredientAnalysisItemResponse("production", List.of(60,50,60,60,80,40,80,40,80,40,100,90));
        GetIngredientAnalysisItemResponse analysisItem4 = new GetIngredientAnalysisItemResponse("stock", List.of(40,90,30,170,90,250,170,130,50,210,110,20));

        return GetIngredientAnalysisResponse.builder()
                .timeUnit("month")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 1))
                .itemList(new ListResponse<>(List.of(analysisItem1,analysisItem2, analysisItem3, analysisItem4)))
                .build();
    }

    // 자재 선택 (id = 1), 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 보기, 무게
    // 자재 선택 (id = 1), 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 선택, 무게
    public static GetIngredientAnalysisResponse build17() {
        GetIngredientAnalysisItemResponse analysisItem1 = new GetIngredientAnalysisItemResponse("incoming", List.of(3730.00,3730.00,0.00,7460.00,0.00,7460.00,0.00,0.00,0.00,7460.00,0.00,0.00));
        GetIngredientAnalysisItemResponse analysisItem2 = new GetIngredientAnalysisItemResponse("optimal", List.of(373.00,373.00,373.00,1492.00,1492.00,1492.00,1492.00,1492.00,1492.00,1492.00,1492.00,1492.00));
        GetIngredientAnalysisItemResponse analysisItem3 = new GetIngredientAnalysisItemResponse("production", List.of(2238.00,1865.00,2238.00,2238.00,2984.00,1492.00,2984.00,1492.00,2984.00,1492.00,3730.00,3357.00));
        GetIngredientAnalysisItemResponse analysisItem4 = new GetIngredientAnalysisItemResponse("stock", List.of(1492.00,3357.00,1119.00,6341.00,3357.00,9325.00,6341.00,4849.00,1865.00,7833.00,4103.00,746.00));

        return GetIngredientAnalysisResponse.builder()
                .timeUnit("month")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 1))
                .itemList(new ListResponse<>(List.of(analysisItem1,analysisItem2, analysisItem3, analysisItem4)))
                .build();
    }

    // 자재 선택 (id = 1), 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 단가 - 전체 보기
    // 자재 선택 (id = 1), 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 단가 - 전체 선택
    public static GetIngredientAnalysisResponse build18() {
        GetIngredientAnalysisItemResponse analysisItem1 = new GetIngredientAnalysisItemResponse("sell", List.of(1200,900,900,1200,1200,1200,1200,1200,1000,1000,1000,1000));
        GetIngredientAnalysisItemResponse analysisItem2 = new GetIngredientAnalysisItemResponse("purchase", List.of(1000,800,800,1000,1000,1000,1000,1000,900,900,900,900));

        return GetIngredientAnalysisResponse.builder()
                .timeUnit("month")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 1))
                .itemList(new ListResponse<>(List.of(analysisItem1,analysisItem2)))
                .build();
    }
}
