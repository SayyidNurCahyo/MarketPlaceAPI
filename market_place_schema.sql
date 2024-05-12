--
-- PostgreSQL database dump
--

-- Dumped from database version 16.1
-- Dumped by pg_dump version 16.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: m_customer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.m_customer (
    id character varying(255) NOT NULL,
    name character varying(255),
    phone character varying(255),
    reward_point integer,
    user_account_id character varying(255)
);


ALTER TABLE public.m_customer OWNER TO postgres;

--
-- Name: m_merchant; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.m_merchant (
    id character varying(255) NOT NULL,
    address character varying(255),
    name character varying(255),
    phone character varying(255),
    user_account_id character varying(255)
);


ALTER TABLE public.m_merchant OWNER TO postgres;

--
-- Name: m_product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.m_product (
    id character varying(255) NOT NULL,
    is_active boolean,
    name character varying(255),
    price bigint,
    stock integer,
    merchant_id character varying(255)
);


ALTER TABLE public.m_product OWNER TO postgres;

--
-- Name: m_reward; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.m_reward (
    id character varying(255) NOT NULL,
    name character varying(255),
    reward_point integer,
    is_active boolean
);


ALTER TABLE public.m_reward OWNER TO postgres;

--
-- Name: m_role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.m_role (
    id character varying(255) NOT NULL,
    role character varying(255),
    CONSTRAINT m_role_role_check CHECK (((role)::text = ANY ((ARRAY['ROLE_ADMIN'::character varying, 'ROLE_MERCHANT'::character varying, 'ROLE_CUSTOMER'::character varying])::text[])))
);


ALTER TABLE public.m_role OWNER TO postgres;

--
-- Name: m_user_account; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.m_user_account (
    id character varying(255) NOT NULL,
    is_enable boolean,
    password character varying(255),
    username character varying(255)
);


ALTER TABLE public.m_user_account OWNER TO postgres;

--
-- Name: m_user_account_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.m_user_account_roles (
    user_account_id character varying(255) NOT NULL,
    roles_id character varying(255) NOT NULL
);


ALTER TABLE public.m_user_account_roles OWNER TO postgres;

--
-- Name: t_claim_reward; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_claim_reward (
    id character varying(255) NOT NULL,
    date date,
    customer_id character varying(255),
    reward_id character varying(255)
);


ALTER TABLE public.t_claim_reward OWNER TO postgres;

--
-- Name: t_transaction; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_transaction (
    id character varying(255) NOT NULL,
    date date,
    customer_id character varying(255),
    merchant_id character varying(255)
);


ALTER TABLE public.t_transaction OWNER TO postgres;

--
-- Name: t_transaction_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_transaction_detail (
    id character varying(255) NOT NULL,
    quantity integer,
    product_id character varying(255),
    transaction_id character varying(255)
);


ALTER TABLE public.t_transaction_detail OWNER TO postgres;

--
-- Name: m_customer m_customer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.m_customer
    ADD CONSTRAINT m_customer_pkey PRIMARY KEY (id);


--
-- Name: m_merchant m_merchant_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.m_merchant
    ADD CONSTRAINT m_merchant_pkey PRIMARY KEY (id);


--
-- Name: m_product m_product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.m_product
    ADD CONSTRAINT m_product_pkey PRIMARY KEY (id);


--
-- Name: m_reward m_reward_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.m_reward
    ADD CONSTRAINT m_reward_pkey PRIMARY KEY (id);


--
-- Name: m_role m_role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.m_role
    ADD CONSTRAINT m_role_pkey PRIMARY KEY (id);


--
-- Name: m_user_account m_user_account_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.m_user_account
    ADD CONSTRAINT m_user_account_pkey PRIMARY KEY (id);


--
-- Name: t_claim_reward t_claim_reward_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_claim_reward
    ADD CONSTRAINT t_claim_reward_pkey PRIMARY KEY (id);


--
-- Name: t_transaction_detail t_transaction_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_transaction_detail
    ADD CONSTRAINT t_transaction_detail_pkey PRIMARY KEY (id);


