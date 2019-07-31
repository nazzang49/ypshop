-- 주문
ALTER TABLE `orders`
	DROP FOREIGN KEY `FK_member_TO_orders`; -- 회원 -> 주문

-- 상품
ALTER TABLE `product`
	DROP FOREIGN KEY `FK_category_TO_product`; -- 카테고리 -> 상품

-- 장바구니
ALTER TABLE `cart`
	DROP FOREIGN KEY `FK_member_TO_cart`; -- 회원 -> 장바구니

-- 장바구니
ALTER TABLE `cart`
	DROP FOREIGN KEY `FK_product_option_TO_cart`; -- 상품옵션 -> 장바구니

-- 주문 상세
ALTER TABLE `orders_detail`
	DROP FOREIGN KEY `FK_orders_TO_orders_detail`; -- 주문 -> 주문 상세

-- 주문 상세
ALTER TABLE `orders_detail`
	DROP FOREIGN KEY `FK_product_option_TO_orders_detail`; -- 상품옵션 -> 주문 상세

-- 옵션
ALTER TABLE `option`
	DROP FOREIGN KEY `FK_product_TO_option`; -- 상품 -> 옵션

-- 상품옵션
ALTER TABLE `product_option`
	DROP FOREIGN KEY `FK_product_TO_product_option`; -- 상품 -> 상품옵션

-- 상품옵션
ALTER TABLE `product_option`
	DROP FOREIGN KEY `FK_option_TO_product_option`; -- 옵션 -> 상품옵션

-- 상품옵션
ALTER TABLE `product_option`
	DROP FOREIGN KEY `FK_option_TO_product_option2`; -- 옵션 -> 상품옵션2

-- 이미지
ALTER TABLE `image`
	DROP FOREIGN KEY `FK_product_TO_image`; -- 상품 -> 이미지

-- 게시판
ALTER TABLE `board`
	DROP FOREIGN KEY `FK_member_TO_board`; -- 회원 -> 게시판

-- 포인트
ALTER TABLE `point`
	DROP FOREIGN KEY `FK_member_TO_point`; -- 회원 -> 포인트

-- 쿠폰
ALTER TABLE `coupon`
	DROP FOREIGN KEY `FK_member_TO_coupon`; -- 회원 -> 쿠폰

-- 주문 로그_관리자
ALTER TABLE `order_log`
	DROP FOREIGN KEY `FK_orders_TO_order_log`; -- 주문 -> 주문 로그_관리자

-- 수령자 주소록
ALTER TABLE `delivery`
	DROP FOREIGN KEY `FK_member_TO_delivery`; -- 회원 -> 수령자 주소록

-- 회원
ALTER TABLE `member`
	DROP PRIMARY KEY; -- 회원 기본키

-- 주문
ALTER TABLE `orders`
	DROP PRIMARY KEY; -- 주문 기본키

-- 상품
ALTER TABLE `product`
	DROP PRIMARY KEY; -- 상품 기본키

-- 장바구니
ALTER TABLE `cart`
	DROP PRIMARY KEY; -- 장바구니 기본키

-- 주문 상세
ALTER TABLE `orders_detail`
	DROP PRIMARY KEY; -- 주문 상세 기본키

-- 옵션
ALTER TABLE `option`
	DROP PRIMARY KEY; -- 옵션 기본키

-- 상품옵션
ALTER TABLE `product_option`
	DROP PRIMARY KEY; -- 상품옵션 기본키

-- 이미지
ALTER TABLE `image`
	DROP PRIMARY KEY; -- 이미지 기본키

-- 게시판
ALTER TABLE `board`
	DROP PRIMARY KEY; -- 게시판 기본키

-- 포인트
ALTER TABLE `point`
	DROP PRIMARY KEY; -- 포인트 기본키

-- 카테고리
ALTER TABLE `category`
	DROP PRIMARY KEY; -- 카테고리 기본키

-- 수령자 주소록
ALTER TABLE `delivery`
	DROP PRIMARY KEY; -- 수령자 주소록 기본키

-- 쿠키
ALTER TABLE `TABLE`
	DROP PRIMARY KEY; -- 쿠키 기본키

-- 회원
DROP TABLE IF EXISTS `member` RESTRICT;

-- 주문
DROP TABLE IF EXISTS `orders` RESTRICT;

