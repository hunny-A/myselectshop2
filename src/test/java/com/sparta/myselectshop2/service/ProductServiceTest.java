package com.sparta.myselectshop2.service;


import com.sparta.myselectshop2.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop2.dto.ProductRequestDto;
import com.sparta.myselectshop2.entity.Product;
import com.sparta.myselectshop2.entity.User;
import com.sparta.myselectshop2.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


import static com.sparta.myselectshop2.service.ProductService.MIN_MY_PRICE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock //  (1)
    ProductRepository productRepository;

    //@Mock붙은 객체를  @InjectMocks이 붙은 객체에 주입시킬 수 있다.
    @InjectMocks //  (2)
    ProductService productService;

    @Mock
    User user;


    @Test
    @DisplayName("관심 상품 희망가 - 최저가 이상으로 변경")
    void updateProduct_Success() {
        // given
        Long productId = 100L;
        int myprice = MIN_MY_PRICE + 100;
        Long userId = 777L;

        ProductMypriceRequestDto requestMyPriceDto = new ProductMypriceRequestDto(
                myprice
        );


        ProductRequestDto requestProductDto = new ProductRequestDto(
                "오리온 꼬북칩 초코츄러스맛 160g",
                "https://shopping-phinf.pstatic.net/main_2416122/24161228524.20200915151118.jpg",
                "https://search.shopping.naver.com/gate.nhn?id=24161228524",
                2350
        );

        Product product = new Product(requestProductDto, userId);

        //  (3)
        when(user.getId())
                .thenReturn(userId);
        when(productRepository.findByIdAndUserId(productId, userId))
                .thenReturn(Optional.of(product));


        // when, then
        assertDoesNotThrow( () -> {
            productService.updateProduct(productId, requestMyPriceDto, user);
        });
    }

    @Test
    @DisplayName("관심 상품 희망가 - 최저가 미만으로 변경")
    void updateProduct_Failed() {
        // given
        Long productId = 100L;
        int myprice = MIN_MY_PRICE - 50;

        ProductMypriceRequestDto requestMyPriceDto = new ProductMypriceRequestDto(
                myprice
        );

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Long result = productService.updateProduct(productId, requestMyPriceDto, user);
        });

        // then
        assertEquals(
                "유효하지 않은 관심 가격입니다. 최소 " + MIN_MY_PRICE + " 원 이상으로 설정해 주세요.",
                exception.getMessage()
        );
    }
}