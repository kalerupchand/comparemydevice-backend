-- One-time conditional reset. Controlled by placeholder: ${RESET_SCHEMA}
DO $block$
BEGIN
  IF '${RESET_SCHEMA}' = 'true' THEN
    RAISE NOTICE 'RESET_SCHEMA=true → Dropping all tables in schema public';
    EXECUTE $sql$
      DO $inner$
      DECLARE r RECORD;
      BEGIN
        FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = 'public') LOOP
          EXECUTE 'DROP TABLE IF EXISTS "' || r.tablename || '" CASCADE';
        END LOOP;
      END
      $inner$;
    $sql$;
  ELSE
    RAISE NOTICE 'RESET_SCHEMA is not true → Skipping destructive reset';
  END IF;
END
$block$;