-- 상품
DROP TABLE IF EXISTS `product` RESTRICT;

-- 장바구니
DROP TABLE IF EXISTS `cart` RESTRICT;

-- 주문 상세
DROP TABLE IF EXISTS `orders_detail` RESTRICT;

-- 옵션
DROP TABLE IF EXISTS `option` RESTRICT;

-- 상품옵션
DROP TABLE IF EXISTS `product_option` RESTRICT;

-- 이미지
DROP TABLE IF EXISTS `image` RESTRICT;

-- 게시판
DROP TABLE IF EXISTS `board` RESTRICT;

-- 포인트
DROP TABLE IF EXISTS `point` RESTRICT;

-- 쿠폰
DROP TABLE IF EXISTS `coupon` RESTRICT;

-- 카테고리
DROP TABLE IF EXISTS `category` RESTRICT;

-- 주문 로그_관리자
DROP TABLE IF EXISTS `order_log` RESTRICT;

-- 수령자 주소록
DROP TABLE IF EXISTS `delivery` RESTRICT;

-- 쿠키
DROP TABLE IF EXISTS `TABLE` RESTRICT;

-- 회원
CREATE TABLE `member` (
	`id`       VARCHAR(30)           NOT NULL COMMENT '아이디', -- 아이디
	`password` VARBINARY(256)        NOT NULL COMMENT '비밀번호', -- 비밀번호
	`name`     VARBINARY(256)        NOT NULL COMMENT '이름', -- 이름
	`address`  VARBINARY(256)        NOT NULL COMMENT '주소', -- 주소
	`phone`    VARBINARY(256)        NOT NULL COMMENT '휴대전화', -- 휴대전화
	`email`    VARBINARY(256)        NOT NULL COMMENT '이메일', -- 이메일
	`role`     ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER' COMMENT '권한 구분', -- 권한 구분
	`regDate`  DATE                  NOT NULL COMMENT '가입일' -- 가입일
)
COMMENT '회원';

-- 회원
ALTER TABLE `member`
	ADD CONSTRAINT `PK_member` -- 회원 기본키
		PRIMARY KEY (
			`id` -- 아이디
		);

-- 주문
CREATE TABLE `orders` (
	`no`              INT UNSIGNED                                                      NOT NULL COMMENT '번호', -- 번호
	`memberId`        VARCHAR(30)                                                       NULL     COMMENT '회원 아이디(null이면 비회원)', -- 회원 아이디(null이면 비회원)
	`customerName`    VARBINARY(256)                                                    NOT NULL COMMENT '주문자 이름', -- 주문자 이름
	`customerAddress` VARBINARY(256)                                                    NOT NULL COMMENT '주문자 주소', -- 주문자 주소
	`customerPhone`   VARBINARY(256)                                                    NOT NULL COMMENT '주문자 휴대전화', -- 주문자 휴대전화
	`customerEmail`   VARBINARY(256)                                                    NOT NULL COMMENT '주문자 이메일', -- 주문자 이메일
	`receiverName`    VARBINARY(256)                                                    NOT NULL COMMENT '수령자 이름', -- 수령자 이름
	`receiverAddress` VARBINARY(256)                                                    NOT NULL COMMENT '수령자 주소', -- 수령자 주소
	`receiverPhone`   VARBINARY(256)                                                    NOT NULL COMMENT '수령자 휴대전화', -- 수령자 휴대전화
	`receiverMsg`     LONGTEXT                                                          NOT NULL COMMENT '배송메시지', -- 배송메시지
	`orderDate`       DATE                                                              NOT NULL COMMENT '주문일자', -- 주문일자
	`paymentCategory` VARCHAR(20)                                                       NOT NULL COMMENT '결제종류', -- 결제종류
	`paymentPrice`    INT UNSIGNED                                                      NOT NULL COMMENT '결제금액', -- 결제금액
	`status`          ENUM('주문 확인','입금 확인','배송 진행','배송 완료','환불 완료') NOT NULL DEFAULT '주문 확인' COMMENT '주문상태(임금 전/배송 완료 등)' -- 주문상태(임금 전/배송 완료 등)
)
COMMENT '주문';

-- 주문
ALTER TABLE `orders`
	ADD CONSTRAINT `PK_orders` -- 주문 기본키
		PRIMARY KEY (
			`no` -- 번호
		);

