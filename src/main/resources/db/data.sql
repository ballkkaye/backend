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
VALUES (1, NULL, 'LG 트윈스', '/img/logo/LG로고.png',
        'https://www.lgtwins.com/service/html.ncd?baRs=OUT_DS&view=%2Fpc_twins%2Fticket%2Fticketbuy&actID=BR_RetrieveTicketing'),
       (1, NULL, '두산 베어스', '/img/logo/두산로고.png',
        'https://ticket.interpark.com/Contents/Sports/GoodsInfo?SportsCode=07001&TeamCode=PB004'),
       (2, NULL, '키움 히어로즈', '/img/logo/키움로고.png',
        'https://ticket.interpark.com/Contents/Sports/GoodsInfo?SportsCode=07001&TeamCode=PB003'),
       (3, NULL, 'SSG 랜더스', '/img/logo/SSG로고.png',
        'https://www.ticketlink.co.kr/sports#reservation'),
       (4, 13, 'KIA 타이거즈', '/img/logo/KIA로고.png',
        'https://tigers.co.kr/ticket/reservation'),
       (5, 11, '삼성 라이온즈', '/img/logo/삼성로고.png',
        'https://www.ticketlink.co.kr/sports#reservation'),
       (6, 12, '롯데 자이언츠', '/img/logo/롯데로고.png',
        'https://ticket.giantsclub.com/loginForm.do'),
       (7, 10, '한화 이글스', '/img/logo/한화로고.png',
        'https://www.hanwhaeagles.co.kr/ticketInfo.do'),
       (8, NULL, 'NC 다이노스', '/img/logo/NC로고.png',
        'https://ticket.ncdinos.com/login'),
       (9, NULL, 'KT 위즈', '/img/logo/KT로고.png',
        'https://www.ktwiz.co.kr/ticket/reservation');

-- 5.user_tb
INSERT INTO user_tb
(username, password, name, nickname, team_id, phone_number, email, birth_date, gender, profile_url, provider_type,
 user_role, fcm_token, prediction_score, prediction_tier)
VALUES ('ssar123', '1234', '쌀', 'ssar', 1, '01011112222', 'ssar@nate.com', '1999-09-09', 'MALE', '/img/profile.png',
        'BALLKKAYE', 'USER', '1111', 0, 'NONE'),
       ('cos123', '1234', '코스', 'cos', 2, '01022223333', 'cos@nate.com', '2000-01-01', 'FEMALE', '/img/profile.png',
        'BALLKKAYE', 'USER', '2222', 50, 'IRON'),
       ('love123', '1234', '러브', 'love', 3, '01033334444', 'love@nate.com', '1998-08-08', 'MALE', '/img/profile.png',
        'BALLKKAYE', 'USER', '3333', 200, 'BRONZE'),
       ('haha123', '1234', '하하', 'haha', NULL, '01044445555', 'haha@nate.com', '1997-07-07', 'FEMALE',
        '/img/profile.png', 'BALLKKAYE', 'ADMIN', null, 400, 'SILVER'),
       ('harim123', '1234', '백하림', 'harim', 4, '01055556666', 'harim@example.com', '1995-05-05', 'MALE',
        '/img/profile.png', 'BALLKKAYE', 'USER', '4444', 600, 'GOLD'),
       ('jungwon123', '1234', '김정원', 'jungwon', 5, '01066667777', 'jungwon@example.com', '1996-06-06', 'MALE',
        '/img/profile.png', 'BALLKKAYE', 'USER', '5555', 800, 'PLATINUM'),
       ('misook123', '1234', '김미숙', 'misook', 6, '01077778888', 'misook@example.com', '1994-04-04', 'FEMALE',
        '/img/profile.png', 'BALLKKAYE', 'USER', '6666', 1000, 'DIAMOND');


-- 6.game_tb
INSERT INTO game_tb (id, game_time, stadium_id, broadcast_channel, game_status,
                     home_team_id, away_team_id,
                     home_win_per, away_win_per,
                     home_prediction_score, away_prediction_score,
                     home_result_score, away_result_score,
                     total_prediction_score)
