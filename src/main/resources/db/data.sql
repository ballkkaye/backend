-- 1. stadium_tb
INSERT INTO stadium_tb (stadium_name, location, stadium_type)
VALUES ('잠실야구장', '서울특별시 잠실동', 'OUTDOOR'),
       ('고척스카이돔', '서울특별시 고척동', 'INDOOR'),
       ('인천 SSG 랜더스필드', '인천광역시 문학동', 'OUTDOOR'),
       ('광주-기아 챔피언스필드', '광주광역시 임동', 'OUTDOOR'),
       ('대구 삼성라이온즈파크', '대구광역시 연호동', 'OUTDOOR'),
       ('부산 사직야구장', '부산광역시 사직동', 'OUTDOOR'),
       ('대전 한화생명이글스파크', '대전광역시 부사동', 'OUTDOOR'),
       ('창원 NC파크', '경상남도 창원시 양덕동', 'OUTDOOR'),
       ('수원 KT위즈파크', '경기도 수원시 조원동', 'OUTDOOR'),
       ('청주야구장', '충청북도 청주시 사직동', 'OUTDOOR'),
       ('울산 문수야구장', '울산광역시 옥동', 'OUTDOOR'),
       ('포항야구장', '경상북도 포항시', 'OUTDOOR'),
       ('군산 월명야구장', '전라북도 군산시', 'OUTDOOR');

INSERT INTO stadium_tb (stadium_name, location, stadium_type)
VALUES ('부산 사직야구장', '부산광역시 사직동', 'OUTDOOR');

-- 2. stadium_correction_tb (구장 보정 계수)테이블
INSERT INTO stadium_correction_tb (stadium_id, correction, this_year)
VALUES (1, 0.732, 2025);
INSERT INTO stadium_correction_tb (stadium_id, correction, this_year)
VALUES (2, 0.822, 2025);
INSERT INTO stadium_correction_tb (stadium_id, correction, this_year)
VALUES (3, 1.489, 2025);
INSERT INTO stadium_correction_tb (stadium_id, correction, this_year)
VALUES (4, 0.953, 2025);
INSERT INTO stadium_correction_tb (stadium_id, correction, this_year)
VALUES (5, 1.522, 2025);
INSERT INTO stadium_correction_tb (stadium_id, correction, this_year)
VALUES (6, 0.729, 2025);
INSERT INTO stadium_correction_tb (stadium_id, correction, this_year)
VALUES (7, 0.982, 2025);
INSERT INTO stadium_correction_tb (stadium_id, correction, this_year)
VALUES (8, 1.085, 2025);
INSERT INTO stadium_correction_tb (stadium_id, correction, this_year)
VALUES (9, 1.24, 2025);
INSERT INTO stadium_correction_tb (stadium_id, correction, this_year)
VALUES (10, 1.16, 2025);
INSERT INTO stadium_correction_tb (stadium_id, correction, this_year)
VALUES (11, 1.08, 2025);
INSERT INTO stadium_correction_tb (stadium_id, correction, this_year)
VALUES (12, 1.02, 2025);

-- 3. stadium_coordinate_tb 테이블
INSERT INTO stadium_coordinate_tb (stadium_id, longitude, latitude)
VALUES (1, 127.07, 37.51); -- 잠실야구장 (서울특별시 송파구 잠실동)
INSERT INTO stadium_coordinate_tb (stadium_id, longitude, latitude)
VALUES (2, 126.89, 37.50); -- 고척스카이돔 (서울특별시 구로구 고척동)
INSERT INTO stadium_coordinate_tb (stadium_id, longitude, latitude)
VALUES (3, 126.69, 37.45); -- 인천 SSG 랜더스필드 (인천광역시 미추홀구 문학동)
INSERT INTO stadium_coordinate_tb (stadium_id, longitude, latitude)
VALUES (4, 126.89, 35.56); -- 광주-기아 챔피언스필드 (광주광역시 북구 임동)
INSERT INTO stadium_coordinate_tb (stadium_id, longitude, latitude)
VALUES (5, 128.65, 35.32); -- 대구 삼성라이온즈파크 (대구 수성구 연호동)
INSERT INTO stadium_coordinate_tb (stadium_id, longitude, latitude)
VALUES (6, 129.06, 35.52); -- 부산 사직야구장 (부산 동래구 사직동)
INSERT INTO stadium_coordinate_tb (stadium_id, longitude, latitude)
VALUES (7, 127.42, 36.29); -- 대전 한화생명이글스파크 (대전 중구 부사동)
INSERT INTO stadium_coordinate_tb (stadium_id, longitude, latitude)
VALUES (8, 128.57, 35.33); -- 창원 NC파크 (창원 마산회원구 양덕동)
INSERT INTO stadium_coordinate_tb (stadium_id, longitude, latitude)
VALUES (9, 127.01, 37.59); -- 수원 KT위즈파크 (경기 수원시 장안구 조원동)
INSERT INTO stadium_coordinate_tb (stadium_id, longitude, latitude)
VALUES (10, 127.49, 36.64); -- 청주야구장 (청주시 서원구 사직동)
INSERT INTO stadium_coordinate_tb (stadium_id, longitude, latitude)
VALUES (11, 129.27, 35.53); -- 울산 문수야구장 (울산 남구 옥동)
INSERT INTO stadium_coordinate_tb (stadium_id, longitude, latitude)
VALUES (12, 128.80, 35.86); -- 포항야구장 (경북 포항시 남구)
INSERT INTO stadium_coordinate_tb (stadium_id, longitude, latitude)
VALUES (13, 126.50, 35.98);
-- 군산 월명야구장 (전북 군산시)

-- 4. team_tb
INSERT INTO team_tb (stadium_id1, stadium_id2, team_name, logo_url, ticket_link)
VALUES (1, NULL, 'LG 트윈스', 'https://upload.wikimedia.org/wikipedia/ko/4/4f/LG_Twins.png',
        'https://www.lgtwins.com/service/html.ncd?baRs=OUT_DS&view=%2Fpc_twins%2Fticket%2Fticketbuy&actID=BR_RetrieveTicketing'),
       (1, NULL, '두산 베어스', 'https://upload.wikimedia.org/wikipedia/ko/0/09/Doosan_Bears.png',
        'https://ticket.interpark.com/Contents/Sports/GoodsInfo?SportsCode=07001&TeamCode=PB004'),
       (2, NULL, '키움 히어로즈', 'https://upload.wikimedia.org/wikipedia/ko/5/52/Kiwoom_Heroes.png',
        'https://ticket.interpark.com/Contents/Sports/GoodsInfo?SportsCode=07001&TeamCode=PB003'),
       (3, NULL, 'SSG 랜더스', 'https://upload.wikimedia.org/wikipedia/ko/0/02/SSG_Landers.png',
        'https://www.ticketlink.co.kr/sports#reservation'),
       (4, 13, 'KIA 타이거즈', 'https://upload.wikimedia.org/wikipedia/ko/6/6e/KIA_Tigers.png',
        'https://tigers.co.kr/ticket/reservation'),
       (5, 11, '삼성 라이온즈', 'https://upload.wikimedia.org/wikipedia/ko/9/95/Samsung_Lions.png',
        'https://www.ticketlink.co.kr/sports#reservation'),
       (6, 12, '롯데 자이언츠', 'https://upload.wikimedia.org/wikipedia/ko/9/91/Lotte_Giants.png',
        'https://ticket.giantsclub.com/loginForm.do'),
       (7, 10, '한화 이글스', 'https://upload.wikimedia.org/wikipedia/ko/9/99/Hanwha_Eagles.png',
        'https://www.hanwhaeagles.co.kr/ticketInfo.do'),
       (8, NULL, 'NC 다이노스', 'https://upload.wikimedia.org/wikipedia/ko/b/bd/NC_Dinos.png',
        'https://ticket.ncdinos.com/login'),
       (9, NULL, 'KT 위즈', 'https://upload.wikimedia.org/wikipedia/ko/b/b3/KT_Wiz.png',
        'https://www.ktwiz.co.kr/ticket/reservation');

