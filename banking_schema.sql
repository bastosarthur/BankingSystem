--
-- PostgreSQL database dump
--

\restrict TpE6Aej5dYWh6YlaFmnbGWyIygekymJK6ulWsylPYiPFLc7a9S7xpUCq6gUgwRR

-- Dumped from database version 17.11
-- Dumped by pg_dump version 17.11

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
-- Name: banking; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA banking;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: account_password; Type: TABLE; Schema: banking; Owner: -
--

CREATE TABLE banking.account_password (
    account integer NOT NULL,
    password character varying(60) NOT NULL,
    CONSTRAINT password_hash_length CHECK ((char_length((password)::text) = 60))
);


--
-- Name: accounts; Type: TABLE; Schema: banking; Owner: -
--

CREATE TABLE banking.accounts (
    account_number integer NOT NULL,
    cpf character varying(11) NOT NULL,
    balance numeric(15,2) DEFAULT 0 NOT NULL
);


--
-- Name: accounts_account_number_seq; Type: SEQUENCE; Schema: banking; Owner: -
--

ALTER TABLE banking.accounts ALTER COLUMN account_number ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME banking.accounts_account_number_seq
    START WITH 100000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: transactions; Type: TABLE; Schema: banking; Owner: -
--

CREATE TABLE banking.transactions (
    id integer NOT NULL,
    type character varying(20) NOT NULL,
    sender_account integer,
    recipient_account integer,
    amount numeric(15,2) NOT NULL,
    status boolean DEFAULT true NOT NULL,
    transaction_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT type_accepted CHECK (((type)::text = ANY ((ARRAY['deposit'::character varying, 'withdraw'::character varying, 'transfer'::character varying])::text[]))),
    CONSTRAINT valid_amount CHECK ((amount > (0)::numeric)),
    CONSTRAINT validate_transactions CHECK (((((type)::text = 'deposit'::text) AND (sender_account IS NULL) AND (recipient_account IS NOT NULL)) OR (((type)::text = 'withdraw'::text) AND (sender_account IS NOT NULL) AND (recipient_account IS NULL)) OR (((type)::text = 'transfer'::text) AND (sender_account IS NOT NULL) AND (recipient_account IS NOT NULL))))
);


--
-- Name: transactions_id_seq; Type: SEQUENCE; Schema: banking; Owner: -
--

ALTER TABLE banking.transactions ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME banking.transactions_id_seq
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: account_password account_password_pkey; Type: CONSTRAINT; Schema: banking; Owner: -
--

ALTER TABLE ONLY banking.account_password
    ADD CONSTRAINT account_password_pkey PRIMARY KEY (account);


--
-- Name: accounts accounts_pkey; Type: CONSTRAINT; Schema: banking; Owner: -
--

ALTER TABLE ONLY banking.accounts
    ADD CONSTRAINT accounts_pkey PRIMARY KEY (account_number);


--
-- Name: transactions transactions_pkey; Type: CONSTRAINT; Schema: banking; Owner: -
--

ALTER TABLE ONLY banking.transactions
    ADD CONSTRAINT transactions_pkey PRIMARY KEY (id);


--
-- Name: accounts unique_cpf; Type: CONSTRAINT; Schema: banking; Owner: -
--

ALTER TABLE ONLY banking.accounts
    ADD CONSTRAINT unique_cpf UNIQUE (cpf);


--
-- Name: account_password account_pk; Type: FK CONSTRAINT; Schema: banking; Owner: -
--

ALTER TABLE ONLY banking.account_password
    ADD CONSTRAINT account_pk FOREIGN KEY (account) REFERENCES banking.accounts(account_number);


--
-- Name: transactions recipient_fk; Type: FK CONSTRAINT; Schema: banking; Owner: -
--

ALTER TABLE ONLY banking.transactions
    ADD CONSTRAINT recipient_fk FOREIGN KEY (recipient_account) REFERENCES banking.accounts(account_number);


--
-- Name: transactions sender_fk; Type: FK CONSTRAINT; Schema: banking; Owner: -
--

ALTER TABLE ONLY banking.transactions
    ADD CONSTRAINT sender_fk FOREIGN KEY (sender_account) REFERENCES banking.accounts(account_number);


--
-- PostgreSQL database dump complete
--

\unrestrict TpE6Aej5dYWh6YlaFmnbGWyIygekymJK6ulWsylPYiPFLc7a9S7xpUCq6gUgwRR

