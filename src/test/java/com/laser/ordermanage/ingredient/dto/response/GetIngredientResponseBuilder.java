package com.laser.ordermanage.ingredient.dto.response;

import java.util.List;

public class GetIngredientResponseBuilder {

    public static List<GetIngredientResponse> buildList() {
        GetIngredientStockDetailResponse stockCountOfIngredient1 = new GetIngredientStockDetailResponse(70, 0, 0, 70, 60);
        GetIngredientStockDetailResponse stockWeightOfIngredient1 = new GetIngredientStockDetailResponse(2611.0, 0.0, 0.0, 2611.0, 2238.0);
        GetIngredientPriceResponse priceOfIngredient1 = new GetIngredientPriceResponse(900, 1000);
        GetIngredientResponse ingredient1 = new GetIngredientResponse(1L, "SS 400", 1.6, 4, 8, 37.3, stockCountOfIngredient1, stockWeightOfIngredient1, priceOfIngredient1, Boolean.FALSE);

        GetIngredientStockDetailResponse stockCountOfIngredient2 = new GetIngredientStockDetailResponse(50, 0, 0, 50, 30);
        GetIngredientStockDetailResponse stockWeightOfIngredient2 = new GetIngredientStockDetailResponse(2335.0, 0.0, 0.0, 2335.0, 1401.0);
        GetIngredientPriceResponse priceOfIngredient2 = new GetIngredientPriceResponse(1200, 1300);
        GetIngredientResponse ingredient2 = new GetIngredientResponse(2L, "SS 400", 2.0, 4, 8, 46.7, stockCountOfIngredient2, stockWeightOfIngredient2, priceOfIngredient2, Boolean.FALSE);

        GetIngredientStockDetailResponse stockCountOfIngredient3 = new GetIngredientStockDetailResponse(80, 0, 0, 80, 30);
        GetIngredientStockDetailResponse stockWeightOfIngredient3 = new GetIngredientStockDetailResponse(5600.0, 0.0, 0.0, 5600.0, 2100.0);
        GetIngredientPriceResponse priceOfIngredient3 = new GetIngredientPriceResponse(2600, 2800);
        GetIngredientResponse ingredient3 = new GetIngredientResponse(3L, "SS 400", 3.0, 4, 8, 70.0, stockCountOfIngredient3, stockWeightOfIngredient3, priceOfIngredient3, Boolean.FALSE);

        GetIngredientStockDetailResponse stockCountOfIngredient4 = new GetIngredientStockDetailResponse(20, 0, 0, 20, 10);
        GetIngredientStockDetailResponse stockWeightOfIngredient4 = new GetIngredientStockDetailResponse(1866.0, 0.0, 0.0, 1866.0, 933.0);
        GetIngredientPriceResponse priceOfIngredient4 = new GetIngredientPriceResponse(3400, 3600);
        GetIngredientResponse ingredient4 = new GetIngredientResponse(4L, "SS 400", 4.0, 4, 8, 93.3, stockCountOfIngredient4, stockWeightOfIngredient4, priceOfIngredient4, Boolean.FALSE);

        GetIngredientStockDetailResponse stockCountOfIngredient5 = new GetIngredientStockDetailResponse(7, 0, 0, 7, 7);
        GetIngredientStockDetailResponse stockWeightOfIngredient5 = new GetIngredientStockDetailResponse(819.0, 0.0, 0.0, 819.0, 819.0);
        GetIngredientPriceResponse priceOfIngredient5 = new GetIngredientPriceResponse(4600, 4800);
        GetIngredientResponse ingredient5 = new GetIngredientResponse(5L, "SS 400", 5.0, 4, 8, 117.0, stockCountOfIngredient5, stockWeightOfIngredient5, priceOfIngredient5, Boolean.FALSE);

        GetIngredientStockDetailResponse stockCountOfIngredient6 = new GetIngredientStockDetailResponse(90, 0, 0, 90, 20);
        GetIngredientStockDetailResponse stockWeightOfIngredient6 = new GetIngredientStockDetailResponse(6561.0, 0.0, 0.0, 6561.0, 1458.0);
        GetIngredientPriceResponse priceOfIngredient6 = new GetIngredientPriceResponse(2800, 3000);
        GetIngredientResponse ingredient6 = new GetIngredientResponse(7L, "ATOS", 3.2, 5, 10, 72.9, stockCountOfIngredient6, stockWeightOfIngredient6, priceOfIngredient6, Boolean.FALSE);

        GetIngredientStockDetailResponse stockCountOfIngredient7 = new GetIngredientStockDetailResponse(15, 0, 0, 15, 5);
        GetIngredientStockDetailResponse stockWeightOfIngredient7 = new GetIngredientStockDetailResponse(1258.5, 0.0, 0.0, 1258.5, 419.5);
        GetIngredientPriceResponse priceOfIngredient7 = new GetIngredientPriceResponse(3300, 3500);
        GetIngredientResponse ingredient7 = new GetIngredientResponse(8L, "ATOS", 4.5, 5, 10, 83.9, stockCountOfIngredient7, stockWeightOfIngredient7, priceOfIngredient7, Boolean.FALSE);

        GetIngredientStockDetailResponse stockCountOfIngredient8 = new GetIngredientStockDetailResponse(30, 0, 0, 30, 20);
        GetIngredientStockDetailResponse stockWeightOfIngredient8 = new GetIngredientStockDetailResponse(3270.0, 0.0, 0.0, 3270.0, 2180.0);
        GetIngredientPriceResponse priceOfIngredient8 = new GetIngredientPriceResponse(3800, 4200);
        GetIngredientResponse ingredient8 = new GetIngredientResponse(9L, "ATOS", 6.0, 5, 10, 109.0, stockCountOfIngredient8, stockWeightOfIngredient8, priceOfIngredient8, Boolean.FALSE);

        GetIngredientStockDetailResponse stockCountOfIngredient9 = new GetIngredientStockDetailResponse(40, 0, 0, 40, 40);
        GetIngredientStockDetailResponse stockWeightOfIngredient9 = new GetIngredientStockDetailResponse(4680.0, 0.0, 0.0, 4680.0, 4680.0);
        GetIngredientPriceResponse priceOfIngredient9 = new GetIngredientPriceResponse(4400, 4600);
        GetIngredientResponse ingredient9 = new GetIngredientResponse(10L, "ATOS", 9.0, 5, 10, 117.0, stockCountOfIngredient9, stockWeightOfIngredient9, priceOfIngredient9, Boolean.FALSE);

        return List.of(ingredient1, ingredient2, ingredient3, ingredient4, ingredient5, ingredient6, ingredient7, ingredient8, ingredient9);
    }
}