-- 5.user_tb
INSERT INTO user_tb
(username, password, name, nickname, team_id, phone_number, email, birth_date, gender, profile_url, provider_type,
 user_role)
VALUES ('ssar123', '1234', '쌀', 'ssar', 1, '01011112222', 'ssar@nate.com', '1999-09-09', 'MALE', '/img/profile.png',
        'BALLKKAYE', 'USER'),
       ('cos123', '1234', '코스', 'cos', 2, '01022223333', 'cos@nate.com', '2000-01-01', 'FEMALE', '/img/profile.png',
        'BALLKKAYE', 'USER'),
       ('love123', '1234', '러브', 'love', 3, '01033334444', 'love@nate.com', '1998-08-08', 'MALE', '/img/profile.png',
        'BALLKKAYE', 'USER'),
       ('haha123', '1234', '하하', 'haha', NULL, '01044445555', 'haha@nate.com', '1997-07-07', 'FEMALE',
        '/img/profile.png', 'BALLKKAYE', 'ADMIN'),
       ('harim123', '1234', '백하림', 'harim', 4, '01055556666', 'harim@example.com', '1995-05-05', 'MALE',
        '/img/profile.png', 'BALLKKAYE', 'USER'),
       ('jungwon123', '1234', '김정원', 'jungwon', 5, '01066667777', 'jungwon@example.com', '1996-06-06', 'MALE',
        '/img/profile.png', 'BALLKKAYE', 'USER'),
       ('misook123', '1234', '김미숙', 'misook', 6, '01077778888', 'misook@example.com', '1994-04-04', 'FEMALE',
        '/img/profile.png', 'BALLKKAYE', 'USER');


-- 6.game_tb
INSERT INTO game_tb (id, game_time, stadium_id, broadcast_channel, game_status,
                     home_team_id, away_team_id,
                     home_win_per, away_win_per,
                     home_prediction_score, away_prediction_score,
                     home_result_score, away_result_score,
                     total_prediction_score)
VALUES (418, TIMESTAMP '2025-07-04 18:30:00', 1, 'MS_T', 'SCHEDULED', 2, 4, 50.0, 50.0, NULL, NULL, 3, 5, NULL),
       (419, TIMESTAMP '2025-07-04 18:30:00', 5, 'KN_T', 'SCHEDULED', 6, 8, 50.0, 50.0, NULL, NULL, 3, 5, NULL),
       (420, TIMESTAMP '2025-07-04 18:30:00', 8, 'SS_T', 'SCHEDULED', 9, 7, 50.0, 50.0, NULL, NULL, 3, 5, NULL),
       (421, TIMESTAMP '2025-07-04 18:30:00', 9, 'SPO_T', 'SCHEDULED', 10, 1, 50.0, 50.0, NULL, NULL, 5, 5, NULL),
       (422, TIMESTAMP '2025-07-04 18:30:00', 2, 'SPO_2T', 'SCHEDULED', 3, 5, 50.0, 50.0, NULL, NULL, 2, 0, NULL),
       (423, TIMESTAMP '2025-07-05 18:30:00', 1, 'MS_T', 'SCHEDULED', 2, 4, 50.0, 50.0, NULL, NULL, 3, 5, NULL),
       (424, TIMESTAMP '2025-07-05 18:30:00', 5, 'KN_T', 'SCHEDULED', 6, 8, 50.0, 50.0, NULL, NULL, 3, 5, NULL),
       (425, TIMESTAMP '2025-07-02 18:30:00', 8, 'SS_T', 'SCHEDULED', 9, 7, 50.0, 50.0, NULL, NULL, 3, 5, NULL),
       (426, TIMESTAMP '2025-07-02 18:30:00', 9, 'SPO_T', 'SCHEDULED', 10, 1, 50.0, 50.0, NULL, NULL, 5, 5, NULL),
       (427, TIMESTAMP '2025-07-02 18:30:00', 2, 'SPO_2T', 'SCHEDULED', 3, 5, 50.0, 50.0, NULL, NULL, 2, 0, NULL),
       (428, TIMESTAMP '2025-07-03 18:30:00', 1, 'MS_T', 'SCHEDULED', 2, 4, 50.0, 50.0, NULL, NULL, 3, 5, NULL),
       (429, TIMESTAMP '2025-07-03 18:30:00', 5, 'KN_T', 'SCHEDULED', 6, 8, 50.0, 50.0, NULL, NULL, 3, 5, NULL),
       (430, TIMESTAMP '2025-07-03 18:30:00', 8, 'SS_T', 'SCHEDULED', 9, 7, 50.0, 50.0, NULL, NULL, 3, 5, NULL),
       (431, TIMESTAMP '2025-07-03 18:30:00', 9, 'SPO_T', 'SCHEDULED', 10, 1, 50.0, 50.0, NULL, NULL, 5, 5, NULL),
       (432, TIMESTAMP '2025-07-03 18:30:00', 2, 'SPO_2T', 'SCHEDULED', 3, 5, 50.0, 50.0, NULL, NULL, 2, 0, NULL),
       (433, TIMESTAMP '2025-07-04 18:30:00', 1, 'MS_T', 'SCHEDULED', 2, 4, 50.0, 50.0, NULL, NULL, 3, 5, NULL),
       (434, TIMESTAMP '2025-07-04 18:30:00', 5, 'KN_T', 'SCHEDULED', 6, 8, 50.0, 50.0, NULL, NULL, 2, 3, NULL),
       (435, TIMESTAMP '2025-07-04 18:30:00', 8, 'SS_T', 'SCHEDULED', 9, 7, 50.0, 50.0, NULL, NULL, 4, 4, NULL),
       (436, TIMESTAMP '2025-07-04 18:30:00', 9, 'SPO_T', 'SCHEDULED', 10, 1, 50.0, 50.0, NULL, NULL, 6, 7, NULL),
       (437, TIMESTAMP '2025-07-04 18:30:00', 2, 'SPO_2T', 'SCHEDULED', 3, 5, 50.0, 50.0, NULL, NULL, 3, 2, NULL),

