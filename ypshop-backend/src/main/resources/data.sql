-- 관리자 및 회원 초기화
insert into member values("user1", aes_encrypt("jy@park2@@","shop-keyValue"), aes_encrypt("user1","shop-keyValue"), aes_encrypt("부산","shop-keyValue"), aes_encrypt("010","shop-keyValue"), aes_encrypt("user1@shop.com","shop-keyValue"), "ADMIN", now());
insert into member values("user2", aes_encrypt("jy@park2@@","shop-keyValue"), aes_encrypt("user2","shop-keyValue"), aes_encrypt("부산","shop-keyValue"), aes_encrypt("010","shop-keyValue"), aes_encrypt("user2@shop.com","shop-keyValue"), "USER", now());

-- 카테고리 초기화
insert into category values(null,"category1-1",1,1);
insert into category values(null,"category1-2",1,2);
insert into category values(null,"category1-3",1,3);
insert into category values(null,"category2-1",2,1);
insert into category values(null,"category2-2",2,2);
insert into category values(null,"category2-3",2,3);
insert into category values(null,"category3-1",1,1);

-- 상품 초기화
insert into product values(null, 1, "product1", 20000, "설명1", "N", 1);
insert into product values(null, 2, "product2", 30000, "설명2", default, 2);
insert into product values(null, 3, "product3", 40000, "설명3", "N", 3);
insert into product values(null, 4, "product4", 50000, "설명4", default, 1);
insert into product values(null, 5, "product5", 60000, "설명5", "N", 2);

-- 이미지 초기화
insert into image values(null,1,"image1","R",now());
insert into image values(null,2,"image2","R",now());
insert into image values(null,3,"image3","R",now());
insert into image values(null,4,"image4","R",now());
insert into image values(null,5,"image5","R",now());

insert into image values(null,1,"image6","B",now());
insert into image values(null,2,"image7","B",now());
insert into image values(null,3,"image8","B",now());
insert into image values(null,4,"image9","B",now());
insert into image values(null,5,"image10","B",now());

-- 옵션 초기화
insert into option values(null,1,"black",1);
insert into option values(null,2,"white",1);
insert into option values(null,3,"green",1);
insert into option values(null,4,"blue",1);
insert into option values(null,5,"yellow",1);

insert into option values(null,1,"L",2);
insert into option values(null,1,"XL",2);
insert into option values(null,2,"M",2);
insert into option values(null,3,"S",2);
insert into option values(null,4,"XS",2);
insert into option values(null,5,"XL",2);

-- 상품옵션 초기화
insert into product_option values(null, 1, 1, 6, 1000, 1000);
insert into product_option values(null, 2, 2, 8, 500, 500);
insert into product_option values(null, 3, 3, 9, 400, 400);
insert into product_option values(null, 4, 4, 10, 2000, 2000);
insert into product_option values(null, 5, 5, 11, 3000, 3000);
insert into product_option values(null, 1, 1, 7, 1000, 1000);

-- 장바구니 초기화
insert into cart values(null, "user2", 1, 5, 20000);
insert into cart values(null, "user2", 2, 10, 30000);
insert into cart values(null, "user2", 3, 3, 40000);
insert into cart values(null, "user2", 4, 2, 50000);
insert into cart values(null, "user2", 5, 1, 60000);

-- 주문 초기화
insert into orders values(null, "user2", aes_encrypt("박진영","shop-order"), aes_encrypt("서울","shop-order"), aes_encrypt("010","shop-order"), aes_encrypt("user2@shop.com","shop-order"), aes_encrypt("박진수","shop-order"), aes_encrypt("부산","shop-order"), aes_encrypt("010","shop-order"), "부재 시 경비실", now(), "무통장", 400000, "주문 확인");
insert into orders values(null, "user2", aes_encrypt("박진영","shop-order"), aes_encrypt("서울","shop-order"), aes_encrypt("010","shop-order"), aes_encrypt("user2@shop.com","shop-order"), aes_encrypt("박우성","shop-order"), aes_encrypt("울산","shop-order"), aes_encrypt("010","shop-order"), "부재 시 경비실", now(), "계좌이체", 220000, "주문 확인");
insert into orders values(null, "user2", aes_encrypt("박진영","shop-order"), aes_encrypt("서울","shop-order"), aes_encrypt("010","shop-order"), aes_encrypt("user2@shop.com","shop-order"), aes_encrypt("안영남","shop-order"), aes_encrypt("광주","shop-order"), aes_encrypt("010","shop-order"), "부재 시 경비실", now(), "무통장", 60000, "주문 확인");

-- 주문상세 초기화
insert into orders_detail values(null, 1, 1, "product1", "black", "L", "image1", 5, 20000);
insert into orders_detail values(null, 1, 2, "product2", "white", "M", "image2", 10, 30000);
insert into orders_detail values(null, 2, 3, "product3", "green", "S", "image3", 3, 40000);
insert into orders_detail values(null, 2, 4, "product4", "blue", "XS", "image4", 2, 50000);
insert into orders_detail values(null, 3, 5, "product5", "yellow", "XL", "image5", 5, 60000);