ALTER TABLE `orders`
	MODIFY COLUMN `no` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '번호';

-- 상품
CREATE TABLE `product` (
	`no`               INT UNSIGNED   NOT NULL COMMENT '번호', -- 번호
	`categoryNo`       INT UNSIGNED   NOT NULL COMMENT '카테고리번호', -- 카테고리번호
	`name`             VARCHAR(50)    NOT NULL COMMENT '상품명', -- 상품명
	`price`            INT UNSIGNED   NOT NULL COMMENT '상품가격', -- 상품가격
	`shortDescription` VARCHAR(300)   NOT NULL COMMENT '상품 간단 설명', -- 상품 간단 설명
	`alignUse`         ENUM('Y', 'N') NOT NULL DEFAULT 'Y' COMMENT '진열 구분', -- 진열 구분
	`alignNo`          INT UNSIGNED   NOT NULL COMMENT '진열 순서' -- 진열 순서
)
COMMENT '상품';

-- 상품
ALTER TABLE `product`
	ADD CONSTRAINT `PK_product` -- 상품 기본키
		PRIMARY KEY (
			`no` -- 번호
		);

ALTER TABLE `product`
	MODIFY COLUMN `no` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '번호';

-- 장바구니
CREATE TABLE `cart` (
	`no`              INT UNSIGNED NOT NULL COMMENT '번호', -- 번호
	`memberId`        VARCHAR(30)  NULL     COMMENT '회원 아이디(null이면 비회원)', -- 회원 아이디(null이면 비회원)
	`productOptionNo` INT UNSIGNED NOT NULL COMMENT '상품옵션번호', -- 상품옵션번호
	`cartAmount`      INT UNSIGNED NOT NULL COMMENT '수량', -- 수량
	`cartPrice`       INT UNSIGNED NOT NULL COMMENT '상품별 금액' -- 상품별 금액
)
COMMENT '장바구니';

-- 장바구니
ALTER TABLE `cart`
	ADD CONSTRAINT `PK_cart` -- 장바구니 기본키
		PRIMARY KEY (
			`no` -- 번호
		);

ALTER TABLE `cart`
	MODIFY COLUMN `no` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '번호';

-- 주문 상세
CREATE TABLE `orders_detail` (
	`no`               INT UNSIGNED NOT NULL COMMENT '번호', -- 번호
	`orderNo`          INT UNSIGNED NOT NULL COMMENT '주문번호', -- 주문번호
	`productOptionNo`  INT UNSIGNED NULL     COMMENT '상품옵션번호', -- 상품옵션번호
	`productName`      VARCHAR(50)  NOT NULL COMMENT '상품명', -- 상품명
	`firstOptionName`  VARCHAR(50)  NOT NULL COMMENT '1차 옵션명', -- 1차 옵션명
	`secondOptionName` VARCHAR(50)  NOT NULL COMMENT '2차 옵션명', -- 2차 옵션명
	`imageUrl`         VARCHAR(100) NOT NULL COMMENT '썸네일', -- 썸네일
	`orderAmount`      INT UNSIGNED NOT NULL COMMENT '주문수량', -- 주문수량
	`orderPrice`       INT UNSIGNED NOT NULL COMMENT '상품별 주문금액' -- 상품별 주문금액
)
COMMENT '주문 상세';

-- 주문 상세
ALTER TABLE `orders_detail`
	ADD CONSTRAINT `PK_orders_detail` -- 주문 상세 기본키
		PRIMARY KEY (
			`no` -- 번호
		);

ALTER TABLE `orders_detail`
	MODIFY COLUMN `no` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '번호';

-- 옵션
CREATE TABLE `option` (
	`no`        INT UNSIGNED NOT NULL COMMENT '번호', -- 번호
	`productNo` INT UNSIGNED NOT NULL COMMENT '상품번호', -- 상품번호
	`name`      VARCHAR(30)  NOT NULL COMMENT '옵션내용(블랙/275 등)', -- 옵션내용(블랙/275 등)
	`depth`     INT UNSIGNED NOT NULL COMMENT '깊이' -- 깊이
)
COMMENT '옵션';

-- 옵션
ALTER TABLE `option`
	ADD CONSTRAINT `PK_option` -- 옵션 기본키
		PRIMARY KEY (
			`no` -- 번호
		);