-- 7월 5일 추가 경기
       (438, TIMESTAMP '2025-07-05 18:30:00', 8, 'SS_T', 'SCHEDULED', 9, 7, 50.0, 50.0, NULL, NULL, 2, 2, NULL),
       (439, TIMESTAMP '2025-07-05 18:30:00', 9, 'SPO_T', 'SCHEDULED', 10, 1, 50.0, 50.0, NULL, NULL, 4, 6, NULL),
       (440, TIMESTAMP '2025-07-05 18:30:00', 2, 'SPO_2T', 'SCHEDULED', 3, 5, 50.0, 50.0, NULL, NULL, 3, 1, NULL),

-- 8월 1일 경기
       (441, TIMESTAMP '2025-08-01 18:30:00', 1, 'MS_T', 'SCHEDULED', 4, 6, 50.0, 50.0, NULL, NULL, NULL, NULL, NULL),
       (442, TIMESTAMP '2025-08-01 18:30:00', 5, 'KN_T', 'SCHEDULED', 8, 2, 50.0, 50.0, NULL, NULL, NULL, NULL, NULL),

-- 8월 2일 경기
       (443, TIMESTAMP '2025-08-02 18:30:00', 8, 'SS_T', 'SCHEDULED', 7, 9, 50.0, 50.0, NULL, NULL, NULL, NULL, NULL),
       (444, TIMESTAMP '2025-08-02 18:30:00', 9, 'SPO_T', 'SCHEDULED', 1, 10, 50.0, 50.0, NULL, NULL, NULL, NULL, NULL),

-- 8월 3일 경기
       (445, TIMESTAMP '2025-08-03 18:30:00', 2, 'SPO_2T', 'SCHEDULED', 3, 5, 50.0, 50.0, NULL, NULL, NULL, NULL, NULL),
       (446, TIMESTAMP '2025-08-03 18:30:00', 1, 'MS_T', 'SCHEDULED', 2, 6, 50.0, 50.0, NULL, NULL, NULL, NULL, NULL);


-- 7. today_game_tb
INSERT INTO today_game_tb (id, game_time, stadium_id, broadcast_channel, game_status,
                           home_team_id, away_team_id,
                           home_win_per, away_win_per,
                           home_prediction_score, away_prediction_score,
                           home_result_score, away_result_score,
                           total_prediction_score, game_id)
VALUES
-- 두산 vs SSG
(1, TIMESTAMP '2025-07-08 18:30:00', 1, 'MS_T', 'SCHEDULED', 2, 4,
 52.0, 48.0, 4.2, 3.9, 3, 5, 8.1, 423),

-- 삼성 vs 한화
(2, TIMESTAMP '2025-07-08 18:30:00', 5, 'KN_T', 'SCHEDULED', 6, 8,
 49.2, 50.8, 3.7, 3.8, 3, 5, 7.5, 424),

-- NC vs 롯데
(3, TIMESTAMP '2025-07-08 18:30:00', 8, 'SS_T', 'SCHEDULED', 9, 7,
 55.6, 44.4, 4.5, 3.6, 3, 5, 8.1, 425),

-- KT vs LG
(4, TIMESTAMP '2025-07-08 18:30:00', 9, 'SPO_T', 'SCHEDULED', 10, 1,
 50.0, 50.0, 3.8, 3.8, 5, 5, 7.6, 426),

-- 키움 vs KIA
(5, TIMESTAMP '2025-07-08 18:30:00', 2, 'SPO_2T', 'SCHEDULED', 3, 5,
 47.6, 52.4, 3.5, 3.9, 2, 0, 7.4, 427);
;

-- 8. player_tb
INSERT INTO player_tb (kbo_player_id, team_id, name)
VALUES (51867, 4, '김건우'),
       (55257, 2, '콜어빈'),
       (52701, 8, '문동주'),
       (55460, 6, '가라비토'),
       (55532, 7, '감보아'),
       (66920, 9, '최성영'),
       (61101, 1, '임찬규'),
       (50030, 10, '소형준'),
       (76225, 5, '김건국'),
       (64350, 3, '하영민'),
       (100201, 2, '홍성우'),
       (100202, 2, '박정호'),
       (100203, 2, '이재원'),
       (100204, 2, '정우영'),
       (100205, 2, '최재훈'),
       (100206, 2, '김재현'),
       (100207, 2, '류지혁'),
       (100208, 2, '한지민'),
       (100209, 2, '노성현'),
       (100301, 4, '김도형'),
       (100302, 4, '서인국'),
       (100303, 4, '정경호'),
       (100304, 4, '남주혁'),
       (100305, 4, '이진혁'),
       (100306, 4, '김요한'),
       (100307, 4, '유승우'),
       (100308, 4, '장도연'),
       (100309, 4, '강호동');

-- 9. 선발투수
INSERT INTO starting_pitcher_tb (game_id, player_id, profile_url,
                                 era, game_count, result, qs, whip)
VALUES (423, 1, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/51867.png', 4.58, 27, '2승 3패', 0,
        1.53),
       (423, 2, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/55257.png', 4.86, 14, '5승 7패', 8,
        1.38),
       (424, 3, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/52701.png', 4.09, 11, '5승 2패', 4,
        1.2),
       (424, 4, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/55460.png', NULL, NULL, '없음', NULL,
        NULL),
       (425, 5, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/55532.png', 2.37, 5, '4승 1패', 4,
        1.02),
       (425, 6, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/66920.png', 5.34, 17, '2승 2패', 0,
        1.71),
       (426, 7, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/61101.png', 2.61, 14, '8승 2패', 9,
        1.18),
       (426, 8, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/50030.png', 3.13, 13, '5승 2패', 10,
        1.18),
       (427, 9, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/76225.png', 7.53, 11, '없음', 0,
        1.88),
       (427, 10, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/64350.png', 4.88, 15, '6승 7패', 8,
        1.51);

-- 10. 오늘의 선발투수
INSERT INTO today_starting_pitcher_tb (game_id, player_id, profile_url,
                                       era, game_count, qs, whip, result)
