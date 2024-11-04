package com.laser.ordermanage.ingredient.dto.response;

import java.util.List;

public class GetIngredientInfoResponseBuilder {
    public static List<GetIngredientInfoResponse> buildList() {
        GetIngredientInfoResponse ingredient1 = new GetIngredientInfoResponse(1L, "SS 400", 1.6);
        GetIngredientInfoResponse ingredient2 = new GetIngredientInfoResponse(2L, "SS 400", 2.0);
        GetIngredientInfoResponse ingredient3 = new GetIngredientInfoResponse(3L, "SS 400", 3.0);
        GetIngredientInfoResponse ingredient4 = new GetIngredientInfoResponse(4L, "SS 400", 4.0);
        GetIngredientInfoResponse ingredient5 = new GetIngredientInfoResponse(5L, "SS 400", 5.0);
        GetIngredientInfoResponse ingredient6 = new GetIngredientInfoResponse(6L, "SS 400", 8.0);
        GetIngredientInfoResponse ingredient7 = new GetIngredientInfoResponse(7L, "ATOS", 3.2);
        GetIngredientInfoResponse ingredient8 = new GetIngredientInfoResponse(8L, "ATOS", 4.5);
        GetIngredientInfoResponse ingredient9 = new GetIngredientInfoResponse(9L, "ATOS", 6.0);
        GetIngredientInfoResponse ingredient10 = new GetIngredientInfoResponse(10L, "ATOS", 9.0);
        return List.of(ingredient1, ingredient2, ingredient3, ingredient4, ingredient5, ingredient6, ingredient7, ingredient8, ingredient9, ingredient10);
    }
}