VALUES (418, TIMESTAMP '2025-07-14 18:30:00', 1, 'MS_T', 'SCHEDULED', 2, 4, 50.0, 50.0, NULL, NULL, 6, 5, NULL),
       (419, TIMESTAMP '2025-07-14 18:30:00', 5, 'KN_T', 'SCHEDULED', 6, 8, 50.0, 50.0, NULL, NULL, 5, 5, NULL),
       (420, TIMESTAMP '2025-07-14 18:30:00', 8, 'SS_T', 'SCHEDULED', 9, 7, 50.0, 50.0, NULL, NULL, 3, 5, NULL),
       (421, TIMESTAMP '2025-07-14 18:30:00', 9, 'SPO_T', 'SCHEDULED', 10, 1, 50.0, 50.0, NULL, NULL, 5, 5, NULL),
       (422, TIMESTAMP '2025-07-14 18:30:00', 2, 'SPO_2T', 'SCHEDULED', 3, 5, 50.0, 50.0, NULL, NULL, 2, 0, NULL),
       (423, TIMESTAMP '2025-07-15 18:30:00', 1, 'MS_T', 'SCHEDULED', 2, 4, 50.0, 50.0, NULL, NULL, 3, 5, NULL),
       (424, TIMESTAMP '2025-07-15 18:30:00', 5, 'KN_T', 'SCHEDULED', 6, 8, 50.0, 50.0, NULL, NULL, 3, 5, NULL),
       (425, TIMESTAMP '2025-07-15 18:30:00', 8, 'SS_T', 'SCHEDULED', 9, 7, 50.0, 50.0, NULL, NULL, 3, 5, NULL),
       (426, TIMESTAMP '2025-07-15 18:30:00', 9, 'SPO_T', 'SCHEDULED', 10, 1, 50.0, 50.0, NULL, NULL, 5, 5, NULL),
       (427, TIMESTAMP '2025-07-15 18:30:00', 2, 'SPO_2T', 'SCHEDULED', 3, 5, 50.0, 50.0, NULL, NULL, 2, 0, NULL),
       (428, TIMESTAMP '2025-07-16 18:30:00', 1, 'MS_T', 'SCHEDULED', 2, 4, 50.0, 50.0, NULL, NULL, 3, 5, NULL),
       (429, TIMESTAMP '2025-07-16 18:30:00', 5, 'KN_T', 'SCHEDULED', 6, 8, 50.0, 50.0, NULL, NULL, 3, 5, NULL),
       (430, TIMESTAMP '2025-07-16 18:30:00', 8, 'SS_T', 'SCHEDULED', 9, 7, 50.0, 50.0, NULL, NULL, 3, 5, NULL),
       (431, TIMESTAMP '2025-07-16 18:30:00', 9, 'SPO_T', 'SCHEDULED', 10, 1, 50.0, 50.0, NULL, NULL, 5, 5, NULL),
       (432, TIMESTAMP '2025-07-16 18:30:00', 2, 'SPO_2T', 'SCHEDULED', 3, 5, 50.0, 50.0, NULL, NULL, 2, 0, NULL),
       (433, TIMESTAMP '2025-07-17 18:30:00', 1, 'MS_T', 'SCHEDULED', 2, 4, 50.0, 50.0, NULL, NULL, 3, 5, NULL),
       (434, TIMESTAMP '2025-07-17 18:30:00', 5, 'KN_T', 'SCHEDULED', 6, 8, 50.0, 50.0, NULL, NULL, 2, 3, NULL),
       (435, TIMESTAMP '2025-07-17 18:30:00', 8, 'SS_T', 'SCHEDULED', 9, 7, 50.0, 50.0, NULL, NULL, 4, 4, NULL),
       (436, TIMESTAMP '2025-07-17 18:30:00', 9, 'SPO_T', 'SCHEDULED', 10, 1, 50.0, 50.0, NULL, NULL, 6, 7, NULL),
       (437, TIMESTAMP '2025-07-17 18:30:00', 2, 'SPO_2T', 'SCHEDULED', 3, 5, 50.0, 50.0, NULL, NULL, 3, 2, NULL),

-- 7월 5일 추가 경기
       (438, TIMESTAMP '2025-08-05 18:30:00', 8, 'SS_T', 'SCHEDULED', 9, 7, 50.0, 50.0, NULL, NULL, 2, 2, NULL),
       (439, TIMESTAMP '2025-08-05 18:30:00', 9, 'SPO_T', 'SCHEDULED', 10, 1, 50.0, 50.0, NULL, NULL, 4, 6, NULL),
       (440, TIMESTAMP '2025-08-05 18:30:00', 2, 'SPO_2T', 'SCHEDULED', 3, 5, 50.0, 50.0, NULL, NULL, 3, 1, NULL),

-- 8월 06일 경기
       (441, TIMESTAMP '2025-08-06 18:30:00', 3, 'MS_T', 'SCHEDULED', 4, 6, 50.0, 50.0, NULL, NULL, NULL, NULL, NULL),
       (442, TIMESTAMP '2025-08-06 18:30:00', 7, 'KN_T', 'SCHEDULED', 8, 2, 50.0, 50.0, NULL, NULL, NULL, NULL, NULL),

-- 8월 2일 경기
       (443, TIMESTAMP '2025-08-07 18:30:00', 6, 'SS_T', 'SCHEDULED', 7, 9, 50.0, 50.0, NULL, NULL, NULL, NULL, NULL),
       (444, TIMESTAMP '2025-08-07 18:30:00', 1, 'SPO_T', 'SCHEDULED', 1, 10, 50.0, 50.0, NULL, NULL, NULL, NULL, NULL),

-- 8월 3일 경기
       (445, TIMESTAMP '2026-08-08 18:30:00', 2, 'SPO_2T', 'SCHEDULED', 3, 5, 50.0, 50.0, NULL, NULL, NULL, NULL, NULL),
       (446, TIMESTAMP '2026-08-08 18:30:00', 1, 'MS_T', 'SCHEDULED', 2, 6, 50.0, 50.0, NULL, NULL, NULL, NULL, NULL);


-- 7. today_game_tb
INSERT INTO today_game_tb (id, game_time, stadium_id, broadcast_channel, game_status,
                           home_team_id, away_team_id,
                           home_win_per, away_win_per,
                           home_prediction_score, away_prediction_score,
                           home_result_score, away_result_score,
                           total_prediction_score, game_id)
VALUES
------------------------------------
-- 두산 vs SSG
(1, TIMESTAMP '2025-07-16 18:30:00', 1, 'MS_T', 'SCHEDULED', 2, 4,
 52.0, 48.0, 4.2, 3.9, 3, 5, 8.1, 433),

-- 삼성 vs 한화
(2, TIMESTAMP '2025-07-16 18:30:00', 5, 'KN_T', 'SCHEDULED', 6, 8,
 49.2, 50.8, 3.7, 3.8, 3, 5, 7.5, 434),

-- NC vs 롯데
(3, TIMESTAMP '2025-07-16 18:30:00', 8, 'SS_T', 'SCHEDULED', 9, 7,
 55.6, 44.4, 4.5, 3.6, 3, 5, 8.1, 435),

-- KT vs LG
(4, TIMESTAMP '2025-07-16 18:30:00', 9, 'SPO_T', 'SCHEDULED', 10, 1,
 50.0, 50.0, 3.8, 3.8, 5, 5, 7.6, 436),

-- 키움 vs KIA
(5, TIMESTAMP '2025-07-16 18:30:00', 2, 'SPO_2T', 'SCHEDULED', 3, 5,
 47.6, 52.4, 3.5, 3.9, 2, 0, 7.4, 437);