VALUES (423, 1, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/51867.png', 4.58, 27, 0, 1.53,
        '2승 3패'),
       (423, 2, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/55257.png', 4.86, 14, 8, 1.38,
        '5승 7패'),
       (424, 3, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/52701.png', 4.09, 11, 4, 1.2,
        '5승 2패'),
       (424, 4, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/55460.png', NULL, NULL, NULL, NULL,
        '없음'),
       (425, 5, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/55532.png', 2.37, 5, 4, 1.02,
        '4승 1패'),
       (425, 6, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/66920.png', 5.34, 17, 0, 1.71,
        '2승 2패'),
       (426, 7, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/61101.png', 2.61, 14, 9, 1.18,
        '8승 2패'),
       (426, 8, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/50030.png', 3.13, 13, 10, 1.18,
        '5승 2패'),
       (427, 9, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/76225.png', 7.53, 11, 0, 1.88,
        '없음'),
       (427, 10, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/64350.png', 4.88, 15, 8, 1.51,
        '6승 7패'),
       (428, 1, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/51867.png', 4.58, 27, 0, 1.53,
        '2승 3패'),
       (428, 2, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/55257.png', 4.86, 14, 8, 1.38,
        '5승 7패'),
       (429, 3, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/52701.png', 4.09, 11, 4, 1.2,
        '5승 2패'),
       (429, 4, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/55460.png', NULL, NULL, NULL, NULL,
        '없음'),
       (430, 5, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/55532.png', 2.37, 5, 4, 1.02,
        '4승 1패'),
       (430, 6, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/66920.png', 5.34, 17, 0, 1.71,
        '2승 2패'),
       (431, 7, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/61101.png', 2.61, 14, 9, 1.18,
        '8승 2패'),
       (431, 8, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/50030.png', 3.13, 13, 10, 1.18,
        '5승 2패'),
       (432, 9, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/76225.png', 7.53, 11, 0, 1.88,
        '없음'),
       (432, 10, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/64350.png', 4.88, 15, 8, 1.51,
        '6승 7패');


INSERT INTO board_tb (team_id, user_id, title, content, delete_status, created_at)
VALUES (3, 1, '직관 다녀온 후기', '롯데 경기 진짜 재밌었음. 9회말 역전승은 감동이었어.', 'NOT_DELETED', '2025-06-30 16:30:00'),
       (3, 1, '직관 다녀온 후기', '롯데 경기 진짜 재밌었음. 9회말 역전승은 감동이었어.', 'NOT_DELETED', '2025-06-30 16:30:00'),
       (3, 1, '직관 다녀온 후기', '롯데 경기 진짜 재밌었음. 9회말 역전승은 감동이었어.', 'NOT_DELETED', '2025-06-30 16:30:00'),
       (3, 1, '직관 다녀온 후기', '롯데 경기 진짜 재밌었음. 9회말 역전승은 감동이었어.', 'NOT_DELETED', '2025-06-30 16:30:00'),
       (3, 1, '직관 다녀온 후기', '롯데 경기 진짜 재밌었음. 9회말 역전승은 감동이었어.', 'NOT_DELETED', '2025-06-30 16:30:00'),
       (3, 1, '직관 다녀온 후기', '롯데 경기 진짜 재밌었음. 9회말 역전승은 감동이었어.', 'NOT_DELETED', '2025-06-30 16:30:00'),
       (3, 1, '직관 다녀온 후기', '롯데 경기 진짜 재밌었음. 9회말 역전승은 감동이었어.', 'NOT_DELETED', '2025-06-30 16:30:00'),
       (3, 1, '직관 다녀온 후기', '롯데 경기 진짜 재밌었음. 9회말 역전승은 감동이었어.', 'NOT_DELETED', '2025-06-30 16:30:00'),
       (3, 1, '직관 다녀온 후기', '롯데 경기 진짜 재밌었음. 9회말 역전승은 감동이었어.', 'NOT_DELETED', '2025-06-30 16:30:00'),
       (3, 1, '직관 다녀온 후기', '롯데 경기 진짜 재밌었음. 9회말 역전승은 감동이었어.', 'NOT_DELETED', '2025-06-30 16:30:00'),
       (3, 1, '직관 다녀온 후기', '롯데 경기 진짜 재밌었음. 9회말 역전승은 감동이었어.', 'NOT_DELETED', '2025-06-29 15:30:00');

-- 2. team_record_tb에 데이터 삽입
INSERT INTO team_record_tb (team_id, team_rank, total_game, win_game, lose_game, tie_game, win_rate, gap,
                            recent_ten_game,
                            streak, ops, r, era)
VALUES (1, 1, 78, 45, 32, 1, 0.584, 0, '5승0무5패', '1패', 0.950, 720, 3.50),
       (2, 2, 79, 44, 33, 2, 0.571, 1, '4승0무6패', '2패', 0.930, 710, 3.20),
       (3, 3, 80, 43, 34, 3, 0.558, 3.5, '6승0무4패', '1승', 0.920, 690, 3.40),
       (4, 4, 79, 41, 35, 3, 0.539, 5, '6승2무2패', '2승', 0.880, 680, 3.60),
       (5, 5, 78, 39, 36, 3, 0.520, 5, '5승1무4패', '1패', 0.870, 650, 3.70),
       (6, 6, 80, 40, 37, 3, 0.519, 6.5, '4승0무6패', '1패', 0.850, 630, 3.80),
       (7, 7, 79, 39, 39, 3, 0.500, 6, '3승0무7패', '4패', 0.840, 620, 3.90),
       (8, 8, 76, 35, 37, 4, 0.486, 7.5, '4승2무4패', '1패', 0.830, 600, 4.00),
       (9, 9, 79, 31, 45, 3, 0.408, 13.5, '4승0무6패', '1승', 0.810, 570, 4.20),
       (10, 10, 82, 25, 54, 3, 0.316, 21, '5승1무4패', '3승', 0.790, 540, 4.30);

-- 2. today_team_record_tb에 데이터 삽입
INSERT INTO today_team_record_tb (team_id, era, gap, win_game, lose_game, tie_game, total_game, win_rate, ops, r,
                                  team_rank, created_at, recent_ten_game, streak)
VALUES (8, 3.43, 0.0, 46, 33, 1, 80, 0.582, 0.754, 411, 1, NOW(), '4승0무6패', '1패'),
       (1, 3.52, 1.0, 45, 34, 2, 81, 0.570, 0.753, 425, 2, NOW(), '4승0무6패', '1패'),
       (7, 3.61, 2.0, 44, 35, 3, 82, 0.557, 0.758, 418, 3, NOW(), '6승0무4패', '1승'),
       (5, 3.84, 3.5, 42, 36, 3, 81, 0.538, 0.739, 391, 4, NOW(), '5승2무3패', '1패'),
       (4, 4.18, 5.0, 40, 37, 3, 80, 0.519, 0.710, 361, 5, NOW(), '5승1무4패', '1승'),
       (10, 4.22, 5.0, 41, 38, 3, 82, 0.519, 0.732, 370, 6, NOW(), '5승0무5패', '1승'),
       (6, 4.43, 6.5, 40, 40, 1, 81, 0.500, 0.692, 342, 7, NOW(), '2승0무8패', '1패'),
       (9, 4.56, 7.5, 36, 38, 4, 78, 0.486, 0.703, 354, 8, NOW(), '6승0무4패', '1승'),
       (2, 4.69, 13.5, 32, 46, 3, 81, 0.410, 0.683, 328, 9, NOW(), '5승0무5패', '1승'),
       (3, 5.41, 21.0, 26, 55, 3, 84, 0.321, 0.653, 313, 10, NOW(), '5승1무4패', '1패');

