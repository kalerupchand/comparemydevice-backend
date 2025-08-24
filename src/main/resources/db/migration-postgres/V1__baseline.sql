--db/migration-postgres/V1__baseline.sql
-- PostgreSQL baseline matching current entities

CREATE TABLE IF NOT EXISTS brand (
  id           BIGSERIAL PRIMARY KEY,
  name         VARCHAR(255) NOT NULL UNIQUE,
  slug         VARCHAR(150) NOT NULL UNIQUE,
  logo_url     TEXT,
  created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_brand_slug ON brand(slug);

CREATE TABLE IF NOT EXISTS category (
  id           BIGSERIAL PRIMARY KEY,
  name         VARCHAR(255) NOT NULL UNIQUE,
  slug         VARCHAR(150) NOT NULL UNIQUE,
  icon_url     TEXT,
  created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_category_slug ON category(slug);

CREATE TABLE IF NOT EXISTS device (
  id             BIGSERIAL PRIMARY KEY,
  name           VARCHAR(255) NOT NULL,
  processor      VARCHAR(255),
  ram            VARCHAR(255),
  storage        VARCHAR(255),
  price_amount   NUMERIC(12,2),
  price_currency CHAR(3) DEFAULT 'INR',
  release_date   DATE,
  slug           VARCHAR(255) NOT NULL UNIQUE,
  is_deleted     BOOLEAN NOT NULL DEFAULT FALSE,

  brand_id       BIGINT NOT NULL REFERENCES brand(id),
  category_id    BIGINT NOT NULL REFERENCES category(id),

  created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),

  CONSTRAINT uq_device_brand_name_release UNIQUE (brand_id, name, release_date)
);
CREATE INDEX IF NOT EXISTS idx_device_brand_id     ON device(brand_id);
CREATE INDEX IF NOT EXISTS idx_device_category_id  ON device(category_id);
CREATE INDEX IF NOT EXISTS idx_device_release_date ON device(release_date);
CREATE INDEX IF NOT EXISTS idx_device_price        ON device(price_amount);
CREATE INDEX IF NOT EXISTS idx_device_is_deleted   ON device(is_deleted);
CREATE INDEX IF NOT EXISTS idx_device_slug         ON device(slug);

CREATE TABLE IF NOT EXISTS image (
  id           BIGSERIAL PRIMARY KEY,
  url          TEXT NOT NULL,
  alt_text     TEXT,
  is_primary   BOOLEAN NOT NULL DEFAULT FALSE,
  sort_order   INTEGER,
  device_id    BIGINT NOT NULL REFERENCES device(id) ON DELETE CASCADE,
  created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_image_device_id ON image(device_id);

-- Optional: allow only one primary image per device
CREATE UNIQUE INDEX IF NOT EXISTS uq_image_primary_per_device
  ON image(device_id) WHERE is_primary;

CREATE TABLE IF NOT EXISTS review (
  id             BIGSERIAL PRIMARY KEY,
  reviewer_name  TEXT,
  content        TEXT,
  rating         NUMERIC(2,1),
  source_url     TEXT,
  device_id      BIGINT NOT NULL REFERENCES device(id) ON DELETE CASCADE,
  created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT chk_review_rating_range
    CHECK (rating IS NULL OR (rating >= 0 AND rating <= 5.0))
);
CREATE INDEX IF NOT EXISTS idx_review_device_id ON review(device_id);

CREATE TABLE IF NOT EXISTS tag (
  id    BIGSERIAL PRIMARY KEY,
  name  VARCHAR(100) NOT NULL UNIQUE,
  slug  VARCHAR(150) NOT NULL UNIQUE
);
CREATE INDEX IF NOT EXISTS idx_tag_slug ON tag(slug);

CREATE TABLE IF NOT EXISTS device_tag (
  device_id BIGINT NOT NULL REFERENCES device(id) ON DELETE CASCADE,
  tag_id    BIGINT NOT NULL REFERENCES tag(id) ON DELETE CASCADE,
  PRIMARY KEY (device_id, tag_id)
);

CREATE TABLE IF NOT EXISTS spec_key (
  id        BIGSERIAL PRIMARY KEY,
  name      VARCHAR(100) NOT NULL UNIQUE,
  spec_type VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS device_spec (
  id           BIGSERIAL PRIMARY KEY,
  device_id    BIGINT NOT NULL REFERENCES device(id) ON DELETE CASCADE,
  spec_key_id  BIGINT NOT NULL REFERENCES spec_key(id),
  value_text   TEXT,
  created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT uq_device_spec UNIQUE (device_id, spec_key_id)
);
CREATE INDEX IF NOT EXISTS idx_device_spec_device ON device_spec(device_id);
CREATE INDEX IF NOT EXISTS idx_device_spec_key    ON device_spec(spec_key_id);

-- Auto-update updated_at on UPDATE
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DO $$ BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'trg_brand_updated_at') THEN
    CREATE TRIGGER trg_brand_updated_at        BEFORE UPDATE ON brand        FOR EACH ROW EXECUTE FUNCTION set_updated_at();
    CREATE TRIGGER trg_category_updated_at     BEFORE UPDATE ON category     FOR EACH ROW EXECUTE FUNCTION set_updated_at();
    CREATE TRIGGER trg_device_updated_at       BEFORE UPDATE ON device       FOR EACH ROW EXECUTE FUNCTION set_updated_at();
    CREATE TRIGGER trg_image_updated_at        BEFORE UPDATE ON image        FOR EACH ROW EXECUTE FUNCTION set_updated_at();
    CREATE TRIGGER trg_review_updated_at       BEFORE UPDATE ON review       FOR EACH ROW EXECUTE FUNCTION set_updated_at();
    CREATE TRIGGER trg_device_spec_updated_at  BEFORE UPDATE ON device_spec  FOR EACH ROW EXECUTE FUNCTION set_updated_at();
  END IF;
END $$;