-- 8. user_prediction_tb 유저 예측 테이블
INSERT INTO user_prediction_tb (user_id, game_id, team_id, result)
VALUES (1, 423, 2, 'INCORRECT'), -- homeTeamId = 6
       (1, 424, 6, 'CORRECT'),   -- homeTeamId = 5
       (1, 425, 9, 'INCORRECT'), -- homeTeamId = 3
       (1, 426, 10, 'CORRECT'),  -- homeTeamId = 5
       (1, 427, 3, 'INCORRECT'), -- homeTeamId = 2
       (1, 433, 2, 'WAITING'),   -- gameId 433, userChoiceTeamId = 3
       (1, 434, 6, 'WAITING'),   -- gameId 434, userChoiceTeamId = 2
       (1, 435, 9, 'WAITING'),   -- gameId 435, userChoiceTeamId = 4
       (1, 436, 10, 'WAITING'),  -- gameId 436, userChoiceTeamId = 6
       (1, 437, 3, 'WAITING');
-- gameId 437, userChoiceTeamId = 5


-- 9. board_tb
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

-- 10 .board_image_tb
INSERT INTO board_image_tb (board_id, delete_status, img_url)
VALUES (1, 'NOT_DELETED', 'https://ballkkaye-bucket.s3.ap-northeast-2.amazonaws.com/test/image1.jpg'),
       (1, 'NOT_DELETED', 'https://ballkkaye-bucket.s3.ap-northeast-2.amazonaws.com/test/image2.jpg');

-- 11. baord_like_tb 게시글 좋아요
INSERT INTO board_like_tb (user_id, board_id)
VALUES (1, 1),
       (1, 2),
       (2, 1);


-- 12 .board_reply_tb
INSERT INTO board_reply_tb (board_id, user_id, parent_reply_id, tag_reply_id, delete_status, content, created_at,
                            updated_at)
