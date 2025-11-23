--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5 (Debian 17.5-1.pgdg120+1)
-- Dumped by pg_dump version 17.5 (Debian 17.5-1.pgdg120+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: instructor; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (12, '550e8400-e29b-41d4-a716-446655440000', '国島', '2025-08-17 02:33:29.564964+00', 'RYO', '2025-08-17 02:33:29.564964+00', 'RYO', 'JPN');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (9, '550e8400-e29b-41d4-a716-446655440000', '国木', '2025-08-17 02:33:01.436953+00', 'RYO', '2025-08-17 02:33:01.436953+00', 'RYO', 'JPN');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (10, '550e8400-e29b-41d4-a716-446655440000', '国原', '2025-08-17 02:33:14.265191+00', 'RYO', '2025-08-17 02:33:14.265191+00', 'RYO', 'JPN');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (11, '550e8400-e29b-41d4-a716-446655440000', '国田', '2025-08-17 02:33:25.50705+00', 'RYO', '2025-08-17 02:33:25.50705+00', 'RYO', 'JPN');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (13, '550e8400-e29b-41d4-a716-446655440000', '国川', '2025-08-17 02:33:40.62806+00', 'RYO', '2025-08-17 02:33:40.62806+00', 'RYO', 'JPN');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (14, '550e8400-e29b-41d4-a716-446655440000', '国谷', '2025-08-17 02:33:57.380575+00', 'RYO', '2025-08-17 02:33:57.380575+00', 'RYO', 'JPN');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (15, '550e8400-e29b-41d4-a716-446655440000', '国藤', '2025-08-17 02:34:05.630186+00', 'RYO', '2025-08-17 02:34:05.630186+00', 'RYO', 'JPN');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (16, '550e8400-e29b-41d4-a716-446655440000', '国内', '2025-08-17 02:35:03.498583+00', 'RYO', '2025-08-17 02:35:03.498583+00', 'RYO', 'JPN');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (17, '550e8400-e29b-41d4-a716-446655440000', '地木', '2025-08-17 02:36:24.405453+00', 'RYO', '2025-08-17 02:36:24.405453+00', 'RYO', 'GGH');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (18, '550e8400-e29b-41d4-a716-446655440000', '地原', '2025-08-17 02:36:30.063303+00', 'RYO', '2025-08-17 02:36:30.063303+00', 'RYO', 'GGH');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (19, '550e8400-e29b-41d4-a716-446655440000', '地田', '2025-08-17 02:36:36.313927+00', 'RYO', '2025-08-17 02:36:36.313927+00', 'RYO', 'GGH');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (20, '550e8400-e29b-41d4-a716-446655440000', '地島', '2025-08-17 02:36:39.847753+00', 'RYO', '2025-08-17 02:36:39.847753+00', 'RYO', 'GGH');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (21, '550e8400-e29b-41d4-a716-446655440000', '地川', '2025-08-17 02:36:56.262242+00', 'RYO', '2025-08-17 02:36:56.262242+00', 'RYO', 'GGH');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (22, '550e8400-e29b-41d4-a716-446655440000', '地谷', '2025-08-17 02:37:00.849836+00', 'RYO', '2025-08-17 02:37:00.849836+00', 'RYO', 'GGH');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (23, '550e8400-e29b-41d4-a716-446655440000', '地藤', '2025-08-17 02:37:37.469377+00', 'RYO', '2025-08-17 02:37:37.469377+00', 'RYO', 'GGH');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (24, '550e8400-e29b-41d4-a716-446655440000', '地内', '2025-08-17 02:37:43.731471+00', 'RYO', '2025-08-17 02:37:43.731471+00', 'RYO', 'GGH');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (25, '550e8400-e29b-41d4-a716-446655440000', '地野', '2025-08-17 02:38:03.662516+00', 'RYO', '2025-08-17 02:38:03.662516+00', 'RYO', 'GGH');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (26, '550e8400-e29b-41d4-a716-446655440000', '数木', '2025-08-17 02:39:05.697468+00', 'RYO', '2025-08-17 02:39:05.697468+00', 'RYO', 'MTH');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (27, '550e8400-e29b-41d4-a716-446655440000', '数原', '2025-08-17 02:40:55.825781+00', 'RYO', '2025-08-17 02:40:55.825781+00', 'RYO', 'MTH');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (28, '550e8400-e29b-41d4-a716-446655440000', '数田', '2025-08-17 02:41:01.076858+00', 'RYO', '2025-08-17 02:41:01.076858+00', 'RYO', 'MTH');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (29, '550e8400-e29b-41d4-a716-446655440000', '数島', '2025-08-17 02:41:04.744919+00', 'RYO', '2025-08-17 02:41:04.744919+00', 'RYO', 'MTH');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (30, '550e8400-e29b-41d4-a716-446655440000', '数川', '2025-08-17 02:41:09.026098+00', 'RYO', '2025-08-17 02:41:09.026098+00', 'RYO', 'MTH');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (31, '550e8400-e29b-41d4-a716-446655440000', '数谷', '2025-08-17 02:41:13.230626+00', 'RYO', '2025-08-17 02:41:13.230626+00', 'RYO', 'MTH');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (32, '550e8400-e29b-41d4-a716-446655440000', '数藤', '2025-08-17 02:41:17.826097+00', 'RYO', '2025-08-17 02:41:17.826097+00', 'RYO', 'MTH');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (33, '550e8400-e29b-41d4-a716-446655440000', '数内', '2025-08-17 02:41:24.124358+00', 'RYO', '2025-08-17 02:41:24.124358+00', 'RYO', 'MTH');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (34, '550e8400-e29b-41d4-a716-446655440000', '数野', '2025-08-17 02:41:28.959801+00', 'RYO', '2025-08-17 02:41:28.959801+00', 'RYO', 'MTH');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (35, '550e8400-e29b-41d4-a716-446655440000', '数ヶ崎', '2025-08-17 02:43:47.636383+00', 'RYO', '2025-08-17 02:43:47.636383+00', 'RYO', 'MTH');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (36, '550e8400-e29b-41d4-a716-446655440000', '理木', '2025-08-17 02:43:56.410582+00', 'RYO', '2025-08-17 02:43:56.410582+00', 'RYO', 'SCI');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (37, '550e8400-e29b-41d4-a716-446655440000', '理原', '2025-08-17 02:44:01.888612+00', 'RYO', '2025-08-17 02:44:01.888612+00', 'RYO', 'SCI');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (38, '550e8400-e29b-41d4-a716-446655440000', '理田', '2025-08-17 02:45:07.626666+00', 'RYO', '2025-08-17 02:45:07.626666+00', 'RYO', 'SCI');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (39, '550e8400-e29b-41d4-a716-446655440000', '理島', '2025-08-17 02:45:11.222593+00', 'RYO', '2025-08-17 02:45:11.222593+00', 'RYO', 'SCI');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (40, '550e8400-e29b-41d4-a716-446655440000', '理川', '2025-08-17 02:45:15.074244+00', 'RYO', '2025-08-17 02:45:15.074244+00', 'RYO', 'SCI');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (41, '550e8400-e29b-41d4-a716-446655440000', '理谷', '2025-08-17 02:45:19.541955+00', 'RYO', '2025-08-17 02:45:19.541955+00', 'RYO', 'SCI');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (42, '550e8400-e29b-41d4-a716-446655440000', '理藤', '2025-08-17 02:45:25.174449+00', 'RYO', '2025-08-17 02:45:25.174449+00', 'RYO', 'SCI');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (43, '550e8400-e29b-41d4-a716-446655440000', '理内', '2025-08-17 02:45:30.291265+00', 'RYO', '2025-08-17 02:45:30.291265+00', 'RYO', 'SCI');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (44, '550e8400-e29b-41d4-a716-446655440000', '理野', '2025-08-17 02:45:35.840984+00', 'RYO', '2025-08-17 02:45:35.840984+00', 'RYO', 'SCI');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (45, '550e8400-e29b-41d4-a716-446655440000', '理ヶ崎', '2025-08-17 02:46:18.4771+00', 'RYO', '2025-08-17 02:46:18.4771+00', 'RYO', 'SCI');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (46, '550e8400-e29b-41d4-a716-446655440000', '理江', '2025-08-17 02:46:33.90569+00', 'RYO', '2025-08-17 02:46:33.90569+00', 'RYO', 'SCI');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (47, '550e8400-e29b-41d4-a716-446655440000', '体木', '2025-08-17 02:47:09.138641+00', 'RYO', '2025-08-17 02:47:09.138641+00', 'RYO', 'HPE');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (48, '550e8400-e29b-41d4-a716-446655440000', '体原', '2025-08-17 02:47:16.64866+00', 'RYO', '2025-08-17 02:47:16.64866+00', 'RYO', 'HPE');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (49, '550e8400-e29b-41d4-a716-446655440000', '体田', '2025-08-17 02:47:32.24203+00', 'RYO', '2025-08-17 02:47:32.24203+00', 'RYO', 'HPE');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (50, '550e8400-e29b-41d4-a716-446655440000', '体島', '2025-08-17 02:47:37.288594+00', 'RYO', '2025-08-17 02:47:37.288594+00', 'RYO', 'HPE');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (51, '550e8400-e29b-41d4-a716-446655440000', '体川', '2025-08-17 02:47:59.240767+00', 'RYO', '2025-08-17 02:47:59.240767+00', 'RYO', 'HPE');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (52, '550e8400-e29b-41d4-a716-446655440000', '体谷', '2025-08-17 02:48:05.021546+00', 'RYO', '2025-08-17 02:48:05.021546+00', 'RYO', 'HPE');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (53, '550e8400-e29b-41d4-a716-446655440000', '体藤', '2025-08-17 02:48:10.436995+00', 'RYO', '2025-08-17 02:48:10.436995+00', 'RYO', 'HPE');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (54, '550e8400-e29b-41d4-a716-446655440000', '芸木', '2025-08-17 02:48:24.589908+00', 'RYO', '2025-08-17 02:48:24.589908+00', 'RYO', 'ART');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (55, '550e8400-e29b-41d4-a716-446655440000', '芸原', '2025-08-17 02:48:31.271828+00', 'RYO', '2025-08-17 02:48:31.271828+00', 'RYO', 'ART');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (56, '550e8400-e29b-41d4-a716-446655440000', '芸田', '2025-08-17 02:48:36.006456+00', 'RYO', '2025-08-17 02:48:36.006456+00', 'RYO', 'ART');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (57, '550e8400-e29b-41d4-a716-446655440000', '英木', '2025-08-17 02:48:58.804802+00', 'RYO', '2025-08-17 02:48:58.804802+00', 'RYO', 'ENG');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (58, '550e8400-e29b-41d4-a716-446655440000', '英原', '2025-08-17 02:49:03.338939+00', 'RYO', '2025-08-17 02:49:03.338939+00', 'RYO', 'ENG');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (59, '550e8400-e29b-41d4-a716-446655440000', '英田', '2025-08-17 02:49:08.787104+00', 'RYO', '2025-08-17 02:49:08.787104+00', 'RYO', 'ENG');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (60, '550e8400-e29b-41d4-a716-446655440000', '英島', '2025-08-17 02:49:12.701783+00', 'RYO', '2025-08-17 02:49:12.701783+00', 'RYO', 'ENG');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (61, '550e8400-e29b-41d4-a716-446655440000', '英川', '2025-08-17 02:49:16.123283+00', 'RYO', '2025-08-17 02:49:16.123283+00', 'RYO', 'ENG');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (62, '550e8400-e29b-41d4-a716-446655440000', '英谷', '2025-08-17 02:49:20.634966+00', 'RYO', '2025-08-17 02:49:20.634966+00', 'RYO', 'ENG');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (63, '550e8400-e29b-41d4-a716-446655440000', '英藤', '2025-08-17 02:49:25.104077+00', 'RYO', '2025-08-17 02:49:25.104077+00', 'RYO', 'ENG');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (64, '550e8400-e29b-41d4-a716-446655440000', '英内', '2025-08-17 02:49:29.836967+00', 'RYO', '2025-08-17 02:49:29.836967+00', 'RYO', 'ENG');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (65, '550e8400-e29b-41d4-a716-446655440000', '英野', '2025-08-17 02:50:14.371055+00', 'RYO', '2025-08-17 02:50:14.371055+00', 'RYO', 'ENG');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (66, '550e8400-e29b-41d4-a716-446655440000', '英ヶ崎', '2025-08-17 02:50:21.668698+00', 'RYO', '2025-08-17 02:50:21.668698+00', 'RYO', 'ENG');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (67, '550e8400-e29b-41d4-a716-446655440000', '家木', '2025-08-17 02:50:54.220883+00', 'RYO', '2025-08-17 02:50:54.220883+00', 'RYO', 'HEC');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (68, '550e8400-e29b-41d4-a716-446655440000', '家原', '2025-08-17 02:51:00.542285+00', 'RYO', '2025-08-17 02:51:00.542285+00', 'RYO', 'HEC');
INSERT INTO public.instructor (id, ttid, instructor_name, created_at, created_by, updated_at, updated_by, discipline_code) VALUES (69, '550e8400-e29b-41d4-a716-446655440000', '情木', '2025-08-17 02:51:06.956736+00', 'RYO', '2025-08-17 02:51:06.956736+00', 'RYO', 'INF');


--
-- PostgreSQL database dump complete
--