INSERT INTO today_hitter_lineup_tb (game_id, team_id, player_id, today_hitter_order, position,
                                    ab, h, avg, season_avg, ops, created_at)
VALUES (423, 2, 11, 1, '1루수', 3, 1, 0.333, 0.285, 0.821, CURRENT_TIMESTAMP),
       (423, 2, 12, 2, '2루수', 4, 2, 0.500, 0.312, 0.920, CURRENT_TIMESTAMP),
       (423, 2, 13, 3, '3루수', 2, 1, 0.500, 0.298, 0.850, CURRENT_TIMESTAMP),
       (423, 2, 14, 4, '유격수', 3, 0, 0.000, 0.245, 0.650, CURRENT_TIMESTAMP),
       (423, 2, 15, 5, '좌익수', 3, 2, 0.667, 0.320, 0.940, CURRENT_TIMESTAMP),
       (423, 2, 16, 6, '중견수', 4, 1, 0.250, 0.278, 0.770, CURRENT_TIMESTAMP),
       (423, 2, 17, 7, '우익수', 2, 1, 0.500, 0.299, 0.865, CURRENT_TIMESTAMP),
       (423, 2, 18, 8, '포수', 3, 1, 0.333, 0.260, 0.745, CURRENT_TIMESTAMP),
       (423, 2, 19, 9, '지명타자', 3, 0, 0.000, 0.240, 0.680, CURRENT_TIMESTAMP),
       (423, 4, 20, 1, '1루수', 3, 2, 0.667, 0.310, 0.910, CURRENT_TIMESTAMP),
       (423, 4, 21, 2, '2루수', 4, 2, 0.500, 0.288, 0.850, CURRENT_TIMESTAMP),
       (423, 4, 22, 3, '3루수', 2, 0, 0.000, 0.260, 0.690, CURRENT_TIMESTAMP),
       (423, 4, 23, 4, '유격수', 3, 1, 0.333, 0.275, 0.800, CURRENT_TIMESTAMP),
       (423, 4, 24, 5, '좌익수', 3, 1, 0.333, 0.282, 0.815, CURRENT_TIMESTAMP),
       (423, 4, 25, 6, '중견수', 4, 3, 0.750, 0.340, 1.020, CURRENT_TIMESTAMP),
       (423, 4, 26, 7, '우익수', 3, 1, 0.333, 0.295, 0.880, CURRENT_TIMESTAMP),
       (423, 4, 27, 8, '포수', 2, 0, 0.000, 0.255, 0.700, CURRENT_TIMESTAMP),
       (423, 4, 28, 9, '지명타자', 3, 2, 0.667, 0.305, 0.950, CURRENT_TIMESTAMP);

-- 날씨
INSERT INTO weather_ultra_tb (game_id, humidity_per, rain_amount, rain_per, stadium_id,
                              temperature, wind_speed, created_at, forecast_at,
                              weather_code, wind_direction)
