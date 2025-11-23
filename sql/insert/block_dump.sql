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
-- Data for Name: block; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.block (id, homeroom_id, block_name, created_at, created_by, updated_at, updated_by) VALUES (1, 106, '1-1共通', '2025-09-06 12:49:35.809682+00', 'RYO', '2025-09-06 12:49:35.809682+00', 'RYO');
INSERT INTO public.block (id, homeroom_id, block_name, created_at, created_by, updated_at, updated_by) VALUES (2, 107, '1-2共通', '2025-09-06 12:49:44.524887+00', 'RYO', '2025-09-06 12:49:44.524887+00', 'RYO');
INSERT INTO public.block (id, homeroom_id, block_name, created_at, created_by, updated_at, updated_by) VALUES (3, 107, '1-2芸術', '2025-09-06 12:49:53.141091+00', 'RYO', '2025-09-06 12:49:53.141091+00', 'RYO');
INSERT INTO public.block (id, homeroom_id, block_name, created_at, created_by, updated_at, updated_by) VALUES (4, 108, '1-3共通', '2025-09-06 12:50:02.232832+00', 'RYO', '2025-09-06 12:50:02.232832+00', 'RYO');
INSERT INTO public.block (id, homeroom_id, block_name, created_at, created_by, updated_at, updated_by) VALUES (5, 108, '1-3芸術', '2025-09-06 12:50:11.925372+00', 'RYO', '2025-09-06 12:50:11.925372+00', 'RYO');
INSERT INTO public.block (id, homeroom_id, block_name, created_at, created_by, updated_at, updated_by) VALUES (14850, 110, '1-5共通', '2025-11-02 09:53:09.868636+00', 'system', '2025-11-02 09:53:09.868636+00', 'system');
INSERT INTO public.block (id, homeroom_id, block_name, created_at, created_by, updated_at, updated_by) VALUES (14900, 110, '1-5芸術', '2025-11-02 10:04:39.044783+00', 'system', '2025-11-02 10:04:39.044783+00', 'system');
INSERT INTO public.block (id, homeroom_id, block_name, created_at, created_by, updated_at, updated_by) VALUES (10300, 109, '1-4共通', '2025-09-14 10:32:45.442973+00', 'RYO', '2025-09-14 10:32:45.442973+00', 'RYO');
INSERT INTO public.block (id, homeroom_id, block_name, created_at, created_by, updated_at, updated_by) VALUES (10301, 109, '1-4芸術', '2025-09-14 10:32:45.442973+00', 'RYO', '2025-09-14 10:32:45.442973+00', 'RYO');
INSERT INTO public.block (id, homeroom_id, block_name, created_at, created_by, updated_at, updated_by) VALUES (15650, 110, 'テスト', '2025-11-15 03:20:13.646647+00', 'system', '2025-11-15 03:20:13.646647+00', 'system');


--
-- PostgreSQL database dump complete
--