ALTER TABLE `option`
	MODIFY COLUMN `no` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '번호';

-- 상품옵션
CREATE TABLE `product_option` (
	`no`              INT UNSIGNED NOT NULL COMMENT '번호', -- 번호
	`productNo`       INT UNSIGNED NOT NULL COMMENT '상품번호', -- 상품번호
	`firstOptionNo`   INT UNSIGNED NOT NULL COMMENT '1차 옵션', -- 1차 옵션
	`secondOptionNo`  INT UNSIGNED NOT NULL COMMENT '2차 옵션', -- 2차 옵션
	`remainAmount`    INT UNSIGNED NOT NULL COMMENT '재고 수량(-1이면 비재고)', -- 재고 수량(-1이면 비재고)
	`availableAmount` INT UNSIGNED NOT NULL COMMENT '판매 가능 수량(-1이면 비재고)' -- 판매 가능 수량(-1이면 비재고)
)
COMMENT '상품옵션';

-- 상품옵션
ALTER TABLE `product_option`
	ADD CONSTRAINT `PK_product_option` -- 상품옵션 기본키
		PRIMARY KEY (
			`no` -- 번호
		);

ALTER TABLE `product_option`
	MODIFY COLUMN `no` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '번호';

-- 이미지
CREATE TABLE `image` (
	`no`         INT UNSIGNED   NOT NULL COMMENT '번호', -- 번호
	`productNo`  INT UNSIGNED   NOT NULL COMMENT '상품번호', -- 상품번호
	`url`        VARCHAR(200)   NOT NULL COMMENT '이미지 경로', -- 이미지 경로
	`repOrBasic` ENUM('R', 'B') NOT NULL DEFAULT 'B' COMMENT '이미지 구분', -- 이미지 구분
	`regDate`    DATE           NOT NULL COMMENT '등록일' -- 등록일
)
COMMENT '이미지';

-- 이미지
ALTER TABLE `image`
	ADD CONSTRAINT `PK_image` -- 이미지 기본키
		PRIMARY KEY (
			`no` -- 번호
		);

ALTER TABLE `image`
	MODIFY COLUMN `no` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '번호';

-- 게시판
CREATE TABLE `board` (
	`no`       INT UNSIGNED   NOT NULL COMMENT '번호', -- 번호
	`memberId` VARCHAR(30)    NOT NULL COMMENT '회원 아이디', -- 회원 아이디
	`password` VARBINARY(256) NOT NULL COMMENT '비밀번호', -- 비밀번호
	`title`    VARCHAR(100)   NOT NULL COMMENT '제목', -- 제목
	`content`  LONGTEXT       NOT NULL COMMENT '내용', -- 내용
	`hit`      INT UNSIGNED   NOT NULL COMMENT '조회수', -- 조회수
	`groupNo`  INT UNSIGNED   NOT NULL COMMENT '그룹번호', -- 그룹번호
	`orderNo`  INT UNSIGNED   NOT NULL COMMENT '순서번호', -- 순서번호
	`depth`    INT UNSIGNED   NOT NULL COMMENT '깊이', -- 깊이
	`regDate`  DATE           NOT NULL COMMENT '등록일' -- 등록일
)
COMMENT '게시판';

-- 게시판
ALTER TABLE `board`
	ADD CONSTRAINT `PK_board` -- 게시판 기본키
		PRIMARY KEY (
			`no` -- 번호
		);

ALTER TABLE `board`
	MODIFY COLUMN `no` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '번호';

-- 포인트
CREATE TABLE `point` (
	`memberId` VARCHAR(30)  NOT NULL COMMENT '회원 아이디', -- 회원 아이디
	`reward`   INT UNSIGNED NOT NULL COMMENT '포인트 점수', -- 포인트 점수
	`dueDate`  DATE         NOT NULL COMMENT '유효기간' -- 유효기간
)
COMMENT '포인트';

-- 포인트
ALTER TABLE `point`
	ADD CONSTRAINT `PK_point` -- 포인트 기본키
		PRIMARY KEY (
			`memberId` -- 회원 아이디
		);

