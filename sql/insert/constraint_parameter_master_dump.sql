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
-- Data for Name: constraint_parameter_master; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.constraint_parameter_master VALUES ('CONSECUTIVE_PERIOD', 'courseId', '講座ID', NULL, '2025-11-16 06:52:28.419379+00', 'system', '2025-11-16 06:52:28.419379+00', 'system', false);
INSERT INTO public.constraint_parameter_master VALUES ('COURSES_PER_DAY', 'exclude_courseIds', '除外講座IDリスト', NULL, '2025-11-16 06:52:28.419379+00', 'system', '2025-11-16 06:52:28.419379+00', 'system', true);
INSERT INTO public.constraint_parameter_master VALUES ('INSTCUTOR_COURSES_PER_DAY', 'max_periods', '最大時限数', NULL, '2025-11-16 06:52:28.419379+00', 'system', '2025-11-16 06:52:28.419379+00', 'system', false);
INSERT INTO public.constraint_parameter_master VALUES ('INSTRUCTOR_CONSECUTIVE_PERIOD', 'max_consecutive_periods', '最大連続時限数', NULL, '2025-11-16 06:52:28.419379+00', 'system', '2025-11-16 06:52:28.419379+00', 'system', false);
INSERT INTO public.constraint_parameter_master VALUES ('SPECIFIC_DAY_PERIOD', 'courseId', '講座ID', NULL, '2025-11-16 06:52:28.419379+00', 'system', '2025-11-16 06:52:28.419379+00', 'system', false);
INSERT INTO public.constraint_parameter_master VALUES ('SPECIFIC_DAY_PERIOD', 'dayOfWeek', '曜日', NULL, '2025-11-16 06:52:28.419379+00', 'system', '2025-11-16 06:52:28.419379+00', 'system', false);
INSERT INTO public.constraint_parameter_master VALUES ('SPECIFIC_DAY_PERIOD', 'period', '時限', NULL, '2025-11-16 06:52:28.419379+00', 'system', '2025-11-16 06:52:28.419379+00', 'system', false);


--
-- PostgreSQL database dump complete
--

