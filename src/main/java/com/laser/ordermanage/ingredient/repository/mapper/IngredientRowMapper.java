package com.laser.ordermanage.ingredient.repository.mapper;

import com.laser.ordermanage.ingredient.dto.response.GetIngredientPriceResponse;
import com.laser.ordermanage.ingredient.dto.response.GetIngredientResponse;
import com.laser.ordermanage.ingredient.dto.response.GetIngredientStockDetailResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IngredientRowMapper implements RowMapper<GetIngredientResponse> {
    @Override
    public GetIngredientResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return GetIngredientResponse.builder()
                .id(rs.getLong("id"))
                .texture(rs.getString("texture"))
                .thickness(rs.getDouble("thickness"))
                .width(rs.getInt("width"))
                .height(rs.getInt("height"))
                .weight(rs.getDouble("weight"))
                .stockCount(
                        GetIngredientStockDetailResponse.builder()
                                .previousDay(rs.getInt("previousDay"))
                                .incoming(rs.getInt("incoming"))
                                .production(rs.getInt("production"))
                                .currentDay(rs.getInt("currentDay"))
                                .optimal(rs.getInt("optimal"))
                                .build()
                )
                .stockWeight(
                        GetIngredientStockDetailResponse.builder()
                                .previousDay(rs.getBigDecimal("previousDay").multiply(rs.getBigDecimal("weight")).doubleValue())
                                .incoming(rs.getBigDecimal("incoming").multiply(rs.getBigDecimal("weight")).doubleValue())
                                .production(rs.getBigDecimal("production").multiply(rs.getBigDecimal("weight")).doubleValue())
                                .currentDay(rs.getBigDecimal("currentDay").multiply(rs.getBigDecimal("weight")).doubleValue())
                                .optimal(rs.getBigDecimal("optimal").multiply(rs.getBigDecimal("weight")).doubleValue())
                                .build()
                )
                .price(
                        GetIngredientPriceResponse.builder()
                                .purchase(rs.getInt("purchase"))
                                .sell(rs.getInt("sell"))
                                .build()
                )
                .isDeleted(rs.getBoolean("isDeleted"))
                .build();
    }
}