VALUES (418, 95.0, 0.0, 0.0, 1, 24.0, 1.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 06:00:00', 'DB01', 'SOUTH_EAST'),
       (418, 90.0, 0.0, 0.0, 1, 26.0, 1.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 07:00:00', 'DB01', 'SOUTH'),
       (418, 85.0, 0.0, 0.0, 1, 27.0, 1.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 08:00:00', 'DB01', 'SOUTH_WEST'),
       (418, 80.0, 0.0, 0.0, 1, 28.0, 1.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 09:00:00', 'DB01', 'WEST'),
       (418, 75.0, 0.0, 20.0, 1, 29.0, 1.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 10:00:00', 'DB01', 'NORTH_WEST'),
       (418, 65.0, 0.0, 20.0, 1, 30.0, 2.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 11:00:00', 'DB01', 'NORTH_WEST'),
       (418, 65.0, 0.0, 30.0, 1, 32.0, 2.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 12:00:00', 'DB01', 'NORTH_WEST'),
       (418, 60.0, 0.0, 30.0, 1, 33.0, 2.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 13:00:00', 'DB01', 'NORTH_WEST'),
       (418, 55.0, 0.0, 20.0, 1, 34.0, 2.7, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 14:00:00', 'DB01', 'NORTH_WEST'),
       (418, 60.0, 0.0, 0.0, 1, 33.0, 3.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 15:00:00', 'DB01', 'NORTH_WEST'),
       (418, 60.0, 0.0, 0.0, 1, 33.0, 3.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 16:00:00', 'DB01', 'NORTH_WEST'),
       (418, 70.0, 0.0, 20.0, 1, 32.0, 3.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 17:00:00', 'DB01', 'NORTH_WEST'),
       (418, 75.0, 0.0, 30.0, 1, 31.0, 3.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 18:00:00', 'DB01', 'NORTH_WEST'),
       (418, 80.0, 0.0, 0.0, 1, 28.0, 1.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 19:00:00', 'DB01', 'NORTH_WEST'),
       (418, 80.0, 0.0, 20.0, 1, 27.0, 1.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 20:00:00', 'DB01', 'NORTH_WEST'),
       (418, 85.0, 0.0, 30.0, 1, 26.0, 1.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 21:00:00', 'DB01', 'NORTH'),
       (418, 90.0, 0.0, 30.0, 1, 26.0, 0.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 22:00:00', 'DB01', 'NORTH'),
       (418, 90.0, 0.0, 30.0, 1, 25.0, 0.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 23:00:00', 'DB01', 'NORTH'),
       (419, 85.0, 0.0, 0.0, 5, 25.0, 1.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 06:00:00', 'DB01', 'NORTH_WEST'),
       (419, 75.0, 0.0, 0.0, 5, 27.0, 1.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 07:00:00', 'DB01', 'NORTH'),
       (419, 75.0, 0.0, 0.0, 5, 28.0, 2.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 08:00:00', 'DB01', 'NORTH'),
       (419, 65.0, 0.0, 0.0, 5, 30.0, 2.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 09:00:00', 'DB01', 'NORTH'),
       (419, 60.0, 0.0, 0.0, 5, 31.0, 2.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 10:00:00', 'DB01', 'NORTH'),
       (419, 55.0, 0.0, 0.0, 5, 33.0, 2.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 11:00:00', 'DB01', 'NORTH'),
       (419, 50.0, 0.0, 0.0, 5, 34.0, 2.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 12:00:00', 'DB01', 'NORTH'),
       (419, 45.0, 0.0, 20.0, 5, 35.0, 2.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 13:00:00', 'DB01', 'NORTH'),
       (419, 45.0, 0.0, 20.0, 5, 35.0, 2.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 14:00:00', 'DB01', 'NORTH'),
       (419, 45.0, 0.0, 0.0, 5, 36.0, 2.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 15:00:00', 'DB01', 'NORTH'),
       (419, 50.0, 0.0, 0.0, 5, 36.0, 2.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 16:00:00', 'DB01', 'NORTH'),
       (419, 50.0, 0.0, 0.0, 5, 34.0, 1.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 17:00:00', 'DB01', 'NORTH'),
       (419, 55.0, 0.0, 0.0, 5, 32.0, 1.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 18:00:00', 'DB01', 'SOUTH_EAST'),
       (419, 60.0, 0.0, 0.0, 5, 31.0, 2.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 19:00:00', 'DB01', 'SOUTH_EAST'),
       (419, 65.0, 0.0, 0.0, 5, 29.0, 2.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 20:00:00', 'DB01', 'SOUTH_EAST'),
       (419, 70.0, 0.0, 0.0, 5, 28.0, 2.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 21:00:00', 'DB01', 'SOUTH_EAST'),
       (419, 80.0, 0.0, 20.0, 5, 27.0, 1.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 22:00:00', 'DB01', 'SOUTH_EAST'),
       (419, 80.0, 0.0, 0.0, 5, 27.0, 1.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 23:00:00', 'DB01', 'SOUTH_EAST'),
       (420, 85.0, 0.0, 0.0, 9, 25.0, 1.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 06:00:00', 'DB01', 'NORTH_WEST'),
       (420, 75.0, 0.0, 0.0, 9, 27.0, 2.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 07:00:00', 'DB01', 'NORTH'),
       (420, 70.0, 0.0, 0.0, 9, 29.0, 2.7, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 08:00:00', 'DB01', 'NORTH'),
       (420, 65.0, 0.0, 0.0, 9, 31.0, 3.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 09:00:00', 'DB01', 'NORTH'),
       (420, 60.0, 0.0, 0.0, 9, 32.0, 3.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 10:00:00', 'DB01', 'NORTH'),
       (420, 55.0, 0.0, 0.0, 9, 33.0, 2.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 11:00:00', 'DB01', 'NORTH'),
       (420, 50.0, 0.0, 0.0, 9, 34.0, 2.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 12:00:00', 'DB01', 'NORTH'),
       (420, 45.0, 0.0, 20.0, 9, 35.0, 2.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 13:00:00', 'DB01', 'NORTH'),
       (420, 40.0, 0.0, 20.0, 9, 36.0, 2.7, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 14:00:00', 'DB01', 'NORTH'),
       (420, 45.0, 0.0, 0.0, 9, 37.0, 2.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 15:00:00', 'DB01', 'NORTH'),
       (420, 45.0, 0.0, 0.0, 9, 36.0, 1.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 16:00:00', 'DB01', 'NORTH'),
       (420, 50.0, 0.0, 0.0, 9, 34.0, 0.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 17:00:00', 'DB01', 'NORTH_EAST'),
       (420, 55.0, 0.0, 0.0, 9, 33.0, 2.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 18:00:00', 'DB01', 'SOUTH_EAST'),
       (420, 60.0, 0.0, 0.0, 9, 32.0, 3.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 19:00:00', 'DB01', 'SOUTH_EAST'),
       (420, 65.0, 0.0, 0.0, 9, 30.0, 2.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 20:00:00', 'DB01', 'SOUTH_EAST'),
       (420, 70.0, 0.0, 0.0, 9, 29.0, 2.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 21:00:00', 'DB01', 'SOUTH_EAST'),
       (420, 75.0, 0.0, 0.0, 9, 28.0, 1.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 22:00:00', 'DB01', 'SOUTH_EAST'),
       (420, 75.0, 0.0, 0.0, 9, 27.0, 1.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 23:00:00', 'DB01', 'SOUTH_EAST'),
       (421, 85.0, 0.0, 20.0, 9, 27.0, 1.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 07:00:00', 'DB01', 'SOUTH'),
       (421, 85.0, 0.0, 30.0, 9, 27.0, 1.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 08:00:00', 'DB01', 'SOUTH'),
       (421, 80.0, 0.0, 30.0, 9, 28.0, 2.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 09:00:00', 'DB01', 'SOUTH_WEST'),
       (421, 80.0, 0.0, 30.0, 9, 28.0, 2.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 10:00:00', 'DB01', 'SOUTH_WEST'),
       (421, 75.0, 0.0, 30.0, 9, 29.0, 3.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 11:00:00', 'DB01', 'WEST'),
       (421, 70.0, 0.0, 30.0, 9, 30.0, 3.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 12:00:00', 'DB01', 'WEST'),
       (421, 70.0, 0.0, 20.0, 9, 30.0, 3.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 13:00:00', 'DB01', 'WEST'),
       (421, 65.0, 0.0, 20.0, 9, 32.0, 3.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 14:00:00', 'DB01', 'WEST'),
       (421, 60.0, 0.0, 30.0, 9, 32.0, 3.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 15:00:00', 'DB01', 'WEST'),
       (421, 60.0, 0.0, 20.0, 9, 33.0, 3.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 16:00:00', 'DB01', 'WEST'),
       (421, 60.0, 0.0, 20.0, 9, 32.0, 3.7, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 17:00:00', 'DB01', 'WEST'),
       (421, 60.0, 0.0, 20.0, 9, 32.0, 3.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 18:00:00', 'DB01', 'WEST'),
       (421, 65.0, 0.0, 30.0, 9, 31.0, 2.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 19:00:00', 'DB01', 'WEST'),
       (421, 70.0, 0.0, 0.0, 9, 30.0, 1.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 20:00:00', 'DB01', 'WEST'),
       (421, 75.0, 0.0, 0.0, 9, 30.0, 1.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 21:00:00', 'DB01', 'WEST'),
       (421, 80.0, 0.0, 0.0, 9, 29.0, 1.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 22:00:00', 'DB01', 'WEST'),
       (421, 80.0, 0.0, 0.0, 9, 28.0, 0.7, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 23:00:00', 'DB01', 'WEST'),
       (422, 85.0, 0.0, 20.0, 2, 26.0, 1.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 06:00:00', 'DB01', 'SOUTH'),
       (422, 85.0, 0.0, 20.0, 2, 26.0, 1.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 07:00:00', 'DB01', 'SOUTH'),
       (422, 85.0, 0.0, 30.0, 2, 27.0, 2.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 08:00:00', 'DB01', 'SOUTH_WEST'),
       (422, 80.0, 0.0, 30.0, 2, 27.0, 2.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 09:00:00', 'DB01', 'SOUTH_WEST'),
       (422, 80.0, 0.0, 30.0, 2, 28.0, 3.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 10:00:00', 'DB01', 'SOUTH_WEST'),
       (422, 70.0, 0.0, 30.0, 2, 28.0, 3.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 11:00:00', 'DB01', 'SOUTH_WEST'),
       (422, 70.0, 0.0, 30.0, 2, 29.0, 3.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 12:00:00', 'DB01', 'SOUTH_WEST'),
       (422, 65.0, 0.0, 30.0, 2, 30.0, 3.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 13:00:00', 'DB01', 'SOUTH_WEST'),
       (422, 65.0, 0.0, 20.0, 2, 31.0, 4.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 14:00:00', 'DB01', 'WEST'),
       (422, 65.0, 0.0, 20.0, 2, 31.0, 4.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 15:00:00', 'DB01', 'WEST'),
       (422, 65.0, 0.0, 0.0, 2, 31.0, 3.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 16:00:00', 'DB01', 'WEST'),
       (422, 65.0, 0.0, 0.0, 2, 31.0, 3.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 17:00:00', 'DB01', 'WEST'),
       (422, 65.0, 0.0, 0.0, 2, 31.0, 2.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 18:00:00', 'DB01', 'WEST'),
       (422, 70.0, 0.0, 0.0, 2, 30.0, 2.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 19:00:00', 'DB01', 'WEST'),
       (422, 75.0, 0.0, 20.0, 2, 29.0, 1.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 20:00:00', 'DB01', 'WEST'),
       (422, 80.0, 0.0, 0.0, 2, 28.0, 1.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 21:00:00', 'DB01', 'WEST'),
       (422, 80.0, 0.0, 0.0, 2, 28.0, 0.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 22:00:00', 'DB01', 'WEST'),
       (422, 85.0, 0.0, 0.0, 2, 27.0, 0.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-06 23:00:00', 'DB01', 'WEST'),
       (423, 85.0, 0.0, 20.0, 1, 26.0, 1.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 06:00:00', 'DB01', 'SOUTH'),
       (423, 85.0, 0.0, 20.0, 1, 26.0, 1.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 07:00:00', 'DB01', 'SOUTH'),
       (423, 85.0, 0.0, 30.0, 1, 27.0, 2.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 08:00:00', 'DB01', 'SOUTH_WEST'),
       (423, 80.0, 0.0, 30.0, 1, 27.0, 2.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 09:00:00', 'DB01', 'SOUTH_WEST'),
       (423, 80.0, 0.0, 30.0, 1, 28.0, 3.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 10:00:00', 'DB01', 'SOUTH_WEST'),
       (423, 70.0, 0.0, 30.0, 1, 28.0, 3.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 11:00:00', 'DB01', 'SOUTH_WEST'),
       (423, 70.0, 0.0, 30.0, 1, 29.0, 3.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 12:00:00', 'DB01', 'SOUTH_WEST'),
       (423, 65.0, 0.0, 30.0, 1, 30.0, 3.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 13:00:00', 'DB01', 'SOUTH_WEST'),
       (423, 65.0, 0.0, 20.0, 1, 31.0, 4.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 14:00:00', 'DB01', 'WEST'),
       (423, 65.0, 0.0, 20.0, 1, 31.0, 4.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 15:00:00', 'DB01', 'WEST'),
       (423, 65.0, 0.0, 0.0, 1, 31.0, 3.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 16:00:00', 'DB01', 'WEST'),
       (423, 65.0, 0.0, 0.0, 1, 31.0, 3.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 17:00:00', 'DB01', 'WEST'),
       (423, 65.0, 0.0, 0.0, 1, 31.0, 2.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 18:00:00', 'DB01', 'WEST'),
       (423, 70.0, 0.0, 0.0, 1, 30.0, 2.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 19:00:00', 'DB01', 'WEST'),
       (423, 75.0, 0.0, 20.0, 1, 29.0, 1.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 20:00:00', 'DB01', 'WEST'),
       (423, 80.0, 0.0, 0.0, 1, 28.0, 1.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 21:00:00', 'DB01', 'WEST'),
       (423, 80.0, 0.0, 0.0, 1, 28.0, 0.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 22:00:00', 'DB01', 'WEST'),
       (423, 85.0, 0.0, 0.0, 1, 27.0, 0.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 23:00:00', 'DB01', 'WEST'),
       (424, 85.0, 0.0, 20.0, 5, 27.0, 1.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 06:00:00', 'DB01', 'SOUTH_EAST'),
       (424, 85.0, 0.0, 20.0, 5, 27.0, 1.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 07:00:00', 'DB01', 'SOUTH'),
       (424, 85.0, 0.0, 30.0, 5, 27.0, 1.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 08:00:00', 'DB01', 'SOUTH'),
       (424, 80.0, 0.0, 30.0, 5, 28.0, 2.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 09:00:00', 'DB01', 'SOUTH_WEST'),
       (424, 80.0, 0.0, 30.0, 5, 28.0, 2.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 10:00:00', 'DB01', 'SOUTH_WEST'),
       (424, 75.0, 0.0, 30.0, 5, 29.0, 3.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 11:00:00', 'DB01', 'WEST'),
       (424, 70.0, 0.0, 30.0, 5, 30.0, 3.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 12:00:00', 'DB01', 'WEST'),
       (424, 70.0, 0.0, 20.0, 5, 30.0, 3.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 13:00:00', 'DB01', 'WEST'),
       (424, 65.0, 0.0, 20.0, 5, 32.0, 3.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 14:00:00', 'DB01', 'WEST'),
       (424, 60.0, 0.0, 30.0, 5, 32.0, 3.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 15:00:00', 'DB01', 'WEST'),
       (424, 60.0, 0.0, 20.0, 5, 33.0, 3.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 16:00:00', 'DB01', 'WEST'),
       (424, 60.0, 0.0, 20.0, 5, 32.0, 3.7, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 17:00:00', 'DB01', 'WEST'),
       (424, 60.0, 0.0, 20.0, 5, 32.0, 3.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 18:00:00', 'DB01', 'WEST'),
       (424, 65.0, 0.0, 30.0, 5, 31.0, 2.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 19:00:00', 'DB01', 'WEST'),
       (424, 70.0, 0.0, 0.0, 5, 30.0, 1.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 20:00:00', 'DB01', 'WEST'),
       (424, 75.0, 0.0, 0.0, 5, 30.0, 1.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 21:00:00', 'DB01', 'WEST'),
       (424, 80.0, 0.0, 0.0, 5, 29.0, 1.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 22:00:00', 'DB01', 'WEST'),
       (424, 80.0, 0.0, 0.0, 5, 28.0, 0.7, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 23:00:00', 'DB01', 'WEST'),
       (425, 85.0, 0.0, 0.0, 8, 25.0, 1.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 06:00:00', 'DB01', 'NORTH_WEST'),
       (425, 75.0, 0.0, 0.0, 8, 27.0, 2.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 07:00:00', 'DB01', 'NORTH'),
       (425, 70.0, 0.0, 0.0, 8, 29.0, 2.7, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 08:00:00', 'DB01', 'NORTH'),
       (425, 65.0, 0.0, 0.0, 8, 31.0, 3.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 09:00:00', 'DB01', 'NORTH'),
       (425, 60.0, 0.0, 0.0, 8, 32.0, 3.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 10:00:00', 'DB01', 'NORTH'),
       (425, 55.0, 0.0, 0.0, 8, 33.0, 2.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 11:00:00', 'DB01', 'NORTH'),
       (425, 50.0, 0.0, 0.0, 8, 34.0, 2.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 12:00:00', 'DB01', 'NORTH'),
       (425, 45.0, 0.0, 20.0, 8, 35.0, 2.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 13:00:00', 'DB01', 'NORTH'),
       (425, 40.0, 0.0, 20.0, 8, 36.0, 2.7, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 14:00:00', 'DB01', 'NORTH'),
       (425, 45.0, 0.0, 0.0, 8, 37.0, 2.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 15:00:00', 'DB01', 'NORTH'),
       (425, 45.0, 0.0, 0.0, 8, 36.0, 1.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 16:00:00', 'DB01', 'NORTH'),
       (425, 50.0, 0.0, 0.0, 8, 34.0, 0.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 17:00:00', 'DB01', 'NORTH_EAST'),
       (425, 55.0, 0.0, 0.0, 8, 33.0, 2.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 18:00:00', 'DB01', 'SOUTH_EAST'),
       (425, 60.0, 0.0, 0.0, 8, 32.0, 3.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 19:00:00', 'DB01', 'SOUTH_EAST'),
       (425, 65.0, 0.0, 0.0, 8, 30.0, 2.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 20:00:00', 'DB01', 'SOUTH_EAST'),
       (425, 70.0, 0.0, 0.0, 8, 29.0, 2.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 21:00:00', 'DB01', 'SOUTH_EAST'),
       (425, 75.0, 0.0, 0.0, 8, 28.0, 1.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 22:00:00', 'DB01', 'SOUTH_EAST'),
       (425, 75.0, 0.0, 0.0, 8, 27.0, 1.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 23:00:00', 'DB01', 'SOUTH_EAST'),
       (426, 85.0, 0.0, 0.0, 9, 25.0, 1.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 06:00:00', 'DB01', 'NORTH_WEST'),
       (426, 75.0, 0.0, 0.0, 9, 27.0, 1.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 07:00:00', 'DB01', 'NORTH'),
       (426, 75.0, 0.0, 0.0, 9, 28.0, 2.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 08:00:00', 'DB01', 'NORTH'),
       (426, 65.0, 0.0, 0.0, 9, 30.0, 2.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 09:00:00', 'DB01', 'NORTH'),
       (426, 60.0, 0.0, 0.0, 9, 31.0, 2.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 10:00:00', 'DB01', 'NORTH'),
       (426, 55.0, 0.0, 0.0, 9, 33.0, 2.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 11:00:00', 'DB01', 'NORTH'),
       (426, 50.0, 0.0, 0.0, 9, 34.0, 2.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 12:00:00', 'DB01', 'NORTH'),
       (426, 45.0, 0.0, 20.0, 9, 35.0, 2.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 13:00:00', 'DB01', 'NORTH'),
       (426, 45.0, 0.0, 20.0, 9, 35.0, 2.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 14:00:00', 'DB01', 'NORTH'),
       (426, 45.0, 0.0, 0.0, 9, 36.0, 2.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 15:00:00', 'DB01', 'NORTH'),
       (426, 50.0, 0.0, 0.0, 9, 36.0, 2.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 16:00:00', 'DB01', 'NORTH'),
       (426, 50.0, 0.0, 0.0, 9, 34.0, 1.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 17:00:00', 'DB01', 'NORTH'),
       (426, 55.0, 0.0, 0.0, 9, 32.0, 1.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 18:00:00', 'DB01', 'SOUTH_EAST'),
       (426, 60.0, 0.0, 0.0, 9, 31.0, 2.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 19:00:00', 'DB01', 'SOUTH_EAST'),
       (426, 65.0, 0.0, 0.0, 9, 29.0, 2.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 20:00:00', 'DB01', 'SOUTH_EAST'),
       (426, 70.0, 0.0, 0.0, 9, 28.0, 2.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 21:00:00', 'DB01', 'SOUTH_EAST'),
       (426, 80.0, 0.0, 20.0, 9, 27.0, 1.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 22:00:00', 'DB01', 'SOUTH_EAST'),
       (426, 80.0, 0.0, 0.0, 9, 27.0, 1.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 23:00:00', 'DB01', 'SOUTH_EAST'),
       (427, 95.0, 0.0, 0.0, 2, 24.0, 1.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 06:00:00', 'DB01', 'SOUTH_EAST'),
       (427, 90.0, 0.0, 0.0, 2, 26.0, 1.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 07:00:00', 'DB01', 'SOUTH'),
       (427, 85.0, 0.0, 0.0, 2, 27.0, 1.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 08:00:00', 'DB01', 'SOUTH_WEST'),
       (427, 80.0, 0.0, 0.0, 2, 28.0, 1.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 09:00:00', 'DB01', 'WEST'),
       (427, 75.0, 0.0, 20.0, 2, 29.0, 1.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 10:00:00', 'DB01', 'NORTH_WEST'),
       (427, 65.0, 0.0, 20.0, 2, 30.0, 2.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 11:00:00', 'DB01', 'NORTH_WEST'),
       (427, 65.0, 0.0, 30.0, 2, 32.0, 2.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 12:00:00', 'DB01', 'NORTH_WEST'),
       (427, 60.0, 0.0, 30.0, 2, 33.0, 2.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 13:00:00', 'DB01', 'NORTH_WEST'),
       (427, 55.0, 0.0, 20.0, 2, 34.0, 2.7, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 14:00:00', 'DB01', 'NORTH_WEST'),
       (427, 60.0, 0.0, 0.0, 2, 33.0, 3.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 15:00:00', 'DB01', 'NORTH_WEST'),
       (427, 60.0, 0.0, 0.0, 2, 33.0, 3.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 16:00:00', 'DB01', 'NORTH_WEST'),
       (427, 70.0, 0.0, 20.0, 2, 32.0, 3.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 17:00:00', 'DB01', 'NORTH_WEST'),
       (427, 75.0, 0.0, 30.0, 2, 31.0, 3.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 18:00:00', 'DB01', 'NORTH_WEST'),
       (427, 80.0, 0.0, 0.0, 2, 28.0, 1.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 19:00:00', 'DB01', 'NORTH_WEST'),
       (427, 80.0, 0.0, 20.0, 2, 27.0, 1.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 20:00:00', 'DB01', 'NORTH_WEST'),
       (427, 85.0, 0.0, 30.0, 2, 26.0, 1.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 21:00:00', 'DB01', 'NORTH'),
       (427, 90.0, 0.0, 30.0, 2, 26.0, 0.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 22:00:00', 'DB01', 'NORTH'),
       (427, 90.0, 0.0, 30.0, 2, 25.0, 0.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 23:00:00', 'DB01', 'NORTH');