-- 쿠폰
CREATE TABLE `coupon` (
	`memberId`  VARCHAR(30)  NOT NULL COMMENT '회원 아이디', -- 회원 아이디
	`name`      VARCHAR(50)  NOT NULL COMMENT '쿠폰명', -- 쿠폰명
	`content`   VARCHAR(200) NOT NULL COMMENT '쿠폰 내용', -- 쿠폰 내용
	`startDate` DATE         NOT NULL COMMENT '발급일', -- 발급일
	`endDate`   DATE         NOT NULL COMMENT '종료일' -- 종료일
)
COMMENT '쿠폰';

-- 카테고리
CREATE TABLE `category` (
	`no`      INT UNSIGNED NOT NULL COMMENT '번호', -- 번호
	`name`    VARCHAR(30)  NOT NULL COMMENT '카테고리명', -- 카테고리명
	`groupNo` INT UNSIGNED NOT NULL COMMENT '그룹번호', -- 그룹번호
	`depth`   INT UNSIGNED NOT NULL COMMENT '깊이' -- 깊이
)
COMMENT '카테고리';

-- 카테고리
ALTER TABLE `category`
	ADD CONSTRAINT `PK_category` -- 카테고리 기본키
		PRIMARY KEY (
			`no` -- 번호
		);

ALTER TABLE `category`
	MODIFY COLUMN `no` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '번호';

-- 주문 로그_관리자
CREATE TABLE `order_log` (
	`orderNo`   INT UNSIGNED NOT NULL COMMENT '주문번호', -- 주문번호
	`logStatus` VARCHAR(30)  NOT NULL COMMENT '로그상태', -- 로그상태
	`logDate`   DATE         NOT NULL COMMENT '로그일자' -- 로그일자
)
COMMENT '주문 로그_관리자';

-- 수령자 주소록
CREATE TABLE `delivery` (
	`no`              INT UNSIGNED NOT NULL COMMENT '번호', -- 번호
	`memberId`        VARCHAR(30)  NOT NULL COMMENT '회원 아이디', -- 회원 아이디
	`receiverName`    VARCHAR(30)  NOT NULL COMMENT '수령자 이름', -- 수령자 이름
	`receiverAddress` VARCHAR(200) NOT NULL COMMENT '수령자 주소', -- 수령자 주소
	`receiverPhone`   VARCHAR(30)  NOT NULL COMMENT '수령자 휴대전화' -- 수령자 휴대전화
)
COMMENT '수령자 주소록';

-- 수령자 주소록
ALTER TABLE `delivery`
	ADD CONSTRAINT `PK_delivery` -- 수령자 주소록 기본키
		PRIMARY KEY (
			`no` -- 번호
		);

-- 쿠키
CREATE TABLE `TABLE` (
	`COL`  <데이터 타입 없음> NOT NULL COMMENT '쿠키', -- 쿠키
	`COL2` <데이터 타입 없음> NULL     COMMENT '새 컬럼' -- 새 컬럼
)
COMMENT '쿠키';

-- 쿠키
ALTER TABLE `TABLE`
	ADD CONSTRAINT `PK_TABLE` -- 쿠키 기본키
		PRIMARY KEY (
			`COL` -- 쿠키
		);

-- 주문
ALTER TABLE `orders`
	ADD CONSTRAINT `FK_member_TO_orders` -- 회원 -> 주문
		FOREIGN KEY (
			`memberId` -- 회원 아이디(null이면 비회원)
		)
		REFERENCES `member` ( -- 회원
			`id` -- 아이디
		)
		ON DELETE CASCADE;

-- 상품
ALTER TABLE `product`
	ADD CONSTRAINT `FK_category_TO_product` -- 카테고리 -> 상품
		FOREIGN KEY (
			`categoryNo` -- 카테고리번호
		)
		REFERENCES `category` ( -- 카테고리
			`no` -- 번호
		);

-- 장바구니
ALTER TABLE `cart`
	ADD CONSTRAINT `FK_member_TO_cart` -- 회원 -> 장바구니
		FOREIGN KEY (
			`memberId` -- 회원 아이디(null이면 비회원)
		)
		REFERENCES `member` ( -- 회원
			`id` -- 아이디
		)
		ON DELETE CASCADE;

