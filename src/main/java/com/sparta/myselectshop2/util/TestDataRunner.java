package com.sparta.myselectshop2.util;

import com.sparta.myselectshop2.controller.ProductController;
import com.sparta.myselectshop2.dto.ProductRequestDto;
import com.sparta.myselectshop2.entity.Product;
import com.sparta.myselectshop2.entity.User;
import com.sparta.myselectshop2.entity.UserRoleEnum;
import com.sparta.myselectshop2.naver.dto.ItemDto;
import com.sparta.myselectshop2.naver.service.NaverApiService;
import com.sparta.myselectshop2.repository.ProductRepository;
import com.sparta.myselectshop2.repository.UserRepository;
import com.sparta.myselectshop2.security.UserDetailsImpl;
import com.sparta.myselectshop2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TestDataRunner implements ApplicationRunner {

    private final ProductController productController;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NaverApiService naverApiService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 테스트 User 생성
        User testUser1 = new User("User1", passwordEncoder.encode("123"), "user1@sparta.com", UserRoleEnum.USER);
        User testUser2 = new User("User2", passwordEncoder.encode("123"), "user2@sparta.com", UserRoleEnum.USER);
        User testAdminUser1 = new User("Admin", passwordEncoder.encode("123"), "admin@sparta.com", UserRoleEnum.ADMIN);
        testUser1 = userRepository.save(testUser1);
        testUser2 = userRepository.save(testUser2);
        testAdminUser1 = userRepository.save(testAdminUser1);

        // 테스트 User 의 관심상품 등록
        // 검색어 당 관심상품 10개 등록
        createTestData(testUser1, "신발");
        createTestData(testUser1, "과자");
        createTestData(testUser1, "키보드");
        createTestData(testUser1, "휴지");
        createTestData(testUser1, "휴대폰");
        createTestData(testUser1, "앨범");
        createTestData(testUser2, "헤드폰");
        createTestData(testUser2, "이어폰");
        createTestData(testUser2, "노트북");
        createTestData(testUser2, "무선 이어폰");
        createTestData(testUser2, "모니터");
    }

    private void createTestData(User user, String searchWord) throws IOException {
        // 네이버 쇼핑 API 통해 상품 검색
        List<ProductRequestDto> productRequestDtoList;
        //productRequestDtoList = naverApiService.searchItems(searchWord).stream().map(ProductRequestDto::new).toList();
        productRequestDtoList = naverApiService.searchItems(searchWord).stream().map(ProductRequestDto::new).collect(Collectors.toList());

        UserDetailsImpl userDetails = new UserDetailsImpl(user, user.getUsername());

        for (ProductRequestDto dto : productRequestDtoList) {
            productController.createProduct(dto, userDetails);
        }
    }
}