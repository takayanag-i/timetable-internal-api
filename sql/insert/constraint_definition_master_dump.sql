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
-- Data for Name: constraint_definition_master; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.constraint_definition_master VALUES ('HOMEROOM', '学級', NULL, true, false, '2025-11-16 06:13:37.069683+00', 'system', '2025-11-16 06:13:37.069683+00', 'system');
INSERT INTO public.constraint_definition_master VALUES ('COURSE', '講座', NULL, true, false, '2025-11-16 06:13:37.069683+00', 'system', '2025-11-16 06:13:37.069683+00', 'system');
INSERT INTO public.constraint_definition_master VALUES ('CREDIT', '単位数', NULL, true, false, '2025-11-16 06:13:37.069683+00', 'system', '2025-11-16 06:13:37.069683+00', 'system');
INSERT INTO public.constraint_definition_master VALUES ('BLOCK', 'ブロック', NULL, true, false, '2025-11-16 06:13:37.069683+00', 'system', '2025-11-16 06:13:37.069683+00', 'system');
INSERT INTO public.constraint_definition_master VALUES ('INSTRUCTOR', '教員', NULL, true, false, '2025-11-16 06:13:37.069683+00', 'system', '2025-11-16 06:13:37.069683+00', 'system');
INSERT INTO public.constraint_definition_master VALUES ('CONSECUTIVE_PERIOD', '連続時限（講座）', NULL, false, false, '2025-11-16 06:13:37.069683+00', 'system', '2025-11-16 06:13:37.069683+00', 'system');
INSERT INTO public.constraint_definition_master VALUES ('COURSES_PER_DAY', '同日開講数（講座）', NULL, false, false, '2025-11-16 06:13:37.069683+00', 'system', '2025-11-16 06:13:37.069683+00', 'system');
INSERT INTO public.constraint_definition_master VALUES ('AFTERNOON', '午前午後', NULL, false, true, '2025-11-16 06:13:37.069683+00', 'system', '2025-11-16 06:13:37.069683+00', 'system');
INSERT INTO public.constraint_definition_master VALUES ('CONSECUTIVE_DAY', '連続曜日開講', NULL, false, true, '2025-11-16 06:13:37.069683+00', 'system', '2025-11-16 06:13:37.069683+00', 'system');
INSERT INTO public.constraint_definition_master VALUES ('INSTCUTOR_COURSES_PER_DAY', '同日開講数（教員）', NULL, false, true, '2025-11-16 06:13:37.069683+00', 'system', '2025-11-16 06:13:37.069683+00', 'system');
INSERT INTO public.constraint_definition_master VALUES ('INSTRUCTOR_CONSECUTIVE_PERIOD', '連続時限（教員）', NULL, false, true, '2025-11-16 06:13:37.069683+00', 'system', '2025-11-16 06:13:37.069683+00', 'system');
INSERT INTO public.constraint_definition_master VALUES ('SPECIFIC_DAY_PERIOD', '曜日時限指定', NULL, false, false, '2025-11-16 06:13:37.069683+00', 'system', '2025-11-16 06:13:37.069683+00', 'system');


--
-- PostgreSQL database dump complete
--