-- 장바구니
ALTER TABLE `cart`
	ADD CONSTRAINT `FK_product_option_TO_cart` -- 상품옵션 -> 장바구니
		FOREIGN KEY (
			`productOptionNo` -- 상품옵션번호
		)
		REFERENCES `product_option` ( -- 상품옵션
			`no` -- 번호
		)
		ON DELETE CASCADE;

-- 주문 상세
ALTER TABLE `orders_detail`
	ADD CONSTRAINT `FK_orders_TO_orders_detail` -- 주문 -> 주문 상세
		FOREIGN KEY (
			`orderNo` -- 주문번호
		)
		REFERENCES `orders` ( -- 주문
			`no` -- 번호
		)
		ON DELETE CASCADE;

-- 주문 상세
ALTER TABLE `orders_detail`
	ADD CONSTRAINT `FK_product_option_TO_orders_detail` -- 상품옵션 -> 주문 상세
		FOREIGN KEY (
			`productOptionNo` -- 상품옵션번호
		)
		REFERENCES `product_option` ( -- 상품옵션
			`no` -- 번호
		)
		ON DELETE SET NULL;

-- 옵션
ALTER TABLE `option`
	ADD CONSTRAINT `FK_product_TO_option` -- 상품 -> 옵션
		FOREIGN KEY (
			`productNo` -- 상품번호
		)
		REFERENCES `product` ( -- 상품
			`no` -- 번호
		)
		ON DELETE CASCADE;

-- 상품옵션
ALTER TABLE `product_option`
	ADD CONSTRAINT `FK_product_TO_product_option` -- 상품 -> 상품옵션
		FOREIGN KEY (
			`productNo` -- 상품번호
		)
		REFERENCES `product` ( -- 상품
			`no` -- 번호
		)
		ON DELETE CASCADE;

-- 상품옵션
ALTER TABLE `product_option`
	ADD CONSTRAINT `FK_option_TO_product_option` -- 옵션 -> 상품옵션
		FOREIGN KEY (
			`firstOptionNo` -- 1차 옵션
		)
		REFERENCES `option` ( -- 옵션
			`no` -- 번호
		)
		ON DELETE CASCADE;

-- 상품옵션
ALTER TABLE `product_option`
	ADD CONSTRAINT `FK_option_TO_product_option2` -- 옵션 -> 상품옵션2
		FOREIGN KEY (
			`secondOptionNo` -- 2차 옵션
		)
		REFERENCES `option` ( -- 옵션
			`no` -- 번호
		)
		ON DELETE CASCADE;

-- 이미지
ALTER TABLE `image`
	ADD CONSTRAINT `FK_product_TO_image` -- 상품 -> 이미지
		FOREIGN KEY (
			`productNo` -- 상품번호
		)
		REFERENCES `product` ( -- 상품
			`no` -- 번호
		)
		ON DELETE CASCADE;

-- 게시판
ALTER TABLE `board`
	ADD CONSTRAINT `FK_member_TO_board` -- 회원 -> 게시판
		FOREIGN KEY (
			`memberId` -- 회원 아이디
		)
		REFERENCES `member` ( -- 회원
			`id` -- 아이디
		);

-- 포인트
ALTER TABLE `point`
	ADD CONSTRAINT `FK_member_TO_point` -- 회원 -> 포인트
		FOREIGN KEY (
			`memberId` -- 회원 아이디
		)
		REFERENCES `member` ( -- 회원
			`id` -- 아이디
		);

-- 쿠폰
ALTER TABLE `coupon`
	ADD CONSTRAINT `FK_member_TO_coupon` -- 회원 -> 쿠폰
		FOREIGN KEY (
			`memberId` -- 회원 아이디
		)
		REFERENCES `member` ( -- 회원
			`id` -- 아이디
		);

-- 주문 로그_관리자
ALTER TABLE `order_log`
	ADD CONSTRAINT `FK_orders_TO_order_log` -- 주문 -> 주문 로그_관리자
		FOREIGN KEY (
			`orderNo` -- 주문번호
		)
		REFERENCES `orders` ( -- 주문
			`no` -- 번호
		);

-- 수령자 주소록
ALTER TABLE `delivery`
	ADD CONSTRAINT `FK_member_TO_delivery` -- 회원 -> 수령자 주소록
		FOREIGN KEY (
			`memberId` -- 회원 아이디
		)
		REFERENCES `member` ( -- 회원
			`id` -- 아이디
		);