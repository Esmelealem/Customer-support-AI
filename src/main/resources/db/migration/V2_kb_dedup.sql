BEGIN;
-- Clean duplicates in kb_docs
DELETE FROM kb_docs a
    USING kb_docs b
WHERE
        lower(btrim(a.title)) = lower(btrim(b.title))
  AND a.id > b.id;

-- Enforce unique KB titles (case/space insensitive)
CREATE UNIQUE INDEX IF NOT EXISTS ux_kb_docs_unique_title
    ON kb_docs (lower(btrim(title)))
    WHERE title IS NOT NULL;

-- Clean duplicates in kb_files
DELETE FROM kb_files a
    USING kb_files b
WHERE
        a.kb_doc_id = b.kb_doc_id
  AND lower(btrim(a.file_name)) = lower(btrim(b.file_name))
  AND a.id > b.id;

-- Enforce unique filenames per KB doc
CREATE UNIQUE INDEX IF NOT EXISTS ux_kb_files_unique_doc_filename
    ON kb_files (kb_doc_id, lower(btrim(file_name)))
    WHERE file_name IS NOT NULL;

COMMIT;