VALUES
-- 부모 댓글
(1, 1, NULL, NULL, 'NOT_DELETED', '직관 최고였어요! 9회말 역전 감동이네요.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 2, NULL, NULL, 'NOT_DELETED', '분위기 미쳤어요. 응원 열기 대단했음!', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 3, NULL, NULL, 'NOT_DELETED', '중계로 봤는데도 소름 쫙 돋음.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 대댓글 및 대대댓글
(1, 4, 1, NULL, 'NOT_DELETED', '저도 그 장면에서 소리 질렀어요ㅋㅋ', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 5, 1, 4, 'NOT_DELETED', '그 장면 유튜브에 클립 떠서 또 봤어요.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 6, 1, 5, 'NOT_DELETED', '직관 갔던 사람 부럽다ㅠㅠ', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 7, 2, NULL, 'NOT_DELETED', '현장 분위기 진짜 보고 싶었음...', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(2, 1, NULL, NULL, 'NOT_DELETED', '직관 최고였어요! 9회말 역전 감동이네요.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, NULL, NULL, 'NOT_DELETED', '분위기 미쳤어요. 응원 열기 대단했음!', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 3, NULL, NULL, 'NOT_DELETED', '중계로 봤는데도 소름 쫙 돋음.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 대댓글 및 대대댓글
(2, 4, 8, NULL, 'NOT_DELETED', '저도 그 장면에서 소리 질렀어요ㅋㅋ', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 5, 8, 11, 'NOT_DELETED', '그 장면 유튜브에 클립 떠서 또 봤어요.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 6, 8, 12, 'NOT_DELETED', '직관 갔던 사람 부럽다ㅠㅠ', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 7, 9, NULL, 'NOT_DELETED', '현장 분위기 진짜 보고 싶었음...', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(11, 1, NULL, NULL, 'NOT_DELETED', '직관 최고였어요! 9회말 역전 감동이네요.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 2, NULL, NULL, 'NOT_DELETED', '분위기 미쳤어요. 응원 열기 대단했음!', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 3, NULL, NULL, 'NOT_DELETED', '중계로 봤는데도 소름 쫙 돋음.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 1, NULL, NULL, 'NOT_DELETED', '직관 최고였어요! 9회말 역전 감동이네요.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 2, NULL, NULL, 'NOT_DELETED', '분위기 미쳤어요. 응원 열기 대단했음!', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 3, NULL, NULL, 'NOT_DELETED', '중계로 봤는데도 소름 쫙 돋음.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 2, NULL, NULL, 'NOT_DELETED', '분위기 미쳤어요. 응원 열기 대단했음!', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 3, NULL, NULL, 'NOT_DELETED', '중계로 봤는데도 소름 쫙 돋음.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 2, NULL, NULL, 'NOT_DELETED', '분위기 미쳤어요. 응원 열기 대단했음!', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 3, NULL, NULL, 'NOT_DELETED', '중계로 봤는데도 소름 쫙 돋음.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 3, NULL, NULL, 'NOT_DELETED', '중계로 봤는데도 소름 쫙 돋음.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 2, NULL, NULL, 'NOT_DELETED', '분위기 미쳤어요. 응원 열기 대단했음!', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 3, NULL, NULL, 'NOT_DELETED', '중계로 봤는데도 소름 쫙 돋음.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 13. baord_like_tb 게시글 좋아요
INSERT INTO board_reply_like_tb (user_id, board_reply_id)
VALUES (1, 1),
       (1, 2),
       (2, 1);


-- 14. chat_room_tb
INSERT INTO chat_room_tb (game_id, team_id, max_participants, preferred_gender,
                          preferred_age, delete_status, is_same_team, created_at)
VALUES (433, 2, 5, 'NONE',
        'NONE', 'NOT_DELETED', false, '2025-07-13 20:01:00'),
       (434, 6, 5, 'NONE',
        'NONE', 'NOT_DELETED', false, '2025-07-13 20:02:00'),
       (435, 7, 5, 'FEMALE',
        'FROM_20_TO_30', 'NOT_DELETED', false, '2025-07-13 20:03:00'),
       (436, 1, 5, 'NONE',
        'NONE', 'NOT_DELETED', false, '2025-07-13 20:04:00'),
       (437, 5, 5, 'NONE',
        'NONE', 'NOT_DELETED', false, '2025-07-13 20:05:00');

-- 15. match_tb
INSERT INTO match_tb (user_id, chat_room_id, title, content, delete_status, created_at)
VALUES (1, 1,
        '혹시 내일 같이 야구 볼 사람?',
        '야구 좋아하는 사람 모여라! 롯데 팬이면 더 좋고, 같이 맛있는 거 먹으면서 직관할 사람 구합니다. 편하게 연락 주세요!',
        'NOT_DELETED',
        '2025-07-13 20:01:00'),
       (2, 2,
        '퇴근하고 같이 야구 보실 분!',
        '퇴근 후 맥주 한잔하면서 야구 같이 볼 분 찾아요! 두산 팬이시면 좋고, 직관도 환영입니다. 잠실 근처에서 같이 봐요~',
        'NOT_DELETED',
        '2025-07-13 20:02:00'),
       (3, 3,
        '기아 경기 같이 응원할 친구 구해요',
        '이번 주말 기아 경기 같이 직관 갈 사람 없나요? 혼자 가기 심심해서 같이 응원하고 놀 사람 구합니다. 20대 후반 여성분이면 더 좋아요!',
        'NOT_DELETED',
        '2025-07-13 20:03:00'),
       (4, 4,
        '야구 입문하고 싶은 분!',
        '야구는 잘 모르지만 흥미 있는 분들 같이 배워가면서 즐겨요! 편한 분위기에서 가볍게 이야기 나눌 분들 환영합니다.',
        'NOT_DELETED',
        '2025-07-13 20:04:00'),
       (5, 5,
        '직관 같이 갈 삼성팬 구합니다!',
        '삼성 경기 같이 직관 갈 사람 구해요! 혼자 가면 재미없잖아요. 같이 가서 응원도 하고 사진도 찍을 분 연락 주세요~',
        'NOT_DELETED',
        '2025-07-13 20:05:00');

-- 16. chat_room_user_tb
INSERT INTO chat_room_user_tb (user_id, chat_room_id, is_owner, delete_status, created_at)
VALUES (1, 1, true, 'NOT_DELETED', NOW()),
       (1, 2, true, 'NOT_DELETED', NOW()),
       (2, 2, false, 'NOT_DELETED', NOW()),
       (1, 3, true, 'NOT_DELETED', NOW()),
       (3, 3, false, 'NOT_DELETED', NOW()),
       (1, 4, true, 'NOT_DELETED', NOW()),
       (2, 4, false, 'NOT_DELETED', NOW()),
       (3, 4, false, 'NOT_DELETED', NOW());

-- 17. match_like_tb
INSERT INTO match_like_tb (user_id, match_id)
VALUES (1,
        1),
       (1,
        2),
       (2,
        1),
       (2,
        2),
       (3,
        1),
       (5,
        1),
       (6,
        1);

-- 18. player_tb
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
       (100309, 4, '강호동'),
       (100310, 3, '김혜성'),
       (100311, 3, '이정후'),
       (100312, 3, '이형종'),
       (100313, 3, '송성문'),
       (100314, 3, '김재현'),
       (100315, 3, '임병욱'),
       (100316, 3, '박병호'),
       (100317, 3, '이용규'),
       (100318, 3, '전병우'),
       (100319, 5, '나성범'),
       (100320, 5, '김도영'),
       (100321, 5, '박찬호'),
       (100322, 5, '최형우'),
       (100323, 5, '소크라테스'),
       (100324, 5, '이우성'),
       (100325, 5, '김선빈'),
       (100326, 5, '이창진'),
       (100327, 5, '한준수');

-- 19. today_hitter_lineup_tb
INSERT INTO today_hitter_lineup_tb (game_id, team_id, player_id, today_hitter_order, position,
                                    ab, h, avg, season_avg, ops, created_at)
VALUES (433, 3, 29, 1, '1루수', 3, 1, 0.333, 0.285, 0.821, CURRENT_TIMESTAMP),
       (433, 3, 30, 2, '2루수', 4, 2, 0.500, 0.312, 0.920, CURRENT_TIMESTAMP),
       (433, 3, 31, 3, '3루수', 2, 1, 0.500, 0.298, 0.850, CURRENT_TIMESTAMP),
       (433, 3, 32, 4, '유격수', 3, 0, 0.000, 0.245, 0.650, CURRENT_TIMESTAMP),
       (433, 3, 33, 5, '좌익수', 3, 2, 0.667, 0.320, 0.940, CURRENT_TIMESTAMP),
       (433, 3, 34, 6, '중견수', 4, 1, 0.250, 0.278, 0.770, CURRENT_TIMESTAMP),
       (433, 3, 35, 7, '우익수', 2, 1, 0.500, 0.299, 0.865, CURRENT_TIMESTAMP),
       (433, 3, 36, 8, '포수', 3, 1, 0.333, 0.260, 0.745, CURRENT_TIMESTAMP),
       (433, 3, 37, 9, '지명타자', 3, 0, 0.000, 0.240, 0.680, CURRENT_TIMESTAMP),
       (433, 4, 20, 1, '1루수', 3, 2, 0.667, 0.310, 0.910, CURRENT_TIMESTAMP),
       (433, 4, 21, 2, '2루수', 4, 2, 0.500, 0.288, 0.850, CURRENT_TIMESTAMP),
       (433, 4, 22, 3, '3루수', 2, 0, 0.000, 0.260, 0.690, CURRENT_TIMESTAMP),
       (433, 4, 23, 4, '유격수', 3, 1, 0.333, 0.275, 0.800, CURRENT_TIMESTAMP),
       (433, 4, 24, 5, '좌익수', 3, 1, 0.333, 0.282, 0.815, CURRENT_TIMESTAMP),
       (433, 4, 25, 6, '중견수', 4, 3, 0.750, 0.340, 1.020, CURRENT_TIMESTAMP),
       (433, 4, 26, 7, '우익수', 3, 1, 0.333, 0.295, 0.880, CURRENT_TIMESTAMP),
       (433, 4, 27, 8, '포수', 2, 0, 0.000, 0.255, 0.700, CURRENT_TIMESTAMP),
       (433, 4, 28, 9, '지명타자', 3, 2, 0.667, 0.305, 0.950, CURRENT_TIMESTAMP);

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


-- 9. 선발투수
INSERT INTO starting_pitcher_tb (game_id, player_id, profile_url,
                                 era, game_count, result, qs, whip)
VALUES (433, 1, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/51867.png', 4.58, 27, '2승 3패', 0,
        1.53),
       (433, 2, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/55257.png', 4.86, 14, '5승 7패', 8,
        1.38),
       (434, 3, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/52701.png', 4.09, 11, '5승 2패', 4,
        1.2),
       (434, 4, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/55460.png', NULL, NULL, '없음', NULL,
        NULL),
       (435, 5, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/55532.png', 2.37, 5, '4승 1패', 4,
        1.02),
       (435, 6, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/66920.png', 5.34, 17, '2승 2패', 0,
        1.71),
       (436, 7, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/61101.png', 2.61, 14, '8승 2패', 9,
        1.18),
       (436, 8, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/50030.png', 3.13, 13, '5승 2패', 10,
        1.18),
       (437, 9, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/76225.png', 7.53, 11, '없음', 0,
        1.88),
       (437, 10, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/64350.png', 4.88, 15, '6승 7패', 8,
        1.51);

-- 10. 오늘의 선발투수
INSERT INTO today_starting_pitcher_tb (game_id, player_id, profile_url,
                                       era, game_count, qs, whip, result)
VALUES (433, 1, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/51867.png', 4.58, 27, 0, 1.53,
        '2승 3패'),
       (433, 2, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/55257.png', 4.86, 14, 8, 1.38,
        '5승 7패'),
       (434, 3, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/52701.png', 4.09, 11, 4, 1.2,
        '5승 2패'),
       (434, 4, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/55460.png', NULL, NULL, NULL, NULL,
        '없음'),
       (435, 5, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/55532.png', 2.37, 5, 4, 1.02,
        '4승 1패'),
       (435, 6, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/66920.png', 5.34, 17, 0, 1.71,
        '2승 2패'),
       (436, 7, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/61101.png', 2.61, 14, 9, 1.18,
        '8승 2패'),
       (436, 8, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/50030.png', 3.13, 13, 10, 1.18,
        '5승 2패'),
       (437, 9, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/76225.png', 7.53, 11, 0, 1.88,
        '없음'),
       (437, 10, 'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/64350.png', 4.88, 15, 8, 1.51,
        '6승 7패');


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


-- 9. visit_record_tb
INSERT INTO visit_record_tb (user_id, game_id, team_id, result, img_url, content, delete_status, created_at)
VALUES (1, 418, 2, 'WIN', 'https://example.com/photo1.jpg', '응원 열심히 했고, 분위기 최고였어요!', 'NOT_DELETED', NOW()),
       (2, 418, 4, 'LOSE', 'https://example.com/photo2.jpg', '경기 내용은 아쉬웠지만, 그래도 응원은 즐거웠어요.', 'NOT_DELETED', NOW()),
       (3, 420, 9, 'LOSE', 'https://example.com/photo3.jpg', '경기 끝난 후 분위기는 좋았지만, 결과는 아쉬웠습니다.', 'NOT_DELETED', NOW());


-- 날씨
INSERT INTO weather_ultra_tb (game_id, humidity_per, rain_amount, rain_per, stadium_id,
                              temperature, wind_speed, created_at, forecast_at,
                              weather_code, wind_direction)
VALUES (437, 95.0, 0.0, 0.0, 2, 24.0, 1.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 06:00:00', 'DB01', 'SOUTH_EAST'),
       (433, 90.0, 0.0, 0.0, 2, 26.0, 1.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 07:00:00', 'DB01', 'SOUTH'),
       (433, 85.0, 0.0, 0.0, 2, 27.0, 1.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 08:00:00', 'DB01', 'SOUTH_WEST'),
       (433, 80.0, 0.0, 0.0, 2, 28.0, 1.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 09:00:00', 'DB01', 'WEST'),
       (433, 75.0, 0.0, 20.0, 2, 29.0, 1.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 10:00:00', 'DB01', 'NORTH_WEST'),
       (433, 65.0, 0.0, 20.0, 2, 30.0, 2.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 11:00:00', 'DB01', 'NORTH_WEST'),
       (433, 65.0, 0.0, 30.0, 2, 32.0, 2.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 12:00:00', 'DB01', 'NORTH_WEST'),
       (433, 60.0, 0.0, 30.0, 2, 33.0, 2.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 13:00:00', 'DB01', 'NORTH_WEST'),
       (433, 55.0, 0.0, 20.0, 2, 34.0, 2.7, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 14:00:00', 'DB01', 'NORTH_WEST'),
       (433, 60.0, 0.0, 0.0, 2, 33.0, 3.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 15:00:00', 'DB01', 'NORTH_WEST'),
       (433, 60.0, 0.0, 0.0, 2, 33.0, 3.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 16:00:00', 'DB01', 'NORTH_WEST'),
       (433, 70.0, 0.0, 20.0, 2, 32.0, 3.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 17:00:00', 'DB01', 'NORTH_WEST'),
       (433, 75.0, 0.0, 30.0, 2, 31.0, 3.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 18:00:00', 'DB01', 'NORTH_WEST'),
       (433, 80.0, 0.0, 0.0, 2, 28.0, 1.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 19:00:00', 'DB01', 'NORTH_WEST'),
       (433, 80.0, 0.0, 20.0, 2, 27.0, 1.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 20:00:00', 'DB01', 'NORTH_WEST'),
       (433, 85.0, 0.0, 30.0, 2, 26.0, 1.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 21:00:00', 'DB01', 'NORTH'),
       (433, 90.0, 0.0, 30.0, 2, 26.0, 0.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 22:00:00', 'DB01', 'NORTH'),
       (433, 90.0, 0.0, 30.0, 2, 25.0, 0.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 23:00:00', 'DB01', 'NORTH'),
       (434, 85.0, 0.0, 0.0, 5, 25.0, 1.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 06:00:00', 'DB01', 'NORTH_WEST'),
       (434, 75.0, 0.0, 0.0, 5, 27.0, 1.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 07:00:00', 'DB01', 'NORTH'),
       (434, 75.0, 0.0, 0.0, 5, 28.0, 2.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 08:00:00', 'DB01', 'NORTH'),
       (434, 65.0, 0.0, 0.0, 5, 30.0, 2.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 09:00:00', 'DB01', 'NORTH'),
       (434, 60.0, 0.0, 0.0, 5, 31.0, 2.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 10:00:00', 'DB01', 'NORTH'),
       (434, 55.0, 0.0, 0.0, 5, 33.0, 2.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 11:00:00', 'DB01', 'NORTH'),
       (434, 50.0, 0.0, 0.0, 5, 34.0, 2.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 12:00:00', 'DB01', 'NORTH'),
       (434, 45.0, 0.0, 20.0, 5, 35.0, 2.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 13:00:00', 'DB01', 'NORTH'),
       (434, 45.0, 0.0, 20.0, 5, 35.0, 2.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 14:00:00', 'DB01', 'NORTH'),
       (434, 45.0, 0.0, 0.0, 5, 36.0, 2.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 15:00:00', 'DB01', 'NORTH'),
       (434, 50.0, 0.0, 0.0, 5, 36.0, 2.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 16:00:00', 'DB01', 'NORTH'),
       (434, 50.0, 0.0, 0.0, 5, 34.0, 1.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 17:00:00', 'DB01', 'NORTH'),
       (434, 55.0, 0.0, 0.0, 5, 32.0, 1.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 18:00:00', 'DB01', 'SOUTH_EAST'),
       (434, 60.0, 0.0, 0.0, 5, 31.0, 2.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 19:00:00', 'DB01', 'SOUTH_EAST'),
       (434, 65.0, 0.0, 0.0, 5, 29.0, 2.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 20:00:00', 'DB01', 'SOUTH_EAST'),
       (434, 70.0, 0.0, 0.0, 5, 28.0, 2.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 21:00:00', 'DB01', 'SOUTH_EAST'),
       (434, 80.0, 0.0, 20.0, 5, 27.0, 1.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 22:00:00', 'DB01', 'SOUTH_EAST'),
       (434, 80.0, 0.0, 0.0, 5, 27.0, 1.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 23:00:00', 'DB01', 'SOUTH_EAST'),
       (435, 85.0, 0.0, 0.0, 9, 25.0, 1.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 06:00:00', 'DB01', 'NORTH_WEST'),
       (435, 75.0, 0.0, 0.0, 9, 27.0, 2.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 07:00:00', 'DB01', 'NORTH'),
       (435, 70.0, 0.0, 0.0, 9, 29.0, 2.7, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 08:00:00', 'DB01', 'NORTH'),
       (435, 65.0, 0.0, 0.0, 9, 31.0, 3.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 09:00:00', 'DB01', 'NORTH'),
       (435, 60.0, 0.0, 0.0, 9, 32.0, 3.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 10:00:00', 'DB01', 'NORTH'),
       (435, 55.0, 0.0, 0.0, 9, 33.0, 2.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 11:00:00', 'DB01', 'NORTH'),
       (435, 50.0, 0.0, 0.0, 9, 34.0, 2.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 12:00:00', 'DB01', 'NORTH'),
       (435, 45.0, 0.0, 20.0, 9, 35.0, 2.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 13:00:00', 'DB01', 'NORTH'),
       (435, 40.0, 0.0, 20.0, 9, 36.0, 2.7, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 14:00:00', 'DB01', 'NORTH'),
       (435, 45.0, 0.0, 0.0, 9, 37.0, 2.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 15:00:00', 'DB01', 'NORTH'),
       (435, 45.0, 0.0, 0.0, 9, 36.0, 1.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 16:00:00', 'DB01', 'NORTH'),
       (435, 50.0, 0.0, 0.0, 9, 34.0, 0.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 17:00:00', 'DB01', 'NORTH_EAST'),
       (435, 55.0, 0.0, 0.0, 9, 33.0, 2.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 18:00:00', 'DB01', 'SOUTH_EAST'),
       (435, 60.0, 0.0, 0.0, 9, 32.0, 3.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 19:00:00', 'DB01', 'SOUTH_EAST'),
       (435, 65.0, 0.0, 0.0, 9, 30.0, 2.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 20:00:00', 'DB01', 'SOUTH_EAST'),
       (435, 70.0, 0.0, 0.0, 9, 29.0, 2.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 21:00:00', 'DB01', 'SOUTH_EAST'),
       (435, 75.0, 0.0, 0.0, 9, 28.0, 1.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 22:00:00', 'DB01', 'SOUTH_EAST'),
       (435, 75.0, 0.0, 0.0, 9, 27.0, 1.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 23:00:00', 'DB01', 'SOUTH_EAST'),
       (436, 85.0, 0.0, 20.0, 9, 27.0, 1.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 07:00:00', 'DB01', 'SOUTH'),
       (436, 85.0, 0.0, 30.0, 9, 27.0, 1.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 08:00:00', 'DB01', 'SOUTH'),
       (436, 80.0, 0.0, 30.0, 9, 28.0, 2.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 09:00:00', 'DB01', 'SOUTH_WEST'),
       (436, 80.0, 0.0, 30.0, 9, 28.0, 2.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 10:00:00', 'DB01', 'SOUTH_WEST'),
       (436, 75.0, 0.0, 30.0, 9, 29.0, 3.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 11:00:00', 'DB01', 'WEST'),
       (436, 70.0, 0.0, 30.0, 9, 30.0, 3.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 12:00:00', 'DB01', 'WEST'),
       (436, 70.0, 0.0, 20.0, 9, 30.0, 3.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 13:00:00', 'DB01', 'WEST'),
       (436, 65.0, 0.0, 20.0, 9, 32.0, 3.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 14:00:00', 'DB01', 'WEST'),
       (436, 60.0, 0.0, 30.0, 9, 32.0, 3.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 15:00:00', 'DB01', 'WEST'),
       (436, 60.0, 0.0, 20.0, 9, 33.0, 3.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 16:00:00', 'DB01', 'WEST'),
       (436, 60.0, 0.0, 20.0, 9, 32.0, 3.7, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 17:00:00', 'DB01', 'WEST'),
       (436, 60.0, 0.0, 20.0, 9, 32.0, 3.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 18:00:00', 'DB01', 'WEST'),
       (436, 65.0, 0.0, 30.0, 9, 31.0, 2.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 19:00:00', 'DB01', 'WEST'),
       (436, 70.0, 0.0, 0.0, 9, 30.0, 1.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 20:00:00', 'DB01', 'WEST'),
       (436, 75.0, 0.0, 0.0, 9, 30.0, 1.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 21:00:00', 'DB01', 'WEST'),
       (436, 80.0, 0.0, 0.0, 9, 29.0, 1.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 22:00:00', 'DB01', 'WEST'),
       (436, 80.0, 0.0, 0.0, 9, 28.0, 0.7, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 23:00:00', 'DB01', 'WEST'),
       (437, 85.0, 0.0, 20.0, 2, 26.0, 1.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 06:00:00', 'DB01', 'SOUTH'),
       (437, 85.0, 0.0, 20.0, 2, 26.0, 1.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 07:00:00', 'DB01', 'SOUTH'),
       (437, 85.0, 0.0, 30.0, 2, 27.0, 2.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 08:00:00', 'DB01', 'SOUTH_WEST'),
       (437, 80.0, 0.0, 30.0, 2, 27.0, 2.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 09:00:00', 'DB01', 'SOUTH_WEST'),
       (437, 80.0, 0.0, 30.0, 2, 28.0, 3.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 10:00:00', 'DB01', 'SOUTH_WEST'),
       (437, 70.0, 0.0, 30.0, 2, 28.0, 3.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 11:00:00', 'DB01', 'SOUTH_WEST'),
       (437, 70.0, 0.0, 30.0, 2, 29.0, 3.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 12:00:00', 'DB01', 'SOUTH_WEST'),
       (437, 65.0, 0.0, 30.0, 2, 30.0, 3.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 13:00:00', 'DB01', 'SOUTH_WEST'),
       (437, 65.0, 0.0, 20.0, 2, 31.0, 4.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 14:00:00', 'DB01', 'WEST'),
       (437, 65.0, 0.0, 20.0, 2, 31.0, 4.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 15:00:00', 'DB01', 'WEST'),
       (437, 65.0, 0.0, 0.0, 2, 31.0, 3.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 16:00:00', 'DB01', 'WEST'),
       (437, 65.0, 0.0, 0.0, 2, 31.0, 3.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 17:00:00', 'DB01', 'WEST'),
       (437, 65.0, 0.0, 0.0, 2, 31.0, 2.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 18:00:00', 'DB01', 'WEST'),
       (437, 70.0, 0.0, 0.0, 2, 30.0, 2.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 19:00:00', 'DB01', 'WEST'),
       (437, 75.0, 0.0, 20.0, 2, 29.0, 1.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 20:00:00', 'DB01', 'WEST'),
       (437, 80.0, 0.0, 0.0, 2, 28.0, 1.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 21:00:00', 'DB01', 'WEST'),
       (437, 80.0, 0.0, 0.0, 2, 28.0, 0.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 22:00:00', 'DB01', 'WEST'),
       (437, 85.0, 0.0, 0.0, 2, 27.0, 0.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-16 23:00:00', 'DB01', 'WEST'),
       (438, 85.0, 0.0, 20.0, 1, 26.0, 1.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 06:00:00', 'DB01', 'SOUTH'),
       (438, 85.0, 0.0, 20.0, 1, 26.0, 1.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 07:00:00', 'DB01', 'SOUTH'),
       (438, 85.0, 0.0, 30.0, 1, 27.0, 2.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 08:00:00', 'DB01', 'SOUTH_WEST'),
       (438, 80.0, 0.0, 30.0, 1, 27.0, 2.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 09:00:00', 'DB01', 'SOUTH_WEST'),
       (438, 80.0, 0.0, 30.0, 1, 28.0, 3.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 10:00:00', 'DB01', 'SOUTH_WEST'),
       (438, 70.0, 0.0, 30.0, 1, 28.0, 3.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 11:00:00', 'DB01', 'SOUTH_WEST'),
       (438, 70.0, 0.0, 30.0, 1, 29.0, 3.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 12:00:00', 'DB01', 'SOUTH_WEST'),
       (438, 65.0, 0.0, 30.0, 1, 30.0, 3.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 13:00:00', 'DB01', 'SOUTH_WEST'),
       (438, 65.0, 0.0, 20.0, 1, 31.0, 4.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 14:00:00', 'DB01', 'WEST'),
       (438, 65.0, 0.0, 20.0, 1, 31.0, 4.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 15:00:00', 'DB01', 'WEST'),
       (438, 65.0, 0.0, 0.0, 1, 31.0, 3.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 16:00:00', 'DB01', 'WEST'),
       (438, 65.0, 0.0, 0.0, 1, 31.0, 3.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 17:00:00', 'DB01', 'WEST'),
       (438, 65.0, 0.0, 0.0, 1, 31.0, 2.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 18:00:00', 'DB01', 'WEST'),
       (438, 70.0, 0.0, 0.0, 1, 30.0, 2.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 19:00:00', 'DB01', 'WEST'),
       (438, 75.0, 0.0, 20.0, 1, 29.0, 1.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 20:00:00', 'DB01', 'WEST'),
       (438, 80.0, 0.0, 0.0, 1, 28.0, 1.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 21:00:00', 'DB01', 'WEST'),
       (438, 80.0, 0.0, 0.0, 1, 28.0, 0.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 22:00:00', 'DB01', 'WEST'),
       (438, 85.0, 0.0, 0.0, 1, 27.0, 0.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 23:00:00', 'DB01', 'WEST'),
       (439, 85.0, 0.0, 20.0, 5, 27.0, 1.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 06:00:00', 'DB01', 'SOUTH_EAST'),
       (439, 85.0, 0.0, 20.0, 5, 27.0, 1.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 07:00:00', 'DB01', 'SOUTH'),
       (439, 85.0, 0.0, 30.0, 5, 27.0, 1.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 08:00:00', 'DB01', 'SOUTH'),
       (439, 80.0, 0.0, 30.0, 5, 28.0, 2.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 09:00:00', 'DB01', 'SOUTH_WEST'),
       (439, 80.0, 0.0, 30.0, 5, 28.0, 2.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 10:00:00', 'DB01', 'SOUTH_WEST'),
       (439, 75.0, 0.0, 30.0, 5, 29.0, 3.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 11:00:00', 'DB01', 'WEST'),
       (439, 70.0, 0.0, 30.0, 5, 30.0, 3.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 12:00:00', 'DB01', 'WEST'),
       (439, 70.0, 0.0, 20.0, 5, 30.0, 3.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 13:00:00', 'DB01', 'WEST'),
       (439, 65.0, 0.0, 20.0, 5, 32.0, 3.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 14:00:00', 'DB01', 'WEST'),
       (439, 60.0, 0.0, 30.0, 5, 32.0, 3.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 15:00:00', 'DB01', 'WEST'),
       (439, 60.0, 0.0, 20.0, 5, 33.0, 3.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 16:00:00', 'DB01', 'WEST'),
       (439, 60.0, 0.0, 20.0, 5, 32.0, 3.7, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 17:00:00', 'DB01', 'WEST'),
       (439, 60.0, 0.0, 20.0, 5, 32.0, 3.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 18:00:00', 'DB01', 'WEST'),
       (439, 65.0, 0.0, 30.0, 5, 31.0, 2.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 19:00:00', 'DB01', 'WEST'),
       (439, 70.0, 0.0, 0.0, 5, 30.0, 1.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 20:00:00', 'DB01', 'WEST'),
       (439, 75.0, 0.0, 0.0, 5, 30.0, 1.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 21:00:00', 'DB01', 'WEST'),
       (439, 80.0, 0.0, 0.0, 5, 29.0, 1.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 22:00:00', 'DB01', 'WEST'),
       (439, 80.0, 0.0, 0.0, 5, 28.0, 0.7, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 23:00:00', 'DB01', 'WEST'),
       (440, 85.0, 0.0, 0.0, 8, 25.0, 1.3, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 06:00:00', 'DB01', 'NORTH_WEST'),
       (440, 75.0, 0.0, 0.0, 8, 27.0, 2.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 07:00:00', 'DB01', 'NORTH'),
       (440, 70.0, 0.0, 0.0, 8, 29.0, 2.7, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 08:00:00', 'DB01', 'NORTH'),
       (440, 65.0, 0.0, 0.0, 8, 31.0, 3.0, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 09:00:00', 'DB01', 'NORTH'),
       (440, 60.0, 0.0, 0.0, 8, 32.0, 3.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 10:00:00', 'DB01', 'NORTH'),
       (440, 55.0, 0.0, 0.0, 8, 33.0, 2.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 11:00:00', 'DB01', 'NORTH'),
       (440, 50.0, 0.0, 0.0, 8, 34.0, 2.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 12:00:00', 'DB01', 'NORTH'),
       (440, 45.0, 0.0, 20.0, 8, 35.0, 2.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 13:00:00', 'DB01', 'NORTH'),
       (440, 40.0, 0.0, 20.0, 8, 36.0, 2.7, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 14:00:00', 'DB01', 'NORTH'),
       (440, 45.0, 0.0, 0.0, 8, 37.0, 2.4, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 15:00:00', 'DB01', 'NORTH'),
       (440, 45.0, 0.0, 0.0, 8, 36.0, 1.9, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 16:00:00', 'DB01', 'NORTH'),
       (440, 50.0, 0.0, 0.0, 8, 34.0, 0.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 17:00:00', 'DB01', 'NORTH_EAST'),
       (440, 55.0, 0.0, 0.0, 8, 33.0, 2.5, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 18:00:00', 'DB01', 'SOUTH_EAST'),
       (440, 60.0, 0.0, 0.0, 8, 32.0, 3.1, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 19:00:00', 'DB01', 'SOUTH_EAST'),
       (440, 65.0, 0.0, 0.0, 8, 30.0, 2.8, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 20:00:00', 'DB01', 'SOUTH_EAST'),
       (440, 70.0, 0.0, 0.0, 8, 29.0, 2.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 21:00:00', 'DB01', 'SOUTH_EAST'),
       (440, 75.0, 0.0, 0.0, 8, 28.0, 1.6, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 22:00:00', 'DB01', 'SOUTH_EAST'),
       (440, 75.0, 0.0, 0.0, 8, 27.0, 1.2, CURRENT_TIMESTAMP, TIMESTAMP '2025-07-07 23:00:00', 'DB01', 'SOUTH_EAST'),
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