--
-- Name: t_transaction t_transaction_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_transaction
    ADD CONSTRAINT t_transaction_pkey PRIMARY KEY (id);


--
-- Name: m_customer uk_go9cudeqskvo4j36x3wpf2fxa; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.m_customer
    ADD CONSTRAINT uk_go9cudeqskvo4j36x3wpf2fxa UNIQUE (user_account_id);


--
-- Name: m_user_account uk_lveldxh9ud3mkuqk8vexltugi; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.m_user_account
    ADD CONSTRAINT uk_lveldxh9ud3mkuqk8vexltugi UNIQUE (username);


--
-- Name: m_merchant uk_r01jqjfbwbx3gggy3bkmpo5hs; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.m_merchant
    ADD CONSTRAINT uk_r01jqjfbwbx3gggy3bkmpo5hs UNIQUE (user_account_id);


--
-- Name: m_merchant fk11fbe0qp6mnets61kqvys8jwg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.m_merchant
    ADD CONSTRAINT fk11fbe0qp6mnets61kqvys8jwg FOREIGN KEY (user_account_id) REFERENCES public.m_user_account(id);


--
-- Name: m_user_account_roles fk18vpvyg8n1w39qdc4qujjcn0h; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.m_user_account_roles
    ADD CONSTRAINT fk18vpvyg8n1w39qdc4qujjcn0h FOREIGN KEY (roles_id) REFERENCES public.m_role(id);


--
-- Name: t_claim_reward fk1f58im9u7g7oxig05x5y9xsir; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_claim_reward
    ADD CONSTRAINT fk1f58im9u7g7oxig05x5y9xsir FOREIGN KEY (customer_id) REFERENCES public.m_customer(id);


--
-- Name: t_transaction_detail fk1u7cv0m514x6m4k4mbfwnrejx; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_transaction_detail
    ADD CONSTRAINT fk1u7cv0m514x6m4k4mbfwnrejx FOREIGN KEY (transaction_id) REFERENCES public.t_transaction(id);


--
-- Name: m_product fk2bevy95e7koud043qtu0q19rd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.m_product
    ADD CONSTRAINT fk2bevy95e7koud043qtu0q19rd FOREIGN KEY (merchant_id) REFERENCES public.m_merchant(id);


--
-- Name: t_claim_reward fk8le2wmat474bwbkqb56eknhnj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_claim_reward
    ADD CONSTRAINT fk8le2wmat474bwbkqb56eknhnj FOREIGN KEY (reward_id) REFERENCES public.m_reward(id);


--
-- Name: t_transaction_detail fkdn9fjqtjnp9spa4d8csxvh27c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_transaction_detail
    ADD CONSTRAINT fkdn9fjqtjnp9spa4d8csxvh27c FOREIGN KEY (product_id) REFERENCES public.m_product(id);


--
-- Name: t_transaction fkekpbga8gh8acb3qq6uq73gj2i; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_transaction
    ADD CONSTRAINT fkekpbga8gh8acb3qq6uq73gj2i FOREIGN KEY (customer_id) REFERENCES public.m_customer(id);


--
-- Name: m_user_account_roles fkg3dd2oxr1bj1fuf9k4y9xk8ql; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.m_user_account_roles
    ADD CONSTRAINT fkg3dd2oxr1bj1fuf9k4y9xk8ql FOREIGN KEY (user_account_id) REFERENCES public.m_user_account(id);


--
-- Name: t_transaction fkmx9rtmcvapxi7168r4mfac4c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_transaction
    ADD CONSTRAINT fkmx9rtmcvapxi7168r4mfac4c FOREIGN KEY (merchant_id) REFERENCES public.m_merchant(id);


--
-- Name: m_customer fknitqtaye2i97o64fwa7c9jtik; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.m_customer
    ADD CONSTRAINT fknitqtaye2i97o64fwa7c9jtik FOREIGN KEY (user_account_id) REFERENCES public.m_user_account(id);


--
-- PostgreSQL database dump complete
--

