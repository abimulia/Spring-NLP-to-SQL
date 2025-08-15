CREATE TABLE IF NOT EXISTS products (
  product_id            TEXT PRIMARY KEY,
  product_name          TEXT NOT NULL,
  product_category      TEXT NOT NULL,
  product_description   TEXT,
  price                 NUMERIC(10,2) NOT NULL,
  stock_quantity        INTEGER NOT NULL,
  warranty_period       INTEGER, -- years
  product_dimensions    TEXT,
  manufacturing_date    DATE,
  expiration_date       DATE,
  sku                   TEXT,
  product_tags          TEXT,
  color_size_variations TEXT,
  product_ratings       INTEGER
);

CREATE INDEX IF NOT EXISTS idx_products_category ON products (product_category);
CREATE INDEX IF NOT EXISTS idx_products_name ON products (product_name);
CREATE INDEX IF NOT EXISTS idx_products_fulltext ON products USING GIN (to_tsvector('simple', coalesce(product_name,'') || ' ' || coalesce(product_description,'')));