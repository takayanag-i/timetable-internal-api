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
-- Data for Name: room; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.room (id, ttid, room_name, created_at, created_by, updated_at, updated_by) VALUES (1, '550e8400-e29b-41d4-a716-446655440000', 'テスト教室', '2025-09-06 12:48:13.007598+00', 'RYO', '2025-09-06 12:48:13.007598+00', 'RYO');


--
-- PostgreSQL database dump complete
--

