-- ============================================================
-- V1__init_schema.sql  (PostgreSQL)
-- Fresh baseline schema for CompareMyDevice
-- Matches entities:
--   Brand, Category, Device, Image, Review, Specification
-- Notes:
-- - device.brand_id / device.category_id (NOT brand/category)
-- - price_ininr: NUMERIC(10,2)
-- - tags: TEXT[]
-- - review.rating: NUMERIC(2,1) with 0..5.0 CHECK
-- - Child tables CASCADE on device deletion
-- - Simple "updated_at" auto-update trigger
-- ============================================================

-- 0) helper: auto-update updated_at on every UPDATE
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 1) brand
CREATE TABLE IF NOT EXISTS brand (
  id           BIGSERIAL PRIMARY KEY,
  name         VARCHAR(255) NOT NULL UNIQUE,
  logo_url     TEXT,
  created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TRIGGER trg_brand_updated_at
BEFORE UPDATE ON brand
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- 2) category
CREATE TABLE IF NOT EXISTS category (
  id           BIGSERIAL PRIMARY KEY,
  name         VARCHAR(255) NOT NULL UNIQUE,
  icon_url     TEXT,
  created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TRIGGER trg_category_updated_at
BEFORE UPDATE ON category
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- 3) device
CREATE TABLE IF NOT EXISTS device (
  id             BIGSERIAL PRIMARY KEY,
  name           VARCHAR(255) NOT NULL,
  processor      VARCHAR(255),
  ram            VARCHAR(255),
  storage        VARCHAR(255),
  price_ininr    NUMERIC(10,2),
  release_date   DATE,
  slug           VARCHAR(255) UNIQUE,
  tags           TEXT[],
  is_deleted     BOOLEAN NOT NULL DEFAULT FALSE,

  brand_id       BIGINT NOT NULL,
  category_id    BIGINT NOT NULL,

  created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),

  CONSTRAINT fk_device_brand_id
    FOREIGN KEY (brand_id) REFERENCES brand(id) ON DELETE RESTRICT,
  CONSTRAINT fk_device_category_id
    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS idx_device_brand_id    ON device(brand_id);
CREATE INDEX IF NOT EXISTS idx_device_category_id ON device(category_id);

CREATE TRIGGER trg_device_updated_at
BEFORE UPDATE ON device
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- 4) image
CREATE TABLE IF NOT EXISTS image (
  id           BIGSERIAL PRIMARY KEY,
  url          TEXT NOT NULL,
  alt_text     TEXT,
  is_primary   BOOLEAN NOT NULL DEFAULT FALSE,

  device_id    BIGINT NOT NULL REFERENCES device(id) ON DELETE CASCADE,

  created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_image_device_id ON image(device_id);

CREATE TRIGGER trg_image_updated_at
BEFORE UPDATE ON image
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- 5) review
CREATE TABLE IF NOT EXISTS review (
  id             BIGSERIAL PRIMARY KEY,
  reviewer_name  TEXT,
  content        TEXT,
  rating         NUMERIC(2,1),
  source         TEXT,

  device_id      BIGINT NOT NULL REFERENCES device(id) ON DELETE CASCADE,

  created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),

  CONSTRAINT chk_review_rating_range
    CHECK (rating IS NULL OR (rating >= 0 AND rating <= 5.0))
);

CREATE INDEX IF NOT EXISTS idx_review_device_id ON review(device_id);

CREATE TRIGGER trg_review_updated_at
BEFORE UPDATE ON review
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- 6) specification
--   "key" is a valid identifier in Postgres, but we quote it to be explicit.
CREATE TABLE IF NOT EXISTS specification (
  id           BIGSERIAL PRIMARY KEY,
  "key"        TEXT NOT NULL,
  value        TEXT,
  spec_type    TEXT,

  device_id    BIGINT NOT NULL REFERENCES device(id) ON DELETE CASCADE,

  created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),

  CONSTRAINT uq_spec_device_key UNIQUE (device_id, "key")
);

CREATE INDEX IF NOT EXISTS idx_spec_device_id ON specification(device_id);

CREATE TRIGGER trg_spec_updated_at
BEFORE UPDATE ON specification
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- ============================================================
-- End of V1
-- ============================================================