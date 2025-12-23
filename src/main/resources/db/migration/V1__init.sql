-- CREATE TABLE orders (
--                         id BIGSERIAL PRIMARY KEY,
--                         user_id BIGINT NOT NULL,
--                         order_number VARCHAR(50) UNIQUE NOT NULL,
--                         status VARCHAR(30) NOT NULL,
--                         total_amount NUMERIC(10,2) NOT NULL,
--                         created_at TIMESTAMP NOT NULL DEFAULT NOW()
-- );
--
-- -- Enable pgvector
-- CREATE EXTENSION IF NOT EXISTS vector;
--
-- CREATE TABLE kb_docs (
--                          id BIGSERIAL PRIMARY KEY,
--                          title TEXT NOT NULL,
--                          content TEXT NOT NULL,
--                          embedding vector(1536)
-- );
--
-- -- index for fast vector search
-- CREATE INDEX ON kb_docs
--     USING ivfflat (embedding vector_cosine_ops)
--     WITH (lists = 100);
-- =========================================
-- V1__init.sql
-- Initial schema for AI Support System
-- =========================================

-- Enable pgvector (must be installed at DB level)
CREATE EXTENSION IF NOT EXISTS vector;

-- =========================
-- Orders table
-- =========================
CREATE TABLE IF NOT EXISTS orders (
                                      id BIGSERIAL PRIMARY KEY,
                                      user_id BIGINT NOT NULL,
                                      order_number VARCHAR(50) UNIQUE NOT NULL,
                                      status VARCHAR(30) NOT NULL,
                                      total_amount NUMERIC(10,2) NOT NULL,
                                      created_at TIMESTAMP NOT NULL DEFAULT now()
);

-- =========================
-- Knowledge base documents
-- =========================
CREATE TABLE IF NOT EXISTS kb_docs (
                                       id BIGSERIAL PRIMARY KEY,
                                       title TEXT NOT NULL,
                                       content TEXT NOT NULL,
                                       embedding vector(1536),
                                       created_at TIMESTAMP NOT NULL DEFAULT now()
);

-- =========================
-- Vector index for RAG search
-- =========================
CREATE INDEX IF NOT EXISTS kb_docs_embedding_idx
    ON kb_docs
        USING ivfflat (embedding vector_cosine_ops)
    WITH (lists = 100);

